

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.attribute.*;
import weka.filters.unsupervised.instance.NonSparseToSparse;

public class TransformRaw {
	/**
	 * Transforma el espacio de atributos del conjunto de entrenamiento a BoW
	 * o TF·IDF
	 *
	 * @param args
	 *            Parámetros de entrada
	 * @throws Exception
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	public static void main(String[] args) throws Exception {
		
		if (args.length == 0) {
			System.out.println("=====TRANSFORM RAW=====");
			System.out.println(
					"Este programa tiene como función transformar el espacio de atributos del conjunto de entrenamiento a BoW o TF·IDF.");
			System.out.println("Este programa necesita que introduzcas 4 argumentos para funcionar correctamente.");
			System.out.println(
					"PRECONDICIONES:\nEl primer argumento será el path del conjunto de entrenamiento a transformar. "
							+ "El segundo es el path destino. El tercero indicará si queremos transformar a BoW o TF·IDF, los valores válidos: 'BOW' , 'TF·IDF'."
							+ "Y el cuarto indicará si queremos una representación Sparse o NonSparse, los valores válidos: 'Sparse' , 'NonSparse'");
			System.out.println(
					"POSTCONDICIONES:\nEl resultado de esta aplicación será una representación BoW o TF·IDF del espacio de atributos del conjunto de entrenamiento ");
			System.out.println("Lista de argumentos:\n" + "-- Path del conjunto de entrenamiento a transformar."
					+ "\n-- Path de salida."
					+ "\n-- Opción BoW o TF·IDF"
					+ "\n-- Opción Sparse o NonSparse");
			System.out.println(
					"Ejemplo de una correcta ejecución: java -jar TransformRaw.jar /path/to/train.arff /path/to/trainBOW.arff BOW NonSparse");
			System.exit(0);
		} 
		else{
						Instances data = cargarFichero(args[0]);
						data.setClassIndex(data.numAttributes()-1);
						
						StringToWordVector filter = null;
						Instances dataFiltered = null;
				/**
				 * Transformamos el arff raw a BOW.
				 */
				if(args[2].equals("BOW")){
					filter = new StringToWordVector();
					filter.setInputFormat(data);
					filter.setIDFTransform(false);
					filter.setTFTransform(false);
					dataFiltered = filter.useFilter(data,filter);
				
				}
				/**
				 * Transformamos el arff raw a TF·IDF
				 */
				else{
					filter = new StringToWordVector();
					filter.setInputFormat(data);
					filter.setIDFTransform(true);
					filter.setTFTransform(true);
					filter.setOutputWordCounts(true);
					dataFiltered = filter.useFilter(data,filter);
				}
				
				/**
				 * Aplicamos el filtro NonSparseToSparse.
				 */
				if(args[3].equals("Sparse"))
				{
					NonSparseToSparse filtroSparse = new NonSparseToSparse();
					filtroSparse.setInputFormat(dataFiltered);
					dataFiltered= filtroSparse.useFilter(dataFiltered, filtroSparse);
				}
				
				/*
				 * guardamos los datos en el path especificado
				 */
				guardarDatos(args[1],dataFiltered);
		}
		
		
	}
	
	/**
	 * Cargamos las instancias del fichero
	 * @param path de entrada de ficheros
	 * @exception Exception
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	private static Instances cargarFichero(String path) throws Exception{

		
			FileReader file = null;
					
					try{
						file = new FileReader(path);
					}
					catch(FileNotFoundException e)
					{
						System.out.println("ERROR: Comprueba el path del fichero: "+path);
					}
					
					Instances data = null;
					
					try{
						data = new Instances(file);
					}
					catch(IOException e)
					{
						System.out.println("ERROR: Comprueba el path del fichero: "+path);
					}
					
					try{
						file.close();
					}
					catch(IOException e)
					{}
		return data;
	}
	
	/**
	 * guarda los datos (instancias) en la ruta especificada
	 * @param path 
	 * @param data 
	 */
	private static void guardarDatos(String path, Instances data) {
		try {
			File file = new File(path);
			ArffSaver salvador = new ArffSaver();
			salvador.setInstances(data);
			salvador.setFile(file);
			salvador.writeBatch();
		} catch (IOException e) {
			System.err.print("Error al guardar.");
		}
	}
}
