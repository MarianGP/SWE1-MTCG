# Monster Trading Card Game

- Author: Marian García Peters - if19b0333

## Technical steps and Design

### 1) Game structure definition: Interfaces, Classes, Parameter, Enum, Parameters.

- Preparations of UML diagrams.
- Separation of the project in 2 main sections: game, rest-server.
- Definition of interfaces (functionality which would be included in each class).
- Working with enums makes the code more readable and it's not difficult to implement.

### 2) Unit test for TDD

- To guide the development of class methods.
- To test each method and to proof after posterior changes that code didn't break.
- Extremely important to test the attack between card and to get the expected result
depending on cards: instance of Cards (MonsterCard/SpellCard), Element, Type and damage (complex logic).

### 3) Server structure definition: Server, RequestContext, RequestHandler, Response, Controllers

- Connection using websockets and basic authentication.
- Each new connection will initiate a new thread.
- Request data gets handled and stored in RequestContext and its information is used to handle the request.

### 4) Endpoints definition in RequestHandler

- Accordingly to URL/Path and HttpMethod.
- Error handling in case of unauthorized access modifying the response status (StatusCode: code, name).

### 5) Allow threads in the server to handle multiple client requests

- Implement threads and make collections and variables thread safe. To prevent race condition.

### 6) Creation of DB and tables for data persistence

- Creation of tables and db structure for persistence of data.
- Tables: card, session, trade, user.
- Include db connection 

### 7) Definition of trading system based on requirements

- Simple comparison between offered card features and card being traded.
- 4 options: add new trade, delete trade, trade cards, and to buy a card being traded by paying 5 coins.

### 8) Election of collections

a) `Map`s: for storing key-value pairs `Hashmap<Key,Value>`
    
- Http request header.
- Map Card's class to Json.

b) `List`s:

- Stack, deck, and many other list of objects in general.
- `ArrayBlockingQueue<User>` for add 2 player to the same battle.
This collection gives access to extra methods like: pop(), 

c) Arrays

- `String[]` Strings of array for spliting URL

### 9) Files

- To practice the use of files I included the user of ` FileInputStream ` to read
the database connection properties from a separated .properties document

### Failures
1) Problem 1:
   
Try to modify simultaneously gameController in GameServer
If a request hasn't been finished another request from a different client won't be run until the first one is finished.

Solution: Threading

2) Problem 2:
   
Couldn't parse with jackson directly to the wanted class.

Solution: Create ClassData classes (package serializer), which won't have constructors (Compiler error) 

3) Problem 3:

Difficulty to find the right package structure. 

Solution: Ask to more experienced collegues.


### Unit tests 

- To make sure methods work as intended.
- To detect anormalities and bugs.
- To test after code changes.
- Ideal coverage 80% of code: 47 test (not all methods covered)
- To test if the program works as intended.

### Integration test

- Postman was an excellent tool for this purpose. It gives the possibility to 
run automated test. This way the response body and response status can be compared.

- curl: didn't give the option of running test but allows to check if the expected 
and fuctionality was properly implemented. 
  
- Used to test in real use scenarios of the program (requirements).

### Time tracking

~ 130-150 hrs

I underestimated the time needed for documentarion and the curl preparation. I had only
worked with postman before the "Abgabe".

### Git-history

https://github.com/MarianGP/SWE1-MTCG

### Additional documents

Postman and curl test are to be found in /assets directory.



