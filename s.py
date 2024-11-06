"some unclear description"
import socket
from Objects import Circle, Player, Vector, IdSet, GameState
from Network import Server

class ServerGame:
    "Some unclear description"
    def __init__(self):
        self.game_state = GameState.default()
        self.server = Server(socket, 'localhost', 12345)
        self.ids = IdSet()
        self.client_player_mapping = {}
        self.server.on_receive(self.process_request)
        self.server.listen_loop()


    def process_request(self, data, adress):
        "handle callback from servers listen() method"
        self.server.broadcast_data(data)
        if not data.get("type"):
            return
        match data["type"]:
            case "c_connect":
                self.handle_new_connection(adress)
            case "c_movement":
                self.handle_movement(adress, Vector.from_dict(data["delta"]))
            case "c_disconnect":
                self.handle_disconnection(adress)

    def broadcast_update(self):
        "send to all clients the latest game state"
        self.server.broadcast_data({"type": "s_update", "game_state": self.game_state.to_dict()})

    def handle_new_connection(self, adress):
        "process request of new client joining the game"
        i_d = self.ids.newid()
        self.ids.add(i_d)
        self.client_player_mapping[adress] = i_d
        self.game_state.add_player(Player(Circle(50, 50, 15, (0, 0, 255)), str(i_d), 0, i_d))
        welcome_data = {
            "type": "s_welcome",
            "id": i_d,
            "game_state": self.game_state.to_dict()
        }
        self.server.broadcast_to(adress, welcome_data)
        self.broadcast_update()

    def handle_movement(self, adress, delta):
        "process request of client movement"
        delta = Vector.binarize(delta) 
        self.game_state.players[self.client_player_mapping[adress]].move(delta, 10)
        self.server.broadcast_data({"type": "s_update", "game_state": self.game_state.to_dict()})

    def handle_disconnection(self, adress):
        "process statement of client disconnecting"
        self.ids.discard(self.client_player_mapping[adress])
        self.game_state.remove_player(self.client_player_mapping[adress])
        self.broadcast_update()
        self.client_player_mapping.pop(adress)

ServerGame()
