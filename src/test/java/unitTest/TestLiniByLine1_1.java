package unitTest;

import java.io.IOException;

import com.omniscien.lslanguageid.process.LSLanguageid;

public class TestLiniByLine1_1 {
	
//	static String host = null;
//	static String port = null;
	
	static LSLanguageid instant;
	
//	static String input = "/home/chanwit/Documents/LSLanguageIDProject/H.SpecialTest/fr.txt";
	static String input = "/home/chanwit/workspace-LSTokenizeLocale/TestCallLSTools/1_input_test_Languuageid/InputTest.txt";
//	static String output = "/home/chanwit/Documents/LSLanguageIDProject/H.SpecialTest/fr_out.txt";
	static String output = "/home/chanwit/Documents/LSLanguageIDProject/D.Test/Input1Gig/InputTest-out.txt";		

	public static void main(String[] args) throws Exception {
		
		LSLanguageid langID = new LSLanguageid();
		//instant = langID.Init(host, port);
//		langID.propertiesSetting("/home/chanwit/Documents/LSTokenize/D.Test/TestPropertieseSetting/LanguageidConfig.properties");
		
		try {
			String result = langID.GetLanguageIDLineByLine(input, output);
			System.out.println("result: "+result);
			
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

}
