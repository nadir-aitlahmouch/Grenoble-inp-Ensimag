echo -e "\e[41mDEBUT TEST\e[0m"
PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"
echo "$PATH"
for image in "$PATH"/images/*.jp*g
do
    echo $image
    "$PATH"/bin/jpeg2ppm "$image"
done

PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"
echo "$PATH"
for image in "$PATH"/images/Images_tests/*.jp*g
do
    echo $image
    "$PATH"/bin/jpeg2ppm "$image"
done

echo -e "\e[41mAUTO_TEST SUCCESS\e[0m"


