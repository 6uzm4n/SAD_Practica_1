package Entrega3;
	
import Utilities.CommonUtilities;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Predictions {

	/**
	 * Clasifica un conjunto de instancias usando un clasificador baseline y un clasificador SVM y escribe los resultados
	 * en dos archivos de texto.
	 *
	 * @param args  Parámetros de entrada. En caso de no introducir ninguno se muestra una descripción de estos.
	 */
	public static void main(String[] args){
		String pathIn = "";
		String pathBaseline = "";
		String pathSVM = "";
		String pathOut = "";
		if (args.length == 0) {
			System.out.println("=====Predictions=====");
			System.out.println("Este programa tiene como función predecir la clase de las instancias de un documento dado con un modelo binario.");
			System.out.println("Este programa necesita introducir 4 argumentos.");
			System.out.println("PRECONDICIONES:\nEl archivo usado para la predicción será de formato .arff");
			System.out.println("POSTCONDICIONES:\nEl resultado de este algoritmo serán las instancias, clase real y clase estimada "
					+ "del conjunto de test con cada uno de los dos algoritmos considerados.");
			System.out.println("Lista de argumentos:\n" + "Argumento 1: Path de origen del documento a predecir"
														+ "Argumento 2: Path de origen del modelo Baseline"
														+ "Argumento 3: Path de origen del modelo SVM"
														+ "Argumento 4: Path de destino de los resultados.");
			System.out.println("Ejemplo de una correcta ejecuci�n: java -jar Predictions.jar /path/to/data.arff /path/to/model_baseline /path/to/model_SVM /path/to/destination/");
			System.exit(0);
		}else if(args.length == 4){
			try {
				pathIn = args[0];
				pathBaseline = args[1];
				pathSVM = args[2];
				pathOut = args[3];
			} catch (Exception e) {
				CommonUtilities.printlnError("Error en los argumentos.");
				e.printStackTrace();
			}
		}else{
			 CommonUtilities.printlnError("Error en el input. Revise su sintaxis.");
			 System.exit(1);
		}

		Instances data = CommonUtilities.loadArff(pathIn, -1);
		Classifier baseline = CommonUtilities.loadModel(pathBaseline);
		Classifier svm = CommonUtilities.loadModel(pathSVM);
		predictClass(data, svm, pathOut + "TestPredictionSVM.txt");
		predictClass(data, baseline, pathOut + "TestPredictionsBaseline.txt");
	}

	/**
	 * Dado un conjunto de instancias, un clasificador y un path de un archivo de texto, clasifica las instancias
	 * mediante el clasificador y escribe los resultados obtenidos en el archivo de texto indicado.
	 *
	 * @param data			Conjunto de instancias a clasificar.
	 * @param classifier	Clasificador a utilizar.
	 * @param pathOut		Archivo en el que escribir los resultados obtenidos.
	 */
	private static void predictClass(Instances data, Classifier classifier, String pathOut) {
		StringBuilder output = new StringBuilder();
		output.append("INSTANCIA	CLASE REAL	CLASE PREDICHA\n");
		output.append("==============================================\n");
		Instance cntInstance;
		double realClassValue;
		String realClass;
		double predictedClassValue;
		String predictedClass;
		for (int i = 0; i < data.numInstances(); i++){
			try {
				cntInstance = data.instance(i);
				predictedClassValue = classifier.classifyInstance(cntInstance);
				predictedClass = data.classAttribute().value(((int) predictedClassValue));
				realClassValue = cntInstance.classValue();
				realClass = data.classAttribute().value((int) realClassValue);
				output.append(i + "\t\t" + realClass + "\t\t" + predictedClass + "\n");
			}catch (Exception e){
				CommonUtilities.printlnError("Error al clasificar la instancia " + i);
				e.printStackTrace();
			}
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathOut)));
			writer.write(output.toString());
			writer.flush();
			writer.close();
		} catch (Exception e){
			CommonUtilities.printlnError("Error al escribir los resultados. Revise el fichero output.");
			e.printStackTrace();
			System.exit(1);
		}
	}
}

