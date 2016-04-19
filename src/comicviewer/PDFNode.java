package comicviewer;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.io.*;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.Dimension;

import com.sun.pdfview.*;

import java.awt.image.BufferedImage;

/**
 * This is a class intended to function as a controllable Node in JavaFX that displays PDF.
 * It uses the com.sun.pdfview "PDFRenderer" library to render the PDF using Swing, and then
 * wraps that in a SwingNode for use in JavaFX.
 * 
 * @author Jack
 *
 */


public class PDFNode extends ImageView{
	
//	private ImageView imageView;
	private PDFFile pdfFile;
	private PDFPage currentPage;
//	private JLabel label;
	private WritableImage currentImage;
//	private ImageIcon currentImageIcon;
	
	
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
		//Try to open the file to read from a PDF. Auto-close in try/catch
		try(RandomAccessFile file = new RandomAccessFile(fileToOpen, "r")){
			FileChannel channel = file.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			pdfFile = new PDFFile(buffer);
			setPageNumber(0);
			file.close();
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
		this.setStyle("-fx-background-color: yellow;");
	}
	
	/**
	 * Changes this object's state so that it represents the lack of a file and displays nothing.
	 */
	public void clear(){
		pdfFile = null;
		currentPage = null;
	}
	
	@Override
	public void resize(double width, double height){
		//System.out.printf("Resizing to %.0fx%.0f\n",width,height);
		super.resize(width, height);
		//if(currentImage!=null)
			
			//System.out.printf("Image is %.0fx%.0f\n",currentImage.getWidth(), currentImage.getHeight());
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
		java.awt.Image awtImage = currentPage.getImage(realPageSize.width, realPageSize.height, pageBox, null);
		currentImage = toFXImage(awtImage,currentImage);
		this.setImage(currentImage);
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
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static java.awt.image.BufferedImage toBufferedImage(java.awt.Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public static WritableImage toFXImage(java.awt.Image img, WritableImage buffer){
		return SwingFXUtils.toFXImage(toBufferedImage(img), buffer);
	}
}
