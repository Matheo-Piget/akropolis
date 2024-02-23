#!/bin/sh

# Define the source directory, resources directory, bin directory, and test directory
SRC_DIR="src"
RES_DIR="res"
BIN_DIR="bin"
TEST_DIR="src/test"

# Delete all .class files in the bin directory
find . -type f -path "./$BIN_DIR/*/*" -name "*.class" -delete

# Create the bin directory if it doesn't exist
mkdir -p $BIN_DIR

# Compile all Java files in the source directory (excluding the test directory) into the bin directory
find $SRC_DIR -name "*.java" -not -path "$TEST_DIR/*" -print | xargs javac -cp $SRC_DIR -d $BIN_DIR

# If compilation was successful, run the main class
if [ $? -eq 0 ]; then
    java -cp $BIN_DIR:$RES_DIR view.main.App
else
    echo "Compilation failed"
fi