package ehu.weka.uiControllers;

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
    void onClick(ActionEvent event) {
        if(event.getSource()==btn_start){

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
