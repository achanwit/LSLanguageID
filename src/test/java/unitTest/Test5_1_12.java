package unitTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.omniscien.lslanguageid.process.LSLanguageid;

public class Test5_1_12 {	
	//static LSLanguageid instant;	
	static String inputFilePath = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input";

	
	public static void main(String[] args) throws Exception {
		//init
		LSLanguageid lsLangId = new LSLanguageid();
		//instant = lsLangId.Init(host, port);
		
		File folder = new File(inputFilePath);
		File[] listOfFiles = folder.listFiles();
		
		for(int i = 0;i< listOfFiles.length; i++) {
			int idInt = 1+i;
			
			//Set idStr
			String idStr = String.valueOf(idInt);
			String inputFilrPath = inputFilePath+"/"+listOfFiles[i].getName();
			
			
			//Test API 
			String result = lsLangId.GetLanguageIDGeneralFromFile(inputFilrPath);
			
			System.out.println("-----::: Result of 5-"+idStr+" :::----- ");			
			System.out.println("Input file is: "+listOfFiles[i].getName());	
			System.out.println("CLD: "+"cld1");
			System.out.println("Result: "+result);			

		}	
	}
}
