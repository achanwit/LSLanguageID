package com.omniscien.lslanguageid.util;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import com.omniscien.lslanguageid.model.LanguageIDPrepertiesModel;

public class ReadProp {
	
	private String propFilepath = "";
	private FileReader reader = null;
	private LanguageIDPrepertiesModel propertiesModel = null;
	private JSONParser jsonParser = null;
	private Gson gson = null;
	

	public ReadProp() {
		
	}
	
	public ReadProp(String propFilepath) {
		this.propFilepath = propFilepath;
		this.jsonParser = new JSONParser();
		this.gson = new Gson();
		
	}
	
	public String getProp(String key)  {
		if(jsonParser == null ) {
			jsonParser = new JSONParser();
		}

		String result = "";
		
		
		if(propertiesModel == null) {
		try {
			String propFileName = propFilepath;
			reader = new FileReader(propFileName);

			if (reader != null) {
				// Read JSON file
				String propertiesContentStr = jsonParser.parse(reader).toString();
				if(gson == null) {
					gson = new Gson();
				}
				propertiesModel = gson.fromJson(propertiesContentStr, LanguageIDPrepertiesModel.class);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
		
		result = getPropettiesValue(key);
		

		
		
		return result;
		
	}

private String getPropettiesValue(String key) {
	String result = "";
	if(key.equals(Constant.CONFIG_PATH_CLEANBADCHARS)) {
		result = propertiesModel.getCONFIG_PATH_CLEANBADCHARS();
	}else if(key.equals(Constant.CLEAN_BAD_CHAR_CONFIG)) {
		result = propertiesModel.getCLEAN_BAD_CHAR_CONFIG();
	}else if(key.equals(Constant.CONFIG_NUM_PUNC)) {
		result = propertiesModel.getCLEAN_BAD_CHAR_CONFIG();
	}else if(key.equals(Constant.RESULT_PATH)) {
		result = propertiesModel.getRESULT_PATH();
	}else if(key.equals(Constant.TEMP_PATH)) {
		result = propertiesModel.getTEMP_PATH();
	}else if(key.equals(Constant.DEBUGPATH)) {
		result = propertiesModel.getDEBUGPATH();
	}else if(key.equals(Constant.PROCESS_TEMP_PATH)) {
		result = propertiesModel.getPROCESS_TEMP_PATH();
	}else if(key.equals(Constant.LOG_PATH)) {
		result = propertiesModel.getLOG_PATH();
	}else if(key.equals(Constant.LOG_4J)) {
		result = propertiesModel.getLOG_4J();
	}
	
	return result;
}

}
