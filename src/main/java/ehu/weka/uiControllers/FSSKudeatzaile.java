package ehu.weka.uiControllers;

import ehu.weka.Atal1.MakeCompatible;
import ehu.weka.Atal2.FSS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.ResourceBundle;

public class FSSKudeatzaile implements Initializable {

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

                String bow = "1";
                if(rdbtn_BoW.isSelected()){
                    bow = "0";
                }

                String sparse = "no";
                if(rdbtn_sparse.isSelected()){
                    sparse = "yes";
                }

                String[] argumentsFSS = new String[] {trainFile,testFile,bow,sparse,path+"train"+bow+sparse+"_InfoGain.arff",path+"test"+bow+sparse+"_InfoGain.arff"};
                FSS.main(argumentsFSS);
                txtA_egoera.setText("\nTrain fitxategia transformatuta: BoW="+bow+" , Sparse="+sparse+" , path="+path+"train"+bow+sparse+"_InfoGain.arff");
                txtA_egoera.appendText("\nTest fitxategia transformatuta: BoW="+bow+" , Sparse="+sparse+" , path="+path+"test"+bow+sparse+"_InfoGain.arff");
            }
            else{
                txtA_egoera.setText("Test eta train arff fitxategiak kargatu mesedez");
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
