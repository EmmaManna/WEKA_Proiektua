package ehu.weka.Atal2;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class GetModel {

    /** Egin beharrekoa: Parametro optimoak emanda, eredu iragarlea eta bere kalitatearen estimazioa lortu
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.out.println("Programaren helburua:");
            System.out.println("\tModeloa sortu MultiLayerPerceptron erabiliz eta bere kalitatearen estimazioa lortu");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train.arff fitxategia");
            System.out.println("\t2- Bigarren parametro bezala eredu iragarlearen .model fitxategia");
            System.out.println("\t3- Hirugarren parametro bezala kalitatearen estimazioa gordetzeko .txt fitxategiaren path-a.");
            System.out.println("\nPost baldintza:");
            System.out.println("\t1- Hirugarren parametroan adierazitako helbidean sortutako .txt fitxategia gordeko da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Sarrerako eredu iragalearen MultiLayerPerceptron.model fitxategiaren helbidea");
            System.out.println("\t3- Irteerako .txt fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar GetModel.jar <train.arff> <MultiLayerPerceptron.model> <MPKalitateEstimazioa.txt> ");
            System.exit(0);
        }

        //Train kargatu
        Instances train = datuakKargatu(args[0]);
    }

    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes()-1);
        return data;
    }

}
