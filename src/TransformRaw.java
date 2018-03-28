
import java.io.File;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;
import weka.filters.unsupervised.instance.NonSparseToSparse;

public class TransformRaw {
	/**
	 * Transforma el espacio de atributos del conjunto de entrenamiento a BoW o
	 * TF�IDF
	 *
	 * @param args
	 *            Par�metros de entrada
	 * @throws Exception
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("=====TRANSFORM RAW=====");
			System.out.println(
					"Este programa tiene como funci�n transformar el espacio de atributos del conjunto de entrenamiento a BoW o TF�IDF.");
			System.out.println("Este programa necesita que introduzcas 5 argumentos para funcionar correctamente.");
			System.out.println(
					"PRECONDICIONES:\nEl primer argumento ser� el path del conjunto de entrenamiento a transformar. "
							+ "El segundo es el path destino. El tercero indicar� si queremos transformar a BoW o TF�IDF, los valores v�lidos: 'BOW' , 'TF�IDF'."
							+ "El cuarto indicar� si queremos una representaci�n Sparse o NonSparse, los valores v�lidos: 'Sparse' , 'NonSparse'"+
							"Y el último será el path donde guardar el diccionario generado");
			System.out.println(
					"POSTCONDICIONES:\nEl resultado de esta aplicaci�n ser� una representaci�n BoW o TF�IDF del espacio de atributos del conjunto de entrenamiento ");
			System.out.println("Lista de argumentos:\n" + "-- Path del conjunto de entrenamiento a transformar."
					+ "\n-- Path de salida." + "\n-- Opci�n BoW o TF�IDF" + "\n-- Opci�n Sparse o NonSparse"+"\n-- Path del diccionario generado");
			System.out.println(
					"Ejemplo de una correcta ejecuci�n: java -jar TransformRaw.jar /path/to/train.arff /path/to/trainBOW.arff BOW NonSparse /path/to/diccionario");
			System.exit(0);
		} else {
			Instances data = Utilities.loadArff(args[0]);
			data.setClassIndex(data.numAttributes() - 1);
			String pathDicc= args[4];
			StringToWordVector filter = null;
			Instances dataFiltered = null;
			/*
			 * Transformamos el arff raw a BOW.
			 */
			if (args[2].equals("BOW")) {
				filter = new StringToWordVector();
				filter.setInputFormat(data);
				filter.setIDFTransform(false);
				filter.setTFTransform(false);
				filter.setDictionaryFileToSaveTo(new File(pathDicc));
				dataFiltered = Filter.useFilter(data, filter);

			}
			/*
			 * Transformamos el arff raw a TF�IDF
			 */
			else {
				filter = new StringToWordVector();
				filter.setInputFormat(data);
				filter.setIDFTransform(true);
				filter.setTFTransform(true);
				filter.setOutputWordCounts(true);
				filter.setDictionaryFileToSaveTo(new File(pathDicc));
				dataFiltered = Filter.useFilter(data, filter);
			}

			/*
			 * Aplicamos el filtro NonSparseToSparse.
			 */
			if (args[3].equals("Sparse")) {
				NonSparseToSparse filtroSparse = new NonSparseToSparse();
				filtroSparse.setInputFormat(dataFiltered);
				dataFiltered = Filter.useFilter(dataFiltered, filtroSparse);
			}

			/*
			 * guardamos los datos en el path especificado
			 */
            Utilities.saveArff(dataFiltered, args[1]);
		}

	}
}
