package ehu.weka.uiControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class PredictionsKudeatzaile implements Initializable {
    private PredictionsApplication mainApp;

    public void setMain(PredictionsApplication main) {
        this.mainApp = main;
    }

    @FXML
    private TextArea txtA_tweet;

    @FXML
    private Button btn_upload;

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
    void onClick(ActionEvent event) {
        if(event.getSource()==btn_start){
            txtA_tweet.setText("START");
        }
        else if(event.getSource()==btn_upload){
            txtA_tweet.setText("UPLOAD");

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
