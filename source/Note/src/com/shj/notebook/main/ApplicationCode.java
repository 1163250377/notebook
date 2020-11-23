package com.shj.notebook.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ApplicationCode extends Application {

	public void appRun(String[] args) {
		launch(args);
	}

	private static Stage primaryStage;
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	private AnchorPane rootLayout;

	@Override
	public void start(Stage Stage) {

		primaryStage = Stage;
		primaryStage.setTitle("记事本");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ApplicationCode.class.getResource("application.fxml"));
			loader.setBuilderFactory(new JavaFXBuilderFactory());
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/static/image/icon.png")));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

}
