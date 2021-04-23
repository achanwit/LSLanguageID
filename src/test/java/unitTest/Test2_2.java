package unitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniscien.lslanguageid.process.LSLanguageid;

public class Test2_2 {
	
	static String host = "172.17.105.217";
	static String port = "7085";	
	static LSLanguageid instant;
	
	static String id = "01";
	static String input = "日本語テスト";
	static String option = "TEST";
	
	public static void main(String[] args) throws Exception {

		
		LSLanguageid lsLangId = new LSLanguageid();
		
		System.out.println("----------Start Test----------");
		
//		instant = lsLangId.Init(host, port);
		
		String result = lsLangId.GetLanguageIDGeneral(id, input, option);
		
		ObjectMapper mapper = new ObjectMapper();
	    try {
			JsonNode actualObj = mapper.readTree(result);
			System.out.println(actualObj.get("result").asText());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		System.out.println("result: "+result);

		System.out.println("----------End Test----------");
		
	}

}
