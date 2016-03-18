package comicviewer;

import javafx.embed.swing.SwingNode;
import javafx.geometry.Bounds;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.layout.*;

import javax.swing.*;

import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.io.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Dimension;

import com.sun.pdfview.*;

/**
 * This is a class intended to function as a controllable Node in JavaFX that displays PDF.
 * It uses the com.sun.pdfview "PDFRenderer" library to render the PDF using Swing, and then
 * wraps that in a SwingNode for use in JavaFX.
 * 
 * @author Jack
 *
 */

//TODO: resize the displayed image automatically as this container resizes

public class PDFNode extends SwingNode {
	
//	private ImageView imageView;
	private PDFFile pdfFile;
	private PDFPage currentPage;
	private JLabel label;
	private Image currentImage;
	private ImageIcon currentImageIcon;
	
	
	//This initializer block runs before the constructor.
	{
		initialize();
	}

	/**
	 * Creates an empty PDFNode that displays nothing.
	 */
	public PDFNode(){
	}
	
	/**
	 * Changes the file that is open and displayed so that a new file is displayed.
	 * If null or an unreadable file is passed in, the display is cleared so that 
	 * this PDFNode displays nothing.
	 * If a valid file is given, the first page is displayed.
	 *  
	 * @param fileToOpen	A File object representing a PDF file to display, or null to display nothing.
	 * @throws IOException	If the file cannot be read.
	 */
	public void openFile(File fileToOpen) throws IOException{
		if(fileToOpen == null){
			clear();
			return;
		}
		//TODO: finish writing this so that it encapsulates setting up a file
		try{
			RandomAccessFile file = new RandomAccessFile( fileToOpen, "r");
			FileChannel channel = file.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			pdfFile = new PDFFile(buffer);
			setPageNumber(0);
		} catch (IOException|NullPointerException e){
			System.err.println("Error loading a pdf to display.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a handle to the PDFFile that this PDFNode represents
	 * @return a handle to the PDFFile that this PDFNode represents
	 */
	public PDFFile getFile(){
		return pdfFile;
	}
	/**
	 * Returns the number of pages in the PDF file that this PDFNode is displaying
	 * @return the number of pages in the PDF file that this PDFNode represents, or 0 if it does not have a file loaded
	 */
	public int getNumPages(){
		if(pdfFile == null){
			return 0;
		}
		return pdfFile.getNumPages();
	}
	
	/**
	 * Creates the GUI elements (such as JLabel objects) that this object will use.
	 * After this method is run, this object should be in an empty state that displays nothing.
	 * 
	 * It should only be run once, when the object is first being created.
	 */
	private void initialize(){
//		imageView = new ImageView();
		label = new JLabel();
		label.setBackground(java.awt.Color.GRAY);
		SwingUtilities.invokeLater( () -> setContent(label) );
		boundsInParentProperty().addListener( (b, oldBounds, newBounds) -> {
			Bounds parentBounds = getParent().getLayoutBounds();
			zoomToFit(parentBounds.getWidth(), parentBounds.getHeight());
			super.resize(label.getWidth(), label.getHeight());
			System.out.println(newBounds);
		});
//		this.setBackground( new Background( new BackgroundFill(Color.GRAY,null,null) ) ); //kinda tedious, but it sets the background to be gray
	}
	
	/**
	 * Changes this object's state so that it represents the lack of a file and displays nothing.
	 */
	public void clear(){
		pdfFile = null;
		currentPage = null;
		label.removeAll();
		//imageView.setImage(null);
	}
	
	@Override
	public void resize(double width, double height){
		if(currentImageIcon != null)
			zoomToFit(width, height);
		super.resize(width, height);
	}
	
	public void zoomToFit(double width, double height){
		if(width == 0 || height == 0)
			return;
		if(width / height > currentImageIcon.getIconWidth()/currentImageIcon.getIconHeight()){
			//container is wider than the image, match the heights
			int newWidth = (int)(height * currentImageIcon.getIconWidth())/currentImageIcon.getIconHeight();
			label.setSize(new Dimension(newWidth, (int) height));
			currentImageIcon.setImage(currentImage.getScaledInstance(newWidth, (int)height, Image.SCALE_FAST));
		} else {
			//container is taller than the image, match the widths
			int newHeight = (int)(width * currentImageIcon.getIconHeight()/currentImageIcon.getIconWidth());
			label.setSize((int) width, newHeight);
			currentImageIcon.setImage(currentImage.getScaledInstance((int)width, newHeight, Image.SCALE_FAST));
		}
	}
	
	/**
	 * Displays the specified page
	 * 
	 * @param pageIndex		The index of the requested page, starting from 0.
	 * @throws PDFException	if no PDFFile is loaded or if the page is out of bounds.
	 */
	public void setPageNumber(int pageIndex){
		if(pdfFile == null)
			throw new PDFException("No file is loaded");
		if(pageIndex < 0 || pageIndex >= pdfFile.getNumPages())
			throw new PDFException("Error accessing out-of-bounds page number");
		//Do we need to get rid of the previous page object somehow?
		currentPage = pdfFile.getPage(pageIndex);
		Rectangle2D pageBox = currentPage.getPageBox();			
		Dimension realPageSize = currentPage.getUnstretchedSize((int)pageBox.getWidth(), (int)pageBox.getHeight(), pageBox);
		currentImage = currentPage.getImage(realPageSize.width, realPageSize.height, pageBox, label);
		label.setIcon( currentImageIcon = new ImageIcon(currentImage) );
		SwingUtilities.invokeLater( ()-> {
			Bounds layoutBounds = getLayoutBounds();
			zoomToFit(layoutBounds.getWidth(), layoutBounds.getHeight());
		});
	}
	
	/**
	 * Returns the current page number.
	 * Throws a PDFException if no file is displayed.
	 * @return	the current page number
	 * @throws PDFException	if no file is displayed
	 */
	public int getPageNumber(){
		if(currentPage == null)
			throw new PDFException("No file is loaded");
		return currentPage.getPageNumber();
	}
	
	@Override
	public boolean isResizable(){
		return true;
	}

}
