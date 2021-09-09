for dir in ./*.gv
do
dot -Tpdf ${dir} -o ${dir%\.*}.pdf
done

