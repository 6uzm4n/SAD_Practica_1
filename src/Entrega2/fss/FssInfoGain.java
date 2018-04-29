package Entrega2.fss;

import java.io.File;

import Utilities.CommonUtilities;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class FssInfoGain {
	/**
	 * Realiza una selección de atributos dado un conjunto de datos en un fichero .arff, y genera un nuevo
	 * fichero con sus atributos reducidos.
	 *
	 * @param args	Parámetros de entrada. En caso de no introducir ninguno se muestra una descripción de estos.
	 */
	public static void main(String[] args) {
		String pathIn = null;
		String pathOut = null;
		if (args.length == 0){
			System.out.println("Este programa tiene como función descartar atributos redundantes o irrelevantes para el proceso de clasificación\n"
					+ "El archivo .arff original debe tener por lo menos un atributo de tipo String.\n"
					+ "Este programa espera dos argumentos:\n" + "\t1 - Ruta del fichero .arff de entrada\n"
					+ "\t2 - Ruta del fichero .arff de salida\n"
					+ "\nEjemplo: java -jar FssInfoGain.jar /path/to/input/arff /path/to/output/arff");
			System.exit(1);
		} else if (args.length == 2) {
			pathIn = args[0];
			pathOut = args[1];
		}else{
			System.out.println("Error en los argumentos. Revise su sintaxis.");
			System.exit(1);
		}

		Instances data = Utilities.CommonUtilities.loadArff(pathIn, -1);
		System.out.println("index: " + data.classIndex());
		try {
			Instances newData = useFilter(data);
			CommonUtilities.saveArff(newData, pathOut);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Realiza una seleccion de atributos, en concreto, elimina algunos atributos redundantes o irrelevantes. Recorre el
	 * parámetro threshold para obtener el valor óptimo. Se detendrá en el momento en el que el número de atributos se
	 * reduzca al máximo posible.
	 * 
	 * @param data	Conjunto de nstancias sobre las cuales aplicar la selección de atributos.
	 */
	public static Instances useFilter(Instances data) throws Exception {
		AttributeSelection attSel = new AttributeSelection();
		Ranker rank = new Ranker();
		boolean stop = false;
		double th = -0.1;
		Instances newData = null;
		while (!stop) {
			System.out.println(th);
			rank.setThreshold(th);
			attSel.setEvaluator(new InfoGainAttributeEval());
			attSel.setSearch(rank);
			attSel.setInputFormat(data);
			newData = Filter.useFilter(data, attSel);
			if (data.numAttributes() != newData.numAttributes()) {
				stop = true;
			}
			th += 0.005;
		}
		// Damos a la relación su nombre original
		newData.setRelationName(data.relationName());
		return newData;
	}

}
