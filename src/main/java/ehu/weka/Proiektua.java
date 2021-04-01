package ehu.weka;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

import java.io.*;
import java.util.HashMap;

public class Proiektua {
    private static final Proiektua instance = new Proiektua();

    public static Proiektua getInstance(){
        return instance;
    }

    private Proiektua(){

    }

    public Instances datuakKargatu(String path) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        data.setClassIndex(0);
        return data;
    }

    public void datuakGorde(String path, Instances data) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(path));
        saver.writeBatch();
    }

    public Instances stringToVector(Instances data) throws Exception {
        StringToWordVector filterVector = new StringToWordVector();
        filterVector.setWordsToKeep(3000);
        filterVector.setLowerCaseTokens(true);
        File f = new File("Dictionary2.txt");
        filterVector.setDictionaryFileToSaveTo(f);
        filterVector.setInputFormat(data);
        Instances vectorData = Filter.useFilter(data,filterVector);


        System.out.println(f.getAbsolutePath());


        return vectorData;
    }

    public Instances selection(Instances data) throws Exception {
        AttributeSelection filterSelection = new AttributeSelection();
        filterSelection.setInputFormat(data);
        Instances selection = Filter.useFilter(data,filterSelection);
        return selection;
    }

    public Instances nonSparse(Instances data)throws Exception {
        SparseToNonSparse filterNonSparse = new SparseToNonSparse();
        filterNonSparse.setInputFormat(data);
        Instances nonSparseData = Filter.useFilter(data,filterNonSparse);
        return nonSparseData;
    }

    public HashMap<String,Integer> hiztegiaGorde(Instances data) throws IOException {
        HashMap<String, Integer> hiztegia = new HashMap();
        for(int i=0;i<data.numAttributes()-1;i++) {
            Attribute attrib = data.attribute(i);
            hiztegia.put(attrib.name(),1);
        }
        return hiztegia;

    }

    public HashMap<String,Integer> hiztegiaEguneratu(String path, HashMap<String, Integer> hiztegia) {
        BufferedReader br = null;
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
        dictionaryEzabatu(path);
        return hiztegia;
    }

    private void dictionaryEzabatu(String path){
        File f = new File(path);
        if(f.exists()){
            f.delete();
        }
    }

    public void dictionaryGorde(String path, HashMap<String,Integer> hiztegia) throws IOException {
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

    public Instances fixedDictionaryStringToVector(File dictionary, Instances test) throws Exception {
        FixedDictionaryStringToWordVector filterFixedDictionary = new FixedDictionaryStringToWordVector();
        filterFixedDictionary.setDictionaryFile(dictionary);
        filterFixedDictionary.setInputFormat(test);
        Instances fixedTest = Filter.useFilter(test,filterFixedDictionary);
        return fixedTest;
    }


}
