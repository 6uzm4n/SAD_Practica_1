package Entrega1;

import Utilities.CommonUtilities;

import java.io.File;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;
import weka.filters.unsupervised.instance.NonSparseToSparse;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class TransformRaw {
	/**
	 * Transforma el espacio de atributos del conjunto de entrenamiento a BoW o
	 * TF�IDF
	 *
	 * @param args
	 *            Par�metros de entrada
	 * @throws Exception
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("=====TRANSFORM RAW=====");
			System.out.println(
					"Este programa tiene como función transformar el espacio de atributos del conjunto de entrenamiento a BoW o TF�IDF.");
			System.out.println("Este programa necesita que introduzcas 5 argumentos para funcionar correctamente.");
			System.out.println(
					"PRECONDICIONES:\nEl primer argumento será el path del conjunto de entrenamiento a transformar. "
							+ "El segundo es el path destino. El tercero indicará si queremos transformar a BoW o TF-IDF, los valores válidos: 'BOW' , 'TF-IDF'."
							+ "El cuarto indicará si queremos una representación Sparse o NonSparse, los valores válidos: 'True' , 'False'"+
							"Y el último será el path donde guardar el diccionario generado");
			System.out.println(
					"POSTCONDICIONES:\nEl resultado de esta aplicación será una representación BoW o TF-IDF del espacio de atributos del conjunto de entrenamiento ");
			System.out.println("Lista de argumentos:\n" + "-- Path del conjunto de entrenamiento a transformar."
					+ "\n-- Path de salida." + "\n-- Opción BoW o TF-IDF" + "\n-- ¿Opción Sparse?"+"\n-- Path del diccionario generado");
			System.out.println(
					"Ejemplo de una correcta ejecución: java -jar Entrega1.TransformRaw.jar /path/to/train.arff /path/to/trainBOW.arff BOW NonSparse /path/to/diccionario");
			System.exit(0);
		} else {
			String pathIn = "";
			String pathOut = "";
			String format = "";
			boolean sparse = false;
			String pathDictionary = "";

			if (args.length == 5){
				pathIn = args[0];
				pathOut = args[1];
				format = args[2];
				sparse = Boolean.parseBoolean(args[3]);
				pathDictionary = args[4];
			}else{
				System.out.println("Error en el input. Revise su sintaxis.");
				System.exit(1);
			}

			Instances data = CommonUtilities.loadArff(pathIn);
			data.setClassIndex(data.numAttributes() - 1);//TODO: ¿Debemos dejar como clase el primer o ultimo atributo?
			StringToWordVector filter;
			Instances dataFiltered = null;
			String relationName = data.relationName();

			/*
			 * Transformamos el arff raw a BOW.
			 */
			if (format.equals("BOW")) {
				filter = new StringToWordVector(10000);
				filter.setDictionaryFileToSaveTo(new File(pathDictionary));
				filter.setInputFormat(data);
				dataFiltered = Filter.useFilter(data, filter);

			}
			/*
			 * Transformamos el arff raw a TF-IDF
			 */
			else if (format.equals("TF-IDF")) {
				filter = new StringToWordVector(10000);
				filter.setDictionaryFileToSaveTo(new File(pathDictionary));
				filter.setOutputWordCounts(true);
				filter.setIDFTransform(true);
				filter.setTFTransform(true);
				filter.setInputFormat(data);
				dataFiltered = Filter.useFilter(data, filter);
			}

			/*
			 * Aplicamos el filtro NonSparseToSparse
			 */
			if (!sparse) {
				SparseToNonSparse sparseFilter = new SparseToNonSparse();
				sparseFilter.setInputFormat(dataFiltered);
				dataFiltered = Filter.useFilter(dataFiltered, sparseFilter);
			}

			/*
			 * Damos a la relación su nombre original
			 */
			dataFiltered.setRelationName(relationName);

			/*
			 * guardamos los datos en el path especificado
			 */
            CommonUtilities.saveArff(dataFiltered, pathOut);
		}

	}
}
