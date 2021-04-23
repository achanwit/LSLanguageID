package unitTest;

import LSLanguageid.LSLanguageid;

public class A1_TestLangIDParagraph {

	public A1_TestLangIDParagraph() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		LSLanguageid lsLangId = new LSLanguageid();
		String idStr = "01";
		String inputFilrPath = "/home/chanwit/Documents/01_HMRC_WorkFlow/Output/input_en.doc.txt";
		String result = lsLangId.GetLanguageIDGeneralFromFile(inputFilrPath);
		System.out.println("Result: "+result);

	}

}
