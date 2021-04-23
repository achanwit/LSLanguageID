package unitTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.omniscien.lslanguageid.process.LSLanguageid;
import com.omniscien.lslanguageid.util.ProcessUtilLanguageid;

public class Test7_1_24 {	
	static LSLanguageid instant;	
	static String inputFilePath = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input/";
	static String outputFilePathCLD2 = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Output_CLD2/";
	static String outputFilePathCLD3 = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Output_CLD3/";
//	static String host = null;
//	static String port = null;
	
	public static void main(String[] args) throws Exception {
		//init
		LSLanguageid lsLangId = new LSLanguageid();
		//instant = lsLangId.Init(host, port);
		
		File folder = new File(inputFilePath);
		File[] listOfFiles = folder.listFiles();
		
		//System.out.println("Hello");
		
//		for(int i = 0;i< listOfFiles.length; i++) {
//			int idInt = 1+i;
//			
//			//Set idStr
//			String idStr = String.valueOf(idInt);
//			
//			//Set input File Path
//			String inputFilrPath = inputFilePath+listOfFiles[i].getName();
//			
//			//Set output File Path
//			String tmpName = listOfFiles[i].getName();
//			tmpName = tmpName.replaceAll("-Test", "-Output");
//			String outFilePath = outputFilePathCLD3+tmpName;
//			
//			//Set cld
//			String modeStr ="cld3";
//			
//			//Test API 
//			System.out.println("-----::: Result of 7-"+idStr+" :::----- ");	
//			String result = new String();
//			try {
//				long beginTime = System.currentTimeMillis();
//				result = lsLangId.GetLanguageIDLineByLine(idStr, inputFilrPath, outFilePath, modeStr);
//				long endTime = System.currentTimeMillis();
//				System.out.println("Used time for CLD3: " + (endTime - beginTime) + " millisec");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//					
//			System.out.println("Input file is: "+listOfFiles[i].getName());	
//			System.out.println("CLD: "+modeStr);
//			System.out.println("Result: "+result);			
//
//		}	
//	
		
		for(int i = 0;i< listOfFiles.length; i++) {
			int idInt = 1+i;
			
			//Set idStr
			String idStr = String.valueOf(idInt);
			
			//Set input File Path
			String inputFilrPath = inputFilePath+listOfFiles[i].getName();
			
			//Set output File Path
			String tmpName = listOfFiles[i].getName();
			tmpName = tmpName.replaceAll("-Test", "-Output");
			String outFilePath = outputFilePathCLD3+tmpName;
			
			//Set cld
			String modeStr ="cld3";
			
			//Test API 
			System.out.println("-----::: Result of 7-"+idStr+" :::----- ");	
			String result = new String();
			//ProcessUtil pu = new ProcessUtil();
			//result = lsLangId.GetLanguageIDLineByLine(idStr, inputFilrPath, outFilePath, modeStr);
			
			System.out.println("Input file is: "+listOfFiles[i].getName());	
			System.out.println("CLD: "+modeStr);
			
			long beginTime = System.currentTimeMillis(); 
			result = lsLangId.GetLanguageIDLineByLine(idStr, inputFilrPath, outFilePath, modeStr);
			long endTime = System.currentTimeMillis();
			System.out.println("Used time for CLD3: " + (endTime - beginTime) + " millisec");
			
					
//			System.out.println("Input file is: "+listOfFiles[i].getName());	
//			System.out.println("CLD: "+modeStr);
			System.out.println("Result: "+result);				

		}	
		
	
	}
}
