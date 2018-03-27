import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class MakeCompatible {
	/**
	 * Hace compatible un conjunto de evaluación representandolo en un espacio de
	 * atributos compatible con el del entrenamiento
	 *
	 * @param args
	 *            ParÃ¡metros de entrada
	 * @throws Exception
	 *             Si no se pueden obtener correctamente las instancias a partir de
	 *             los ficheros de entrada
	 */
	public static void main(String[] args) throws Exception {
		String pathBoW = args[0];
		String pathIn = args[1];
		String pathOut = args[2];
		DataSource source0 = new DataSource(pathBoW);
		DataSource source1 = new DataSource(pathIn);

		Instances train = null;
		Instances test = null;
		try {
			train = source0.getDataSet();
			test = source1.getDataSet();
			if (train.classIndex() == -1) {
				train.setClassIndex(train.numAttributes() - 1);
				test.setClassIndex(test.numAttributes() - 1);
			}
		} catch (NullPointerException e) {
			System.out.println("Problema al cargar los datos");
			System.exit(1);
		}

		Standardize filter = new Standardize();
		try {
			filter.setInputFormat(train);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Instances newTrain = Filter.useFilter(train, filter);
		Instances newTest = Filter.useFilter(test, filter);
		saveArff(newTest, pathOut);
	}

	/**
	 * Guarda las instancias que se le pasan como parametro en la ruta especificada
	 * en formato .arff
	 *
	 * @param instances
	 *            Conjunto de instancias a convertir y guardar en el fichero .arff
	 * @param pathOut
	 *            Ruta donde se guardará el fichero resultante de la conversión
	 */
	private static void saveArff(Instances instances, String pathOut) {
		try {
			ArffSaver arffSaver = new ArffSaver();
			arffSaver.setInstances(instances);
			arffSaver.setFile(new File(pathOut));
			arffSaver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
