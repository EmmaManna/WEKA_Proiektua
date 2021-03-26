package ehu.weka;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;

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
}
