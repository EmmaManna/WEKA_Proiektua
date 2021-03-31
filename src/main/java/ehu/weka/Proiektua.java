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
        data.setClassIndex(data.numAttributes()-1);
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
        filterVector.setInputFormat(data);
        filterVector.setWordsToKeep(Integer.MAX_VALUE);
        File f = new File("Dictionary.txt");
        filterVector.setDictionaryFileToSaveTo(f);
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

    public void hiztegiaGorde(Instances data, String path) throws IOException {
        FileWriter fw = new FileWriter(path);
        for(int i=0;i<data.numAttributes()-1;i++) {
            Attribute attrib = data.attribute(i);
            fw.write(attrib.name()+"\n");
        }
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
