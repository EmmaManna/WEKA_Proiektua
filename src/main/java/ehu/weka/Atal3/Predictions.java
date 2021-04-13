package ehu.weka.Atal3;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;


import java.io.FileWriter;


public class Predictions {
    /** Egin beharrekoa: Predictions paketearen helburua dokumentu bat (ala sorta bat) sailkatzea da
     *                  emandako eredu iragarle baten bitartez (e.g. baseline.model)
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static Evaluation main(String[] args) throws Exception {

        if (args.length != 4) {
            System.out.println("Programaren helburua:");
            System.out.println("\tIragarpenak test multzoko instantzien klasea lortu erabilitako algoritmo guztien bidez.");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala test.arff fitxategia");
            System.out.println("\t1- Bigarren parametro bezala test.arff fitxategia");
            System.out.println("\t2- Hirugarren parametro bezala eredu iragarlearen .model fitxategia.");
            System.out.println("\t3- Laugarren parametro bezala iragarpenak gordetzeko .txt fitxategiaren path-a.");
            System.out.println("\nPost baldintza:");
            System.out.println("\t1- Hirugarren parametroan adierazitako helbidean sortutako .txt fitxategia gordeko da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako test.arff fitxategiaren helbidea.");
            System.out.println("\t2- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t3- Sarrerako eredu iragalearen .model fitxategiaren helbidea");
            System.out.println("\t4- Irteerako .txt fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar Predictions.jar <test.arff> <train.arff> <baseline.model> <TestPredictionsBaseline.txt> ");
            System.exit(0);
        }


        //1. Datuak kargatu: test.arff
        Instances test = datuakKargatu(args[0]);
        Instances train = datuakKargatu(args[1]);

        //2. Eredua kargatu: deserialize
        Classifier model = deserialize(args[2]);

        //2.1. Filtered Classifier aplikatu (ez da erabiltzen MultilayerPerceptron-ekin denbora gehiegi behar duelako)
        //FilteredClassifier fc = new FilteredClassifier();
        //fc.setClassifier(model);
        //fc.buildClassifier(train);

        //3. Ebaluatu eta iragarpenak egin
        Evaluation eval = ebaluatu(model,test);

        //4. Iragarpenak fitxategian idatzi
        FileWriter fw = new FileWriter(args[3]);
        fw.write("Exekuzio data: "+java.time.LocalDateTime.now().toString()+"\n");
        fw.write("\n-- Test Set -- \n");
        fw.write("Instantzia\tActual\tPredicted\n");

        for(int i=0; i< eval.predictions().size();i++){
            double actual = eval.predictions().get(i).actual();
            double predicted = eval.predictions().get(i).predicted();
            fw.write((i+1)+"\t"+test.instance(i).stringValue(test.classIndex())+"\t"+test.classAttribute().value((int)predicted)+"\n");
        }

        //5. Informazio gehiago gorde
        fw.write("\n"+eval.toClassDetailsString()+"\n");
        fw.write("\n"+eval.toSummaryString()+"\n");
        fw.write("\n"+eval.toMatrixString()+"\n");

        fw.close();
        return eval;
    }

    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes()-1);
        return data;
    }

    public static Classifier deserialize(String path) throws Exception {
        Classifier cls = (Classifier) weka.core.SerializationHelper.read(path);
        return cls;
    }

    public static Evaluation ebaluatu(Classifier model, Instances test) throws Exception {
        //Ebaluatu
        Evaluation eval = new Evaluation(test);
        eval.evaluateModel(model,test);
        return eval;
    }
}
