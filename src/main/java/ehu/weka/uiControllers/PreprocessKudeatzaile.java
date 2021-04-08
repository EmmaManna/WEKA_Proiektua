package ehu.weka.uiControllers;

import ehu.weka.Atal1.GetRaw;
import ehu.weka.Atal1.MakeCompatible;
import ehu.weka.Atal1.TransformRaw;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.ResourceBundle;

public class PreprocessKudeatzaile implements Initializable {

    private PredictionsApplication mainApp;
    private String testFile;
    private String trainFile;

    public void setMain(PredictionsApplication main) {
        this.mainApp = main;
    }

    @FXML
    private Button btn_train;

    @FXML
    private Button btn_test;

    @FXML
    private Label lbl_train;

    @FXML
    private Label lbl_test;

    @FXML
    private Button btn_start;

    @FXML
    private RadioButton rdbtn_sparse;

    @FXML
    private RadioButton rdbtn_BoW;

    @FXML
    private TextArea txtA_egoera;

    @FXML
    void onClick(ActionEvent event) throws Exception {
        if(event.getSource()==btn_start){
            if(trainFile!=null && testFile!=null){
                String path = trainFile.substring(0,trainFile.length()-lbl_train.getText().length());
                String[] argumentsTrain = new String[] {"train",trainFile,path+lbl_train.getText().substring(0,lbl_train.getText().length()-4)+".arff"};
                GetRaw.main(argumentsTrain);
                txtA_egoera.setText("Train CSV fitxategia egokituta");
                String[] argumentsTest = new String[] {"test",testFile,path+lbl_test.getText().substring(0,lbl_test.getText().length()-4)+".arff"};
                GetRaw.main(argumentsTest);
                txtA_egoera.appendText("\nTest CSV fitxategia egokituta");

                String bow = "1";
                if(rdbtn_BoW.isSelected()){
                    bow = "0";
                }

                String sparse = "no";
                if(rdbtn_sparse.isSelected()){
                    sparse = "yes";
                }

                String[] argumentsTR = new String[] {path+lbl_train.getText().substring(0,lbl_train.getText().length()-4)+".arff",bow,sparse,path+"train"+bow+sparse+".arff"};
                TransformRaw.main(argumentsTR);
                txtA_egoera.appendText("\nTrain fitxategia transformatuta: BoW="+bow+" , Sparse="+sparse+" , path="+path+"train"+bow+sparse+".arff");

                String[] argumentsMC = new String[] {path+"train"+bow+sparse+".arff",path+lbl_test.getText().substring(0,lbl_test.getText().length()-4)+".arff",bow,sparse,path+"test"+bow+sparse+".arff"};
                MakeCompatible.main(argumentsMC);
                txtA_egoera.appendText("\nTest fitxategia transformatuta: BoW="+bow+" , Sparse="+sparse+" , path="+path+"test"+bow+sparse+".arff");
            }
            else{
                txtA_egoera.setText("Test eta train csv fitxategiak kargatu mesedez");
            }

        } else if(event.getSource()==btn_train){
            FileChooser fileChooser = new FileChooser();
            trainFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = trainFile.split("\\\\");
            lbl_train.setText(split[split.length-1]);
        }
        else if(event.getSource()==btn_test){
            FileChooser fileChooser = new FileChooser();
            testFile = fileChooser.showOpenDialog(mainApp.getStage()).getAbsolutePath();
            String[] split = testFile.split("\\\\");
            lbl_test.setText(split[split.length-1]);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rdbtn_sparse.setSelected(true);
    }
}
