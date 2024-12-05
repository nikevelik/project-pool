# agar.io

**[Title]**  
Agar.io  

**[Description]**  
The project is a recreation of the popular online multiplayer PvP game, where each player starts as a small circle and grows by consuming other players. The more opponents you devour, the larger you become, and as your size increases, you climb up the leaderboard. The largest or last player standing wins.  

**[Functionalities]**  

- **Startup**  
  - Users need to download the system source code on a computer that supports Python and its dependencies.  
  - One user must launch the game server by running a command or opening an executable server file.  
  - Players join by starting the game executable (launch command).  
  - **Limitation**: The game will be tested on a single device, meaning multiplayer functionality across multiple devices won’t be validated.  
  - This is implemented using a client-server model where the server handles player network requests via the `socket.io` library and multithreading.

- **Gameplay**  
  - Once the server starts, each player is assigned a circular avatar with an initial radius on a plane.  
  - Players can move around, consume food objects, and absorb other players. Growing makes them larger but slower.  
  - Absorbed players automatically respawn.  
  - A leaderboard ranks players by size.  
  - Player actions are synchronized in real-time across the game state for all participants.  
  - This is achieved through a class managing the game state, broadcasting updates to all nodes when necessary.

- **Exit**  
  - Players can leave the game at any time.

- **End of Game**  
  - The game ends when all players leave or the server shuts down.

**[Milestones]**  

- **GameState Class**  
  - Composed of or indirectly includes:  
    - **Player**: Manages player states.  
    - **Food**: Manages food states.  
    - **Circle**: Handles circular objects.  
    - **Vector**: Manages coordinate pairs.

- **Screen Class**: Manages UI visualization.  
- **Node Interface**: Manages network nodes.  
- **Client Interface**: Manages client nodes.  
- **Server Interface**: Manages server nodes.  
- **Server Class**: Manages the game server.  
- **Client Class**: Manages players in the game.

**[Estimated Time]**  
47 man-hours  

**[Technologies Used]**  
- **Interface**: Pygame  
- **Client/Server Communication**: socket.io, pickle  
- **Multithreading**: threading  
