package Entrega2.fss;

import java.io.File;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class FssInfoGain {
	/**
	 * FssInfoGain sirve para realizar una seleccion de atributos dado un conjunto
	 * de datos en un fichero .arff, y genera uno nuevo una vez realizada esta
	 * selección. La selección de atributos consiste en la eliminación de atributos
	 * redundante e irrelevantes.
	 *
	 * @param args
	 *            Parámetros de entrada args[0] - ruta del fichero .arff sobre la
	 *            cual se quiere trabajar args[1] - ruta del fichero .arff de salida
	 */
	public static void main(String[] args) {
		String trainBow = null;
		String trainBowFss = null;
		try {
			trainBow = args[0];
			trainBowFss = args[1];

		} catch (IndexOutOfBoundsException e) {
			String q = "Este programa tiene como función descartar atributos redundantes o irrelevantes para el proceso de clasificación\n"
					+ "El archivo .arff original debe tener por lo menos un atributo de tipo String.\n"
					+ "Este programa espera dos argumentos:\n" + "\t1 - Ruta del fichero .arff de entrada\n"
					+ "\t2 - Ruta del fichero .arff de salida\n"
					+ "\nEjemplo: java -jar FssInfoGain.jar /path/to/input/arff /path/to/output/arff";
			System.out.println(q);
			System.exit(1);
		} catch (IllegalArgumentException e) {
			System.out.println((String.format("El argumento %s introduci es incorrecto.")));
			System.exit(1);
		}
		Instances datos = Utilities.CommonUtilities.loadInstances(trainBow, 0);
		System.out.println("index: " + datos.classIndex());
		try {
			Instances newData = useFilter(datos);

			ArffSaver saver = new ArffSaver();
			saver.setInstances(newData);
			saver.setFile(new File(trainBowFss));
			saver.writeBatch();

		} catch (Exception e) {
			System.out.println("No se ha podido completar el filtrado ");
			e.printStackTrace();
		}

	}

	/**
	 * Realiza una seleccion de atributos, en concreto, elimina algunos atributos,
	 * recorre el parámetro threshold para obtener el valor óptimo. Se detendrá en
	 * el momento en el que el número de atributos se reduzca.
	 * 
	 * @param pData
	 *            instancias sobre las cuales aplicar la seleccion de tributos
	 */
	public static Instances useFilter(Instances pData) throws Exception {
		AttributeSelection attSel = new AttributeSelection();
		Ranker rank = new Ranker();
		boolean stop = false;
		double th = -0.1;
		Instances newData = null;
		while (!stop) {
			rank.setThreshold(th);
			attSel.setEvaluator(new InfoGainAttributeEval());
			attSel.setSearch(rank);
			attSel.setInputFormat(pData);
			newData = Filter.useFilter(pData, attSel);
			if (pData.numAttributes() != newData.numAttributes()) {
				stop = true;
			}
			th += 0.005;
			System.out.println(th);
		}
		return newData;
	}

}
