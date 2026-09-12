"Module for Complex Data Structure for storing what determines a game"
import random
from Objects import Circle, Player, Food

class GameState:
    """Store the information from which a game can be determined"""

    def __init__(self, players, food):
        self.players = players
        self.food = food

    def to_dict(self):
        """convert to dictionary"""
        return {
            "food": [item.to_dict() for item in self.food],  
            "players": {key: value.to_dict() for key, value in self.players.items()}
        }

    def get_ranking(self):
        """Get sorted ranking of all players by score"""
        rankings = [player.get_ranking() for player in self.players.values()]
        return sorted(rankings, key=lambda x: x["score"], reverse=True)

    def add_player(self, player):
        """add a player to the game"""
        self.players[player.id] = player

    def remove_player(self, player_id):
        """remove a player from the game"""
        self.players.pop(player_id)

    def remove_food(self, food_id):
        """remove a food item from the game"""
        self.food.pop(food_id)

    def respawn_player(self, player_id, circle):
        """respawn a player"""
        player = self.players[player_id]
        player.respawn(circle)

    def fill(self, x1, y1, x2, y2, n, radius=5):
        """Fill a rectangular field with random food objects"""
        for _ in range(n):
            x = random.uniform(x1, x2)
            y = random.uniform(y1, y2)
            r = random.randint(50, 200)
            g = random.randint(50, 200)
            b = random.randint(50, 200)
            self.food.append(Food(x, y, radius, (r, g, b)))

    def move_player(self, player_id, delta, speed):
        """move a player"""
        self.players[player_id].move(delta, speed)

    def feed_player(self, player_id, radius_increase, score_increase):
        """feed a player"""
        self.players[player_id].feed(radius_increase, score_increase)

    def get_collision_for_player(self, player_id):
        """get collisions for a player"""
        player = self.players[player_id]
        for i, enemy in self.players.items():
            if i == player_id:
                continue
            if Circle.is_circle_inside_circle(enemy.get_circle(), player.get_circle()):
                return ("conquer", enemy)
            if Circle.is_circle_inside_circle(player.get_circle(), enemy.get_circle()):
                return ("defeat", enemy)

        return ("idle", None)

    def get_overlapping_food_for_player(self, player_id):
        """get food item that overlaps with a player"""
        player = self.players[player_id]
        res = []
        for i, food_item in enumerate(self.food):
            if Circle.is_circle_inside_circle(food_item, player.get_circle()):
                res.append(i)
        return res

    def generate_valid_circle_for_player_with_color(self, x1, x2, y1, y2, color, radius, offset):
        "find a valid circle for a person to spawn into"
        while True:
            x = random.randint(x1, x2)
            y = random.randint(y1, y2)
            if(self.is_generated_position_valid(x, y, radius + offset)):
                return Circle(x, y, radius, color)

    def is_generated_position_valid(self, x, y, mindist):
        "check whether a given point is far enough from existing players"
        for enemy in self.players.values():
            ex = enemy.getx()
            ey = enemy.gety()
            er = enemy.getr()
            if (ex - x) ** 2 + (ey - y) ** 2 < (mindist + er) ** 2:
                return False
        return True

    @classmethod
    def default(cls):
        """create empty game state"""
        return cls({}, [])

    @classmethod
    def from_dict(cls, data):
        """create instance from dictionary"""
        players = {key: Player.from_dict(value) for key, value in data["players"].items()}
        food = [Food.from_dict(entry) for entry in data["food"]]
        return cls(players=players, food=food)
