package Entrega1;

import Utilities.CommonUtilities;

import java.io.File;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.core.converters.ConverterUtils.DataSource;

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
					"Este programa tiene como función hacer que un conjunto de evaluaci�n dado sea compatible con un conjunto de entrenamiento dado.");
			System.out.println("Este programa necesita que introduzcas 3 argumentos para funcionar correctamente.");
			System.out.println(
					"PRECONDICIONES:\nEl primer argumento será el path del diccionario. "
							+ "El segundo es el path del conjunto de evaluaci�n que se debe hacer compatible. El tercero ser� el path de la salida para este nuevo conjunto de datos compatible");
			System.out.println("POSTCONDICIONES:\nEl resultado de esta aplicación será la creación de un fichero .arff "
					+ "que contiene un conjunto de datos de evauaci�n compatible con el del entrenamiento\n");
			System.out.println("Lista de argumentos:\n" + "-- Path del conjunto de entrenamiento con el que comparar."
					+ "\n-- Path de la raíz del fichero .arff de evaluaci�n a convertir."
					+ "\n-- Path del destino donde se guardará el fichero .arff resultante tras la ejecución");
			System.out.println(
					"Ejemplo de una correcta ejecución: java -jar Entrega1.MakeCompatible.jar /path/to/train.arff /path/to/test.arff /path/to/newArff.arff /path/to/diccionario");
			System.exit(0);
		} else if (args.length != 3) {
			System.out.println("Error en el input. Revise su sintaxis.");
			System.exit(1);
		} else if (args.length == 3) {
			String pathDicc = args[0];
			String pathIn = args[1];
			String pathOut = args[2];
			DataSource source1 = new DataSource(pathIn);

			Instances test = null;
			try {

				test = source1.getDataSet();
				if (test.classIndex() == -1) {
					test.setClassIndex(test.numAttributes() - 1);
				}
			} catch (NullPointerException e) {
				System.out.println("Problema al cargar los datos");
				System.exit(1);
			}

			FixedDictionaryStringToWordVector fixedFilter = new FixedDictionaryStringToWordVector();
			fixedFilter.setDictionaryFile(new File(pathDicc));
			fixedFilter.setInputFormat(test);
			Instances newTest = Filter.useFilter(test, fixedFilter);

			CommonUtilities.saveArff(newTest, pathOut);
		}
	}

}
