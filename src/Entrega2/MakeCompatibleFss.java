package Entrega2;

import java.io.File;
import java.io.FileWriter;
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
		String pathFss = "";
		String pathRaw = "";
		String pathNewFss = "";
		if (args.length == 0){
			System.out.println("=====Make Cmpatible FSS=====");
			System.out.println("Este programa hace que el espacio de atributos del conjunto de evaluacion sea compatible con el de entrenamiento.");
			System.out.println("Este programa necesita que introduzcas 3 argumentos.");
			System.out.println("PRECONDICIONES:\nEl fichero modelo a utilizar será de formato .arff y tener un " +
					"atributo de tipo String");
			System.out.println("POSTCONDICIONES:\nEl resultado de este programa será un fichero .arff compatible con el introducido como parámetro "
					+ ".\n");
			System.out.println("Lista de argumentos:\n-Ruta del fichero .arff de referencia\n" +
					"-Ruta del fichero .arff para compatibilizar.\n" +
					"-Ruta del fichero .arff compatible de salida");
			System.out.println("Ejemplo de una correcta ejecución: java -jar MakeCompatibleFss.jar /path/to/input/trainFss /path/to/input/dev /path/to/input/devFSSCompatible");

		}else if (args.length == 3){
			pathFss = args[0];
			pathRaw = args[1];
			pathNewFss = args[2];
		}else{
			CommonUtilities.printlnError("Error en el input. Revise su sintaxis.");
			System.exit(1);
		}

		try{
			makeCompatible(pathFss, pathRaw, pathNewFss);
		}catch (Exception e){
			CommonUtilities.printlnError("Error en los ficheros de entrada.");
			e.printStackTrace();
			System.exit(1);
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