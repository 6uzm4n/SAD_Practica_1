package Utilities;

import java.io.*;

public class DirectoryCleaner {

    public static void main (String[] args){
        try{
            cleanFiles(args[0]);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param pDirRoot
     */
    private static void cleanFiles(String pDirRoot) {
        File rootFolder = new File(pDirRoot);
        File[] listOfFiles = rootFolder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    cleanFile(file);
                } else if (file.isDirectory()) {
                    cleanFiles(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * @param pFile
     */
    private static void cleanFile(File pFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(pFile));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            String newText = text.toString().replaceAll("[^\\p{Print}]", "");
            BufferedWriter bw = new BufferedWriter(new FileWriter(pFile));
            bw.write(newText, 0, newText.length());
            bw.newLine();
            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
