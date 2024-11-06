"Basic helper object structures"

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

    def draw_on_screen(self, screen):
        "define behavior for drawing on screen"
        self._circle.draw_on_screen(screen)
        screen.draw_text(self.name, self._circle.x, self._circle.y)

    def move(self, delta, speed):
        "move player"
        self._circle.x += delta.x * speed
        self._circle.y += delta.y * speed

    def to_dict(self):
        "convert to dictionary"
        return {
            "circle": self._circle.to_dict(),
            "name": self.name,
            "id": self.id,
            "score": self.score,
        }

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

    def add_player(self, player):
        "add a player to the game"
        self.players[player.id] = player

    def remove_player(self, player_id):
        "remove a player from the game"
        self.players.pop(player_id)

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
