@echo off

IF exist pclib (echo 1/3 PCLib already installed, updating & cd pclib & git reset --hard & git pull & cd ../) ELSE (echo 1/3 Installing PCLib & git clone https://github.com/UnKabaraQuiDev/pclib)

IF exist jbcodec (echo 2/3 JBcodec already installed, updating & cd jbcodec & git reset --hard & git pull & cd ../) ELSE (echo 2/3 Installing JBCodec & git clone https://github.com/UnKabaraQuiDev/jbcodec)

IF exist GameBoxES (echo 3/3 GameBoxES already installed, updating & cd GameBoxES & git reset --hard & git pull & cd ../) ELSE (echo 3/3 Installing GameBoxES & git clone https://github.com/UnKabaraQuiDev/GameBoxES)

echo 1/3 Building PCLib
cd pclib & call mvn clean package install:install & cd ../
echo 2/3 Building JBCodec
cd jbcodec & call mvn clean package install:install & cd ../
echo 3/3 Building GameBoxES
cd GameBoxES & call mvn clean package & cd ../