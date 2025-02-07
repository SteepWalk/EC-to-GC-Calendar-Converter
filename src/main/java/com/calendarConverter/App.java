package com.calendarConverter;

import com.calendarConverter.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.InputStream;
import java.util.Objects;

public class App extends Application {

    private static ApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        // Initialize Spring context before JavaFX application starts
        applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppFXML.fxml"));
        loader.setControllerFactory(applicationContext::getBean);

        Parent root = loader.load();

        try(InputStream iconStream = getClass().getResourceAsStream("/calender_11708439.png")) {
            if (iconStream == null) {
                throw new IllegalArgumentException("Icon file not found");
            }
            Image icon = new Image(iconStream);
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Calendar Converter");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
