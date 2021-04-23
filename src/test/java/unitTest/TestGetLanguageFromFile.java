package unitTest;

import com.omniscien.lslanguageid.process.LSLanguageid;

public class TestGetLanguageFromFile {

	public TestGetLanguageFromFile() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		LSLanguageid lg = new LSLanguageid();
		String result = lg.GetLanguageIDGeneralFromFile("/home/chanwit/Documents/01_HMRC_WorkFlow/Input/Word/input_en.doc.txt");
		
		System.out.println("Result: "+result);

	}

}
