package ehu.weka.Atal2;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

public class GetModel {

    /** Egin beharrekoa: Parametro optimoak emanda, eredu iragarlea eta bere kalitatearen estimazioa lortu
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static void main(String[] args) throws Exception {

        if (args.length != 4) {
            System.out.println("Programaren helburua:");
            System.out.println("\tModeloa sortu MultiLayerPerceptron erabiliz eta bere kalitatearen estimazioa lortu");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train.arff fitxategia");
            System.out.println("\t2- Bigarren parametro bezala eredu iragarlearen .model fitxategia gordetzeko path-a");
            System.out.println("\t3- Hirugarren parametro bezala kalitatearen estimazioa gordetzeko .txt fitxategiaren path-a.");
            System.out.println("\t4- Laugarren parametro bezala parametro egokiak gordetzen dituen .txt fitxategiaren path-a.");
            System.out.println("\nPost baldintza:");
            System.out.println("\t1- Hirugarren parametroan adierazitako helbidean sortutako .txt fitxategia gordeko da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Irteerako eredu iragalearen MultiLayerPerceptron.model fitxategiaren helbidea");
            System.out.println("\t3- Irteerako .txt fitxategiaren helbidea.");
            System.out.println("\t4- Parametro egokiak gordeta dituen .txt fitxategiaren path-a.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar GetModel.jar <train.arff> <MultiLayerPerceptron.model> <MPKalitateEstimazioa.txt> <optimalParameters.txt>");
            System.exit(0);
        }

        //1. Argumentuak gorde
        Instances train = datuakKargatu(args[0]);
        String pathModel = args[1];
        String emaitzakPath = args[2];
        File parametroOptimoak = new File(args[3]);

        //2. Parametroak lortu fitxategitik
        String hiddenLayer = null;
        double lr = 0.0;
        try (BufferedReader br = new BufferedReader(new FileReader(parametroOptimoak))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Hidden Layer")){
                    hiddenLayer = line.split(":")[1];
                }else if (line.contains("Learning Rate")){
                    lr = Double.parseDouble((line.split(":")[1]));
                }
            }
        }

        //3. Modeloa sortu
        System.out.println("Modeloa sortzen...");
        System.out.println("\tHidden Layers:" + hiddenLayer);
        System.out.println("\tLearning Rate: " + lr);
        MultilayerPerceptron cls = new MultilayerPerceptron();
        cls.setHiddenLayers(hiddenLayer);
        cls.setLearningRate(lr);
        cls.buildClassifier(train);

        //4. Modeloa gorde
        SerializationHelper.write(pathModel,cls);

        //5. Ebaluatu eta estimazioa fitxategian gorde
        System.out.println("Ebaluazioa egiten...");
        File emaitzak = new File(emaitzakPath);
        FileWriter fw = new FileWriter(emaitzak);
        fw.write("/////////////////////////////KALITATEAREN ESTIMAZIOA////////////////////////////////\n\n\n");

        //EZ-ZINTZOA
        fw.write("----------------------EZ ZINTZOA----------------------\n\n");
        System.out.println("\tEbaluazio EZ-ZINTZOA hasten...");
        Evaluation evalEZintzoa = new Evaluation(train);
        evalEZintzoa.evaluateModel(cls, train);
        fw.write("\n"+evalEZintzoa.toClassDetailsString()+"\n");
        fw.write("\n"+evalEZintzoa.toSummaryString()+"\n");
        fw.write("\n"+evalEZintzoa.toMatrixString()+"\n");
        System.out.println("\tEbaluazio EZ-ZINTZOA eginda...");

        //CROSS VALIDATION
        fw.write("----------------------CROSS VALIDATION----------------------\n\n");
        System.out.println("\t10 FOLD CROSS VALIDATION ebaluazioa hasten...");
        Evaluation eval10fCV = new Evaluation(train);
        eval10fCV.crossValidateModel(cls, train, 10, new Random(1));
        fw.write("\n"+eval10fCV.toClassDetailsString()+"\n");
        fw.write("\n"+eval10fCV.toSummaryString()+"\n");
        fw.write("\n"+eval10fCV.toMatrixString()+"\n");
        System.out.println("\t10 FOLD CROSS VALIDATION ebaluazioa eginda");

        //HOLD-OUT 100 ALDIZ
        fw.write("----------------------HOLD-OUT 100 ALDIZ----------------------\n\n");
        System.out.println("\t100 HOLD-OUT ebaluazioa hasten...");
        Evaluation evalHoldOut = new Evaluation(train);
        for(int i=0; i<100; i++) {
            //10 iterazio behin printeatu
            if ((i+1)%10==0){
                System.out.println("\t" + (i+1) + "/100 iterazioa");
            }
            //Randomize
            Instances randomData = randomize(train, i);

            //Split Data
            Instances testh = splitData(randomData,70,false);
            Instances trainh = splitData(randomData,70,true);

            //Ebaluatu
            evalHoldOut.evaluateModel(cls, testh);
        }

        fw.write("\n"+evalHoldOut.toClassDetailsString()+"\n");
        fw.write("\n"+evalHoldOut.toSummaryString()+"\n");
        fw.write("\n"+evalHoldOut.toMatrixString()+"\n");
        System.out.println("\t100 HOLD-OUT ebaluazioa eginda...");
        fw.close();
    }

    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes()-1);
        return data;
    }

    private static Instances randomize(Instances data, int i) throws Exception {
        Randomize filter = new Randomize();
        filter.setInputFormat(data);
        filter.setRandomSeed(i);
        Instances randomData = Filter.useFilter(data,filter);
        randomData.setClassIndex(randomData.numAttributes()-1);
        return randomData;
    }

    private static Instances splitData(Instances data, double percent, boolean invert) throws Exception {
        RemovePercentage filterRemove = new RemovePercentage();
        filterRemove.setInputFormat(data);
        filterRemove.setPercentage(percent);
        filterRemove.setInvertSelection(invert);
        Instances split = Filter.useFilter(data,filterRemove);
        split.setClassIndex(split.numAttributes()-1);
        return split;
    }

}
