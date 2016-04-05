#!/bin/bash
j=~/part5/outputs/temp1.txt
k=~/part5/outputs/noTitles.txt
l=~/part5/outputs/temp3.txt
m=~/part5/outputs/temp4.txt
final=~/part5/outputs/grobid_final.txt
for file in $HOME/shared/polar/*
do
java -classpath $HOME/src/grobidparser-resources/:$HOME/tika-app-1.12.jar org.apache.tika.cli.TikaCLI --config=$HOME/src/grobidparser-resources/tika-config.xml -J $file > $j
sed -i 's/Content-Type/ContentType/g;s/meta:author/metaauthor/g' $j
title="$(jq '.[].title' $j)"
ContentType="$(jq '.[].ContentType' $j)"
metaauthor="$(jq '.[].metaauthor' $j)"
if [ \""${title}"\" = \""null"\" ]
then
printf "{\n \"id\":\""`basename $file`"\",\n \"Content-Type\":"${ContentType}"\n},\n" >> $k
else
python $HOME/Downloads/scholar.py-master/scholar.py -c 10 --title --all \""${title}"\" --citation bt >> $l
java -cp $HOME/part5 BibTexToJSON $l > $m
sed -i '/^$/d' $m
sed -i '$s/.*/}/' $m
printf "{\n \"id\":\""`basename $file`"\",\n \"Content-Type\":"${ContentType}",\n\"title\":"${title}",\n\"author\":" >> $final
if [ \""${metaauthor}"\" = \""null"\" ]
then
printf "\"""${metaauthor}""\",\n" >> $final
else
printf "${metaauthor}"",\n" >> $final
fi
printf "\"relatedPublications\":[" >> $final
cat $m >> $final
printf "]},\n" >> $final
sleep 30
fi
done
sed -i '$s/.*/}/' $final
sed -i '$s/.*/}/' $k