# SAD_Practica_1
Código de la práctica grupal de la asignatura SAD.

A continuación se listan las clases del código divididas por paquetes, según la entrega programada en la asignatura.

##Entrega 1
###GetRaw
Dado un conjunto de instancias en un formato ajeno a Weka (texto plano, csv o clasificado según directorios), esta clase
los convierte a .arff para que puedan ser procesados por Weka.
###TransformRaw
Dado un archivo .arff consistente en un texto y su clase, lo transforma en un Bag of Words para su posterior manipulación.
###MakeCompatible
Dado un archivo .arff consistente en un texto y su clase, lo transforma en un Bag of Words tomando como referencia un
diccionario con las palabras que compondrán el espacio de atributos.
##Entrega 2
###FssInfoGain
Dado un conjunto de instancias, esta clase reduce el número de atributos, es decir, elimina aquellos que sean redundantes
o no aporten información.
###MakeCompatibleFss
Dado un archivo .arff consistente en un texto y su clase, lo transforma en un Bag of Words tomando como referencia un
conjunto de instancias al que (preferiblemente) se le ha aplicado el filtrado de atributos anteriormente.
###GetBaselineModel
Dado un conjunto de instancias, esta clase crea un modelo clasificador NaiveBayes optimizado, el cual se podrá usar como
baseline para cualquier otro modelo clasificador.
###ParamOptimization
Dado un conjunto de instancias, esta clase devuelve los parametros óptimos para crear un clasificador SVM.
###GetModel
Dado un conjunto de instancias, esta clase crea un modelo clasificador SVM optimizado.
##Entrega 3
###Predictions
Dado un conjunto de instancias, un modelo clasificador baseline y un segundo modelo clasificador, clasifica las instancias
según ambos y guarda sus resultados.