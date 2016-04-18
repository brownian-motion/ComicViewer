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
	int numPages = 0;
	PDFNode node = new PDFNode();
	Button left = new Button("Previous Page");
	Button right = new Button("Next Page");
	MenuItem next = new MenuItem("Next Page");
	final MenuItem previous = new MenuItem("Previous Page");
	@Override
	public void start(Stage stage) throws IOException{
//		stage.setResizable(false);
		BorderPane root = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setMaxSize(400, 35);
		hbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		
		
		TextField pageNumber = new TextField();
		
		left.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				int page = node.getPageNumber();
				goToPage(page - 1);
			}
			
		});
		
		right.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				int page = node.getPageNumber();
				goToPage(page + 1);
			}
			
		});
		
		final MenuBar bar = new MenuBar();
		final Menu file = new Menu( "File" );
	    final Menu comic = new Menu("Comic");
	    
		
		next.setVisible(true);
		next.setOnAction(e -> {});
		
		previous.setOnAction(e -> {});
		
		final MenuItem bookmark = new MenuItem("Bookmark");
		bookmark.setOnAction(e -> {});
		
		final MenuItem open = new MenuItem("Open Comic");
		open.setOnAction(e -> {
			if (!root.getChildren().contains(node))
			{
				ScrollPane nodeBox = new ScrollPane();
				nodeBox.setFitToWidth(true);
				nodeBox.setFitToHeight(true);
				nodeBox.setContent(node);
				nodeBox.setStyle("-fx-background-color: purple;");
				node.setStyle("-fx-background-color: cyan;");
				root.setCenter(nodeBox);
			}
			
			try{
				File fileToOpen = getPDFFileChooser().showOpenDialog(null);
				node.openFile(fileToOpen);
				numPages = node.getNumPages();
				System.out.println("page upon open: " + node.getPageNumber());
				right.setDisable(false);
				next.setDisable(false);
				pageNumber.setDisable(false);
				bookmark.setDisable(false);
	
			} catch(IOException exception){
				exception.printStackTrace();
				System.err.println("Couldn't open the pdf");
			}
			
			
		});
		
		
		
		final MenuItem darkSide = new MenuItem("Dark Mode");
		final MenuItem lightSide = new MenuItem("Light Mode");
		
		final MenuItem close = new MenuItem( "Close" );
		close.setOnAction( (e) -> { stage.close(); } );
		
		previous.setOnAction((e) -> {
			int page = node.getPageNumber();
			goToPage(page - 1);
		});
		next.setOnAction((e) -> {
			int page = node.getPageNumber();
			goToPage(page + 1);
		});
		
		file.getItems().add(open);
		file.getItems().add(bookmark);
		file.getItems().add(darkSide);
		file.getItems().add(lightSide);
		file.getItems().add( close );
		
		comic.getItems().add(next);
		comic.getItems().add(previous);
	
		bar.getMenus().add( file );
		bar.getMenus().add(comic);
		
		left.setDisable(true);
		right.setDisable(true);
		pageNumber.setDisable(true);
		bookmark.setDisable(true);
		previous.setDisable(true);
		next.setDisable(true);
		
		hbox.getChildren().addAll(left, pageNumber, right);
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #780000;");
		root.setTop(bar);
		root.setBottom(hbox);
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
	
	public void goToPage(int pageNumber){
		
		try{
			node.setPageNumber(pageNumber);
			System.out.println("we just set this!!!!" + node.getPageNumber());
		}
		catch (PDFException e)
		{
			System.out.println("a book end was reached. Page Number: " + node.getPageNumber());
		}
		
		if (pageNumber - 1 == 0)
		{
			previous.setDisable(true);
			left.setDisable(true);
		}
		
		else
		{
			previous.setDisable(false);
			left.setDisable(false);
		}
		
		if (pageNumber + 1 == node.getNumPages() - 1)
		{
			next.setDisable(true);
			right.setDisable(true);
		}
		else
		{
			next.setDisable(false);
			right.setDisable(false);
		}
	}
	
}
