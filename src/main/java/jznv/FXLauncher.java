package jznv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jznv.data.DataBuilder;
import jznv.gui.MainController;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FXLauncher extends Application {
    private final SessionFactory factory = new Configuration()
            .configure("hibernate-update.xml")
            .buildSessionFactory();

    public static void main(String[] args) {
        FXLauncher.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FXLauncher.class.getResource("/model_charts.fxml"));
        Scene scene = new Scene(loader.load());
        MainController controller = loader.getController();
        controller.setDataBuilder(new DataBuilder(factory));
        controller.update();
        stage.setScene(scene);
        stage.show();
    }
}
