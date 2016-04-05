#!/bin/bash
j=~/csci599/temp1.txt
k=~/csci599/temp2.txt
l=~/csci599/temp3.txt
final=~/csci599/sweet.txt
for file in ~/csci599/polar/*
do
i=$file
java -Dner.impl.class=org.apache.tika.parser.ner.regex.RegexNERecogniser -classpath $NER_RES:$TIKA_APP org.apache.tika.cli.TikaCLI --config=tika-config.xml -m  $i > $j
sed -e '1,2d' < $j > $k
head -n -3 < $k > $l
cat $l >> $final
done
