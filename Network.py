"Module for wrappers for socket-based network nodes"
import threading
import pickle


class NetworkEntity:
    "Manage an abstract node in a socket-based network"
    BUFFER_SIZE = 4096*1024
    def __init__(self, socket, host, port):
        self.server_address = (host, port)
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
        self.socket.sendto(serialized_data, self.server_address)

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
        self.addresses = set()

    def listen_loop(self):
        "listen indefinetely for incoming messages"
        while True:
            data, address = self.socket.recvfrom(NetworkEntity.BUFFER_SIZE)
            data = pickle.loads(data)
            self.addresses.add(address)
            self._on_receive(data, address)
            if isinstance(data, dict) and data.get('disconnect'):
                self.addresses.remove(address)

    def broadcast_data(self, data):
        "send data to all clients"
        message = pickle.dumps(data)
        for address in self.addresses:
            self.socket.sendto(message, address)

    def broadcast_to(self, address, data):
        "send data to a specific address"
        message = pickle.dumps(data)
        self.socket.sendto(message, address)

    def close(self):
        "Close the server"
        self.socket.close()
