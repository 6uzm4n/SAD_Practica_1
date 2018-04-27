package Utilities;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.SerializationHelper;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;

import java.io.*;
import java.util.Random;

public class CommonUtilities {

	/**
	 * Escribe por consola el texto pTexto en color rojo.
	 * NO incluye salto de línea.
	 *
	 * @param pText texto a escribir
	 */
	public static void printError(String pText) {
		System.out.print(String.format("\33[31m%s\33[0m", pText));
	}

	/**
	 * Escribe por consola el texto pTexto en color rojo.
	 * SIEMPRE incluye salto de línea.
	 *
	 * @param pText texto a escribir
	 */
	public static void printlnError(String pText) {
		printError(String.format("%s\n", pText));
	}

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

	/**
	 * Guarda el clasificador pClassifier en la ruta pPath.
	 *
	 * @param classifier	clasificador a guardar
	 * @param pathOut		ruta del archivo a crear
	 */
	public static void saveModel(Classifier classifier, String pathOut){
		try {
			SerializationHelper.write(pathOut, classifier);
		} catch (Exception e) {
			printlnError("Error al guardar el clasificador.");
			e.printStackTrace();
		}
	}

	/**
	 * Este método escribe en un archivo la calidad estimada de una evaluación dada.
	 *
	 * @param evaluation    Evaluación cuya calidad se quiere escribir.
	 * @param pathOut       Ruta al fichero que se desea crear/sobreescribir.
	 */
	public static void writeQuality(Evaluation evaluation, String pathOut) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(pathOut + "_quality.txt"))));
			writer.println("==========================================================================================================");
			writer.println("CALIDAD ESTIMADA DE BASELINE:");
			writer.println("Se ha usado 8-fold cross-validation sobre el modelo baseline NaiveBayes para obtener la calidad estimada.");
			writer.println("==========================================================================================================");
			writer.println(evaluation.toSummaryString());
			writer.println(evaluation.toMatrixString());
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carga las instancias de las que obtendremos el �ndice de la clase minoritaria
	 * 
	 * @param instances
	 * Instancias de las que se obtendr� el �ncide de la clase minoritaria
	 * @return
	 * �ndice de la clase minoritaria
	 */
	public static int getMinorityClassIndex(Instances instances){
        int[] nomCounts = instances.attributeStats(instances.classIndex()).nominalCounts;
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

	/**
	 * Evaluación k-Fold Cross-Validation
	 * Realiza una evaluación k-Fold Cross-Validation sobre el clasificador pClassifier con las instancias pIsntaces y la seed pSeed donde k es pFolds.
	 *
	 * @param classifier Clasificador a evaluar
	 * @param instances  Instancias con las que evaluar
	 * @param folds      Número de iteraciones a realizar
	 * @param seed       Seed para la randomizanión
	 * @return el objeto Evaluation que contiene los resultados de la evaluación.
	 */

	public static Evaluation evalKFoldCrossValidation(Classifier classifier, Instances instances, int folds,
													  long seed) {
		Evaluation evaluation = null;
		try {
			evaluation = new Evaluation(instances);
			evaluation.crossValidateModel(classifier, instances, folds, new Random(seed));
		} catch (Exception e) {
			printlnError("Error al evaluar el clasificador");
			e.printStackTrace();
			System.exit(1);
		}
		return evaluation;
	}
}
