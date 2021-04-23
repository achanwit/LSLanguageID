package unitTest;

import LSLanguageid.LSLanguageid;

public class TestLanguangeidNER {

	public TestLanguangeidNER() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String inputPath = "/home/chanwit/Documents/01_HMRC_WorkFlow/Test/output/etc.html.extract";
		LSLanguageid ls = new LSLanguageid();
		String output = ls.GetLanguageIDGeneralFromFile(inputPath);
		
		System.out.println("Output: "+output);

	}

}
