package ehu.weka.Atal2;


import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.AddValues;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.instance.SparseToNonSparse;

import java.io.*;
import java.util.HashMap;

public class FSS {

    /** Egin beharrekoa: FSS software paketean, alde batetik, entrenamendu multzoko atributuak hautatu eta beste
     *                   aldetik, ebaluazio multzoa entrenamenduarekiko bateragarria egiteko baliabideak emango dira.
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */


    public static void main(String[] args) throws Exception {

        if (args.length != 6) {
            System.out.println("Programaren helburua:");
            System.out.println("\tEntrenamendu multzoko atributu egokienak hautatu eta ebaluazio multzoa entrenamendu espaziora egokitu");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train.arff fitxategia");
            System.out.println("\t2- Bigarren parametro bezala test.arff fitxategia.");
            System.out.println("\t3- Zein errepresentzaio bektorial nahi den BoW edo TF·IDF.");
            System.out.println("\t4- Laugarren parametro bezala Sparse edo NonSparse emaitza fitxategi bezala nahi dugun.");
            System.out.println("\t5- Bostgarren parametro bezala train egokituta fitxategiaren path-a.");
            System.out.println("\t6- Seigarren parametro bezala test egokituta fitxategiaren path-a.");
            System.out.println("\nPost baldintzak:");
            System.out.println("\t1- Hirugarren eta laugarren parametroetan adierazitako helbidean sortutako .arff fitxategiak gordeko dira.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Sarrerako dev.arff fitxategiaren helbidea");
            System.out.println("\t3- Parametroa - BoW = 0 | TF·IDF = 1");
            System.out.println("\t4- Parametroa - Sparse = yes | NonSparse = no");
            System.out.println("\t5- Irteerako egokitutako train fitxategiaren helbidea.");
            System.out.println("\t6- Irteerako egokitutako dev fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar FSS.jar <train.arff> <test.arff> 0 yes <trainFSS.arf> <testFSS.arff> ");
            System.exit(0);
        }

        //1. Train eta test kargatu
        Instances train = datuakKargatu(args[0]);
        Instances test = datuakKargatu(args[1]);
        //1.1. Testaren headers-ak moldatu
        test = stringToNominal(test);

        //2. AttributeSelection aplikatu train multzoan eta gorde
        Instances trainFSS = attributeSelection(train);
        datuakGorde(args[4], trainFSS);

        //3. Hiztegi bateragarria lortu
        //3.1. Hash-a sortu
        HashMap<String, Integer> hiztegia = hashSortu("DictionaryRaw.txt",trainFSS);
        //3.2. Hiztegia gorde
        dictionaryGorde(hiztegia,"DictionaryFSS.txt",trainFSS);

        String vector = args[2];
        String emaitza = args[3];
        Instances vectorTest = null;

        //3. BOW edo TF·IDF
        if(vector.equals("0")){ //BoW
            //FixedDictionaryStringToWordVector - BoW
            vectorTest = fixedDictionaryStringToVector("DictionaryFSS.txt",test,false);
        }
        else if(vector.equals("1")){ //TF·IDF
            //FixedDictionaryStringToWordVector - TF·IDF
            vectorTest = fixedDictionaryStringToVector("DictionaryFSS.txt",test,true);
        }
        else{
            System.out.println("Errorea: Hirugarren parametroa ez da zuzena");
            System.exit(0);
        }

        //4. Sparse edo NonSparse
        if(emaitza.equals("no")){ //NonSparse
            vectorTest = nonSparse(vectorTest);
        }
        else if(!emaitza.equals("yes")){
            System.out.println("Errorea: Laugarren parametroa ez da zuzena");
            System.exit(0);
        }

        //5. Klasea azken atributuan jarri
        vectorTest = reorder(vectorTest);
        datuakGorde(args[5],vectorTest);
        System.out.println("FSS AMAITUTA");

    }

    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(0);
        return data;
    }


    public static Instances attributeSelection(Instances data) throws Exception {
        AttributeSelection filterSelect = new AttributeSelection();
        InfoGainAttributeEval evalInfoGain = new InfoGainAttributeEval();
        Ranker ranker = new Ranker();
        ranker.setOptions(new String[] { "-T", "0.001" });
        filterSelect.setInputFormat(data);
        filterSelect.setEvaluator(evalInfoGain);
        filterSelect.setSearch(ranker);
        Instances trainFSS = Filter.useFilter(data, filterSelect);
        return trainFSS;
    }

    public static void datuakGorde(String path, Instances data) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(path));
        saver.writeBatch();
    }

    public static HashMap<String,Integer> hashSortu(String pathRaw, Instances data) throws IOException {
        HashMap<String, Integer> hiztegia = new HashMap();

        for(int i=0;i<data.numAttributes()-1;i++) {
            Attribute attrib = data.attribute(i);
            hiztegia.put(attrib.name(),1);
        }

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(pathRaw));
            String contentLine = br.readLine();
            while (contentLine != null) {
                String[] lerroa = contentLine.split(",");
                String atributua = lerroa[0];
                Integer maiztasuna = Integer.parseInt(lerroa[1]);
                if(hiztegia.containsKey(atributua)){
                    hiztegia.put(atributua,maiztasuna);
                }
                contentLine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return hiztegia;
    }


    public static void dictionaryGorde(HashMap<String, Integer> hiztegia, String path, Instances data) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write("@@@numDocs="+data.numInstances()+"@@@\n"); //Beharrezkoa TF·IDF bihurketa egiteko

        for(int i=0; i<data.numAttributes()-1;i++){
            String atributua = data.attribute(i).name();
            if(hiztegia.containsKey(atributua)){
                fw.write(atributua+","+hiztegia.get(atributua)+"\n");
            }
        }
        fw.close();
    }

    private static Instances stringToNominal(Instances test) throws Exception {
        StringToNominal filterSTN = new StringToNominal();
        filterSTN.setInputFormat(test);
        String[] options = new String[2];
        options[0] = "-R";
        options[1] = "first";
        filterSTN.setOptions(options);
        test = Filter.useFilter(test, filterSTN);
        test = addValues(test);
        test.setClassIndex(0);
        return test;
    }

    private static Instances addValues(Instances test) throws Exception {
        AddValues filterAV =new AddValues();
        String[] options = new String[4];
        options[0] = "-C";
        options[1] = "first";
        options[2] = "-L";
        options[3] = "no,yes";
        filterAV.setOptions(options);
        filterAV.setInputFormat(test);
        test = Filter.useFilter(test, filterAV);
        test.setClassIndex(0);
        return test;
    }

    private static Instances reorder(Instances test) throws Exception {
        Reorder filterR = new Reorder();
        filterR.setAttributeIndices("2-"+ test.numAttributes()+",1"); //2-atributu kop, 1.  2-atributu kop bitarteko atributuak goian jarriko dira eta 1 atributua (klasea dena) amaieran.
        filterR.setInputFormat(test);
        test = Filter.useFilter(test,filterR);
        return test;
    }

    public static Instances fixedDictionaryStringToVector(String dictionary, Instances test,  boolean bool) throws Exception {
        FixedDictionaryStringToWordVector filterFixedDictionary = new FixedDictionaryStringToWordVector();

        String[] options;
        if(bool) { //TF·IDF
            options = new String[8];
            options[0] = "-I";
            options[1] = "-T";
            options[2] = "-R";
            options[3] = "first-last";
            options[4] = "-dictionary";
            options[5] = dictionary;
            options[6] = "-L";
            options[7] = "-C";
        }else{ //BoW
            options = new String[5];
            options[0] = "-R";
            options[1] = "first-last";
            options[2] = "-dictionary";
            options[3] = dictionary;
            options[4] = "-L";
        }
        filterFixedDictionary.setOptions(options);
        filterFixedDictionary.setInputFormat(test);
        Instances fixedTest = Filter.useFilter(test,filterFixedDictionary);
        return fixedTest;
    }

    public static Instances nonSparse(Instances data)throws Exception {
        SparseToNonSparse filterNonSparse = new SparseToNonSparse();
        filterNonSparse.setInputFormat(data);
        Instances nonSparseData = Filter.useFilter(data,filterNonSparse);
        return nonSparseData;
    }
}
