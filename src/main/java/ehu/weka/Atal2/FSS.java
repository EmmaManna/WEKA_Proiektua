package ehu.weka.Atal2;


import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;

import java.io.File;
import java.io.IOException;

public class FSS {

    /** Egin beharrekoa: FSS software paketean, alde batetik, entrenamendu multzoko atributuak hautatu eta beste
     *                   aldetik, ebaluazio multzoa entrenamenduarekiko bateragarria egiteko baliabideak emango dira.
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */


    public static void main(String[] args) throws Exception {

        if (args.length != 4) {
            System.out.println("Programaren helburua:");
            System.out.println("\tEntrenamendu multzoko atributu egokienak hautatu eta ebaluazio multzoa entrenamendu espaziora egokitu");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train.arff fitxategia");
            System.out.println("\t2- Bigarren parametro bezala dev.arff fitxategia.");
            System.out.println("\t3- Hirugarren parametro bezala train egokituta fitxategiaren path-a.");
            System.out.println("\t4- Laugarren parametro bezala dev egokituta fitxategiaren path-a.");
            System.out.println("\nPost baldintzak:");
            System.out.println("\t1- Hirugarren eta laugarren parametroetan adierazitako helbidean sortutako .arff fitxategiak gordeko dira.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Sarrerako dev.arff fitxategiaren helbidea");
            System.out.println("\t3- Irteerako egokitutako train fitxategiaren helbidea.");
            System.out.println("\t3- Irteerako egokitutako dev fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar FSS.jar <train.arff> <dev.arff> <trainFSS.arf> <devFSS.arff> ");
            System.exit(0);
        }

        //Train eta dev kargatu
        Instances train = datuakKargatu(args[0]);
        Instances dev = datuakKargatu(args[1]);



    }

    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes()-1);
        return data;
    }

    public static void datuakGorde(String path, Instances data) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(path));
        saver.writeBatch();
    }
}
