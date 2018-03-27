import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class MakeCompatible {
	/**
	 * Hace compatible un conjunto de evaluaciÛn representandolo en un espacio de
	 * atributos compatible con el del entrenamiento
	 *
	 * @param args
	 *            Par√°metros de entrada
	 * @throws Exception
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("=====MAKE COMPATIBLE=====");
			System.out.println(
					"Este programa tiene como funci√≥n hacer que un conjunto de evaluaciÛn dado sea compatible con un conjunto de entrenamiento dado.");
			System.out.println("Este programa necesita que introduzcas 3 argumentos para funcionar correctamente.");
			System.out.println(
					"PRECONDICIONES:\nEl primer argumento ser√° el path del conjunto de entrenamiento con el que comparar. "
							+ "El segundo es el path del conjunto de evaluaciÛn que se debe hacer compatible. Mientras que el tercero ser· el path de la salida para este nuevo conjunto de datos compatible.");
			System.out.println(
					"POSTCONDICIONES:\nEl resultado de esta aplicaci√≥n ser√° la creaci√≥n de un fichero .arff "
							+ "que contiene un conjunto de datos de evauaciÛn compatible con el del entrenamiento\n");
			System.out.println("Lista de argumentos:\n" + "-- Path del conjunto de entrenamiento con el que comparar."
					+ "\n-- Path de la ra√≠z del fichero .arff de evaluaciÛn a convertir."
					+ "\n-- Path del destino donde se guardar√° el fichero .arff resultante tras la ejecuci√≥n");
			System.out.println(
					"Ejemplo de una correcta ejecuci√≥n: java -jar MakeCompatible.jar /path/to/train.arff /path/to/test.arff /path/to/newArff.arff");
			System.exit(0);
		} else if (args.length != 3) {
			System.out.println("Error en el input. Revise su sintaxis.");
			System.exit(1);
		} else if (args.length == 3) {
			String pathBoW = args[0];
			String pathIn = args[1];
			String pathOut = args[2];
			DataSource source0 = new DataSource(pathBoW);
			DataSource source1 = new DataSource(pathIn);

			Instances train = null;
			Instances test = null;
			try {
				train = source0.getDataSet();
				test = source1.getDataSet();
				if (train.classIndex() == -1) {
					train.setClassIndex(train.numAttributes() - 1);
					test.setClassIndex(test.numAttributes() - 1);
				}
			} catch (NullPointerException e) {
				System.out.println("Problema al cargar los datos");
				System.exit(1);
			}

			Standardize filter = new Standardize();
			try {
				filter.setInputFormat(train);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Instances newTrain = Filter.useFilter(train, filter);
			Instances newTest = Filter.useFilter(test, filter);
			saveArff(newTest, pathOut);
		}
	}

	/**
	 * Guarda las instancias que se le pasan como parametro en la ruta especificada
	 * en formato .arff
	 *
	 * @param instances
	 *            Conjunto de instancias a convertir y guardar en el fichero .arff
	 * @param pathOut
	 *            Ruta donde se guardar· el fichero resultante de la conversiÛn
	 */
	private static void saveArff(Instances instances, String pathOut) {
		try {
			ArffSaver arffSaver = new ArffSaver();
			arffSaver.setInstances(instances);
			arffSaver.setFile(new File(pathOut));
			arffSaver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
