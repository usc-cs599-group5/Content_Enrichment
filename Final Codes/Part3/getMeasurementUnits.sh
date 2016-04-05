#!/bin/bash
j=~/part3/outputs/temp1.txt
k=~/part3/outputs/temp2.txt
final=~/part3/outputs/measurements.json
for file in ~/shared/polar/*
do
i=$file
curl -X POST -d @$file http://127.0.0.1:8881/nltk > $j
cat $j | jq '.units' > $k
units="$(jq '.units' $j)"
echo ""${units}""
printf "{\n \"id\":\""`basename $file`"\",\n \"units\":" >> $final
if [ "${units}" = "" ]
then
printf "[]\n" >> $final
else
cat $k >> $final
fi
printf "},\n" >> $final
done
sed -i '$s/.*/}/' $final

