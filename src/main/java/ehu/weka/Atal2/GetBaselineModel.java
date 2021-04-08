package ehu.weka.Atal2;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;

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
            System.out.println("\tjava -jar GetBaselineModeal.jar <train.arff> <baseline.model> <kalitaterenEstimazioa.txt> ");
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
