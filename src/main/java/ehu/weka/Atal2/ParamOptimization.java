package ehu.weka.Atal2;

import ehu.weka.Proiektua;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ParamOptimization {

    /** Egin beharrekoa: ParamOptimization ereduaren parametro optimoak ekortu
    *	@author Xabi Dermit, Jon Gondra eta Emma Manna
    **/

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Programaren helburua:");
            System.out.println("\tEreduaren parametro optimoak ekortu");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala entrenamendurako erabiliko den train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Bigarren parametro bezala lortu diren parametro optimoak gordetzeko .txt fitxategiaren helbidea.");

            System.out.println("\nPost baldintza:");
            System.out.println("\tLortu diren parametro optimoak gordetzeko .txt fitxategiaren helbidea.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");

            System.out.println("\t1- Entrenamendurako .arff fitxategiaren helbidea.");
            System.out.println("\t2- Parametro optimoak gordetzeko .txt fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar ParamOptimization.jar <train.arff> <outPath> ");
            System.exit(0);
        }

        // --------------------- PARAMETRO EKORKETAREKIN HASI --------------------------------------

        //entrenamendurako datuak kargatu
        Instances data = datuakKargatu(args[0]);
        int klaseMinoritarioaIndex = Utils.maxIndex(data.attributeStats(data.classIndex()).nominalCounts);
        System.out.println("Klase minoritarioa: " + data.attribute(data.classIndex()).value(klaseMinoritarioaIndex));

        //hidden layers desberdinak
        ArrayList<String> hiddenLayers = new ArrayList<String>();
        hiddenLayers.add("50,25,12");
        hiddenLayers.add("100");
        hiddenLayers.add("150,100,50");
        hiddenLayers.add("90,45");

        //parametro optimoenak lortzeko
        String bestHiddenLayer = "";
        Double bestLR = 0.0;
        Double bestFmeasure = 0.0;

        //--------------------------- HOLD OUT ---------------------------------------------
        int iterazioKop = 1;
        //learning rate desberdinak
        for(double lr = 0.1; lr < 0.5; lr = lr + 0.1){
            for (String hiddenLayer : hiddenLayers){

                System.out.println("#############################################################################");
                System.out.println(iterazioKop + ". ITERAZIOA "+java.time.LocalDateTime.now().toString());
                //parametroak printeatu
                System.out.println("\tLearning Rate: " + lr);
                System.out.println("\tHidden Layers: " + hiddenLayer + "\n\n");

                //Sailkatzailea sortu
                MultilayerPerceptron cls = new MultilayerPerceptron();

                //parametroak finkatu
                cls.setHiddenLayers(hiddenLayer);
                cls.setLearningRate(lr);
                cls.setTrainingTime(250);  //number of epochs, 500 default

                //instantziak nahastu
                Randomize filterRandomize = new Randomize();
                filterRandomize.setRandomSeed(iterazioKop);
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
                System.out.println(eval.toSummaryString("\n=== Results ===\n",false));

                //parametro optimoak lortu
                double fMeasure = eval.fMeasure(klaseMinoritarioaIndex);
                System.out.println("\tF-measure klase minoritarioarekiko: " + fMeasure);

                if (fMeasure > bestFmeasure){
                    bestFmeasure = fMeasure;
                    bestHiddenLayer = hiddenLayer;
                    bestLR = lr;
                }

                System.out.println("\n\n");

                iterazioKop++;
            }
        }

        File file = new File(args[1]);
        FileWriter fw = new FileWriter(file);

        fw.write("Best Hidden Layer configuration: " + bestHiddenLayer+"\n");
        fw.write("Best Learning Rate configuration: " + bestLR);

        fw.flush();
        fw.close();

    }
    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes()-1);
        return data;
    }
}
