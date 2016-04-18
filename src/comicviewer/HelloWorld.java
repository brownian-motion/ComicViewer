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
	@Override
	public void start(Stage stage) throws IOException{
//		stage.setResizable(false);
		BorderPane root = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setMaxSize(400, 35);
		hbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		
		PDFNode node = new PDFNode();
		TextField pageNumber = new TextField();
		Button left = new Button("Previous Page");
		Button right = new Button("Next Page");
		
		left.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				int page = node.getPageNumber();
				System.out.println("current page: " + page);
				if (page - 1 == 0)
				{
					left.setDisable(true);
				}
				try{
					node.setPageNumber(page - 1);
					System.out.println("we just set this!!!!" + node.getPageNumber());
				}
				catch (PDFException e)
				{
					System.out.println("beginning of book was reached. Page Number: " + node.getPageNumber());
				}
				
			//	if (currentPage - 1 == 0)
			//	{
			//		left.setDisable(true);
			//	}
				
				right.setDisable(false);
			}
			
		});
		
		right.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				//currentPage += 1;
				//System.out.println("currentPage: " + currentPage);
				//node.setPageNumber(currentPage);
				int page = node.getPageNumber();
				try{
					node.setPageNumber(page + 1);
					System.out.println("we are now on this page moving forwards: " + node.getPageNumber());
				}
				catch (PDFException e)
				{
					System.out.println("end of book was reached");
				}
				
				if (page + 1 == (numPages - 1))
				{
					right.setDisable(true);
				}
				//if (currentPage + 1 == (numPages - 1))
				//{
			//		right.setDisable(true);
			//	}
				
				left.setDisable(false);
			}
			
		});
		
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
				//System.out.println("i am adding a node");
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
				//System.out.println( nodeBox.getBoundsInParent() );
//				StackPane.setAlignment(node, Pos.CENTER);
			}
			
			try{
				File fileToOpen = getPDFFileChooser().showOpenDialog(null);
				node.openFile(fileToOpen);
				numPages = node.getNumPages();
				System.out.println("page upon open: " + node.getPageNumber());
				//currentPage = node.getPageNumber();
				//System.out.println("currentPage on open: " + currentPage);
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
		
		left.setDisable(true);
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
