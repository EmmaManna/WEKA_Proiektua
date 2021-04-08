package ehu.weka.uiControllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PredictionsApplication extends Application {

    private Parent mainUI;
    private Stage stage;
    private Scene sceneMain;

    private MainKudeatzaile mainKud;
    private PredictionsKudeatzaile predictionsKud;
    private FSSKudeatzaile fssKud;
    private PreprocessKudeatzaile preprocessKud;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        this.pantailakKargatu();
        stage.setTitle("Text Mining");
        this.ikonoaJarri();

        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(sceneMain);
        stage.show();

    }

    private void pantailakKargatu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
        kudeatzaileakKargatu();

        Callback<Class<?>, Object> controllerFactory = type -> {
            if (type == MainKudeatzaile.class) {
                return mainKud ;
            } else if (type == MainKudeatzaile.class) {
                return mainKud;
            } else if(type == PredictionsKudeatzaile.class) {
                return predictionsKud;
            } else if(type == FSSKudeatzaile.class) {
                return fssKud;
            } else if(type == PreprocessKudeatzaile.class) {
                return preprocessKud;
            }

            else {// default behavior for controllerFactory:
                try {
                    return type.newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc); // fatal, just bail...
                }
            }
        };

        loader.setControllerFactory(controllerFactory);
        mainUI = (Parent) loader.load();
        sceneMain = new Scene(mainUI);

        /*
        mainUI = (Parent) loader.load();
        mainKud = loader.getController();
        mainKud.setMain(this);
        sceneMain = new Scene(mainUI);

         */
    }

    private void kudeatzaileakKargatu(){
        //Aplikazioak erabiltzen dituen kudeatzaileak hasieratzen ditu
        mainKud = new MainKudeatzaile();
        mainKud.setMain(this);
        predictionsKud = new PredictionsKudeatzaile();
        predictionsKud.setMain(this);
        preprocessKud = new PreprocessKudeatzaile();
        preprocessKud.setMain(this);
        fssKud = new FSSKudeatzaile();
        //fssKud.setMain(this);
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

    public Stage getStage() {
        return stage;
    }
}
