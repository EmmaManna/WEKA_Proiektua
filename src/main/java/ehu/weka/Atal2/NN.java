package ehu.weka.Atal2;

import ehu.weka.Proiektua;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class NN {

    // Sare neuronalaren klasea (MultiLayerPerceptron)

    private static Proiektua p = Proiektua.getInstance();

    public static void main(String[] args) throws Exception {
        sareaEntrenatu("/home/xabi/IdeaProjects/WEKA_Proiektua/src/main/java/ehu/weka/Resources/trainTDIFNonSparse.arff",
                "/home/xabi/IdeaProjects/WEKA_Proiektua/src/main/java/ehu/weka/Resources/test.arff",
                "ehu/weka/Resources/sailkatzailea.model","ehu/weka/Resources/parametroEkorketa.txt");
    }
    public static void sareaEntrenatu(String pDataPath, String pEvalDataPath, String pModeloaGordePath, String pParametroakGordePath) throws Exception {

        //entrenamendurako datuak kargatu
        Instances data = p.datuakKargatu(pDataPath);

        //hidden layers desberdinak
        ArrayList<String> hiddenLayers = new ArrayList<String>();
        hiddenLayers.add("100,50");
        hiddenLayers.add("50,25,12");
        hiddenLayers.add("12,6,2");
        hiddenLayers.add("25,12,4");
        hiddenLayers.add("25,25");
        hiddenLayers.add("50,25,12");
        hiddenLayers.add("100,52,25,10");
        hiddenLayers.add("80,40");
        hiddenLayers.add("150");
        hiddenLayers.add("150,100,50");
        hiddenLayers.add("80,40,20,10");
        hiddenLayers.add("157,75,25");
        hiddenLayers.add("50,100,50");
        hiddenLayers.add("25,50,100,50,25");
        hiddenLayers.add("25,75,25");
        hiddenLayers.add("100,100,100");
        hiddenLayers.add("75,50,25,12");
        hiddenLayers.add("20,40,20");
        hiddenLayers.add("15,15,15");
        hiddenLayers.add("35,70,35");
        hiddenLayers.add("12,25,12");
        hiddenLayers.add("30,60,90,60,30");
        hiddenLayers.add("40,20,10,5");
        hiddenLayers.add("60,40,20");
        hiddenLayers.add("150,125,100,75,50,25");
        hiddenLayers.add("125,75,25");
        hiddenLayers.add("75,125,75");
        hiddenLayers.add("30,45,30,15");
        hiddenLayers.add("80,60,40,20");
        hiddenLayers.add("25,50,75,25");
        hiddenLayers.add("200,100,50");
        hiddenLayers.add("300,200,100");
        hiddenLayers.add("200, 150, 100, 50, 25");
        hiddenLayers.add("120, 60");
        hiddenLayers.add("90, 45");
        hiddenLayers.add("150,75");
        hiddenLayers.add("110,55");

        //parametro optimoenak lortzeko
        String bestHiddenLayer = "";
        Double bestLR = 0.0;
        Double bestPCtCorrect = 0.0;

        //learning rate desberdinak
        for( double lr = 0.1; lr < 0.5; lr = lr + 0.05){
            for (String hiddenLayer : hiddenLayers){

                //Sailkatzailea sortu
                MultilayerPerceptron cls = new MultilayerPerceptron();

                //parametroak finkatu
                cls.setHiddenLayers(hiddenLayer);
                cls.setLearningRate(lr);
                cls.setTrainingTime(100);  //number of epochs

                //parametroak printeatu
                System.out.println("Learning Rate: " + lr);
                System.out.println("Hidden Layers: " + hiddenLayer + "\n\n");

                //instantziak nahastu
                Randomize filterRandomize = new Randomize();
                //filterRandomize.setRandomSeed(seed);
                filterRandomize.setInputFormat(data);
                data = Filter.useFilter(data, filterRandomize);


                //train multzoak lortu

                Instances train;
                RemovePercentage rmpct = new RemovePercentage();
                rmpct.setInvertSelection(false);
                rmpct.setPercentage(33);
                rmpct.setInputFormat(data);
                train = Filter.useFilter(data, rmpct);


                //test multzoa lortu
                Instances test;
                RemovePercentage rmpct2 = new RemovePercentage();
                rmpct2.setInvertSelection(true);
                rmpct2.setPercentage(33);
                rmpct2.setInputFormat(data);
                test = Filter.useFilter(data, rmpct2);

                //sailkatzailea entrenatu
                cls.buildClassifier(train);

                //ebaluazioa egin
                Evaluation eval = new Evaluation(train);
                eval.evaluateModel(cls, test);
                //System.out.println(eval.toSummaryString("\n=== Results ===\n",false));
                //System.out.println(eval.toMatrixString());

                double pctCorrect = eval.pctCorrect();
                System.out.println("Accuracy: " + pctCorrect);

                if (pctCorrect > bestPCtCorrect){
                    bestPCtCorrect = pctCorrect;
                    bestHiddenLayer = hiddenLayer;
                    bestLR = lr;
                }

                System.out.println("\n\n");

            }
        }

        File file = new File(pParametroakGordePath);
        FileWriter fw = new FileWriter(file);

        fw.write("Best Hidden Layer configuration: " + bestHiddenLayer);
        fw.write("Best Learning Rate configuration: " + bestLR);

        fw.flush();
        fw.close();

        //Sailkatzailea sortu
        MultilayerPerceptron sailkatzailea = new MultilayerPerceptron();
        sailkatzailea.setHiddenLayers(bestHiddenLayer);
        sailkatzailea.setLearningRate(bestLR);

        //sailkatzaile erreala sortu
        sailkatzailea.buildClassifier(data);
/*
        //ebaluaziorako datuak kargatu
        Instances evalData = p.datuakKargatu(pEvalDataPath);

        //ebaluazio finala egin
        Evaluation evaluazioFinala = new Evaluation(data);
        evaluazioFinala.evaluateModel(sailkatzailea, evalData);
        System.out.println(evaluazioFinala.toSummaryString("\n=== Results ===\n",false));
        System.out.println(evaluazioFinala.toMatrixString());
*/
        //sailkatzailea gorde
        weka.core.SerializationHelper.write(pModeloaGordePath,sailkatzailea);

    }


}
