package Utilities;

import weka.classifiers.Classifier;
import weka.core.SerializationHelper;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;

import java.io.File;
import java.io.FileReader;

public class CommonUtilities {
	/**
	 * Cargamos las instancias del fichero
	 *
	 * @param path
	 *            de entrada de ficheros
	 */
	public static Instances loadArff(String path, int classIndex){

		FileReader file;

		try {
			file = new FileReader(path);
			file.close();
		} catch (Exception e) {
			System.out.println("ERROR CARGANDO LAS INSTANCIAS: Comprueba el path del fichero: " + path);
			System.exit(1);
		}

		Instances data = null;

		try {
			ConverterUtils.DataSource ds = new ConverterUtils.DataSource(path);
			data = ds.getDataSet();
		} catch (Exception e) {
			System.out.println("ERROR LEYENDO LAS INSTANCIAS: Comprueba la estructura interna del fichero: " + path);
			System.exit(1);
		}

		if (classIndex < 0){
			data.setClassIndex(data.numAttributes()-1);
		}else{
			data.setClassIndex(classIndex);
		}

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

	public static void saveModel(Classifier classifier, String pathOut){
		try {
			SerializationHelper.write(pathOut, classifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carga las instancias de las que obtendremos el �ndice de la clase minoritaria
	 * 
	 * @param pInstances
	 * Instancias de las que se obtendr� el �ncide de la clase minoritaria
	 * @return
	 * �ndice de la clase minoritaria
	 */
	public static int getMinorityClassIndex(Instances pInstances){
        int[] nomCounts = pInstances.attributeStats(pInstances.classIndex()).nominalCounts;
        int minClassAmount = -1;
        int minClassIndex = -1;
        for(int i = 0; i < nomCounts.length; i++) {
            if (minClassAmount < 0 || nomCounts[i] < minClassAmount) {
                minClassAmount = nomCounts[i];
                minClassIndex = i;
            }
        }
        return minClassIndex;
    }
}
