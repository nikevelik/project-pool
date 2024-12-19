"Basic helper object structures"
import random

class Circle:
    "Store a quadruple of x, y, radius, color as a basic structure"
    def __init__(self, x, y, r, color):
        self.x = x
        self.y = y
        self.r = r
        self.color = color

    def draw_on_screen(self, screen):
        "define behavior for drawing on screen"
        screen.draw_circle(self)

    def to_dict(self):
        "convert to dictionary"
        return {
            "x": self.x,
            "y": self.y,
            "r": self.r,
            "color": self.color
        }
    @classmethod
    def from_dict(cls, data):
        "create instance from dictionary"
        return cls(x=data["x"], y=data["y"], r=data["r"], color=tuple(data["color"]))

    @staticmethod
    def is_circle_inside_circle(a, b):
        "check if circle1 is inside circle2"
        if a.r > b.r:
            return False
        dx = (a.x - b.x) ** 2
        dy = (a.y - b.y) ** 2
        return dx + dy <= (b.r - a.r) ** 2


class Player:
    "Manage a game player"
    def __init__(self, circle, name, score, i_d):
        self._circle = circle
        self.name = name
        self.score = score
        self.id = i_d

    def getx(self):
        "get x position"
        return self._circle.x

    def gety(self):
        "get y position"
        return self._circle.y

    def getr(self):
        return self._circle.r

    def draw_on_screen(self, screen):
        "define behavior for drawing on screen"
        self._circle.draw_on_screen(screen)
        screen.draw_text(self.name, self._circle.x, self._circle.y)

    def move(self, delta, speed):
        "move player"
        self._circle.x += delta.x * speed
        self._circle.y += delta.y * speed

    def feed(self, radius_increase, score_increase):
        "feed player"
        self.score += score_increase
        self._circle.r += radius_increase

    def get_ranking(self):
        return {
            "name": self.name,
            "score": self.score
        }

    def to_dict(self):
        "convert to dictionary"
        return {
            "circle": self._circle.to_dict(),
            "name": self.name,
            "id": self.id,
            "score": self.score,
        }

    def respawn(self, circle):
        "respawn player"
        self._circle = circle

    @classmethod
    def from_dict(cls, data):
        "create instance from dictionary"
        circle = Circle.from_dict(data["circle"])
        name = data["name"]
        i_d = data["id"]
        score = data["score"]
        return cls(circle=circle, name=name, i_d=i_d, score=score)

class Food(Circle):
    "Manage a game food item"

class Vector:
    "Store a pair of x, y coordinates as a basic structure"
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def to_dict(self):
        "convert to dictionary"
        return {"x": self.x, "y": self.y}

    @classmethod
    def from_dict(cls, data):
        "create instance from dictionary"
        return cls(x=data["x"], y=data["y"])

    @classmethod
    def binarize(cls, vector):
        "convert vector to binary vector"
        x = 0 if vector.x == 0 else vector.x/abs(vector.x)
        y = 0 if vector.y == 0 else vector.y/abs(vector.y)
        return cls(x, y)

class Camera(Vector):
    "Manage a screen camera"


class IdSet:
    "Manage a set of natural identifiers"
    def __init__(self):
        self.numbers = set()

    def newid(self):
        "get the smallest unused identifier"
        smallest_position = 0
        while smallest_position in self.numbers:
            smallest_position += 1
        return smallest_position

    def add(self, number):
        "add a number to the set"
        self.numbers.add(number)

    def discard(self, number):
        "remove a number from the set"
        self.numbers.discard(number)

class GameState:
    "Store the information from which a game can be determined"

    def __init__(self, players, food):
        self.players = players
        self.food = food

    def to_dict(self):
        "convert to dictionary"
        return {
            "food": [item.to_dict() for item in self.food],  
            "players": {key: value.to_dict() for key, value in self.players.items()}
        }

    def get_ranking(self):
        "Get sorted ranking of all players by score"
        rankings = [player.get_ranking() for player in self.players.values()]
        return sorted(rankings, key=lambda x: x["score"], reverse=True)

    def add_player(self, player):
        "add a player to the game"
        self.players[player.id] = player

    def remove_player(self, player_id):
        "remove a player from the game"
        self.players.pop(player_id)

    def remove_food(self, food_id):
        "remove a food item from the game"
        self.food.pop(food_id)

    def respawn_player(self, player_id, circle):
        "respawn a player"
        player = self.players[player_id]
        player.respawn(circle)

    def fill(self, x1, y1, x2, y2, n, radius=5):
        "Fill a rectangular field with random food objects"
        for _ in range(n):
            x = random.uniform(x1, x2)
            y = random.uniform(y1, y2)
            self.food.append(Food(x, y, radius, (random.randint(50, 200), random.randint(50, 200), random.randint(50, 200))))

    def move_player(self, player_id, delta, spped):
        "move a player"
        self.players[player_id].move(delta, spped)

    def feed_player(self, player_id, radius_increase, score_increase):
        "feed a player"
        self.players[player_id].feed(radius_increase, score_increase)
    
    def get_collision_for_player(self, player_id):
        "get collisions for a player"
        player = self.players[player_id]
        for i, enemy in self.players.items():
            if i == player_id:
                continue
            if Circle.is_circle_inside_circle(enemy._circle, player._circle):
                return ("conquer", enemy)
            if Circle.is_circle_inside_circle(player._circle, enemy._circle):
                return ("defeat", enemy)

        return ("idle", None)


    def get_overlapping_food_for_player(self, player_id):
        "get food item that overlaps with a player"
        player = self.players[player_id]
        res = []
        for i, food_item in enumerate(self.food):
            if Circle.is_circle_inside_circle(food_item, player._circle):
                res.append(i)
        
        return res
        

    @classmethod
    def default(cls):
        "create empty game state"
        return cls({}, [])

    @classmethod
    def from_dict(cls, data):
        "create instance from dictionary"
        players = {key: Player.from_dict(value) for key, value in data["players"].items()}
        food = [Food.from_dict(entry) for entry in data["food"]]
        return cls(players=players, food=food)
