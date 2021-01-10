@echo off

REM --------------------------------------------------
REM Monster Trading Cards Game
REM --------------------------------------------------
title Monster Trading Cards Game
echo CURL Testing for Monster Trading Cards Game
echo.

REM --------------------------------------------------
echo 1) Create Users (Registration)
REM Create User
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"daniel\"}"

echo.
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"altenhof\", \"password\":\"markus\"}"

echo.
echo should fail:
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"daniel\"}"

echo.
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"different\"}"

echo.
echo.

REM --------------------------------------------------
echo 2) Login Users
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"daniel\"}"

echo.
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"altenhof\", \"password\":\"markus\"}"

echo.
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"admin\", \"password\":\"istrator\"}"

echo.
echo should fail:
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"different\"}"



REM --------------------------------------------------
echo 3) create packages (done by "admin")

echo should fail:
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic player1-mtcgToken" -d "[{\"id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"North Wind\",\"damage\":90.0},{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Goblin\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Dragon\",\"element\":\"Fire\",\"name\":\"Extrem\",\"damage\":100.0},{\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"fdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"

echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"North Wind\",\"damage\":90.0},{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Goblin\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Dragon\",\"element\":\"Fire\",\"name\":\"Extrem\",\"damage\":100.0},{\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"fdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"


echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"d1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"North Wind\",\"damage\":90.0},{\"id\":\"d845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Wizzard\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"d99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Dragon\",\"element\":\"Fire\",\"name\":\"Extrem\",\"damage\":100.0},{\"id\":\"de85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"ddfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"


echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"y1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"North Wind\",\"damage\":90.0},{\"id\":\"y845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Goblin\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":70.0},{\"id\":\"y99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Dragon\",\"element\":\"Fire\",\"name\":\"Extrem\",\"damage\":100.0},{\"id\":\"ye85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"ydfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"


echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"ry1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"Lala Land\",\"damage\":90.0},{\"id\":\"ry845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Wizzard\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"ry99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Knight\",\"element\":\"Water\",\"name\":\"Super\",\"damage\":100.0},{\"id\":\"rye85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"rydfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"


echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"jy1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"North Wind\",\"damage\":90.0},{\"id\":\"jy845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Goblin\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":70.0},{\"id\":\"jy99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Dragon\",\"element\":\"Fire\",\"name\":\"Extrem\",\"damage\":100.0},{\"id\":\"jye85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"jtydfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"


echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"try1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"Lala Land\",\"damage\":90.0},{\"id\":\"try845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Wizzard\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"try99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Knight\",\"element\":\"Water\",\"name\":\"Super\",\"damage\":100.0},{\"id\":\"trye85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"trydfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"


echo.
echo.

REM --------------------------------------------------
echo 4) acquire packages kienboec
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""

echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""

echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""

echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""

echo.
echo should fail (no money):
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""
echo.
echo.


REM --------------------------------------------------
echo 5) acquire packages altenhof
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo.
echo should fail (no package):
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo.
echo.


REM --------------------------------------------------
echo 6) add new packages
echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"uu1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"North Wind\",\"damage\":90.0},{\"id\":\"uu845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Goblin\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"uu99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Dragon\",\"element\":\"Fire\",\"name\":\"Extrem\",\"damage\":100.0},{\"id\":\"uue85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"uufdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"

echo.
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"ppd1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\",\"monsterType\":\"\",\"element\":\"Normal\",\"name\":\"North Wind\",\"damage\":90.0},{\"id\":\"ppd845f0dc7-37d0-426e-994e-43fc3ac83c08\",\"monsterType\":\"Wizzard\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"ppd99f8f8dc-e25e-4a95-aa2c-782823f36e2a\",\"monsterType\":\"Dragon\",\"element\":\"Fire\",\"name\":\"Extrem\",\"damage\":100.0},{\"id\":\"ppde85e3976-7c86-4d06-9a80-641c2019a79f\",\"monsterType\":\"\",\"element\":\"Fire\",\"name\":\"Supreme\",\"damage\":100.0},{\"id\":\"ppddfdd758f-649c-40f9-ba3a-8657f4b3439f\",\"monsterType\":\"\",\"element\":\"Water\",\"name\":\"Blue\",\"damage\":105.0}]"
echo.
echo.

REM --------------------------------------------------
echo 7) acquire newly created packages altenhof
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo.

echo safe 5 coins to buy a trade
echo.
echo.

REM --------------------------------------------------
echo 8) show all acquired cards kienboec
curl -X GET http://localhost:10001/cards --header "Authorization: Basic kienboec-mtcgToken"
echo should fail (no token)
curl -X GET http://localhost:10001/cards
echo.
echo.

REM --------------------------------------------------
echo 9) show all acquired cards altenhof
curl -X GET http://localhost:10001/cards --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 10) show unconfigured deck
curl -X GET http://localhost:10001/deck --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo.


