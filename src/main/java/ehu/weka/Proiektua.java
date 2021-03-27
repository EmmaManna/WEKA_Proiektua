package ehu.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

import java.io.File;
import java.io.IOException;

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
        Instances vectorData = Filter.useFilter(data,filterVector);
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
}
