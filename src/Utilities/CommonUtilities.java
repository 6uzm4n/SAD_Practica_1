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
	 * Escribe por consola el texto pTexto en color rojo. NO incluye salto de línea.
	 *
	 * @param pText Texto a escribir
	 */
	public static void printError(String pText) {
		System.out.print(String.format("\33[31m%s\33[0m", pText));
	}

	/**
	 * Escribe por consola el texto pTexto en color rojo. SIEMPRE incluye salto de línea.
	 *
	 * @param pText Texto a escribir
	 */
	public static void printlnError(String pText) {
		printError(String.format("%s\n", pText));
	}

	/**
	 * Cargamos las instancias del fichero path y asigna a estas el índice de clase classIndex. En caso de ser negativo
	 * se asigna el último atributo como atributo de clase.
	 *
	 * @param path			Instancias a cargar.
	 * @param classIndex	Indice de la clase.
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
	 * Guarda las instancias instances en la ruta pathOut.
	 *
	 * @param instances	Conjunto de instancias a convertir y guardar en el fichero .arff.
	 * @param pathOut	Ruta donde se guardará el fichero resultante de la conversión.
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
	 * Carga el clasificador ubicado en la ruta pPath.
	 *
	 * @param pathIn	Ruta del clasificador a cargar.
	 * @return 			Clasificador cargado.
	 */
	public static Classifier loadModel(String pathIn){
		Classifier model = null;
		try {
			model = (Classifier) SerializationHelper.read(pathIn);
		} catch (Exception e) {
			printlnError("Error al cargar el clasificador.");
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * Guarda el clasificador pClassifier en la ruta pPath.
	 *
	 * @param classifier	Clasificador a guardar.
	 * @param pathOut		Ruta del archivo a crear.
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
	 * @param pathOut       Ruta del fichero que se desea crear/sobreescribir.
	 */
	public static void writeQuality(Evaluation evaluation, String pathOut) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(pathOut + "_quality.txt"))));
			writer.println("==========================================================================================================");
			writer.println("CALIDAD ESTIMADA DEL MODELO:");
			writer.println("Se ha usado 8-fold cross-validation sobre el modelo para obtener la calidad estimada.");
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
	 * Obtiene el índice de la clase minoritaria del conjunto de instancias dado.
	 * 
	 * @param instances	Instancias de las que se obtendrá el íncide de la clase minoritaria.
	 * @return			Índice de la clase minoritaria.
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
	 * Realiza una evaluación k-Fold Cross-Validation sobre el clasificador pClassifier con las instancias pIsntaces y
	 * la seed pSeed, donde k es el número de folds.
	 *
	 * @param classifier Clasificador a usar en la evaluación.
	 * @param instances  Instancias que evaluar.
	 * @param folds      Número de iteraciones a realizar.
	 * @param seed       Seed para la randomización.
	 * @return 			 Objeto evaluation que contiene los resultados de la evaluación.
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
