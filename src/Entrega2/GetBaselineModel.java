package Entrega2;

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
                System.out.println("INFO");
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

        Instances train = CommonUtilities.loadInstances(trainPath, -1);
        Classifier optimalClassifier = getBaselineModel(train);
        CommonUtilities.saveModel(optimalClassifier, modelPath + "/NaiveBayes.model");
    }

    private static NaiveBayes getBaselineModel(Instances inputTrain) {

        int clasMinId = getMinClass(inputTrain);

        boolean kernelEstimator = false;
        boolean supervisedDiscretization = false;

        boolean kernelEstimatorMax = false;
        boolean supervisedDiscretizationMax = false;
        Double fMeasureMax = 0.00;


        NaiveBayes nb = new NaiveBayes();
        nb.setUseKernelEstimator(kernelEstimator);


        for (int i = 1; i < 3; i++) {
            nb.setUseSupervisedDiscretization(false);
            supervisedDiscretization = false;
            for (int j = 1; j < 3; j++) {
                try {
                    Evaluation ev = evalKFoldCrossValidation(nb, inputTrain, 10);
                    if (fMeasureMax < ev.fMeasure(clasMinId)) {
                        kernelEstimatorMax = kernelEstimator;
                        supervisedDiscretizationMax = supervisedDiscretization;
                        fMeasureMax = ev.fMeasure(clasMinId);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                nb.setUseSupervisedDiscretization(true);
                supervisedDiscretization = true;
            }

            nb.setUseKernelEstimator(true);
            kernelEstimator = true;
        }

        return nb;
    }

    private static Evaluation evalKFoldCrossValidation(Classifier pClassifier, Instances pData, int pK) throws Exception {
        Evaluation evaluator = new Evaluation(pData);
        evaluator.crossValidateModel(pClassifier, pData, pK, new Random(1));
        return evaluator;
    }

    private static int getMinClass(Instances data) {
        int[] classCounts = data.attributeStats(data.classIndex()).nominalCounts;
        int classminid = 0;
        int classmin = classCounts[0];
        for (int i = 0; i < classCounts.length; i++) {
            if (classCounts[i] < classmin) {
                classmin = classCounts[i];
                classminid = i;
            }
        }

        return classminid;
    }
}