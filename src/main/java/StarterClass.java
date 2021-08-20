import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class StarterClass extends Application {
    public static FXMLLoader loader = new FXMLLoader();
    public static Stage mainStage;


    //      C:\DISK D\tariff\Tariff Moscow 2021 - 4 (19.07.2021) - Digital Buildings.xlsb
    public static void main(String[] args) throws Exception {
        Application.launch();
        System.out.println("end of main");
    }

    @Override
    public void start(Stage stage) throws Exception {

        mainStage = stage;
        Parent root = loader.load(getClass().getResource("/wellcome.fxml"));
        InputStream iconStream = getClass().getResourceAsStream("/images/life.png");
        Image image = new Image(iconStream);
        stage.getIcons().add(image);

        stage.setTitle("EcoStruxureBuilder v0.0.1 ");
        stage.setScene(new Scene(root ));
        stage.show();
    }
}

