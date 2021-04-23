package unitTest;

import com.omniscien.lslanguageid.process.LSLanguageid;

public class TestLangidString {

	public TestLangidString() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String result ="";
		LSLanguageid lsLangId = new LSLanguageid();
		lsLangId.propertiesSetting("LanguageidConfig.properties");
		String inputStr = "Vestibulum neque massa, scelerisque sit amet ligula eu, congue molestie mi.";
		
		try {
			 result = lsLangId.GetLanguageIDGeneral(inputStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Result: "+result);

	}

}
