"some unclear description"
import socket
from Objects import Circle, Player, Vector, IdSet, GameState
from Network import Server
from NameMaker import NameMaker

class ServerGame:
    "Some unclear description"
    def __init__(self):
        self.game_state = GameState.default()
        self.game_state.fill(-2000, -15000, 2000, 1500, 500)
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
        self.game_state.add_player(self._generate_new_player(i_d))
        self.server.broadcast_to(adress, self._make_welcome_data(i_d))
        self.broadcast_update()

    def handle_movement(self, adress, delta):
        "process request of client movement"
        delta = Vector.binarize(delta) 
        player = self.client_player_mapping[adress]
        self.game_state.move_player(player, delta, 10)
        self.check_for_collision(player)
        self.check_for_feeding(player)
        self.broadcast_update()

    def handle_disconnection(self, adress):
        "process statement of client disconnecting"
        self.ids.discard(self.client_player_mapping[adress])
        self.game_state.remove_player(self.client_player_mapping[adress])
        self.broadcast_update()
        self.client_player_mapping.pop(adress)

    def _make_welcome_data(self, i_d):
        "make data to send to new client"
        return {
            "type": "s_welcome",
            "id": i_d,
            "game_state": self.game_state.to_dict()
        }
    
    def check_for_collision(self, player_id):
        "check if player collided with another player"
        status, enemy = self.game_state.get_collision_for_player(player_id)
        if status == "conquer":
            reward = enemy.getr()
            self.game_state.respawn_player(enemy.id, Circle(50, 50, 15, (0, 0, 255)))
            self.game_state.feed_player(player_id, reward, reward)
        elif status == "defeat":
            reward = self.game_state.players[player_id].getr()
            self.game_state.respawn_player(player_id, Circle(50, 50, 15, (0, 0, 255)))
            self.game_state.feed_player(enemy.id, reward, reward)


    def check_for_feeding(self, player_id):
        "check if player collided with food"
        eaten_food = self.game_state.get_overlapping_food_for_player(player_id)
        for item in eaten_food:
            self.game_state.remove_food(item)
        
        self.game_state.feed_player(player_id, len(eaten_food), len(eaten_food))

    def _generate_new_player(self, i_d):
        "generate new player"
        return Player(Circle(50, 50, 15, (0, 0, 255)), NameMaker.new(), 0, i_d)
        

ServerGame()
