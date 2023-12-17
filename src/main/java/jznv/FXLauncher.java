package jznv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jznv.data.ChartDataBuilder;
import jznv.gui.MainController;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FXLauncher extends Application {
    private final SessionFactory factory = new Configuration().configure().buildSessionFactory();

    public static void main(String[] args) {
        FXLauncher.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FXLauncher.class.getResource("/main.fxml"));
        Scene scene = new Scene(loader.load());
        MainController controller = loader.getController();
        controller.setDataBuilder(new ChartDataBuilder(factory));
        stage.setScene(scene);
        stage.show();
    }
}
