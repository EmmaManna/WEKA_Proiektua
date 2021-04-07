package ehu.weka.uiControllers;

import ehu.weka.Atal1.GetRaw;
import ehu.weka.Atal1.MakeCompatible;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class PredictionsKudeatzaile implements Initializable {
    private PredictionsApplication mainApp;
    private String testFile;
    private String modelFile;
    private String precitionsFile;
    private String trainFile;

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
    private Text txt_accuracy;

    @FXML
    private Text txt_precision;

    @FXML
    private Text txt_recall;

    @FXML
    private Button btn_model;

    @FXML
    private Button btn_output;

    @FXML
    private Button btn_train;

    @FXML
    private Label lbl_test;

    @FXML
    private Label lbl_model;

    @FXML
    private Label lbl_output;

    @FXML
    private Label lbl_train;

    @FXML
    private RadioButton rdbtn_BoW;

    @FXML
    private RadioButton rdbtn_sparse;


    @FXML
    void onClick(ActionEvent event) throws Exception {
        if(event.getSource()==btn_start){
            if(precitionsFile!=null && modelFile!=null){
                if(testFile!=null){
                    String[] arguments = new String[] {testFile,modelFile,precitionsFile};
                    Evaluation eval = Predictions.main(arguments);
                    txt_accuracy.setText("Accuracy: "+eval.pctCorrect());
                    txt_precision.setText("Precision: "+eval.pctCorrect());
                    txt_recall.setText("Recall: "+eval.pctCorrect());
                    txt_result.setText("Iragarpenak fitxategi honetan gorde dira: "+precitionsFile);
                }
                else if(txtA_tweet.getText()!=null && trainFile!=null){
                    String data = String.valueOf(System.currentTimeMillis());
                    String path = precitionsFile.substring(0,precitionsFile.length()-lbl_output.getText().length());
                    testSortu(data,path);
                    String[] arguments1 = new String[] {"test",path+"\\testInstantzia"+data+".csv",path+"\\testInstantzia"+data+".arff"};
                    GetRaw.main(arguments1);

                    String bow = "1";
                    if(rdbtn_BoW.isSelected()){
                        bow = "0";
                    }

                    String sparse = "no";
                    if(rdbtn_sparse.isSelected()){
                        sparse = "yes";
                    }

                    String[] arguments2 = new String[] {trainFile,path+"\\testInstantzia"+data+".arff",bow,sparse,path+"\\testInstaintzia"+bow+sparse+data+".arff"};
                    MakeCompatible.main(arguments2);

                    String[] arguments = new String[] {path+"\\testInstaintzia"+bow+sparse+data+".arff",modelFile,precitionsFile};
                    Evaluation eval = Predictions.main(arguments);
                    txt_accuracy.setText("Accuracy: "+eval.pctCorrect());
                    txt_precision.setText("Precision: "+eval.pctCorrect());
                    txt_recall.setText("Recall: "+eval.pctCorrect());

                    double predicted = eval.predictions().get(0).predicted();
                    switch ((int)predicted){
                        case 0:
                            txt_result.setText("Tweet hori ez da arrazista :)");
                            txt_result.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
                            break;
                        case 1:
                            txt_result.setText("Tweet hori arrazista da!");
                            txt_result.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                            break;
                        default:
                            txt_result.setText("Tweet hori ez da arrazista :)");
                            txt_result.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
                            break;
                    }
                }
                else{
                    txt_result.setText("Test fitxategi bat kargatu edo tweet bat idatzi mesedez");
                }
            }
            else{
                txt_result.setText("Ereduaren eta iragarpenak egiteko fitxategiak kargatu mesedez");
            }
        }
        else if(event.getSource()==btn_arff){
            FileChooser fileChooser = new FileChooser();
            testFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = testFile.split("\\\\");
            lbl_test.setText(split[split.length-1]);
        }
        else if(event.getSource()==btn_model){
            FileChooser fileChooser = new FileChooser();
            modelFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = modelFile.split("\\\\");
            lbl_model.setText(split[split.length-1]);
        }
        else if(event.getSource()==btn_output){
            FileChooser fileChooser = new FileChooser();
            precitionsFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = precitionsFile.split("\\\\");
            lbl_output.setText(split[split.length-1]);
        }
        else if(event.getSource()==btn_train){
            FileChooser fileChooser = new FileChooser();
            trainFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = trainFile.split("\\\\");
            lbl_train.setText(split[split.length-1]);
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
