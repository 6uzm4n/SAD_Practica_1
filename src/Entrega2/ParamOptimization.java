package Entrega2;

import Utilities.CommonUtilities;
import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;

public class ParamOptimization {

	public static void main(String[] args){

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
            CommonUtilities.printlnError("Error en el input. Revise su sintaxis.");
            System.exit(1);
        	}
        	else{
        		Instances data = null;
        		try {
					data = CommonUtilities.loadArff(args[0], -1);
				} catch (Exception e) {
					e.printStackTrace();
				}

				int valorOptimo = optimizeSupportVectorMachine(data);
        		System.out.println("El valor óptimo para el modelo SVM es: " + valorOptimo);
        	}
	    }

    }
	
	/**
	 * Calcula los parámetros óptimos para el modelo SVM y los imprime por pantalla.
	 * 
	 * @param pInstances
	 * 			Instancias de entrenamiento que usaremos para hallar los parámetros óptimos
	 */
	private static int optimizeSupportVectorMachine(Instances pInstances){
		
		 	SMO classifier = new SMO();
		 	int numIt = 5;
	        double bestFMeasure = -1;
	        int bestExp = -1;
	        int minClassIndex = CommonUtilities.getMinorityClassIndex(pInstances);

			System.out.println("Iniciando optimización. Esto puede llevar un tiempo.");
	        for(int i = 0; i <= numIt; i++) {
	            try {
					System.out.println("Optimización " + (i+1) + " de " + (numIt+1) + "...");
	            	PolyKernel pK = new PolyKernel();
	            	pK.setExponent(i);
	            	classifier.setKernel(pK);
	                Evaluation evaluation = CommonUtilities.evalKFoldCrossValidation(classifier, pInstances, 4, 1);
	                double fMeasure = evaluation.fMeasure(minClassIndex);
	                if (fMeasure > bestFMeasure) {
	                    bestFMeasure = fMeasure;
	                    bestExp = i;
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	                System.exit(1);
	            }
	        }
	        
	        return bestExp;
	}
}