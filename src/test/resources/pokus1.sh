#/usr/bin/bash
while IFS=, read aa;do
        param=$aa
done < $1
inputPath=$2/*
echo arg1 $1
echo arg2 $2
echo arg3 $3
echo param $param
echo inputpath $inputPath
for f in $inputPath
do
        echo Copying "$f" to "$3/$param$(basename "$f")"
    cp "$f" "$3/$param$(basename "$f")"
done
