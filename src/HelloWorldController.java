import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
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

	@FXML private FlowPane pane;
	@FXML private SwingNode swingNode;
	
	
	@FXML
	public void initialize(){
//		PDFFile file = new PDFFile(new ByteBuffer(TEST_FILE));
//		PDFImage viewer = new PDFImage(file);
//		pane.getChildren().add(viewer);
		try{
			FileChooser fChooser = getPDFFileChooser();
			File result = fChooser.showOpenDialog(null);
			RandomAccessFile file = new RandomAccessFile( result, "r");
			FileChannel channel = file.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			PDFFile pdfFile = new PDFFile(buffer);
			PDFPage pageOne = pdfFile.getPage(0);
			Rectangle2D pageBox = pageOne.getPageBox();			
			Dimension realPageSize = pageOne.getUnstretchedSize(800, 800, pageBox);
			final JLabel label = new JLabel();
			Image image = pageOne.getImage(realPageSize.width, realPageSize.height, pageBox, label);
			label.setIcon(new ImageIcon(image));
			SwingUtilities.invokeLater( ()-> {swingNode.setContent(label);} );
		} catch (IOException|NullPointerException e){
			System.err.println("Error loading a pdf to display.");
			e.printStackTrace();
		}
	}
	
	public FileChooser getPDFFileChooser(){
		FileChooser out = new FileChooser();
		out.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
		return out;
	}
}
