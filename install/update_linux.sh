#!/bin/sh

sudo apt install maven git openjdk-8-jdk -y

test -d pclib && (echo "1/3 PCLib already installed, updating" ; cd pclib ; git clean -fd ; git pull ; cd ../) || (echo "1/3 Installing PCLib" ; git clone https://github.com/UnKabaraQuiDev/pclib)

test -d jbcodec && (echo "2/3 JBcodec already installed, updating" ; cd jbcodec ; git clean -fd ; git pull ; cd ../) || (echo "2/3 Installing JBCodec" ; git clone https://github.com/UnKabaraQuiDev/jbcodec)

test -d GameBoxES && (echo "3/3 GameBoxES already installed, updating" ; cd GameBoxES ; git clean -fd ; git pull ; cd ../) || (echo "3/3 Installing GameBoxES" ; git clone https://github.com/UnKabaraQuiDev/GameBoxES)

echo "1/3 Building PCLib"
cd pclib && mvn clean package install && cd ../
echo "2/3 Building JBCodec"
cd jbcodec && mvn clean package install && cd ../
echo "3/3 Building GameBoxES"
cd GameBoxES && mvn clean package && cd ../
