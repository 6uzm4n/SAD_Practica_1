package Entrega2.fss;

import java.io.File;
import java.io.IOException;

import Utilities.CommonUtilities;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class MakeCompatibleFss {

	public static void main(String[] args) throws IOException {
		String pathDev = null;
	//	String pathTrain = null;
		String pOutDev = null;
		String pathDicc = null;
		try {
			pathDev = args[0];
		//	pathTrain = args[1];
			pOutDev = args[1];
			pathDicc = args[2];
		} catch (IndexOutOfBoundsException e) {
			String q = "Este programa hace que el espacio de atributos del conjunto de evaluacion sea compatible con el de entrenamiento\n"
					+ "El fichero .arff a convertir debe tener al menos un atributo de tipo String.\n"
					+ "Este programa espera 3 argumentos:\n" + "\t1 - Ruta del fichero .arff a convertir\n"
			//		+ "\t2 - Ruta del fichero .arff que se toma como modelo\n"
					+ "\t2 - Ruta del fichero .arff compatible de salida\n"
					+ "\t3 - Ruta del diccionario\n"
					+ "\nEjemplo: java -jar MakeCompatibleFss.jar /path/to/input/dev /path/to/output/devCompatible path/to/input/diccionario";
			System.out.println(q);
			System.exit(1);
		}
		try {
			makeCompatible(pathDev, pOutDev, pathDicc);
		} catch (Exception e) {

			System.out.println("Argumentos incorrectos");
			e.printStackTrace();
		}
	}

	/**
	 * Utiliza un diccionario para convertir los atributos String de un fichero
	 * .arff a Word Vector
	 *
	 * @param pathInDev
	 *            ruta del fichero .arff a convertir
	 * @param pathOutDev
	 *            ruta del fichero .arff a generar
	 * @param pathDicc
	 *            ruta del diccionario que se usa como modelo
	 * @throws IOException
	 */
	private static void makeCompatible(String pathInDev, String pathOutDev, String pathDicc) throws Exception {
		//Instances train = Utilities.CommonUtilities.loadInstances(pathTrain, 0);
		Instances dev = Utilities.CommonUtilities.loadInstances(pathInDev, 0);
		
		FixedDictionaryStringToWordVector fixedFilter = new FixedDictionaryStringToWordVector();
		fixedFilter.setDictionaryFile(new File(pathDicc));
		fixedFilter.setInputFormat(dev);
		Instances newTest = Filter.useFilter(dev, fixedFilter);

		CommonUtilities.saveArff(newTest, pathOutDev);
	}

}