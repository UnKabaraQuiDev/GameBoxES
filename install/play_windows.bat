@echo off

echo 1/1 Starting GameBoxES
cd GameBoxES & call mvn clean package exec:exec & cd ../
