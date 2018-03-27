import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;

/**
 * Guarda las instancias que se le pasan como parametro en la ruta especificada
 * en formato .arff
 *
 * @param instances
 *            Conjunto de instancias a convertir y guardar en el fichero .arff
 * @param pathOut
 *            Ruta donde se guardar� el fichero resultante de la conversi�n
 */
public class Utilities {
    public static void saveArff (Instances instances, String pathOut) {
        try {
            ArffSaver arffSaver = new ArffSaver();
            arffSaver.setInstances(instances);
            arffSaver.setFile(new File(pathOut));
            arffSaver.writeBatch();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
