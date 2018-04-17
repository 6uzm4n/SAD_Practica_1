package Utilities;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class CommonUtilities {
    /**
     * Cargamos las instancias del fichero
     *
     * @param path
     *            de entrada de ficheros
     * @exception Exception
     *                Si no se pueden obtener correctamente las instancias a partir
     *                de los ficheros de entrada
     */
    public static Instances loadArff(String path) throws Exception {

        FileReader file = null;

        try {
            file = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Comprueba el path del fichero: " + path);
        }

        Instances data = null;

        try {
            data = new Instances(file);
        } catch (IOException e) {
            System.out.println("ERROR: Comprueba el path del fichero: " + path);
        }

        file.close();

        return data;
    }

    /**
     * Guarda las instancias que se le pasan como parametro en la ruta especificada
     * en formato .arff
     *
     * @param instances
     *            Conjunto de instancias a convertir y guardar en el fichero .arff
     * @param pathOut
     *            Ruta donde se guardar� el fichero resultante de la conversi�n
     */
    public static void saveArff (Instances instances, String pathOut){
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
