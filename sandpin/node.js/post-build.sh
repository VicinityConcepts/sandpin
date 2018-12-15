#!/usr/bin/env bash

echo "## Moving build files to webapp source folder ##"
rm -rf ../src/main/webapp
mkdir ../src/main/webapp
mv build/* ../src/main/webapp/

echo "## Removing build directory ##"
rm -rf build