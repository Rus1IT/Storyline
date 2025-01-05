from flask import Flask, request, jsonify
import pywhatkit as kit
import threading
import queue
from datetime import datetime, timedelta
import configparser

app = Flask(__name__)

# Очередь для сообщений
message_queue = queue.Queue()

config = configparser.ConfigParser()
config.read('config.properties')

server_host = config.get('DEFAULT', 'server.host')
server_port = config.getint('DEFAULT', 'server.port')
db_username = config.get('DEFAULT', 'database.username')
db_password = config.get('DEFAULT', 'database.password')
wait_time = config.getint('DEFAULT', 'message.wait_time')


# Функция для обработки очереди сообщений
def process_queue():
    while True:
        try:
            # Получаем сообщение из очереди
            task = message_queue.get()
            if task is None:  # Завершающий сигнал
                break

            number = task['number']
            message = task['message']
            is_instant = task.get('is_instant', False)

            if is_instant:
                kit.sendwhatmsg_instantly(number, message, wait_time, True, 3)
            else:
                # Запланированная отправка
                hour = task['hour']
                minute = task['minute']

                kit.sendwhatmsg(number, message, hour,minute,wait_time,True,3)


            print(f"Message sent to {number}: {message}")
        except Exception as e:
            print(f"Error sending message: {e}")
        finally:
            message_queue.task_done()

# Запуск обработчика очереди в отдельном потоке
queue_thread = threading.Thread(target=process_queue, daemon=True)
queue_thread.start()

@app.route('/api/v1/sendMessage', methods=['POST'])
def send_message():
    try:
        data = request.json
        number = data.get('number')
        message = data.get('message')
        hour = data.get('hour')
        minute = data.get('minute')

        if not number or not message or hour is None or minute is None:
            return jsonify({'status': 'error', 'message': 'All fields are required'}), 400

        now = datetime.now()
        task_time = now.replace(hour=hour, minute=minute, second=0, microsecond=0)

        if task_time < now:
            return jsonify({'status': 'error', 'message': 'The scheduled time is in the past.'}), 400

        # Проверяем, не превышает ли время лимит (3 минуты вперёд)
        max_allowed_time = now + timedelta(minutes=3)
        if task_time > max_allowed_time:
            return jsonify({'status': 'error', 'message': 'The scheduled time cannot be more than 3 minutes from now.'}), 400

        # Добавляем сообщение в очередь
        message_queue.put({'number': number, 'message': message, 'hour': hour, 'minute': minute})
        return jsonify({'status': 'success', 'message': 'Message added to queue'}), 200

    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500

@app.route('/api/v1/sendMessageNow', methods=['POST'])
def send_message_now():
    try:
        data = request.json
        number = data.get('number')
        message = data.get('message')

        if not number or not message:
            return jsonify({'status': 'error', 'message': 'Number and message are required'}), 400

        # Добавляем мгновенное сообщение в очередь
        message_queue.put({'number': number, 'message': message, 'is_instant': True})
        return jsonify({'status': 'success', 'message': 'Instant message added to queue'}), 200

    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500

if __name__ == '__main__':
    print("Server is running...")
    app.run(debug=True, port=server_port)
