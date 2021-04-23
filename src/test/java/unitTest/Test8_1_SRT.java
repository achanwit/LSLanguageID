package unitTest;

import java.io.FileNotFoundException;
import java.io.IOException;

import LSLanguageid.LSLanguageid;

public class Test8_1_SRT {
	
//	static LSLanguageid instant;	
	static String inputFilePath = "/home/chanwit/Documents/LSLanguageIDProject/A.Pre-Study/srt_file/35f609a9-22f7-4b40-88c7-c8588ace8b44_q1062797_FT_2398_TheR_tag.srt";
//	static String host = null;
//	static String port = null;
	
	public static void main(String[] args) throws Exception {
		
		//init
		LSLanguageid lsLangId = new LSLanguageid();
	//	instant = lsLangId.Init(host, port);
		
		//call 8 GetLanguage id from SRT file
		String result = lsLangId.GetLanguageIDSRTFile(inputFilePath, "cld3");
		
		System.out.println("result: "+result);
		
	}

}
