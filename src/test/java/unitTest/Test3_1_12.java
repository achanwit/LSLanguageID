package unitTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.omniscien.lslanguageid.process.LSLanguageid;

public class Test3_1_12 {	
	static LSLanguageid instant;	
	static String inputFilePath = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input_String";
	static String host = null;
	static String port = null;
	
	public static void main(String[] args) throws Exception {
		//init
		LSLanguageid lsLangId = new LSLanguageid();
		//instant = lsLangId.Init(host, port);
		
		File folder = new File(inputFilePath);
		File[] listOfFiles = folder.listFiles();
		
//		for(int i = 0; i<listOfFiles.length;i++) {
//			System.out.println(listOfFiles[i]);
//		}
		
		for(int i = 0;i< listOfFiles.length; i++) {
			int idInt = 1+i;
			
			//Set idStr
			String idStr = String.valueOf(idInt);
			String inputFilrPath = inputFilePath+"/"+listOfFiles[i].getName();
			
//			//Set mode
			String modeStr = "cld2";
			
			//Read file to String
			File file = new File(inputFilrPath);
			Scanner reader = new Scanner(file);	
			StringBuffer inputBuf = new StringBuffer();
			while(reader.hasNextLine()) {
				inputBuf.append(reader.nextLine());
			}
			
			//Set input
			String inputStr = inputBuf.toString();	
			
			//Test API 
			String result = lsLangId.GetLanguageIDGeneral(inputStr);
		//	String result = instant.GetLanguageIDGeneral(inputStr);
			
			System.out.println("-----::: Result of 3-"+idStr+" :::----- ");			
			System.out.println("Input file is: "+listOfFiles[i].getName());	
			System.out.println("CLD: "+modeStr);
			System.out.println("Result: "+result);			

		}	
		
//		for(int i = 0;i< listOfFiles.length; i++) {
//			int idInt = 13+i;
//			
//			//Set idStr
//			String idStr = String.valueOf(idInt);
//			String inputFilrPath = inputFilePath+"/"+listOfFiles[i].getName();
//			
//			//Set mode
//			String modeStr = "cld3";
//			
//			//Read file to String
//			File file = new File(inputFilrPath);
//			Scanner reader = new Scanner(file);	
//			StringBuffer inputBuf = new StringBuffer();
//			while(reader.hasNextLine()) {
//				inputBuf.append(reader.nextLine());
//			}
//			
//			//Set input
//			String inputStr = inputBuf.toString();			
//			String result = instant.GetLanguageIDGeneral(idStr, inputStr, modeStr);
//			
//			System.out.println("-----::: Result of 2-"+idStr+" :::----- ");			
//			System.out.println("Input file is: "+listOfFiles[i].getName());	
//			System.out.println("CLD: "+modeStr);
//			System.out.println("Result: "+result);			
//
//		}	
	}
}
