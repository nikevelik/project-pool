# Course Project for course "XML Technologies And Web Semantics"

*Faculty of mathematics and informatics at Sofia University "St. Kliment Ohridski", Sofia, Bulgaria*

*November 2025*

# Local development/testing

### 1. Generating PDF document via XSLT transformations

run the following commands in the terminal:
```
fop -xml cars.xml -xsl cars.xsl -foout cars.fo
fop -fo cars.fo -pdf cars.pdf 
```
that should generate a pdf document using the XML and XSLT, if *fop* is installed *(Apache Formatting Objects Processor)* 

### 2. Validating XML using DTD

uncomment the following line in cars.xml:
```
<!-- <!DOCTYPE catalog SYSTEM "cars.dtd"> -->
```

then run the command:
```
xmllint --noout --valid cars.xml 
```
if there is any issue with the xml, errors will appear. Otherwise (if the xml is completely valid), no output will be shown.

### 3. Quick reference

- For quick reference, there is available [example output file](https://github.com/nikevelik/cars-catalogue/tree/main/output/catalogue.pdf)
- [Presentation slides (annotated with comments)](https://docs.google.com/presentation/d/1JefakXi2HpTzZMgVSwpwgVoerhULLBf0HqtNOKd9J6g/edit?usp=sharing)
