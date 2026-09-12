"""Module for managing server-side game logic and network communication in the agar.io game."""
from threading import Timer
import random
from GameState import GameState
from Objects import Player, Vector, IdSet
from Network import Server
from NameMaker import NameMaker

class ServerGame:
    """Manages the communication and syncronisation of game state across all clients""" 
    FOOD_SPAWN = {
        "start_x": -2000,
        "start_y" : - 1500,
        "end_x": 2000,
        "end_y": 1500,
        "initial_frequency": 50,
        "pushing_frequency": 5
    }
    PLAYER_SPAWN = {
        "start_x": -400, 
        "start_y": -300, 
        "end_x": 400,
        "end_y": 300,
        "radius": 10,
        "offset": 40
    }
    SPEED_FACTOR = 17
    SPEED_EXPONENT = -0.231
    @staticmethod
    def calculate_speed(r):
        "calculate speed of player based on radius of player"
        k = ServerGame.SPEED_FACTOR
        n = ServerGame.SPEED_EXPONENT
        return k * (r ** n)


    """Manages server-side state, player interactions, and network communication."""
    def __init__(self, socket_library):
        self.game_state = GameState.default()
        self.game_state.fill(
            ServerGame.FOOD_SPAWN["start_x"], 
            ServerGame.FOOD_SPAWN["start_y"], 
            ServerGame.FOOD_SPAWN["end_x"], 
            ServerGame.FOOD_SPAWN["end_y"], 
            ServerGame.FOOD_SPAWN["initial_frequency"])
        self.make_food_constantly()
        self.server = Server(socket_library, 'localhost', 12345)
        self.ids = IdSet()
        self.client_player_mapping = {}
        self.server.on_receive(self.process_request)
        self.server.listen_loop()

    def process_request(self, data, address):
        """handle callback from servers listen() method"""
        self.server.broadcast_data(data)
        if not data.get("type"):
            return
        match data["type"]:
            case "c_connect":
                self.handle_new_connection(address)
            case "c_movement":
                self.handle_movement(address, Vector.from_dict(data["delta"]))
            case "c_disconnect":
                self.handle_disconnection(address)

    def broadcast_update(self):
        """send to all clients the latest game state"""
        self.server.broadcast_data({"type": "s_update", "game_state": self.game_state.to_dict()})

    def handle_new_connection(self, address):
        """process request of new client joining the game"""
        uid = self.ids.newid()
        self.ids.add(uid)
        self.client_player_mapping[address] = uid
        self.game_state.add_player(self._generate_new_player(uid))
        self.server.broadcast_to(address, self._make_welcome_data(uid))
        self.broadcast_update()

    def handle_movement(self, address, delta):
        """process request of client movement"""
        delta = Vector.binarize(delta)
        player_id = self.client_player_mapping[address]
        player = self.game_state.players[player_id]
        self.game_state.move_player(player_id, delta, ServerGame.calculate_speed(player.getr()))
        self.check_for_collision(player_id)
        self.check_for_feeding(player_id)
        self.broadcast_update()

    def handle_disconnection(self, address):
        """process statement of client disconnecting"""
        self.ids.discard(self.client_player_mapping[address])
        self.game_state.remove_player(self.client_player_mapping[address])
        self.broadcast_update()
        self.client_player_mapping.pop(address)

    def _make_welcome_data(self, uid):
        """make data to send to new client"""
        return {
            "type": "s_welcome",
            "id": uid,
            "game_state": self.game_state.to_dict()
        }

    def check_for_collision(self, player_id):
        """check if player collided with another player"""
        status, enemy = self.game_state.get_collision_for_player(player_id)
        if status == "conquer":
            reward = enemy.getr()
            self.game_state.respawn_player(enemy.id, self.generate_new_player_circle())
            self.game_state.feed_player(player_id, reward, reward)
        elif status == "defeat":
            reward = self.game_state.players[player_id].getr()
            self.game_state.respawn_player(player_id, self.generate_new_player_circle())
            self.game_state.feed_player(enemy.id, reward, reward)


    def check_for_feeding(self, player_id):
        """check if player collided with food"""
        eaten_food = self.game_state.get_overlapping_food_for_player(player_id)
        for item in eaten_food:
            self.game_state.remove_food(item)
        self.game_state.feed_player(player_id, len(eaten_food), len(eaten_food))

    def generate_new_player_circle(self):
        "handle spawning player object position"
        color = (random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))
        return self.game_state.generate_valid_circle_for_player_with_color(
            ServerGame.PLAYER_SPAWN["start_x"],
            ServerGame.PLAYER_SPAWN["end_x"],
            ServerGame.PLAYER_SPAWN["start_y"],
            ServerGame.PLAYER_SPAWN["end_y"],
            color,
            ServerGame.PLAYER_SPAWN["radius"],
            ServerGame.PLAYER_SPAWN["offset"])

    def make_food_constantly(self):
        """Continuously spawn food at regular intervals"""
        self.game_state.fill(
            ServerGame.FOOD_SPAWN["start_x"],
            ServerGame.FOOD_SPAWN["start_y"],
            ServerGame.FOOD_SPAWN["end_x"],
            ServerGame.FOOD_SPAWN["end_y"],
            ServerGame.FOOD_SPAWN["pushing_frequency"]
        )
        print("food added!!!")
        Timer(5.0, self.make_food_constantly).start()

    def _generate_new_player(self, uid):
        """generate new player"""
        return Player(self.generate_new_player_circle(), NameMaker.new(), 0, uid)
