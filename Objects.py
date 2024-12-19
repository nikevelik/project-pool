"""Basic helper object structures"""
class Circle:
    """Store a quadruple of x, y, radius, color as a basic structure"""
    def __init__(self, x, y, r, color):
        self.x = x
        self.y = y
        self.r = r
        self.color = color

    def draw_on_screen(self, screen):
        """define behavior for drawing on screen"""
        screen.draw_circle(self)

    def to_dict(self):
        """convert to dictionary"""
        return {
            "x": self.x,
            "y": self.y,
            "r": self.r,
            "color": self.color
        }
    @classmethod
    def from_dict(cls, data):
        """create instance from dictionary"""
        return cls(x=data["x"], y=data["y"], r=data["r"], color=tuple(data["color"]))

    @staticmethod
    def is_circle_inside_circle(a, b):
        """check if circle1 is inside circle2"""
        if a.r > b.r:
            return False
        dx = (a.x - b.x) ** 2
        dy = (a.y - b.y) ** 2
        return dx + dy <= (b.r - a.r) ** 2


class Player:
    """Manage a game player"""
    def __init__(self, circle, name, score, uid):
        self._circle = circle
        self.name = name
        self.score = score
        self.id = uid

    def get_circle(self):
        """get circle"""
        return self._circle

    def getx(self):
        """get x position"""
        return self._circle.x

    def gety(self):
        """get y position"""
        return self._circle.y

    def getr(self):
        """get the radius of the player's circle"""
        return self._circle.r

    def draw_on_screen(self, screen):
        """define behavior for drawing on screen"""
        self._circle.draw_on_screen(screen)
        screen.draw_text(self.name, self._circle.x, self._circle.y)

    def move(self, delta, speed):
        """move player"""
        self._circle.x += delta.x * speed
        self._circle.y += delta.y * speed

    def feed(self, radius_increase, score_increase):
        """feed player"""
        self.score += score_increase
        self._circle.r += radius_increase

    def get_ranking(self):
        """provide a dictionary with player's current ranking details"""
        return {
            "name": self.name,
            "score": self.score
        }

    def to_dict(self):
        """convert to dictionary"""
        return {
            "circle": self._circle.to_dict(),
            "name": self.name,
            "id": self.id,
            "score": self.score,
        }

    def respawn(self, circle):
        """respawn player"""
        self._circle = circle

    @classmethod
    def from_dict(cls, data):
        """create instance from dictionary"""
        circle = Circle.from_dict(data["circle"])
        name = data["name"]
        uid = data["id"]
        score = data["score"]
        return cls(circle=circle, name=name, uid=uid, score=score)

class Food(Circle):
    """Manage a game food item"""

class Vector:
    """Store a pair of x, y coordinates as a basic structure"""
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def to_dict(self):
        """convert to dictionary"""
        return {"x": self.x, "y": self.y}

    @classmethod
    def from_dict(cls, data):
        """create instance from dictionary"""
        return cls(x=data["x"], y=data["y"])

    @classmethod
    def binarize(cls, vector):
        """convert vector to binary vector"""
        x = 0 if vector.x == 0 else vector.x/abs(vector.x)
        y = 0 if vector.y == 0 else vector.y/abs(vector.y)
        return cls(x, y)

class Camera(Vector):
    """Manage a screen camera"""


class IdSet:
    """Manage a set of natural identifiers"""
    def __init__(self):
        self.numbers = set()

    def newid(self):
        """get the smallest unused identifier"""
        smallest_position = 0
        while smallest_position in self.numbers:
            smallest_position += 1
        return smallest_position

    def add(self, number):
        """add a number to the set"""
        self.numbers.add(number)

    def discard(self, number):
        """remove a number from the set"""
        self.numbers.discard(number)
