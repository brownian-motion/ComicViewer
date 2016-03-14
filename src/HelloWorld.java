import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.fxml.*;
import java.io.*;

public class HelloWorld extends Application{
	
	@Override
	public void start(Stage stage) throws IOException{
		Parent parent = FXMLLoader.load(getClass().getResource("layouts/HelloWorld.fxml"));
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.show();
		System.out.println("Yo");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
