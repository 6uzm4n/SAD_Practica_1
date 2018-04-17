package Entrega2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class GetModel {


    public static void main(String[] args) throws Exception {

        String Instancias = args[0];
        String atributoKernel=  args[1];
        String pathModel = args[2];
        String pathWrite = args[3];
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

            getModel(Inst, AtrOpt, pathModel, pathWrite);
        }
    }

    /**
     * Escribe los resultados de realizar con el algoritmo SVM empleando el 10-FoldCrossValidation y el Resubstitution error, así como el propio modelo
     *
     * @param trainedData instancias con las que evaluar
     * @param atributoExponente atributo optimo para generar el clasificador
     * @param pathToSaveModel ruta del modelo
     * @param pathToWrite ruta del archivo
     */

    private static void getModel(Instances trainedData, double atributoExponente, String pathToSaveModel,String pathToWrite) throws Exception {

        Classifier ClasSMO = new SMO();
        Evaluation eval = evalKFoldCrossValidation(ClasSMO, trainedData, 10, 1);
        escribirResult("10-fold Cross Validation :",eval,trainedData.numAttributes() - 1,pathToWrite);
        ClasSMO.buildClassifier(trainedData);
        Evaluation eval2 = new Evaluation(trainedData);
        eval2.evaluateModel(ClasSMO, trainedData);
        escribirResult("Resubstitution error :",eval2,trainedData.numAttributes() - 1,pathToWrite);
        saveModel(ClasSMO, pathToSaveModel);

    }

    /**
     * Escribe el el evaluador empleado asi como los resultados obtenidos de precision, recall y F-Measure.
     *
     * @param Titulo nombre del evaluador empleado
     * @param eval evaluador empleado
     * @param numClassIndex número de la clase minoritaria
     * @param pathToWrite ruta del archivo
     */

    private static void escribirResult(String Titulo,Evaluation eval, int numClassIndex, String pathToWrite) {
        StringBuilder result = new StringBuilder();
        result.append(Titulo).append("\n");
        double pre = eval.precision(numClassIndex);
        result.append("Precision : ").append(pre).append("\n");
        double re = eval.recall(numClassIndex);
        result.append("Recall : ").append(re).append("\n");
        double fMe = eval.fMeasure(numClassIndex);
        result.append("F-Measure : ").append(fMe).append("\n");

        writeToFile(result.toString(),pathToWrite );
    }

    /**
     * Escribe el texto pTexto en el archivo en la ruta pPath.
     *
     * @param pText texto a escribir
     * @param pPath ruta del archivo
     */
    private static void writeToFile(String pText, String pPath) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(pPath));
            bf.write(pText);
            bf.close();
        } catch (IOException e) {
            printlnError(String.format("Error al escribir en %s", pPath));
            e.printStackTrace();
        }
    }

    /**
     * Devuelve el clasificador del tipo SVM creado con los valores optimos
     *
     * @param trainedData  instancias con las que evaluar
     * @param atributoExponente atributo optimo para generar el clasificador
     * @return el objeto Clasificador
     */

    private Classifier getSMO(Instances trainedData, double atributoExponente) throws Exception {
        Classifier model = new SMO();
        ((PolyKernel) ((SMO) model).getKernel()).setExponent(atributoExponente);
        model.buildClassifier(trainedData);
        return model;
    }
    /**
     * Guarda el clasificador pClassifier en la ruta pPath.
     *
     * @param pClassifier clasificador a guardar
     * @param pPath       ruta del archivo a crear
     *
     */
    private static void saveModel(Classifier pClassifier, String pPath) {
        try {
            SerializationHelper.write(pPath, pClassifier);
        } catch (Exception e) {
            printlnError("Error al guardar el clasificador");
            e.printStackTrace();
        }
    }

    /**
     * Escribe por consola el texto pTexto en color rojo.
     * SIEMPRE incluye salto de línea.
     *
     * @param pText texto a escribir
     */
    private static void printlnError(String pText) {
        printError(String.format("%s\n", pText));
    }

    /**
     * Escribe por consola el texto pTexto en color rojo.
     * NO incluye salto de línea.
     *
     * @param pText texto a escribir
     */
    private static void printError(String pText) {
        System.out.print(String.format("\33[31m%s\33[0m", pText));
    }

    /**
     * Evaluación k-Fold Cross-Validation
     * Realiza una evaluación k-Fold Cross-Validation sobre el clasificador pClassifier con las instancias pIsntaces y la seed pSeed donde k es pFolds.
     *
     * @param pClassifier clasificador a evaluar
     * @param pInstances  instancias con las que evaluar
     * @param pFolds      número de iteraciones a realizar
     * @param pSeed       seed para la randomizanión
     * @return el objeto Evaluation que contiene los resultados de la evaluación, null si hay problemas al evaluar el clasificador.
     */

    private static Evaluation evalKFoldCrossValidation(Classifier pClassifier, Instances pInstances, int pFolds,
                                                       long pSeed) {
        Evaluation evaluation = null;
        try {
            evaluation.crossValidateModel(pClassifier, pInstances, pFolds, new Random(pSeed));
        } catch (Exception e) {
            printlnError("Error al evaluar el clasificador");
            e.printStackTrace();
        }
        return evaluation;
    }

}
