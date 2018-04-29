package Entrega2;

import Utilities.CommonUtilities;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class GetModel {

    /**
     * Genera un modelo SVM optimizado para un conjunto de instancias dado.
     *
     * @param args  Parámetros de entrada. En caso de no introducir ninguno se muestra una descripción de estos.
     */
    public static void main(String[] args) throws Exception {
    	 String Instancias = null;
         String atributoKernel=  null;
         String pathModel = null;
         String pathWrite = null;
        try{
            if (args.length == 0){
                System.out.println("=====================================================================");
                System.out.println("Este programa tiene como función generar un modelo Support Vector Machine óptimo dado un conjunto de instancias.");
                System.out.println("PRECONDICIÓN: El path donde se desea guardar el modelo debe ser un directorio.");
                System.out.println("POSCONDICIÓN: Se han creado tanto SVM.model como SVM_quality.txt");
                System.out.println("ARGUMENTOS:\n--El conjunto de instancias con las que evaluar." +
                                   "\n--El atributo optimo para generar el clasificador empleando el algoritmo SVM" +
                                   "\n--La ruta donde guardar el modelo generado." +
                                   "\n-- Path donde se desea guardar tanto el modelo como su calidad estimada.");
                System.out.println("EJEMPLO DE USO: \"java -jar GetModel.jar /path/to/data.arff numero_optimo  /path/to/model/ /path/to/destination/\"");
                System.out.println("=====================================================================");

                        System.exit(0);
            }else if (args.length == 4) {
                Instancias = args[0];
                atributoKernel=  args[1];
                pathModel = args[2];
                pathWrite = args[3];
            }else{
                CommonUtilities.printlnError("Error en los argumentos.");
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        
        DataSource DataS;
        double AtrOpt=  Double.parseDouble(atributoKernel);
        Instances Inst = null;
        try {
            DataS = new DataSource(Instancias);
            Inst = DataS.getDataSet();
            Inst.setClassIndex(Inst.numAttributes()-1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        getModel(Inst, AtrOpt, pathModel, pathWrite);
    }

    /**
     * Escribe los resultados de realizar con el algoritmo SVM empleando el 10-FoldCrossValidation y guarda el modelo en
     * el directorio dado.
     *
     * @param trainedData       Instancias con las que evaluar.
     * @param atributoExponente Exponente optimo para generar el clasificador SVM.
     * @param pathToSaveModel   Ruta donde guardar el modelo generado.
     * @param pathToWrite       Carpeta donde guardar los archivos generados.
     */
    private static void getModel(Instances trainedData, double atributoExponente, String pathToSaveModel,String pathToWrite) throws Exception {
        Classifier ClassifierSMO = getSMO(atributoExponente);

        Evaluation evalKFold = CommonUtilities.evalKFoldCrossValidation(ClassifierSMO, trainedData, 10, 1);
        CommonUtilities.writeQuality(evalKFold, pathToWrite + trainedData.relationName() + "_SVM_k-fold_quality.txt");
        ClassifierSMO.buildClassifier(trainedData);
        CommonUtilities.saveModel(ClassifierSMO, pathToSaveModel);

    }

    /**
     * Devuelve el clasificador del tipo SVM creado con los valores óptimos.
     *
     * @param atributoExponente Atributo optimo para generar el clasificador SVM.
     * @return                  Classificador generado.
     */

    private static Classifier getSMO(double atributoExponente){
        Classifier model = new SMO();
        PolyKernel pk = new PolyKernel();
        pk.setExponent(atributoExponente);
        ((SMO) model).setKernel(pk);
        return model;
    }
}
