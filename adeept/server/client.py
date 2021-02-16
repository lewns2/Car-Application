import socket

def run(host='192.168.1.51', port=8000):
	with socket.socket() as sock:
		sock.bind((host, port))
		sock.listen()
		conn, addr = sock.accept()
	while True:
		data = conn.recv(1024)
		msg = data.decode()
		print(data.decode())
		conn.sendall(data)
		if msg == 'bye':
			conn.close()
			break


if __name__ == '__main__':
	run()


