"some unclear description"
import socket
import pygame
from Objects import Vector, Camera
from GameState import GameState
from Screen import Screen
from Network import Client

class ClientGame:
    "Manage the communication between the game state, input, output and client's networking logic"
    FPS = 30
    def __init__(self, pygame_obj, socket_obj):
        self._id = None
        self._game_state = GameState.default()
        self._pygame = pygame_obj
        self._client = Client(socket_obj, 'localhost', 12345)
        self._screen = None

    def start(self):
        "start the game"
        self._client.on_receive(self._process_request)
        self._client.listen()
        self._client.send_data({'type': 'c_connect'})
        self._pygame.init()
        self._screen = Screen(self._pygame)
        self._main_loop()

    def _process_request(self, data):
        "handle callback from client's listen() method"
        if not data.get("type"):
            return
        match data["type"]:
            case "s_welcome":
                self._id = data["id"]
                self._game_state = GameState.from_dict(data["game_state"])
            case "s_update":
                self._game_state = GameState.from_dict(data["game_state"])
                print(self._game_state.to_dict())


    def _main_loop(self):
        "Run the main game loop"
        running = True
        clock = self._pygame.time.Clock()
        try: 
            while running:
                for event in self._pygame.event.get():
                    if event.type == self._pygame.QUIT:
                        running = False

                self._handlekey()
                self._screen.set_camera(self._fetch_camera())
                self._screen.draw(self._game_state)
                clock.tick(ClientGame.FPS)
        finally: 
            self._client.send_data({"type": "c_disconnect"})
            self._pygame.quit()
            self._client.disconnect()

    def _handlekey(self):
        "Handle keyboard input"
        delta = Vector(0, 0)
        keys = self._pygame.key.get_pressed()
        if keys[self._pygame.K_UP] or keys[self._pygame.K_w]:
            delta.y -= 1
        if keys[self._pygame.K_DOWN] or keys[self._pygame.K_s]:
            delta.y += 1
        if keys[self._pygame.K_LEFT] or keys[self._pygame.K_a]:
            delta.x -= 1
        if keys[self._pygame.K_RIGHT] or keys[self._pygame.K_d]:
            delta.x += 1
        if keys[self._pygame.K_SPACE]:
            print("space")
        if (delta.x != 0 or delta.y != 0):
            self._client.send_data({"type": "c_movement", "delta": delta.to_dict()})

    def _fetch_camera(self):
        "calculate camera position for drawing"
        player = self._game_state.players.get(self._id)
        return Camera(player.getx(), player.gety()) if player else Camera(0, 0)

client_game = ClientGame(pygame, socket)
client_game.start()
