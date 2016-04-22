package comicviewer;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.embed.swing.SwingNode;

import com.sun.pdfview.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

public class HelloWorldController{

	@FXML private Pane pane;
	@FXML private PDFNode pdf;
	
	
	@FXML
	public void initialize(){
		pane.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.GRAY,null,null)));
		try{
			File fileToOpen = getPDFFileChooser().showOpenDialog(null);
			pdf.openFile(fileToOpen,0);
//			System.out.println("File opened");
//			System.out.println(pdf);
		} catch(IOException e){
			e.printStackTrace();
			System.err.println("Couldn't open the pdf");
		}
	}
	
	public FileChooser getPDFFileChooser(){
		FileChooser out = new FileChooser();
		out.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
		return out;
	}
}
