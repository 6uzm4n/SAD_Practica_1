package Entrega2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Utilities.CommonUtilities;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instances;
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
        Evaluation eval = CommonUtilities.evalKFoldCrossValidation(ClasSMO, trainedData, 10, 1);
        escribirResult("10-fold Cross Validation :",eval,trainedData.numAttributes() - 1,pathToWrite);
        ClasSMO.buildClassifier(trainedData);
        Evaluation eval2 = new Evaluation(trainedData);
        eval2.evaluateModel(ClasSMO, trainedData);
        escribirResult("Resubstitution error :",eval2,trainedData.numAttributes() - 1,pathToWrite);
        CommonUtilities.saveModel(ClasSMO, pathToSaveModel);

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
            CommonUtilities.printlnError(String.format("Error al escribir en %s", pPath));
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
}
