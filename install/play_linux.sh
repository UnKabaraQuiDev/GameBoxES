#!/bin/sh

echo "1/1 Starting GameBoxES"
cd GameBoxES && mvn clean package exec:exec && cd ../
