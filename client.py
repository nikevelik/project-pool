import socket as socket_library
import pygame as pygame_library
from ClientGame import ClientGame
client_game = ClientGame(pygame_library, socket_library)
client_game.start()