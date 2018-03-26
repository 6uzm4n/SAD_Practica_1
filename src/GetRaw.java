import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//TODO: Documentar bien todos los mÃ©todos (JavaDoc).
public class GetRaw {
	/**
	 * Crea un archivo arff a partir de un archivo de texto (raw).
	 *
	 * @param args
	 *            ParÃ¡metros de entrada
	 * @throws IOException
	 *             Si no se puede leer o escribir un archivo.
	 */
	public static void main(String[] args) throws IOException {
		String pathIn = null;
		String pathOut = null;
		if (args.length==0){
			System.out.println("Este programa necesita que introduzcas 2 argumentos para funcionar correctamente\n");
			System.out.println("-Este programa tiene como función obtener un fichero .arff en formato Raw para su uso en minería de datos.\n");
			System.out.println("Precondiciones: el primer argumento será el path de la raíz del árbol de directorios a convertir. El segundo es el path del fichero de salida .arff\n");
			System.out.println("Postcondiciones: el resultado de esta aplicación será la creación de un fichero .arff en el path especificado en los argumentos\n");
			System.out.println("Lista de argumentos:\n"+"- Path de la raíz del árbol de directorios a convertir.\n"+"- Path del destino donde se guardará el fichero resultante tras la ejecución");
			System.out.println("Ejemplo de una correcta ejecución: java -jar getRaw.jar train train.arff");
			System.exit(1);
		}else {
		try {
			pathIn = args[0];
			pathOut = args[1];
		} catch (IndexOutOfBoundsException e) {

			System.out.println("Error en el input. Revise su sintaxis.");
			System.exit(1);
		}

		getRaw(pathIn, pathOut);
	}
	}

	/**
     * Este mÃ©todo crea un archivo arff en la ruta pathOut a partir del archivo de texto raw leÃ­do en la ruta pathIn.
     *
     * @param pathIn        Ruta en la que estÃ¡ ubicado el archivo raw.
     * @param pathOut       Ruta en la que se crea el nuevo archivo arff,
     * @throws IOException  Si no se puede leer o escribir un archivo.
     */
    private static void getRaw (String pathIn, String pathOut) throws IOException {
        //Carga y lectura del archivo raw.
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(pathIn));
        Instances raw = loader.getDataSet();
        raw.renameAttribute(raw.classIndex(), "text");
        raw.setRelationName("class");

        //CreaciÃ³n del archivo arff.
        BufferedWriter bw = new BufferedWriter(new FileWriter(pathOut));
        bw.write(raw.toString());
        bw.close();
    }
}
