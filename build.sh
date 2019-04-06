#!/usr/bin/env bash

cd sandpin;
mvn clean process-sources process-resources process-classes compile package;

cd ../sandpin-launcher;
mvn clean process-sources process-resources process-classes compile package;

cd ..;
exit 0;
