package Entrega2.fss;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import Utilities.CommonUtilities;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Reorder;

public class MakeCompatibleFss {
	/**
	 * Hace compatible un conjunto de evaluación representándolo en un espacio de atributos compatible con el del
	 * conjunto de entrenamiento y lo guarda en la ruta especificada.
	 *
	 * @param args	Parámetros de entrada. En caso de no introducir ninguno se muestra una descripción de estos.
	 */
	public static void main(String[] args){
		//gestionar parámetros
		String pathFss = null;
		String pathRaw = null;
		String pathNewFss = null;
		try {
			pathFss = args[0];
			pathRaw = args[1];
			pathNewFss = args[2];
		} catch (IndexOutOfBoundsException e) {
			String q = "Este programa hace que el espacio de atributos del conjunto de evaluacion sea compatible con el de entrenamiento\n"
					+ "El fichero .arff a convertir debe tener al menos un atributo de tipo String.\n"
					+ "Este programa espera 3 argumentos:\n" + "\t1 - Ruta del fichero .arff de referencia\n"
					+ "\t2 - Ruta del fichero .arff para compatibilizar\n"
					+ "\t3 - Ruta del fichero .arff compatible de salida\n"
					+ "\nEjemplo: java -jar MakeCompatibleFss.jar /path/to/input/trainFss /path/to/input/dev /path/to/input/devFSSCompatible";
			System.out.println(q);
			System.exit(1);
		}
		try {
			makeCompatible(pathFss, pathRaw, pathNewFss);
		} catch (Exception e) {

			System.out.println("Argumentos incorrectos");
			e.printStackTrace();
		}
	}

	/**
	 * Aplica el mismo filtro que se ha aplicado al conjunto de entrenamiento de entrada al conjunto de evaluacion
	 * pasado como parametro y lo guarda en la ruta especificada
	 *
	 * @param pathInFss	ruta del fichero .arff de referencia.
	 * @param pathInRaw	ruta del fichero .arff a compatibilizar.
	 * @param pathOut	ruta del fichero .arff generado de salida.
	 */
	private static void makeCompatible(String pathInFss, String pathInRaw, String pathOut) throws Exception {
		//Cargar instancias
		Instances fss = Utilities.CommonUtilities.loadArff(pathInFss, -1);
		Instances bow = Utilities.CommonUtilities.loadArff(pathInRaw, -1);
		
		//Generar diccionariol temporal
		String pathDiccTmp = "diccionarioTmp";
		FileWriter diccFSS = new FileWriter(new File(pathDiccTmp));
		for (int i = 0; i < fss.numAttributes() - 1; i++) {
			diccFSS.write("\n" + fss.attribute(i).name());
		}
		diccFSS.flush();
		diccFSS.close();
		
		//Transformar el espacio de atributos segun el diccionario generado
		FixedDictionaryStringToWordVector fixedFilter = new FixedDictionaryStringToWordVector();
		fixedFilter.setDictionaryFile(new File(pathDiccTmp));
		fixedFilter.setOutputWordCounts(true);
		fixedFilter.setInputFormat(bow);
		Instances newData = Filter.useFilter(bow, fixedFilter);
		
		//Eliminar fichero temporal del diccionario
		Files.delete(Paths.get(pathDiccTmp));
		
		//Ordenar atributos
		Reorder reorderFilter = new Reorder();
		reorderFilter.setInputFormat(newData);
		reorderFilter.setOptions(new String[] { "-R", "2-last,1" });
		newData = Filter.useFilter(newData, reorderFilter);
		newData.setClassIndex(newData.numAttributes() - 1);

		// Damos a la nueva relación su nombre original
		newData.setRelationName(bow.relationName());

		CommonUtilities.saveArff(newData, pathOut);

	}

}