package ehu.weka.Atal1;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;
import weka.filters.unsupervised.instance.SparseToNonSparse;

import java.io.*;
import java.util.HashMap;

public class MakeCompatible {
    /** Egin beharrekoa: MakeCompatible ebaluazio multzo bat (e.g. dev.arff), entrenamendu multzoak
     *					 ezarritako espazioan errepresentatzeko errutina da (BoW nahiz TF·IDF eta Sparse
     *					 nahiz NonSparse).
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static void main(String[] args) throws Exception {

        if (args.length != 5) {
            System.out.println("Programaren helburua:");
            System.out.println("\tParametro bezala ematen den .arff fitxategia egokitu eta atributu espazioa errepresentazio bektorial batera aldatu (BoW edo TF·IDF).");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train.arff fitxategia.");
            System.out.println("\t2- Bigarren parametro bezala test.arff fitxategia.");
            System.out.println("\t3- Zein errepresentzaio bektorial nahi den BoW edo TF·IDF.");
            System.out.println("\t4- Laugarren parametro bezala Sparse edo NonSparse emaitza fitxategi bezala nahi dugun.");
            System.out.println("\t5- Sortutako .arff fitxategia gordetzeko path-a.");
            System.out.println("\nPost baldintza:");
            System.out.println("\t1- Bostgarren parametroan adierazitako helbidean sortutako .arff fitxategia gordeko da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako train.arff fitxategiaren helbidea.");
            System.out.println("\t2- Sarrerako test.arff fitxategiaren helbidea.");
            System.out.println("\t3- Parametroa - BoW = 0 | TF·IDF = 1");
            System.out.println("\t4- Parametroa - Sparse = yes | NonSparse = no");
            System.out.println("\t5- Irteerako .arff fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar MakeCompatible.jar <train.arff> <test.arff> 1 yes <outputPath> ");
            System.exit(0);
        }

        //Datuak kargatu: train eta test
        Instances train = datuakKargatu(args[0]);
        Instances test = datuakKargatu(args[1]);
        //Testaren headers-ak moldatu
        test = stringToNominal(test);

        //Hash-a sortu
        HashMap<String, Integer> hiztegia = hashSortu(train);
        //Hash-a eguneratu
        hiztegia = hashEguneratu("DictionaryRaw.txt",hiztegia);
        //Hash-a fitxategi batean gorde
        dictionaryGorde("Dictionary.txt", hiztegia);

        //FixedDictoniaryStringToWordVector
        File file = new File("Dictionary.txt");

        String vector = args[2];
        String emaitza = args[3];
        Instances vectorTest = null;

        //BOW edo TF·IDF
        if(vector.equals("0")){ //BoW
            //StringToWordVector - BoW
            vectorTest = fixedDictionaryStringToVector(file,test,false);
        }
        else if(vector.equals("1")){ //TF·IDF
            //StringToWordVector - TF·IDF
            vectorTest = fixedDictionaryStringToVector(file,test,true);
        }
        else{
            System.out.println("Errorea: Hirugarren parametroa ez da zuzena");
            System.exit(0);
        }

        if(emaitza.equals("no")){ //NonSparse
            vectorTest = nonSparse(vectorTest);
        }
        else if(!emaitza.equals("yes")){
            System.out.println("Errorea: Laugarren parametroa ez da zuzena");
            System.exit(0);
        }
        //Klasea azken atributuan jarri
        vectorTest = reorder(vectorTest);
        datuakGorde(args[4],vectorTest);


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
        return test;
    }

    private static Instances reorder(Instances test) throws Exception {
        Reorder filterR = new Reorder();
        filterR.setAttributeIndices("2-"+ test.numAttributes()+",1"); //2-atributu kop, 1.  2-atributu kop bitarteko atributuak goian jarriko dira eta 1 atributua (klasea dena) amaieran.
        filterR.setInputFormat(test);
        test = Filter.useFilter(test,filterR);
        return test;
    }


    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(0);
        return data;
    }


    public static HashMap<String,Integer> hashSortu(Instances data) throws IOException {
        HashMap<String, Integer> hiztegia = new HashMap();
        for(int i=0;i<data.numAttributes()-1;i++) {
            Attribute attrib = data.attribute(i);
            hiztegia.put(attrib.name(),1);
        }
        return hiztegia;
    }


    public static HashMap<String,Integer> hashEguneratu(String path, HashMap<String, Integer> hiztegia) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));
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

    public static void dictionaryGorde(String path, HashMap<String,Integer> hiztegia) throws IOException {
        FileWriter fw = new FileWriter(path);
        hiztegia.forEach((k,v) -> {
            try {
                fw.write(k+","+v+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fw.close();
    }

    public static Instances fixedDictionaryStringToVector(File dictionary, Instances test,  boolean bool) throws Exception {
        FixedDictionaryStringToWordVector filterFixedDictionary = new FixedDictionaryStringToWordVector();
        filterFixedDictionary.setDictionaryFile(dictionary);
        filterFixedDictionary.setTFTransform(bool); //bool --> TRUE - Term Frequency kontuan hartu nahi dugu. Term Frequency (TF) dj dokumentu bateko wi hitzaren maiztasun erlatiboa (1) adierazpenean agertzen den bezala definitzen da.
        filterFixedDictionary.setIDFTransform(bool); //bool --> TRUE - TFIDF kontuan hartu nahi dugu. Sets whether if the word frequencies in a document should be transformed into: fij*log(num of Docs/num of Docs with word i) where fij is the frequency of word i in document(instance) j.
        filterFixedDictionary.setOutputWordCounts(bool); //bool --> FALSE - Hitzen agerpena bakarrik kontuan dugu {0,1}, ez maiztasuna || bool --> TRUE - Maiztasunak kontuan hartzen dugu
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

    public static void datuakGorde(String path, Instances data) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(path));
        saver.writeBatch();
    }



}
