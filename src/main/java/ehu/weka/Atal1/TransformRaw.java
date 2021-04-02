package ehu.weka.Atal1;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

import java.io.File;
import java.io.IOException;

public class TransformRaw {
    /** Egin beharrekoa: TransformRaw atributu espazioa erreprezentazio bektorial batera transformatzeko
     *					 softwarea. Aukera ezberdinak kontsideratzeko malgutasuna izatea probesten da
     *					 (adb BoW ala TF·IDF errepresentazioa). Halaber, Sparse ala NonSparse errepre
     *					 sentazioak ahalbidetzea malgutasuna emango lioke paketeari.
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static void main(String[] args) throws Exception {
        if(args.length!=4) {
            System.out.println("Programaren helburua:");
            System.out.println("\tParametro bezala ematen den .arff fitxategiaren atributu espazioa errepresentazio bektorial batera aldatu (BoW edo TF·IDF).");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala .arff fitxategia.");
            System.out.println("\t2- Zein errepresentzaio bektorial nahi den BoW edo TF·IDF.");
            System.out.println("\t3- Hirugarren parametro bezala Sparse edo NonSparse emaitza fitxategi bezala nahi dugun.");
            System.out.println("\t4- Sortutako .arff fitxategiaren path-a.");
            System.out.println("\nPost baldintza:");
            System.out.println("\t1- Laugarren parametroan adierazitako helbidean sortutako arff fitxategia gordeko da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Sarrerako .arff fitxategiaren helbidea.");
            System.out.println("\t2- Parametroa - BoW = 0 | TF·IDF = 1");
            System.out.println("\t3- Parametroa - Sparse = yes | NonSparse = no");
            System.out.println("\t4- Irteerako .arff fitxategiaren helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar TransformRaw.jar <train.arff> 1 yes <outputPath>");
            System.exit(0);
        }
        else{
            String inputPath = args[0];
            String vector = args[1];
            String emaitza = args[2];
            String outPath = args[3];

            //Datuak kargatu(train)
            Instances data = datuakKargatu(inputPath);

            //BOW edo TF·IDF
            if(vector.equals("0")){ //BoW
                //StringToWordVector - BoW
                data = stringToWordVector(data,false);
                //AttributeSelection
                data = selection(data);
            }
            else if(vector.equals("1")){ //TF·IDF
                //StringToWordVector - TF·IDF
                data = stringToWordVector(data,true);
                //AttributeSelection
                data = selection(data);
            }
            else{
                System.out.println("Errorea: Bigarren parametroa ez da zuzena");
                System.exit(0);
            }

            //Sparse edo NonSparse - 'dispertsio' edo 'ez-dispertsio'
            if(emaitza.equals("yes")){ //Sparse
                //Defektuz Sparse da orduan zuzenean gordetzen dugu
                datuakGorde(outPath,data);
            }
            else if(emaitza.equals("no")){ //NonSparse
                Instances nonSparseData = nonSparse(data);
                datuakGorde(outPath,nonSparseData);
            }
            else{
                System.out.println("Errorea: Hirugarren parametroa ez da zuzena");
                System.exit(0);
            }

        }

    }
    public static Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(0);
        return data;
    }

    public static Instances stringToWordVector(Instances data, boolean bool) throws Exception {
        StringToWordVector filterVector = new StringToWordVector(); //Hitzen agerpena adieraziko du 0 --> Ez agertu eta 1 --> Agertu
        filterVector.setWordsToKeep(3000);
        filterVector.setLowerCaseTokens(true); //Letra larria nahiz xehea baliokidetu
        filterVector.setTFTransform(bool); //bool --> TRUE - Term Frequency kontuan hartu nahi dugu. Term Frequency (TF) dj dokumentu bateko wi hitzaren maiztasun erlatiboa (1) adierazpenean agertzen den bezala definitzen da.
        filterVector.setIDFTransform(bool); //bool --> TRUE - TFIDF kontuan hartu nahi dugu. Sets whether if the word frequencies in a document should be transformed into: fij*log(num of Docs/num of Docs with word i) where fij is the frequency of word i in document(instance) j.
        filterVector.setOutputWordCounts(bool); //bool --> FALSE - Hitzen agerpena bakarrik kontuan dugu {0,1}, ez maiztasuna || bool --> TRUE - Maiztasunak kontuan hartzen dugu
        File f = new File("DictionaryRaw.txt");
        filterVector.setDictionaryFileToSaveTo(f); //Lortutako atributu (hitzak) lista gorde hitz bakiotzaren maiztasunekin
        filterVector.setInputFormat(data);
        Instances vectorData = Filter.useFilter(data,filterVector);


        System.out.println(f.getAbsolutePath());


        return vectorData;
    }

    public static Instances selection(Instances data) throws Exception {
        AttributeSelection filterSelection = new AttributeSelection();
        filterSelection.setInputFormat(data);
        Instances selection = Filter.useFilter(data,filterSelection);
        return selection;
    }

    public static void datuakGorde(String path, Instances data) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(path));
        saver.writeBatch();
    }

    public static Instances nonSparse(Instances data)throws Exception {
        SparseToNonSparse filterNonSparse = new SparseToNonSparse();
        filterNonSparse.setInputFormat(data);
        Instances nonSparseData = Filter.useFilter(data,filterNonSparse);
        return nonSparseData;
    }
}
