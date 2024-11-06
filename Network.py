"Module for wrappers for socket-based network nodes"
import threading
import pickle


class NetworkEntity:
    "Manage an abstract node in a socket-based network"
    BUFFER_SIZE = 4096
    def __init__(self, socket, host, port):
        self.server_adress = (host, port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        self._on_receive = lambda x, y=None: None

    def on_receive(self, function):
        "set callback for incoming data"
        self._on_receive = function

    def listen_loop(self):
        "listen indefinetely for incoming data"

    def listen(self):
        "wrap listen_loop in a thread"
        thread = threading.Thread(target=self.listen_loop)
        thread.daemon = True
        thread.start()
        return thread

class Client(NetworkEntity):
    "Manage Network Entity with common operations in the network"

    def __init__(self, socket, host, port):
        super().__init__(socket, host, port)
        self.socket.bind(('', 0))

    def listen_loop(self):
        "listen indefinetely for incoming data"
        while True:
            data = self.socket.recvfrom(NetworkEntity.BUFFER_SIZE)[0]
            data = pickle.loads(data)
            self._on_receive(data)

    def send_data(self, data):
        "send data to server"
        serialized_data = pickle.dumps(data)
        self.socket.sendto(serialized_data, self.server_adress)

    def disconnect(self):
        "inform server that the client is disconnecting"
        self.send_data({'disconnect': True})
        self.socket.close()

class Server(NetworkEntity):
    "Manage Network Entity with speicial privileges in the network"

    def __init__(self, socket, host, port):
        super().__init__(socket, host, port)
        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(('', port))
        self.adresses = set()

    def listen_loop(self):
        "listen indefinetely for incoming messages"
        while True:
            data, adress = self.socket.recvfrom(NetworkEntity.BUFFER_SIZE)
            data = pickle.loads(data)
            self.adresses.add(adress)
            self._on_receive(data, adress)
            if isinstance(data, dict) and data.get('disconnect'):
                self.adresses.remove(adress)

    def broadcast_data(self, data):
        "send data to all clients"
        message = pickle.dumps(data)
        for adress in self.adresses:
            self.socket.sendto(message, adress)

    def broadcast_to(self, adress, data):
        "send data to a specific adress"
        message = pickle.dumps(data)
        self.socket.sendto(message, adress)

    def close(self):
        "Close the server"
        self.socket.close()
