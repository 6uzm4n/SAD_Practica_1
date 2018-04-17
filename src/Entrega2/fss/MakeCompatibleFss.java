package Entrega2.fss;

import java.io.File;
import java.io.IOException;

import Utilities.CommonUtilities;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MakeCompatibleFss {
	/**
	 * Hace compatible un conjunto de evaluacion representandolo en un espacio de
	 * atributos compatible con el del conjunto de entrenamiento y o guarda en la
	 * ruta especificada
	 *
	 * @param args
	 *            Parámetros de entrada -args[0] - ruta del fichero .arff que se
	 *            debe tomar como referencia -args[1] - ruta del fichero .arff que
	 *            se quiere hacer compatible -args[2] - ruta del fichero .arff
	 *            compatible que se desea generar
	 * @throws IOException
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	public static void main(String[] args) throws IOException {
		String pathFss = null;
		String pathBoW = null;
		String pathNewFss = null;
		try {
			pathFss = args[0];
			pathBoW = args[1];
			pathNewFss = args[2];
		} catch (IndexOutOfBoundsException e) {
			String q = "Este programa hace que el espacio de atributos del conjunto de evaluacion sea compatible con el de entrenamiento\n"
					+ "El fichero .arff a convertir debe tener al menos un atributo de tipo String.\n"
					+ "Este programa espera 3 argumentos:\n" + "\t1 - Ruta del fichero .arff de referencia\n"
					+ "\t2 - Ruta del fichero .arff para compatibilizar\n"
					+ "\t3 - Ruta del fichero .arff compatible de salida\n"
					+ "\nEjemplo: java -jar MakeCompatibleFss.jar /path/to/input/trainFss /path/to/input/dev path/to/output/devCompatible";
			System.out.println(q);
			System.exit(1);
		}
		try {
			makeCompatible(pathFss, pathBoW, pathNewFss);
		} catch (Exception e) {

			System.out.println("Argumentos incorrectos");
			e.printStackTrace();
		}
	}

	/**
	 * Aplica el mismo filtro que se ha aplicado al conjunto de entrenamiento de
	 * entrada al conjunto de evaluacion pasado como parametro y lo guarda en la
	 * ruta especificada
	 *
	 * @param pathInFss
	 *            ruta del fichero .arff referencia
	 * @param pathInBoW
	 *            ruta del fichero .arff a compatibilizar
	 * @param pathOut
	 *            ruta del fichero .arff generado de salida
	 * @throws IOException
	 */
	private static void makeCompatible(String pathInFss, String pathInBoW, String pathOut) throws Exception {
		Instances train = Utilities.CommonUtilities.loadInstances(pathInFss, 0);
		Instances dev = Utilities.CommonUtilities.loadInstances(pathInBoW, 0);
		
		AttributeSelection attSel = new AttributeSelection();

		Instances newDev = null;
		Instances newTrain = null;
		
		NumericToNominal hai = new NumericToNominal();
		Instances trainn = null;
		hai.setInputFormat(train);
		try {
		    trainn = Filter.useFilter(train, hai);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		attSel.setInputFormat(trainn);
		attSel.setEvaluator(new InfoGainAttributeEval());
		attSel.setSearch(new Ranker());
		newTrain = Filter.useFilter(trainn, attSel);
		newDev = Filter.useFilter(dev, attSel);
		newDev.setClassIndex(newDev.numAttributes() - 1);
		
		CommonUtilities.saveArff(newDev, pathOut);

	}

}