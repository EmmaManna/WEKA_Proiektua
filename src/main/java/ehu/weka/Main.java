package ehu.weka;

import weka.core.Instances;

public class Main {
    public static void main(String[] args) throws Exception {
        //Datuak Kargatu
        if(args.length!=3){
            System.out.println("Atazak:");
            System.out.println("\t 1. Datu sortako string atributuak bektorizatu ");

            System.out.println("\nArgumentuak:");
            System.out.println("\t 1. Datu sortaren kokapena (path) .arff  formatuan (input).");
            System.out.println("\t 2. Bektorizatutako atributuak dituzten datuak gordetzeko path");
            System.out.println("\t 3. Errepresentazio dispertsoa dituzten datuak gordetzeko path");
            System.exit(0);
        }

        Proiektua p = Proiektua.getInstance();

        //Datuak kargatu
        Instances data = p.datuakKargatu(args[0]);

        //StringToWordVector
        Instances vectorData = p.stringToVector(data);

        //Atributu hoberenak lortu
        vectorData = p.selection(vectorData);
        p.datuakGorde(args[1],vectorData);

        //NonSparse
        Instances nonSparseData = p.nonSparse(vectorData);
        p.datuakGorde(args[2],nonSparseData);


    }
}
