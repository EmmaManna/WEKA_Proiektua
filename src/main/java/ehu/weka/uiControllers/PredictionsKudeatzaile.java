package ehu.weka.uiControllers;

import ehu.weka.Atal1.GetRaw;
import ehu.weka.Atal2.FSS;
import ehu.weka.Atal3.Predictions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import weka.classifiers.Evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PredictionsKudeatzaile implements Initializable {
    private PredictionsApplication mainApp;
    private String testFile;
    private String modelFile;
    private String precitionsFile;
    private String trainFSS;

    public void setMain(PredictionsApplication main) {
        this.mainApp = main;
    }


    @FXML
    private TextArea txtA_tweet;

    @FXML
    private Button btn_arff;

    @FXML
    private Text txt_result;

    @FXML
    private Button btn_start;

    @FXML
    private Button btn_model;

    @FXML
    private Button btn_output;

    @FXML
    private Label lbl_test;

    @FXML
    private Label lbl_model;

    @FXML
    private Label lbl_output;

    @FXML
    private RadioButton rdbtn_BoW;

    @FXML
    private RadioButton rdbtn_sparse;

    @FXML
    private Button btn_TrainFSS;

    @FXML
    private Label lbl_trainFSS;


    @FXML
    void onClick(ActionEvent event) throws Exception {
        if(event.getSource()==btn_start){ //Hasteko botoia sakatu
            if(precitionsFile!=null && modelFile!=null){ //Derrigorrezko bi fitxategiak kargatuta (predictions.txt eta model)
                if(testFile!=null && trainFSS!=null){ //Test fitxategia kargatu bada
                    String[] arguments = new String[] {testFile,modelFile,precitionsFile};
                    Predictions.main(arguments);
                    txt_result.setText("Iragarpenak fitxategi honetan gorde dira: "+precitionsFile);
                }
                else if(txtA_tweet.getText()!=null && trainFSS!=null){ //Tweet bat sartu bada eta beste train fitxategia sartu bada
                    String data = String.valueOf(System.currentTimeMillis()); //Unea lortu, izenak ezartzeko
                    String path = precitionsFile.substring(0,precitionsFile.length()-lbl_output.getText().length());
                    testSortu(data,path); //Instantzia bakarreko .csv sortu

                    //Test fitxategia formateatu
                    String[] arguments1 = new String[] {"test",path+"\\testInstantzia"+data+".csv",path+"\\testInstantzia"+data+".arff"};
                    GetRaw.main(arguments1);

                    //BoW edo TF·IDF
                    String bow = "1";
                    if(rdbtn_BoW.isSelected()){
                        bow = "0";
                    }

                    //Sparse edo NonSparse
                    String sparse = "no";
                    if(rdbtn_sparse.isSelected()){
                        sparse = "yes";
                    }

                    //FSS aplikatu infoGain egiteko
                    String[] arguments2 = new String[] {trainFSS,path+"\\testInstantzia"+data+".arff",bow,sparse,path+"\\trainInstaintzia"+bow+sparse+data+"_InfoGain.arff",path+"\\testInstaintzia"+bow+sparse+data+"_InfoGain.arff"};
                    FSS.main(arguments2);

                    //Iragarpenak lortu
                    String[] arguments = new String[] {path+"\\testInstaintzia"+bow+sparse+data+"_InfoGain.arff",modelFile,precitionsFile};
                    Evaluation eval = Predictions.main(arguments);


                    double predicted = eval.predictions().get(0).predicted();
                    switch ((int)predicted){
                        case 0:
                            txt_result.setText("Tweet hori ez da arrazista :)");
                            txt_result.setStyle("-fx-text-inner-color: green;");
                            break;
                        case 1:
                            txt_result.setText("Tweet hori arrazista da!");
                            txt_result.setStyle("-fx-text-inner-color: red;");
                            break;
                    }
                }
                else if(btn_TrainFSS == null){
                    txt_result.setText("FSS egin ahal izateko Train fitxategia kargatu mesedez");
                }
                else{
                    txt_result.setText("Test fitxategi bat kargatu edo tweet bat idatzi mesedez");
                }
            }
            else{
                txt_result.setText("Ereduaren eta iragarpenak egiteko fitxategiak kargatu mesedez");
            }
        }
        else if(event.getSource()==btn_arff){ //Test fitxategia kargatu
            FileChooser fileChooser = new FileChooser();
            testFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = testFile.split("\\\\");
            lbl_test.setText(split[split.length-1]);
        }
        else if(event.getSource()==btn_model){ //Eredua kargatu
            FileChooser fileChooser = new FileChooser();
            modelFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = modelFile.split("\\\\");
            lbl_model.setText(split[split.length-1]);
        }
        else if(event.getSource()==btn_output){ //Iragarpenak gordetzeko fitxategia kargatu
            FileChooser fileChooser = new FileChooser();
            precitionsFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = precitionsFile.split("\\\\");
            lbl_output.setText(split[split.length-1]);
        }
        else if(event.getSource()==btn_TrainFSS){ //FSS egiteko train fitxategia kargatu
            FileChooser fileChooser = new FileChooser();
            trainFSS = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = trainFSS.split("\\\\");
            lbl_trainFSS.setText(split[split.length-1]);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rdbtn_sparse.setSelected(true);
    }

    private void testSortu(String data, String path) throws IOException {
        FileWriter fw = new FileWriter(path+"\\testInstantzia"+data+".csv");
        fw.write("id,tweet\n");
        fw.write("0,"+txtA_tweet.getText());
        fw.close();
    }



}
