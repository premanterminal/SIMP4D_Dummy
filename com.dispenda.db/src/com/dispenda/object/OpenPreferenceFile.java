package com.dispenda.object;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.RGB;

public class OpenPreferenceFile {

	public void readFile(){
		Path path = Paths.get(Platform.getLocation().toString()+"/pref.txt");
		try(Scanner scanner =  new Scanner(path)){
			while(scanner.hasNextLine()){
				String [] splitter = scanner.nextLine().split(" = ");
				if(splitter[0].equalsIgnoreCase("FONT_STYLE")){
					Preferences.FONT_STYLE = splitter[1];
				}else if(splitter[0].equalsIgnoreCase("FONT_SIZE")){
					Preferences.FONT_SIZE = Integer.parseInt(splitter[1]);
				}else if(splitter[0].equalsIgnoreCase("FONT_COLOR")){
					String[] strColor = splitter[1].split(",");
					Preferences.FONT_COLOR = new RGB(Integer.parseInt(strColor[0]), Integer.parseInt(strColor[1]), Integer.parseInt(strColor[2]));
				}else if(splitter[0].equalsIgnoreCase("BACKGROUND_COLOR")){
					String[] strColor = splitter[1].split(",");
					Preferences.BACKGROUND_COLOR = new RGB(Integer.parseInt(strColor[0]), Integer.parseInt(strColor[1]), Integer.parseInt(strColor[2]));
				}else if(splitter[0].equalsIgnoreCase("BACKGROUND_IMAGE")){
					Preferences.BACKGROUND_IMAGE = splitter[1];
				}else if(splitter[0].equalsIgnoreCase("ORIENTATION")){
					Preferences.ORIENTATION = Integer.parseInt(splitter[1]);
				}
			} 
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean writeFile(){
		PrintWriter writer;
		try {
			writer = new PrintWriter(Platform.getLocation().toString()+"/pref.txt", "UTF-8");
			writer.println("FONT_STYLE = "+Preferences.FONT_STYLE);
			writer.println("FONT_SIZE = "+Preferences.FONT_SIZE);
			writer.println("FONT_COLOR = "+Preferences.FONT_COLOR.red+","+Preferences.FONT_COLOR.green+","+Preferences.FONT_COLOR.blue);
			writer.println("BACKGROUND_COLOR = "+Preferences.BACKGROUND_COLOR.red+","+Preferences.BACKGROUND_COLOR.green+","+Preferences.BACKGROUND_COLOR.blue);
			writer.println("BACKGROUND_IMAGE = "+Preferences.BACKGROUND_IMAGE);
			writer.println("ORIENTATION = "+Preferences.ORIENTATION);
	    	writer.close();
	    	return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
