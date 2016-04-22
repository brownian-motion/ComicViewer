package comicviewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Bookmarking {

	public Bookmarking() {
	}
	
	public static int getBookmark(String name) throws IOException
	{
		File bookmarks = new File("Bookmarks.txt");
		if(!bookmarks.exists()) {
		    return 0;
		}
		
		FileInputStream fileReader = null;
		try {
			 fileReader = new FileInputStream(bookmarks);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fileReader));
		String line = null;
		while( (line = br.readLine())!= null ){
		    String [] tokens = line.split("\\s+");
		    String constructName = "";
		    int length = tokens.length;
		    if (length != 2)
		    {
		    	constructName = constructName.concat(tokens[0]);
		    	for (int i = 1; i < length - 1; i++)
		    	{
		    		constructName = constructName.concat(" ");
		    		constructName = constructName.concat(tokens[i]);
		    	}
		    }
		    
		    else
		    {
		    	constructName = tokens[0];
		    }
		    
		    if (constructName.equals(name))
		    {
		    	//System.out.println("The pageNum we are looking for is: " + tokens[tokens.length - 1]);
		    	return Integer.valueOf(tokens[tokens.length - 1]);
		    }
		}
		br.close();
		return 0;
	}
	
	public static void setBookmark(String name, int mark) throws IOException
	{
		File temp = new File("temp.txt");
		temp.createNewFile();
		File bookmarks = new File("Bookmarks.txt");
		if(!bookmarks.exists()) {
			bookmarks.createNewFile();
			System.out.println("Filepath is: " + bookmarks.getAbsolutePath());
		}
		
		FileInputStream fileReader = new FileInputStream(bookmarks);
		BufferedReader br = new BufferedReader(new InputStreamReader(fileReader));
	    FileOutputStream out = new FileOutputStream(temp);
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
	    String line = null;
	    while( (line = br.readLine())!= null ){
		    String [] tokens = line.split("\\s+"); 
		    String constructName = "";
		    int length = tokens.length;
		    if (length != 2)
		    {
		    	constructName = constructName.concat(tokens[0]);
		    	for (int i = 1; i < length - 1; i++)
		    	{
		    		constructName = constructName.concat(" ");
		    		constructName = constructName.concat(tokens[i]);
		    	}
		    }
		    
		    else
		    {
		    	constructName = tokens[0];
		    }
		    
		    if (!(constructName.equals(name)))
		    {
		    	bw.write(line + "\n");
		    	bw.flush();
		    }
	    }
	    
	    bw.write(name + " " + Integer.toString(mark) + "\n");
	    
	    bw.close();
	    br.close();
	    
	    bookmarks.delete();
	    temp.renameTo(bookmarks);
	}

}
