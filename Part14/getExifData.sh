#!/bin/bash
j=~/part14/outputs/temp1.txt
final=~/part14/outputs/exif.json
count=1
for file in ~/shared/polar/*
do
count=$((count+1))
java -Dtika.config=exif-tika-config.xml -classpath tika-app-1.12.jar org.apache.tika.cli.TikaCLI -j $file >> $final
printf ",\n" >> $final
done
