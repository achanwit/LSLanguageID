package unitTest;

import LSLanguageid.LSLanguageid;

public class TestLangidString {

	public TestLangidString() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String result ="";
		LSLanguageid lsLangId = new LSLanguageid();
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
