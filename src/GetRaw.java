import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//TODO: Documentar bien todos los métodos (JavaDoc).
public class GetRaw {
    /**
     * Crea un archivo arff a partir de un archivo de texto (raw).
     *
     * @param args          Parámetros de entrada
     * @throws IOException  Si no se puede leer o escribir un archivo.
     */
    public static void main (String[] args) throws IOException {
        String pathIn = null;
        String pathOut = null;
        if (args.length == 2) {
            pathIn = args[0];
            pathOut = args[1];
        }else{
            System.out.println("Error en el input. Revise su sintaxis.");
            System.exit(1);
        }

        getRaw(pathIn, pathOut);
    }

    /**
     * Este método crea un archivo arff en la ruta pathOut a partir del archivo de texto raw leído en la ruta pathIn.
     *
     * @param pathIn        Ruta en la que está ubicado el archivo raw.
     * @param pathOut       Ruta en la que se crea el nuevo archivo arff,
     * @throws IOException  Si no se puede leer o escribir un archivo.
     */
    private static void getRaw (String pathIn, String pathOut) throws IOException {
        //Carga y lectura del archivo raw.
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(pathIn));
        Instances raw = loader.getDataSet();

        //Creación del archivo arff.
        BufferedWriter bw = new BufferedWriter(new FileWriter(pathOut));
        bw.write(raw.toString());
        bw.close();
    }
}
