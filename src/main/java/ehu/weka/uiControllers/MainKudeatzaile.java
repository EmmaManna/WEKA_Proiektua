package ehu.weka.uiControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainKudeatzaile implements Initializable {
    private PredictionsApplication mainApp;

    public void setMain(PredictionsApplication main) {
        this.mainApp = main;
    }

    @FXML
    private Button btn_fss;

    @FXML
    private Button btn_Predictions;

    @FXML
    private Button btn_preprocess;

    @FXML
    private Button btn_close;

    @FXML
    private Button btn_min;


    @FXML
    private AnchorPane anchor_preprocess;

    @FXML
    private AnchorPane anchor_fss;

    @FXML
    private AnchorPane anchor_predictions;

    @FXML
    private AnchorPane anchorServer;


    @FXML
    void onClick(ActionEvent event) {
        //Erlaitz desberdinetan klik egitean bistaratu behar dena kudeatzen du
        if(event.getSource()==btn_Predictions) {
            anchor_predictions.toFront();
        }
        else if (event.getSource()==btn_fss){
            anchor_fss.toFront();
        }
        else if (event.getSource()==btn_preprocess){
            anchor_preprocess.toFront();
        }
        else if (event.getSource()==btn_close){
            //Aplikazioa ixten du
            mainApp.getStage().close();
            System.exit(0);
        }
        else if (event.getSource()==btn_min){
            //Aplikazioa minimizaten du
            mainApp.getStage().setIconified(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchor_preprocess.toFront();

    }
}
