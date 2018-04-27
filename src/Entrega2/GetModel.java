package Entrega2;

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
        }

        getModel(Inst, AtrOpt, pathModel, pathWrite);
    }

    /**
     * Escribe los resultados de realizar con el algoritmo SVM empleando el 10-FoldCrossValidation y el Resubstitution error, así como el propio modelo
     *
     * @param trainedData instancias con las que evaluar
     * @param atributoExponente atributo optimo para generar el clasificador
     * @param pathToSaveModel ruta donde guardar el modelo generado
     * @param pathToWrite carpeta donde guardar los archivos generados
     */
    private static void getModel(Instances trainedData, double atributoExponente, String pathToSaveModel,String pathToWrite) throws Exception {
        Classifier ClassifierSMO = getSMO(atributoExponente);

        Evaluation evalKFold = CommonUtilities.evalKFoldCrossValidation(ClassifierSMO, trainedData, 10, 1);
        CommonUtilities.writeQuality(evalKFold, pathToWrite + trainedData.relationName() + "_SVM_k-fold_quality.txt");

        ClassifierSMO.buildClassifier(trainedData);
        Evaluation evalResubstitution = new Evaluation(trainedData);
        evalResubstitution.evaluateModel(ClassifierSMO, trainedData);
        CommonUtilities.writeQuality(evalResubstitution, pathToWrite + trainedData.relationName() + "_SVM_resubstitution_quality.txt");
        CommonUtilities.saveModel(ClassifierSMO, pathToSaveModel);

    }

    /**
     * Devuelve el clasificador del tipo SVM creado con los valores optimos
     *
     * @param atributoExponente atributo optimo para generar el clasificador
     * @return el objeto Clasificador
     */

    private static Classifier getSMO(double atributoExponente){
        Classifier model = new SMO();
        PolyKernel pk = new PolyKernel();
        pk.setExponent(atributoExponente);
        ((SMO) model).setKernel(pk);
        return model;
    }
}
