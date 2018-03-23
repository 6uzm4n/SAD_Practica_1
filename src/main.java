import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main (String[] args) throws IOException {
        //TODO: Implementar comandos del usuario

        //Esto iría dentro de algun comando
        //getRaw("/home/guzman/Desktop/movies_reviews/dev", "/home/guzman/Desktop/aaa.arff");
    }

    private static void getRaw (String pathIn, String pathOut) throws IOException {
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(path));
        Instances raw = loader.getDataSet();
        BufferedWriter bw = new BufferedWriter(new FileWriter(pathOut));
        bw.write(raw.toString());
        bw.close();
    }

    private static void transformRaw (){

    }

    private static void makeCompatible (){

    }
}
