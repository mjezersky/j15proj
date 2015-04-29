#!/bin/bash

if [ -d "build" ]; then
  rm -rf build
fi
if [ -d "dest-client" ]; then
  rm -rf dest-client
fi

mkdir build
mkdir dest-client

javac -cp src/ src/ija/TUI.java -d build/
jar cfe dest-client/ija2015-client.jar ija.TUI -C build/ ija

java -jar dest-client/ija2015-client.jar