REM --------------------------------------------------
echo 11) configure deck
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "[\"ppd1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"ddfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"d99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"d845f0dc7-37d0-426e-994e-43fc3ac83c08\"]"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: Basic altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "[\"ry1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"try845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"jtydfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"jy99f8f8dc-e25e-4a95-aa2c-782823f36e2a\"]"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.

echo should fail and show original from before:
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "[\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"fdd758f-649c-40f9-ba3a-8657f4b3439f\", \"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\"]"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo.
echo should fail ... only 3 cards set
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "[\"ppd1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"ddfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"d99f8f8dc-e25e-4a95-aa2c-782823f36e2a\"]"
echo.


REM --------------------------------------------------
echo 12) show configured deck
curl -X GET http://localhost:10001/deck --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/deck --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 13) show configured deck different representation
echo kienboec
curl -X GET http://localhost:10001/deck?format=plain --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.
echo altenhof
curl -X GET http://localhost:10001/deck?format=plain --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 14) edit user data
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Basic altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"password\": \"Kienboeck\",  \"bio\": \"me playin...\", \"image\": \":-)\"}"
echo.
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "{\"password\": \"Altenhofer\", \"bio\": \"me codin...\",  \"image\": \":-D\"}"
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo.
echo should fail:
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "{\"password\": \"Hoax\",  \"bio\": \"me playin...\", \"image\": \":-)\"}"
echo.
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"password\": \"Hoax\", \"bio\": \"me codin...\",  \"image\": \":-D\"}"
echo.
curl -X GET http://localhost:10001/users/someGuy  --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 15) stats
curl -X GET http://localhost:10001/stats --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/stats --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 16) scoreboard
curl -X GET http://localhost:10001/score --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 17a) battle
start /b "kienboec battle" curl -X POST http://localhost:10001/battles --header "Authorization: Basic kienboec-mtcgToken"
start /b "altenhof battle" curl -X POST http://localhost:10001/battles --header "Authorization: Basic altenhof-mtcgToken"
ping localhost -n 10 >NUL 2>NUL

REM --------------------------------------------------
echo 17b) battle2
start /b "kienboec battle" curl -X POST http://localhost:10001/battles --header "Authorization: Basic kienboec-mtcgToken"
start /b "altenhof battle" curl -X POST http://localhost:10001/battles --header "Authorization: Basic altenhof-mtcgToken"
ping localhost -n 10 >NUL 2>NUL

echo.
echo.
REM --------------------------------------------------
echo 18) Stats
echo kienboec
curl -X GET http://localhost:10001/stats --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo altenhof
curl -X GET http://localhost:10001/stats --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 19) scoreboard
curl -X GET http://localhost:10001/score --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.


REM --------------------------------------------------
echo 20) trade
echo check trading deals
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo create trading deal
curl -X POST http://localhost:10001/tradings --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"cardToTrade\": \"y99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"type\": \"monster\", \"minimumDamage\": 80}"
echo.
echo check trading deals
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo delete trading deals
curl -X DELETE http://localhost:10001/tradings/y99f8f8dc-e25e-4a95-aa2c-782823f36e2a --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.

REM --------------------------------------------------
echo 21) check trading deals
echo.
curl -X GET http://localhost:10001/tradings  --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.
curl -X POST http://localhost:10001/tradings --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"cardToTrade\": \"y99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"type\": \"monster\", \"minimumDamage\": 80}"
curl -X POST http://localhost:10001/tradings --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"cardToTrade\": \"jy845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"type\": \"spell\", \"minimumDamage\": 90}"
echo.
echo check trading deals
curl -X GET http://localhost:10001/tradings  --header "Authorization: Basic kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/tradings  --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo try to trade with yourself (should fail)
curl -X POST http://localhost:10001/tradings/y99f8f8dc-e25e-4a95-aa2c-782823f36e2a --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "\"ry845f0dc7-37d0-426e-994e-43fc3ac83c08\""
echo.
echo.
echo try to trade
echo.
curl -X POST http://localhost:10001/tradings/y99f8f8dc-e25e-4a95-aa2c-782823f36e2a --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "\"ppd845f0dc7-37d0-426e-994e-43fc3ac83c08\""
echo.
echo.
echo try to buy trade
echo.
curl -X POST http://localhost:10001/tradings/jy845f0dc7-37d0-426e-994e-43fc3ac83c08 --header "Authorization: Basic altenhof-mtcgToken"
echo.
echo.
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic kienboec-mtcgToken"
echo.
echo.
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic altenhof-mtcgToken"
echo.

REM --------------------------------------------------
echo end...

REM this is approx a sleep
ping localhost -n 100 >NUL 2>NUL
@echo on


REM --------------------------------------------------
echo end...

REM this is a sleep
ping localhost -n 100 >NUL 2>NUL
@echo on
