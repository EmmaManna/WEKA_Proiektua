package ehu.weka.Atal2;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.FileWriter;
import java.util.Random;

public class GetBaselineModel {

    /** Egin beharrekoa: GetBaselineModel baseline sortu (logisticRegression.model) eta beronen kalitatearen
     *                   estimazioa gorde (kalitatea.txt).
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.out.println("Programaren helburua:");
            System.out.println("\tBaseline sortu Logistic Regression erabiliz, lor daitekeen kalitatearen behe bornea ezartzeko.");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train.arff fitxategia");
            System.out.println("\t2- Bigarren parametro bezala eredu iragarlearen .model fitxategia gordetzeko path-a.");
            System.out.println("\t3- Hirugarren parametro bezala kalitatearen estimazioa gordetzeko .txt fitxategiaren path-a.");
            System.out.println("\nPost baldintzak:");
            System.out.println("\t1- Bigarren parametroan adierazitako helbidean sortutako .model fitxategia gordeko da.");
            System.out.println("\t2- Hirugarren parametroan adierazitako helbidean sortutako .txt fitxategia gordeko da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Irteerako eredu iragalearen .model fitxategiaren helbidea");
            System.out.println("\t3- Irteerako .txt fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar GetBaselineModel.jar <train.arff> <baseline.model> <kalitaterenEstimazioa.txt> ");
            System.exit(0);
        }
            String inputPath = args[0];
            String model = args[1];
            String txt = args[2];

            //1. Train kargatu
            Instances train = datuakKargatu(inputPath);

            //2. Baseline modeloa sortu
            Logistic logistic = new Logistic();
            logistic.buildClassifier(train);

            //3. Modeloa gorde
            SerializationHelper.write(model,logistic);

            //4. Ebaluatu eta estimazioa fitxategian gorde
            FileWriter fw = new FileWriter(txt);
            fw.write("/////////////////////////////KALITATEAREN ESTIMAZIOA////////////////////////////////\n\n\n");

            //EZ-ZINTZOA
            fw.write("----------------------EZ ZINTZOA----------------------\n\n");
            Evaluation evalEZintzoa = new Evaluation(train);
            evalEZintzoa.evaluateModel(logistic, train);
            fw.write("\n"+evalEZintzoa.toClassDetailsString()+"\n");
            fw.write("\n"+evalEZintzoa.toSummaryString()+"\n");
            fw.write("\n"+evalEZintzoa.toMatrixString()+"\n");

            //CROSS VALIDATION
            fw.write("----------------------CROSS VALIDATION----------------------\n\n");
            Evaluation eval10fCV = new Evaluation(train);
            eval10fCV.crossValidateModel(logistic, train, 10, new Random(1));
            fw.write("\n"+eval10fCV.toClassDetailsString()+"\n");
            fw.write("\n"+eval10fCV.toSummaryString()+"\n");
            fw.write("\n"+eval10fCV.toMatrixString()+"\n");

            //HOLD-OUT 100 ALDIZ
            fw.write("----------------------HOLD-OUT 100 ALDIZ----------------------\n\n");
            Evaluation evalHoldOut = new Evaluation(train);

            for(int i=0; i<100; i++) {
                //Randomize
                Instances randomData = randomize(train, i);

                //Split Data
                Instances testh = splitData(randomData,70,false);
                Instances trainh = splitData(randomData,70,true);

                //Ebaluatu
                evalHoldOut.evaluateModel(logistic, testh);
            }
            fw.write("\n"+evalHoldOut.toClassDetailsString()+"\n");
            fw.write("\n"+evalHoldOut.toSummaryString()+"\n");
            fw.write("\n"+evalHoldOut.toMatrixString()+"\n");
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
