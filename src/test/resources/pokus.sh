#/usr/bin/bash
while IFS=, read aa;do
        param=$aa
done < $1
echo arg1 $1
echo arg2 $2
echo arg3 $3
echo param $param
while read line
do
	if [ ! -z "$line" -a "$line" != " " ]; then
		echo Copying "$line" to "$3/$param$(basename "$line")"
		cp "$line" "$3/$param$(basename "$line")"
	fi
done <"$2"
