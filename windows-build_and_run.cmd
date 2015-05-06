@echo off
if not exist build mkdir build
if not exist dest-client mkdir dest-client

echo [G]UI or [T]UI?
set /p CHOICE=

if ("%CHOICE%") == ("G") goto bgui
if ("%CHOICE%") == ("g") goto bgui
if ("%CHOICE%") == ("T") goto btui
if ("%CHOICE%") == ("t") goto btui
echo Nothing selected - default GUI
goto bgui

:bgui
javac -encoding utf8 -cp src/ src/ija/labyrinth/gui/GameUI.java -d build/
jar cfe dest-client/ija2015-client.jar ija.labyrinth.gui.GameUI -C build/ ija -C build/ images
goto runjar

:btui
javac -encoding utf8 -cp src/ src/ija/labyrinth/tui/TUI.java -d build/
jar cfe dest-client/ija2015-client.jar ija.labyrinth.tui.TUI -C build/ ija -C build/ images
goto runjar

:runjar
java -jar dest-client/ija2015-client.jar
