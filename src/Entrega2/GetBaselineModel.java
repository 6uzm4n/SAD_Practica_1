package Entrega2;

import java.io.*;
import java.util.Random;

import Utilities.CommonUtilities;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class GetBaselineModel {

    public static void main(String[] args) {
        String trainPath = null;
        String modelPath = null;
        try{
            if (args.length == 0){
                System.out.println("=====================================================================");
                System.out.println("Este programa tiene como función generar un modelo NaiveBayes óptimo dado un conjunto de instancias.");
                System.out.println("PRECONDICIÓN: El path donde se desea guardar el modelo debe ser un directorio.");
                System.out.println("POSCONDICIÓN: Se han creado tanto baseline.model como baseline_quality.txt");
                System.out.println("ARGUMENTOS:\n--Path del conjunto de datos de entrenamiento del modelo." +
                        "\n--Path donde se desea guardar tanto el modelo como su calidad estimada.");
                System.out.println("EJEMPLO DE USO: \"java -jar GetBaselineModel.jar /path/to/data.arff /path/to/destination/\"");
                System.out.println("=====================================================================");

                        System.exit(0);
            }else if (args.length == 2) {
                trainPath = args[0];
                modelPath = args[1];
            }else{
                System.out.println("Error en los argumentos.");
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Instances train = CommonUtilities.loadArff(trainPath, -1);
        Classifier optimalClassifier = getBaselineModel(train);
        CommonUtilities.saveModel(optimalClassifier, modelPath + "baseline.model");

        writeResults(optimalClassifier, train, modelPath);

    }

    /**
     * Este método obtiene un modelo NaiveBayes óptimo para clasificar las instancias que se pasan como parámetro.
     *
     * @param inputTrain    Instancias que se desea clasificar.
     * @return              Modelo óptimo.
     */
    private static NaiveBayes getBaselineModel(Instances inputTrain) {

        int minClassIndex = CommonUtilities.getMinorityClassIndex(inputTrain);

        boolean useKernelEstimator = false;
        boolean useSupervisedDiscretization = false;

        boolean optimalKernelEstimator = false;
        boolean optimalSupervisedDiscretization = false;
        Double fMeasureMax = 0.00;

        NaiveBayes naiveBayes = new NaiveBayes();

        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 3; j++) {
                try {
                    naiveBayes.setUseSupervisedDiscretization(useSupervisedDiscretization);
                    naiveBayes.setUseKernelEstimator(useKernelEstimator);

                    Evaluation ev = evalKFoldCrossValidation(naiveBayes, inputTrain, 8);
                    if (fMeasureMax < ev.fMeasure(minClassIndex)) {
                        optimalKernelEstimator = useKernelEstimator;
                        optimalSupervisedDiscretization = useSupervisedDiscretization;
                        fMeasureMax = ev.fMeasure(minClassIndex);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                useSupervisedDiscretization = true;
            }

            useKernelEstimator = true;
        }

        System.out.println("=================================================");
        System.out.println("CONFIGURACIÓN ÓPTIMA DE NAIVE BAYES:");
        System.out.println ("¿Utilizar kernelEstimator?             " + optimalKernelEstimator);
        System.out.println ("¿Utilizat supervisedDiscretization?    " + optimalSupervisedDiscretization);
        System.out.println("=================================================");

        naiveBayes.setUseKernelEstimator(optimalKernelEstimator);
        naiveBayes.setUseSupervisedDiscretization(optimalSupervisedDiscretization);

        return naiveBayes;
    }

    /**
     * Este método escribe en un archivo la calidad estimada de un modelo dado, siendo calculada por 8-fold cross-validation.
     *
     * @param classifier    Modelo clasificador a usar.
     * @param instances     Conjunto de datos de test.
     * @param pathOut       Ruta al fichero que se desea crear/sobreescribir.
     */
    private static void writeResults(Classifier classifier, Instances instances, String pathOut) {
        try {
            Evaluation eval = evalKFoldCrossValidation(classifier, instances, 8);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(pathOut + "baseline_quality.model"))));
            writer.println("==========================================================================================================");
            writer.println("CALIDAD ESTIMADA DE BASELINE:");
            writer.println("Se ha usado 8-fold cross-validation sobre el modelo baseline NaiveBayes para obtener la calidad estimada.");
            writer.println("==========================================================================================================");
            writer.println(eval.toSummaryString());
            writer.println(eval.toMatrixString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método realiza una evaluación k-fold cross-validation según los parámetros de entrada.
     * @param pClassifier   Modelo clasificador a usar.
     * @param pData         Conjunto de datos de test.
     * @param pK            Número de pliegues a realizar.
     * @return              Evaluación completada.
     * @throws Exception    Posibles errores en ejecución.
     */
    private static Evaluation evalKFoldCrossValidation(Classifier pClassifier, Instances pData, int pK) throws Exception {
        Evaluation evaluator = new Evaluation(pData);
        evaluator.crossValidateModel(pClassifier, pData, pK, new Random(1));
        return evaluator;
    }
}