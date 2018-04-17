package Utilities;

import weka.classifiers.Classifier;
import weka.core.SerializationHelper;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CommonUtilities {
	/**
	 * Cargamos las instancias del fichero
	 *
	 * @param path
	 *            de entrada de ficheros
	 * @exception Exception
	 *                Si no se pueden obtener correctamente las instancias a partir
	 *                de los ficheros de entrada
	 */
	public static Instances loadArff(String path) throws Exception {

		FileReader file = null;

		try {
			file = new FileReader(path);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Comprueba el path del fichero: " + path);
		}

		Instances data = null;

		try {
			data = new Instances(file);
		} catch (IOException e) {
			System.out.println("ERROR: Comprueba el path del fichero: " + path);
		}

		file.close();

		return data;
	}

	/**
	 * Guarda las instancias que se le pasan como parametro en la ruta especificada
	 * en formato .arff
	 *
	 * @param instances
	 *            Conjunto de instancias a convertir y guardar en el fichero .arff
	 * @param pathOut
	 *            Ruta donde se guardar� el fichero resultante de la conversi�n
	 */
	public static void saveArff(Instances instances, String pathOut) {
		try {
			ArffSaver arffSaver = new ArffSaver();
			arffSaver.setInstances(instances);
			arffSaver.setFile(new File(pathOut));
			arffSaver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carga las instancias del fichero situado en pPath. Se establece pClassIndex
	 * como el índice de la clase. -1 indica que la clase es el último atributo.
	 *
	 * @param path
	 *            ruta del fichero .arff
	 * @param classIndex
	 *            índice del atributo clase
	 * @return un objeto de tipo Instances con las instancias del fichero, en caso
	 *         de error al cargar los datos, devuelve null
	 */
	public static Instances loadInstances(String path, int classIndex) {
		Instances instances = null;
		try {
			DataSource ds = new DataSource(path);
			instances = ds.getDataSet();
			if (classIndex >= 0)
				instances.setClassIndex(classIndex);
			else
				instances.setClassIndex(instances.numAttributes() - 1);
		} catch (Exception e) {
			System.out.println(String.format("Error al cargar las instancias de %s", path));
			e.printStackTrace();
		}
		return instances;
	}

	public static void saveModel(Classifier classifier, String pathOut){
		try {
			SerializationHelper.write(pathOut, classifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
