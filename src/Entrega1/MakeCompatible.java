package Entrega1;

import Utilities.CommonUtilities;

import java.io.File;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Reorder;

public class MakeCompatible {
	/**
	 * Hace compatible un conjunto de evaluaci�n representandolo en un espacio de
	 * atributos compatible con el del entrenamiento
	 *
	 * @param args
	 *            Parámetros de entrada
	 * @throws Exception
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("=====MAKE COMPATIBLE=====");
			System.out.println(
					"Este programa tiene como función hacer que un conjunto de evaluación dado sea compatible con un conjunto de entrenamiento dado.");
			System.out.println("Este programa necesita que introduzcas 3 argumentos para funcionar correctamente.");
			System.out.println(
					"PRECONDICIONES:\nEl primer argumento será el path del diccionario. "
							+ "El segundo es el path del conjunto de evaluación que se debe hacer compatible. El tercero será el path de la salida para este nuevo conjunto de datos compatible");
			System.out.println("POSTCONDICIONES:\nEl resultado de esta aplicación será la creación de un fichero .arff "
					+ "que contiene un conjunto de datos de evauación compatible con el del entrenamiento\n");
			System.out.println("Lista de argumentos:\n" + "-- Path del conjunto de entrenamiento con el que comparar."
					+ "\n-- Path de la raíz del fichero .arff de evaluación a convertir. No debe estar en formato BOW."
					+ "\n-- Path del destino donde se guardará el fichero .arff resultante tras la ejecución");
			System.out.println(
					"Ejemplo de una correcta ejecución: java -jar Entrega1.MakeCompatible.jar /path/to/diccionario.txt /path/to/test.arff /path/to/newArff.arff");
			System.exit(0);
		} else if (args.length != 3) {
			CommonUtilities.printlnError("Error en el input. Revise su sintaxis.");
			System.exit(1);
		} else {
			String pathDicc = args[0];
			String pathIn = args[1];
			String pathOut = args[2];

			Instances data = null;
			try {
				data = CommonUtilities.loadArff(pathIn, -1);
			} catch (NullPointerException e) {
				CommonUtilities.printlnError("Problema al cargar los datos");
				e.printStackTrace();
				System.exit(1);
			}

			FixedDictionaryStringToWordVector fixedFilter = new FixedDictionaryStringToWordVector();
			fixedFilter.setDictionaryFile(new File(pathDicc));
			fixedFilter.setInputFormat(data);
			Instances newData = Filter.useFilter(data, fixedFilter);

			//Hacemos que la clase sea el último atributo
			Reorder reorderFilter = new Reorder();
			reorderFilter.setInputFormat(newData);
			reorderFilter.setOptions(new String[]{"-R","2-last,1"});
			newData = Filter.useFilter(newData, reorderFilter);
			newData.setClassIndex(newData.numAttributes()-1);

			//Damos a la nueva relación su nombre original
			newData.setRelationName(data.relationName());

			CommonUtilities.saveArff(newData, pathOut);
		}
	}

}
