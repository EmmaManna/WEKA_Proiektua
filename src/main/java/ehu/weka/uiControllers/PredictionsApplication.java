package ehu.weka.uiControllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PredictionsApplication extends Application {

    private Parent predictionsUI;
    private Stage stage;
    private Scene sceneMain;

    private PredictionsKudeatzaile predictionsKud;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        this.pantailakKargatu();
        stage.setTitle("Text Mining");
        this.ikonoaJarri();
        stage.setScene(sceneMain);
        stage.show();

    }

    private void pantailakKargatu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
        predictionsUI = (Parent) loader.load();
        predictionsKud = loader.getController();
        predictionsKud.setMain(this);
        sceneMain = new Scene(predictionsUI);
    }

    private void ikonoaJarri(){
        String imagePath = "src\\main\\resources\\Images\\weka.png";
        try {
            if(stage.getIcons().size()>0){
                stage.getIcons().remove(0);
            }
            stage.getIcons().add(new Image(new FileInputStream(imagePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
