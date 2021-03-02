rm ~/Projet_GL/src/test/correcttest/syntax/invalid/*;
for i in ~/Projet_GL/src/test/deca/syntax/invalid/added/*; do
    FILE_NAME=$(echo "${i##*/}" | cut -d\. -f1);
    echo "${FILE_NAME}";
     ~/Projet_GL/src/test/script/launchers/test_synt "$i" 2> ~/Projet_GL/src/test/correcttest/syntax/invalid/"${FILE_NAME}".lis;
    #sed -i "/INFO/d" ~/Projet_GL/src/test/correcttest/contex/invalid/"${FILE_NAME}".lis;
done;
