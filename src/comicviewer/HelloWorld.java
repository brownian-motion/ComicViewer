package comicviewer;
import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.*;

public class HelloWorld extends Application{
	final int SCENE_HEIGHT = 700;
	final int SCENE_WIDTH = 1500;
	@Override
	public void start(Stage stage) throws IOException{
		stage.setResizable(false);
		BorderPane root = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setMaxSize(400, 35);
		hbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		
		PDFNode node = new PDFNode();
		TextField pageNumber = new TextField();
		Button left = new Button("Previous Page");
		Button right = new Button("Next Page");
		
		final MenuBar bar = new MenuBar();
		final Menu file = new Menu( "File" );
	    final Menu comic = new Menu("Comic");
	    
		MenuItem next = new MenuItem("Next Page");
		next.setVisible(true);
		next.setOnAction(e -> {});
		final MenuItem previous = new MenuItem("Previous Page");
		previous.setOnAction(e -> {});
		
		final MenuItem open = new MenuItem("Open Comic");
		open.setOnAction(e -> {
			if (!root.getChildren().contains(node))
			{
				System.out.println("i am adding a node");
				ScrollPane nodeBox = new ScrollPane();
				nodeBox.setFitToWidth(true);
				nodeBox.setFitToHeight(true);
				nodeBox.setContent(node);
//				Button button = new Button("wat");
//				button.setStyle("-fx-background-color: #0FF;");
//				nodeBox.setContent(button);
				nodeBox.setStyle("-fx-background-color: purple;");
				node.setStyle("-fx-background-color: cyan;");
				root.setCenter(nodeBox);
				System.out.println( nodeBox.getBoundsInParent() );
//				StackPane.setAlignment(node, Pos.CENTER);
			}
			
			try{
				File fileToOpen = getPDFFileChooser().showOpenDialog(null);
				node.openFile(fileToOpen);
//				System.out.println("File opened");
//				System.out.println(pdf);
			} catch(IOException exception){
				exception.printStackTrace();
				System.err.println("Couldn't open the pdf");
			}
			
			
		});
		
		final MenuItem bookmark = new MenuItem("Bookmark");
		bookmark.setOnAction(e -> {});
		
		final MenuItem darkSide = new MenuItem("Dark Mode");
		final MenuItem lightSide = new MenuItem("Light Mode");
		
		final MenuItem close = new MenuItem( "Close" );
		close.setOnAction( (e) -> { stage.close(); } );
		
		file.getItems().add(open);
		file.getItems().add(bookmark);
		file.getItems().add(darkSide);
		file.getItems().add(lightSide);
		file.getItems().add( close );
		
		comic.getItems().add(next);
		comic.getItems().add(previous);
		
		bar.getMenus().add( file );
		bar.getMenus().add(comic);
		
		hbox.getChildren().addAll(left, pageNumber, right);
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #780000;");
		root.setTop(bar);
		root.setBottom(hbox);
//		StackPane.setAlignment(bar, Pos.TOP_CENTER);
//		StackPane.setAlignment(hbox, Pos.BOTTOM_CENTER);
		root.setStyle("-fx-background-color: #336699;");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public FileChooser getPDFFileChooser(){
		FileChooser out = new FileChooser();
		out.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
		return out;
	}
}
