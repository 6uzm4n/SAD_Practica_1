package Entrega2;

import java.io.IOException;
import java.util.Random;

import Utilities.CommonUtilities;
import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;

public class ParamOptimization {

	public static void main(String[] args) throws IOException {

        if (args.length == 0) {
        	System.out.println("=====ParamOptimization=====");
			System.out.println(
					"Este programa tiene como función obtener los parámetros óptimos para el modelo Support Vector Machine.");
			System.out.println("Este programa necesita introducir 1 argumento para funcionar correctamente.");
			System.out.println(
					"PRECONDICIONES:\nEl primer y único argumento será el path del conjunto de entrenamiento que usaremos posteriormente");
			System.out.println(
					"POSTCONDICIONES:\nEl resultado de esta aplicación serán los parámetros óptimos para el modelo Support Vector Machine.");
			System.out.println("Lista de argumentos:\n" + "-- Path del conjunto de entrenamiento a transformar.");
			System.out.println(
					"Ejemplo de una correcta ejecución: java -jar ParamOptimization.jar /path/to/train.arff");
			System.exit(0);
        }else{
        	
        	if(args.length != 1){
            System.out.println("Error en el input. Revise su sintaxis.");
            System.exit(1);
        	}
        	else{
        		Instances data = null;
        		try {
					data = CommonUtilities.loadArff(args[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
        		
        		System.out.println("El valor óptimo para el modelo SVM es: "+optimizeSupportVectorMachine(data));
        	}
	    }

    }
	
	/**
	 * Calcula los parámetros óptimos para el modelo SVM y los imprime por pantalla
	 * 
	 * @param pInstances
	 * 			Instancias de entrenamiento que usaremos para hallar los parámetros óptimos
	 */
	
	
	public static int optimizeSupportVectorMachine( Instances pInstances){
		
		 	SMO classifier = new SMO();
		 	int numIt = 5;
	        double bestFMeasure = -1;
	        int bestExp = -1;
	        int minClassIndex = CommonUtilities.getMinorityClassIndex(pInstances);

	        for(int i = 0; i <= numIt; i++) {
	            try {
	            	PolyKernel pK = new PolyKernel();
	            	pK.setExponent(i);
	            	classifier.setKernel(pK);
	                Evaluation evaluation = new Evaluation(pInstances);  
	                evaluation.crossValidateModel(classifier, pInstances, 4, new Random(3));
	                double fMeasure = evaluation.fMeasure(minClassIndex);
	                if (fMeasure > bestFMeasure) {
	                    bestFMeasure = fMeasure;
	                    bestExp = i;
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        return bestExp;
	}
}