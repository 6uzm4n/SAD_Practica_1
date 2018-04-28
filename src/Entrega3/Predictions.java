package Entrega3;
	
import Utilities.CommonUtilities;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class Predictions {
	
	public static void main(String[] args) throws Exception{
		
	Instances data;
	 if (args.length == 0) {
     	System.out.println("=====Predictions=====");
			System.out.println(
					"Este programa tiene como función predecir la clase de las instancias de un documento dado con un modelo binario.");
			System.out.println("Este programa necesita introducir 4 argumentos.");
			System.out.println(
					"PRECONDICIONES:\nEl archivo usado para la predicción será de formato .arff");
			System.out.println(
					"POSTCONDICIONES:\nEl resultado de este algoritmo serán las instancias, clase real y clase estimada "
					+ "del conjunto de test con cada uno de los dos algoritmos considerados.");
			System.out.println("Lista de argumentos:\n" + "Argumento 1: Path de origen del documento a predecir"
														+ "Argumento 2: Path de origen del modelo Baseline"
														+ "Argumento 3: Path de origen del modelo SVM"
														+ "Argumento 4: Path de destino de los resultados.");
			System.out.println(
					"Ejemplo de una correcta ejecución: java -jar Predictions.jar /path/to/data.arff /path/to/model1 /path/to/model2 /path/to/destination/");
			System.exit(0);
     }
	 else
	 {
		 if(args.length == 4)
		 {
			 try {
					data = CommonUtilities.loadArff(args[0], -1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			 
			 NaiveBayes baselineModel = (NaiveBayes) SerializationHelper.read(args[1]);
			 SMO svmModel = (SMO) SerializationHelper.read(args[2]);
			 
			 
			 //AQUÍ VA EL CÓDIGO DE LA PARTE 3
			 
			 
			 
		 }
		 else
		 {
			 CommonUtilities.printlnError("Error en el input. Revise su sintaxis.");
	         System.exit(1);
		 }
	 }
	}
}
