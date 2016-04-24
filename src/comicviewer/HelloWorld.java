package comicviewer;
import javafx.application.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.io.*;
import java.util.Optional;

public class HelloWorld extends Application{
	final int SCENE_HEIGHT = 700;
	final int SCENE_WIDTH = 1500;
	Bookmarking bookmarking = new Bookmarking();
	int numPages = 0;
	PDFNode node = new PDFNode();
	Button left = new Button("Previous Page");
	Button right = new Button("Next Page");
	MenuItem next = new MenuItem("Next Page");
	TextField pageNumber = new TextField();
	boolean isDisplaying = false;
	final MenuItem previous = new MenuItem("Previous Page");
	ScrollPane nodeBox = new ScrollPane();
	String name;
	@Override
	
	/**
	 * Creates the elements that populate the UI, and connects controls.
	 */
	public void start(Stage stage) throws IOException{
//		stage.setResizable(false);
		BorderPane root = new BorderPane();
		
		HBox hbox = new HBox();
		hbox.setMaxSize(400, 35);
		hbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		
		root.setOnKeyPressed((event) -> {
			if(event.getCode() == KeyCode.PAGE_UP){
				goToNextPage();
			} else if(event.getCode() == KeyCode.PAGE_DOWN){
				goToPreviousPage();
			}
		});
		
		pageNumber.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) {
			int page;
			try{
				page = Integer.parseInt(pageNumber.getText());
				goToPage(page);
			}
			
			catch(NumberFormatException e)
			{
				System.out.println("This wasn't an integer.");
			}
		} });
		
		//create re-usable event handlers to navigate from page to page
		final EventHandler<ActionEvent> previousPageNavigationEventHandler = new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				goToPreviousPage();
			}
		}, nextPageNavigationEventHandler = new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				goToNextPage();
			}
		};
		
		left.setOnAction(previousPageNavigationEventHandler);
		
		right.setOnAction(nextPageNavigationEventHandler);
		
		final MenuBar bar = new MenuBar();
		final Menu file = new Menu( "File" );
	    final Menu comic = new Menu("Comic");
	    
		
		next.setVisible(true);
		next.setOnAction(nextPageNavigationEventHandler);
		
		previous.setOnAction(previousPageNavigationEventHandler);
		
		final MenuItem bookmark = new MenuItem("Bookmark");
		bookmark.setOnAction(e -> {try {
			bookmarking.setBookmark(name, node.getPageNumber());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
		
		final MenuItem open = new MenuItem("Open Comic");
		open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		open.setOnAction(e -> {
			if (!root.getChildren().contains(nodeBox))
			{
				System.out.println("a node does not already exist.");
				nodeBox.setFitToWidth(true);
				nodeBox.setFitToHeight(true);
				nodeBox.setContent(node);
				root.setCenter(nodeBox);
			}
			
			else
			{
				System.out.println("a node already exists");
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Wait!");
				alert.setHeaderText("You are closing a comic without saving your place!");
				alert.setContentText("Would you like me to save this for you?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    try {
						bookmarking.setBookmark(name, node.getPageNumber());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Bookmarking failed.");
					}
				}
			}
			
			try{
				isDisplaying = true;
				File fileToOpen = getPDFFileChooser().showOpenDialog(null);
				name = fileToOpen.getName();
				System.out.println("The name of the file is: " + name);
				int page = bookmarking.getBookmark(name);
				node.openFile(fileToOpen, page);
				if (page != 1)
				{
					left.setDisable(false);
				}
				numPages = node.getNumPages();
				//System.out.println("pages upon open:" + node.getNumPages());
				//System.out.println("Current Page: " + node.getPageNumber());
				right.setDisable(false);
				next.setDisable(false);
				pageNumber.setDisable(false);
				pageNumber.setText(Integer.toString(node.getPageNumber()));
				bookmark.setDisable(false);
	
			} catch(IOException exception){
				exception.printStackTrace();
				System.err.println("Couldn't open the pdf");
			}
		});
		
		final MenuItem darkSide = new MenuItem("Dark Mode");
		final MenuItem lightSide = new MenuItem("Light Mode");
		
		final MenuItem close = new MenuItem( "Close" );
		close.setOnAction( (e) -> { 
			if (isDisplaying)
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Wait!");
				alert.setHeaderText("You are closing a comic without saving your place!");
				alert.setContentText("Would you like me to save this for you?");
				
				ButtonType buttonYes = new ButtonType("Yes");
				ButtonType buttonNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonYes, buttonNo);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonYes){
				    try {
						bookmarking.setBookmark(name, node.getPageNumber());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Bookmarking failed.");
					}
				}
			}
			
			stage.close(); } );
		
		//redundant:
//		previous.setOnAction(previousPageNavigationEventHandler);
//		next.setOnAction(nextPageNavigationEventHandler);
		
		file.getItems().add(open);
		file.getItems().add(darkSide);
		file.getItems().add(lightSide);
		file.getItems().add( close );
		
		comic.getItems().add(next);
		comic.getItems().add(previous);
		comic.getItems().add(bookmark);
	
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
	
	/**
	 * Launches the application.
	 * @param args The command-line arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	/**
	 * Returns a FileChooser object that can be used to open only .pdf files
	 * @return a FileChooser object that can be used to open only .pdf files
	 */
	public FileChooser getPDFFileChooser(){
		FileChooser out = new FileChooser();
		out.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
		return out;
	}
	
	/**
	 * Tries to go to the specified page. Displays an alert if that is not possible.
	 * @param page 	The page number to display
	 */
	public void goToPage(int page){
		
		try{
			node.setPageNumber(page);
			pageNumber.setText(Integer.toString(page));
			System.out.println("Current Page: " + node.getPageNumber());
		}
		catch (PDFException e)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid Page");
			alert.setContentText("The page you have requested does not exist.");
			alert.showAndWait();
			pageNumber.setText(Integer.toString(node.getPageNumber()));
		}
		
		if (page == 1) {
			previous.setDisable(true);
			left.setDisable(true);
		} else {
			previous.setDisable(false);
			left.setDisable(false);
		}
		
		if (page == node.getNumPages()) {
			next.setDisable(true);
			right.setDisable(true);
		} else {
			next.setDisable(false);
			right.setDisable(false);
		}
	}
	
	/**
	 * Navigates to the previous page. Takes no action if that page is out of bounds.
	 */
	public void goToPreviousPage(){
		int page = node.getPageNumber();
		if(page >= 1)
			goToPage(page - 1);
	}
	
	/**
	 * Navigates to the next page. Takes no action if that page is out of bounds.
	 */
	public void goToNextPage(){
		int page = node.getPageNumber();
		if(page <= node.getNumPages())
			goToPage(page + 1);
	}
}
