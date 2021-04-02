package ehu.weka.Atal1;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.util.ArrayList;

public class GetRaw {
    /** Egin beharrekoa: GetRaw dokumentu  sorta  arff  raw  formatora  bihurtzeko  ad-hoc  diseinatutako  softwarea.
     *					 Dokumentu bakoitza instantzia bat izango da.  Hau text deritzan string motako atribu-tu bakar
     *					 batekin karakterizatuko da eta class deritzan klase nominalarekin (e.g.  spam,NOspam).
     *	@author Xabi Dermit, Jon Gondra eta Emma Manna
     */

    public static void main(String[] args) throws Exception {
        if(args.length!=3) {
            System.out.println("Programaren helburua:");
            System.out.println("\tParametro bezala ematen den .csv fitxategia .arff formatura bihutu.");
            System.out.println("\nAurrebaldintzak:");
            System.out.println("\t1- Lehenengo parametro bezala train edo test multzoa den adieraziko da.");
            System.out.println("\t2- Bigarren parametro bezala existitzen den .csv fitxategiaren helbidea pasatzea.");
            System.out.println("\t3- Hirugarren parametro bezala .arff fitxategia gorde nahi den helbidea existitzea.");
            System.out.println("\t4- Datu sortaren atributuen ordena: (1)identifikatzailea, (2)Klasea, (3)Textua");
            System.out.println("\nPost baldintza:");
            System.out.println("\t1- Bigarren parametroan adierazitako helbidean sortutako .arff fitxategia gorde da.");
            System.out.println("\nArgumentuen zerrenda eta deskribapena:");
            System.out.println("\t1- Fitxategia train edo test den adierazi idatziz: {train,test} ");
            System.out.println("\t2- .csv fitxategiaren helbidea.");
            System.out.println("\t3- .arff fitxategia gordeko den helbidea.");
            System.out.println("\nErabilera adibidea komando-lerroan:");
            System.out.println("\tjava -jar getRawARFF.jar <train/test> <input.csv> <outputPath>");
            System.exit(0);
        }

        //1. Fitxategia moldatu: Karaktere bereziak edo arazoak eman ditzaketenak egokitu
        if(args[0].equals("train")){
            fitxategiaIrakurri(args[1]);
        }
        else if(args[0].equals("test")){
            fitxategiaIrakurriTest(args[1]);
        }
        else{
            System.out.println("Errorea: Lehenengo parametroa ez da zuzena");
            System.exit(0);
        }


        //2. CSV fitxategia kargatu
        CSVLoader loader = new CSVLoader();
        String path = args[1].substring(0,args[1].length()-4);
        path = path+"_Egokituta.csv";
        try {
            loader.setSource(new File(path));  //CSV fitxategia kargatu
        } catch (IOException e) {
            System.out.println("Errorea: Sarrerako .csv fitxategiaren helbidea ez da zuzena.");
        }

        Instances data = loader.getDataSet();


        //3. Atributuen egokitzapena
        //3.1 Lehenengo atributua, 'id', ez denez beharrezkoa, ezabatu egingo da Remove filtroa erabilita.
        Remove remove= new Remove();
        remove.setAttributeIndices("1");  //1. posizioko atributua ezabatu nahi da.
        remove.setInputFormat(data);
        remove.setInvertSelection(false); //Zehaztutako atributua ezabatu nahi da, eta besteak mantendu.
        data= Filter.useFilter(data, remove);

        //3.2. 'tweet' atributua Nominal bezala kargatzen denez, String motara aldatuko da NominalToString filtroa erabilita.
        NominalToString nominalToString = new NominalToString();
        nominalToString.setAttributeIndexes("2");  //2. posizioan dago 'tweet' atributua.
        nominalToString.setInputFormat(data);
        data = Filter.useFilter(data, nominalToString);

        data.setClassIndex(0);


        //4. Fitxategia ARFF formatuan gorde
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(args[2]));
        saver.writeBatch();
    }


    //Train fitxategia egokitzeko azpierrutina
    private static void fitxategiaIrakurri(String path) throws FileNotFoundException {
        FileReader fileR = new FileReader(path);
        ArrayList<String> fitxategia = new ArrayList<String>();

        try(BufferedReader bufferedReader = new BufferedReader(fileR)){
            String lerroa;

            while((lerroa=bufferedReader.readLine())!=null){ //Lerroz lerro irakurriko da.
                String[] zatiak = lerroa.split(","); //Lerro bakoitza ',' karakterearen arabera banatu
                String emaitza = "";

                for(int i=0; i<zatiak.length; i++){ //Zati bakoitzeko
                    String s = zatiak[i];
                    if(i==1){ // 'label' atribtutua bada zenbakizko balioa etiketa batekin ordezkatu
                        if(s.equals("0")){
                            s = "no";
                        }
                        else if(s.equals("1")){
                            s = "yes";
                        }
                        else if(s.equals("label")){
                            s = "klasea"; //Atributuaren izena aldatu behar da, bestela arazoak emango ditu gero.
                        }
                    }
                    else if(i==2){ //'tweet' atributua bada, komillak egokitu
                        if(s.contains("\"")){
                            s = s.replace("\"","\\\"");
                        }
                        s = "\""+s+"\"";
                    }
                    else if(i>2){ //twett-ean komak baleude, mezuaren hurrengo zatia gehitu
                        if(s.contains("\"")){
                            s = s.replace("\"","\\\"");
                        }
                        emaitza = emaitza.substring(0,emaitza.length()-2);
                        s = ", "+s+"\"";
                    }
                    emaitza = emaitza+s+",";
                }
                emaitza = emaitza.substring(0,emaitza.length()-1); //Azkenengo koma kendu
                emaitza= emaitza.replaceAll("[^\\x20-\\x7e]", ""); //Karaktere bereziak ezabatu
                fitxategia.add(emaitza);
            }
            fitxategiaIdatzi(path,fitxategia);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Test fitxategia egokitzeko azpierrutina
    private static void fitxategiaIrakurriTest(String path) throws FileNotFoundException {
        FileReader fileR = new FileReader(path);
        ArrayList<String> fitxategia = new ArrayList<String>();
        Boolean lehenengoa = true;

        try(BufferedReader bufferedReader = new BufferedReader(fileR)){
            String lerroa;

            while((lerroa=bufferedReader.readLine())!=null){ //Lerroz lerro irakurriko da.
                String[] zatiak = lerroa.split(","); //Lerro bakoitza ',' karakterearen arabera banatu
                String emaitza = "";

                for(int i=0; i<zatiak.length; i++){ //Zati bakoitzeko
                    String s = zatiak[i];
                    if(i==1){ //'tweet' atributua bada, komillak egokitu
                        if(s.contains("\"")){
                            s = s.replace("\"","\\\"");
                        }
                        if(lehenengoa){ //Burukoa bada, 'klasea' etiketa gehitu
                           lehenengoa = false;
                           s = "klasea,\""+s+"\"";
                        }
                        else{ //Instantzia bat bada, ? gehitu
                            s = "?,\""+s+"\"";
                        }
                    }
                    else if(i>1){ //twett-ean komak baleude, mezuaren hurrengo zatia gehitu
                        if(s.contains("\"")){
                            s = s.replace("\"","\\\"");
                        }
                        emaitza = emaitza.substring(0,emaitza.length()-2);
                        s = ", "+s+"\"";
                    }
                    emaitza = emaitza+s+",";
                }
                emaitza = emaitza.substring(0,emaitza.length()-1); //Azkenengo koma kendu
                emaitza= emaitza.replaceAll("[^\\x20-\\x7e]", ""); //Karaktere bereziak ezabatu
                fitxategia.add(emaitza);
            }
            fitxategiaIdatzi(path,fitxategia);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Egokitutako fitxategia zehaztutako path-ean gordetzeko azpierrutina
    private static void fitxategiaIdatzi(String path, ArrayList<String> fitxategia) throws FileNotFoundException {
        path = path.substring(0,path.length()-4);
        path = path+"_Egokituta.csv";
        try {
            FileWriter myWriter = new FileWriter(path);

            for(int i=0; i<fitxategia.size();i++){ //Lerroz lerro idatzi
                myWriter.write(fitxategia.get(i)+"\n");
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
