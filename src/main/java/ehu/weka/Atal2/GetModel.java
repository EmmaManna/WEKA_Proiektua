package ehu.weka.Atal2;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GetModel {

    /** Egin beharrekoa: Parametro optimoak emanda, eredu iragarlea eta bere kalitatearen estimazioa lortu
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static void main(String[] args) throws Exception {

        if (args.length != 5) {
            System.out.println("Programaren helburua:");
            System.out.println("\tModeloa sortu MultiLayerPerceptron erabiliz eta bere kalitatearen estimazioa lortu");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train.arff fitxategia");
            System.out.println("\t2- Bigarren parametro bezala eredu iragarlearen .model fitxategia");
            System.out.println("\t3- Hirugarren parametro bezala kalitatearen estimazioa gordetzeko .txt fitxategiaren path-a.");
            System.out.println("\t4- Laugarren parametro bezala parametro egokiak gordetzen dituen .txt fitxategiaren path-a.");
            System.out.println("\t5- Bostgarren parametro bezala test.arff fitxategiaren path-a.");
            System.out.println("\nPost baldintza:");
            System.out.println("\t1- Hirugarren parametroan adierazitako helbidean sortutako .txt fitxategia gordeko da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Sarrerako eredu iragalearen MultiLayerPerceptron.model fitxategiaren helbidea");
            System.out.println("\t3- Irteerako .txt fitxategiaren helbidea.");
            System.out.println("\t4- Parametro egokiak gordeta dituen .txt fitxategiaren path-a.");
            System.out.println("\t5- Sarrerako test.arff fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar GetModel.jar <train.arff> <MultiLayerPerceptron.model> <MPKalitateEstimazioa.txt> ");
            System.exit(0);
        }

        //Datuak kargatu
        Instances train = datuakKargatu(args[0]);
        Instances test = datuakKargatu(args[4]);
        File file = new File(args[3]);
        String hiddenLayer = null;
        double lr = 0.0;

        //parametroak lortu
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Hidden Layer")){
                    hiddenLayer = line.split(":")[1];
                }else if (line.contains("Learning Rate")){
                    lr = Double.parseDouble((line.split(":")[1]));
                }
            }
        }

        //sailkatzailea sortu
        MultilayerPerceptron cls = new MultilayerPerceptron();
        cls.setHiddenLayers(hiddenLayer);
        cls.setLearningRate(lr);
        cls.buildClassifier(train);

        //Ebaluazioa egin


    }

    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes()-1);
        return data;
    }

}
