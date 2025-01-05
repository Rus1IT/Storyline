import zipfile

with zipfile.ZipFile('my_python_app.zip', 'w') as zipf:
    zipf.write('MessageSender.py', arcname='MessageSender.py')
