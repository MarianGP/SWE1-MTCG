# Monster Trading Card Game


## Technical steps
1) Game structure definition: Classes, Parameter, Enum, Parameters, etc
2) Unit test definition which will guide the definition of functions to fulfill its purpose.
3) Server structure definition: Server, RequestContext, RequestHandler, Response, 
4) Endpoints definition in RequestHandler
5) Allow threads in the server to handle multiple client requests
6) Creation of DB and tables for data persistence
7) Definition of trading system based on requirements

## Design

### 1) Diagrams

### Game

Original
![Game Diagram](assets/img/game_diagram.jpg) 

Final
![Game Diagram2](assets/img/game_diagram2.jpg)

### Server

![Game_Server Diagram](assets/img/game_server_diagram.jpg)


### Server

![DB Diagram](assets/img/DB2.jpg )

### 2) Structure
Division of project into 2 main section:
- Game
- Server




### Failures
1) 
Problem: Try to define modify simultaneously gameController in GameServer
If a request hasn't been finished another request from a different client won't be run until the first one is finished.
Solution: 

2) Couldn't parse with Mapper 

### Selected solutions




### Unit tests 

chosen and why the tested code is critical

### Integration test

### Time tracking

too much


#### Git-history


