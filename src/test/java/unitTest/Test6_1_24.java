package unitTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.omniscien.lslanguageid.process.LSLanguageid;

public class Test6_1_24 {	
//	static LSLanguageid instant;	
	static String inputFilePath = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input_String";
	//static String host = null;
//	static String port = null;
	
	public static void main(String[] args) throws Exception {
		//init
		LSLanguageid lsLangId = new LSLanguageid();
	//	instant = lsLangId.Init(host, port);
		
		File folder = new File(inputFilePath);
		File[] listOfFiles = folder.listFiles();
		
		for(int i = 0;i< listOfFiles.length; i++) {
			int idInt = 1+i;
			
			//Set idStr
			String idStr = String.valueOf(idInt);
			String inputFilrPath = inputFilePath+"/"+listOfFiles[i].getName();
			
			//Set cld
			String modeStr ="cld2";
			
			//Test API 
			String result = lsLangId.GetLanguageIDGeneralFromFile(inputFilrPath, modeStr);
			
			System.out.println("-----::: Result of 6-"+idStr+" :::----- ");			
			System.out.println("Input file is: "+listOfFiles[i].getName());	
			System.out.println("CLD: "+modeStr);
			System.out.println("Result: "+result);			

		}	
		
		for(int i = 0;i< listOfFiles.length; i++) {
			int idInt = 13+i;
			
			//Set idStr
			String idStr = String.valueOf(idInt);
			String inputFilrPath = inputFilePath+"/"+listOfFiles[i].getName();
			
			//Set cld
			String modeStr ="cld3";
			
			//Test API 
			String result = lsLangId.GetLanguageIDGeneralFromFile(inputFilrPath, modeStr);
			
			System.out.println("-----::: Result of 6-"+idStr+" :::----- ");			
			System.out.println("Input file is: "+listOfFiles[i].getName());	
			System.out.println("CLD: "+modeStr);
			System.out.println("Result: "+result);			

		}	
		
	
	}
}
