package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropLanguageid {
	
	private String propFilepath = "";
	InputStream inputStream;

	public ReadPropLanguageid() {
		// TODO Auto-generated constructor stub
	}
	
	public ReadPropLanguageid(String propFilepath) {
		this.propFilepath = propFilepath;
	}
	
public String getProp(String key)  {
		String result = "";
		
		String propFileName = propFilepath;
		try {
		Properties prop = new Properties();

//		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		inputStream = new FileInputStream(propFileName);
		
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		result = prop.getProperty(key);

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
		
		return result;
		
	}

}
