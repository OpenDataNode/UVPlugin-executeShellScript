#/usr/bin/bash
while read aa;do
    echo $aa
    IFS=' ' read -a array <<< "$aa"
    for element in "${array[@]}"
    do
        echo Creating file "$2/$element"
        touch "$2/$element"
    done
done < $1
