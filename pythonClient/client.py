import socket

host = socket.gethostname()
port = 9000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((host, port))
s.sendall('{"jsonrpc":"2.0", "id":"12", "method":"getUserCount", "params":[5]}\n')
data = ""
while not data.endswith("\n"):
    data += s.recv(64)

s.close()
print('Received', data)
