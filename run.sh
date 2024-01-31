#!/bin/sh

# Define the source directory, resources directory, and bin directory
SRC_DIR="src"
RES_DIR="res"
BIN_DIR="bin"

# Delete all .class files in the bin directory
find . -type f -path "./$BIN_DIR/*/*" -name "*.class" -delete

# Create the bin directory if it doesn't exist
mkdir -p $BIN_DIR

# Compile all Java files in the source directory into the bin directory
find $SRC_DIR -name "*.java" -print | xargs javac -cp $SRC_DIR -d $BIN_DIR

# If compilation was successful, run the main class
if [ $? -eq 0 ]; then
    java -cp $BIN_DIR:$RES_DIR main.App
else
    echo "Compilation failed"
fi