package unitTest;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.omniscien.lslanguageid.process.LSLanguageid;

public class TestSpecialCharBY_CS {
	
	static LSLanguageid instant;	
	static String inputFilePath = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input/";
	static String outputFilePath = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Output-SpecialChar/";
	static String iputFile = "Test-Unknow.txt";
//	static String host = null;
//	static String port = null;

	public static void main(String[] args) throws Exception {
		//init
		LSLanguageid lsLangId = new LSLanguageid();
		//instant = lsLangId.Init(host, port);
				
		
		String inputFilrPath = inputFilePath+iputFile;
		
		//Set output File Path
		String tmpName = iputFile;
		tmpName = tmpName.replaceAll("-Test", "-Output");
		String outFilePath = outputFilePath+tmpName;
		
		//Set cld
		String modeStr ="cld2";
		
		//Test API 
		System.out.println("-----::: Result of 7-"+"Special"+" :::----- ");	
		String result = new String();
		try {
			result = lsLangId.GetLanguageIDLineByLine("", inputFilrPath, outFilePath, modeStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		System.out.println("Input file is: "+iputFile);	
		System.out.println("CLD: "+modeStr);
		System.out.println("Result: "+result);		
		
	}
}
