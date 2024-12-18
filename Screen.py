from Objects import Camera

class Screen:
    "Wraps pygame screen drawing and management logic"
    BACKGROUND_COLOR = (255, 255, 255)
    FONT_COLOR = (0, 0, 0)
    FONT_SIZE = 12
    GRID_COLOR = (128, 128, 128)
    GRID_SIZE = 200
    WIDTH = 800
    HEIGHT = 600

    def __init__(self, pygame):
        self._pygame = pygame
        self._width = Screen.WIDTH
        self._height = Screen.HEIGHT
        self._camera = Camera(self._width / 2, self._height / 2)
        self._map = self._pygame.display.set_mode((self._width, self._height))
        self._pygame.display.set_caption("Pygame Drawing Demo")
        self._font = self._pygame.font.Font(self._pygame.font.get_default_font(), Screen.FONT_SIZE)

    def _offset_x(self, x):
        "calculate relative x position"
        return int(x - self._camera.x + self._width / 2)

    def _offset_y(self, y):
        "calculate relative y position"
        return int(y - self._camera.y + self._height / 2)

    def set_camera(self, camera):
        "update the camera position"
        self._camera = camera

    def draw(self, game_state):
        "draw the game state from camera's perspective"
        self._map.fill(Screen.BACKGROUND_COLOR)
        self.draw_grid()

        self.draw_list(0, 0, list(dat["name"] + ": " + str(dat["score"]) for dat in game_state.get_ranking()))
        
        for player in game_state.players.values():
            player.draw_on_screen(self)
        for food_item in game_state.food:
            food_item.draw_on_screen(self)
        self._pygame.display.flip()

    def draw_list(self, x, start_y, datata):
        "Draw a vertical list of text items starting at given coordinates"
        i = start_y
        for dat in datata:
            self.draw_text(dat, x, i, False)
            i += 2*self.FONT_SIZE

    def draw_text(self, text, x, y, use_camera_offset=True):
        "draw text on at a given position, absolute or relative to camera"
        if use_camera_offset:
            relative_x = self._offset_x(x)
            relative_y = self._offset_y(y)
            if relative_x > 0 and relative_y > 0 and relative_x < self._width and relative_y < self._height:
                self._map.blit(self._font.render(text, True, Screen.FONT_COLOR), (relative_x, relative_y))
        else:
            self._map.blit(self._font.render(text, True, Screen.FONT_COLOR), (x, y))

    def draw_circle(self, circle):
        "draw circle object on screen"
        relative_x = self._offset_x(circle.x)
        relative_y = self._offset_y(circle.y)
        if relative_x + circle.r > 0 and relative_y + circle.r > 0 and relative_x - circle.r < self._width and relative_y - circle.r < self._height:
            self._pygame.draw.circle(self._map, circle.color, (relative_x, relative_y), circle.r)

    def draw_grid(self):
        "draw grid lines on screen"
        start_x = -int(self._camera.x % self.GRID_SIZE)
        start_y = -int(self._camera.y % self.GRID_SIZE)
        for x in range(start_x, self._width, self.GRID_SIZE):
            self._pygame.draw.line(self._map, self.GRID_COLOR, (x, 0), (x, self._height))

        for y in range(start_y, self._height, self.GRID_SIZE):
            self._pygame.draw.line(self._map, self.GRID_COLOR, (0, y), (self._width, y))
