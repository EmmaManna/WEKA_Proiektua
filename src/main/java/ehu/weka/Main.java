package ehu.weka;

import weka.core.Instances;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        //Datuak Kargatu
        if(args.length!=6){
            System.out.println("Atazak:");
            System.out.println("\t 1. Datu sortako string atributuak bektorizatu ");

            System.out.println("\nArgumentuak:");
            System.out.println("\t 1. Datu sortaren kokapena (path) .arff  formatuan (input).");
            System.out.println("\t 2. Bektorizatutako atributuak dituzten datuak gordetzeko path");
            System.out.println("\t 3. Errepresentazio dispertsoa dituzten datuak gordetzeko path");
            System.out.println("\t 4. Test sortaren kokapena (path) .arff  formatuan (input).");
            System.out.println("\t 5. Hiztegia gordetzeko path");
            System.out.println("\t 6. Bektorizatutako atributuak dituzten datuak gordetzeko path");
            System.exit(0);
        }

        Proiektua p = Proiektua.getInstance();

        //Datuak kargatu(train)
        Instances data = p.datuakKargatu(args[0]);

        //StringToWordVector
        Instances vectorData = p.stringToVector(data);

        //Atributu hoberenak lortu
        vectorData = p.selection(vectorData);
        p.datuakGorde(args[1],vectorData);

        //NonSparse
        Instances nonSparseData = p.nonSparse(vectorData);
        p.datuakGorde(args[2],nonSparseData);

        //Datuak kargatu(test)
        Instances test = p.datuakKargatu(args[3]);

        //train.arff-ren hiztegia lortu
        p.hiztegiaGorde(vectorData,args[4]);

        //FixedDictoniaryStringToWordVector
        File file = new File("Dictionary.txt");
        Instances vectorTest = p.fixedDictionaryStringToVector(file,test);
        p.datuakGorde(args[5],vectorTest);
    }
}
