package service;

import java.io.File;
import java.util.regex.Pattern;


import util.ProcessUtilLanguageid;
import util.ReadPropLanguageid;
import util.constantLanguageid;
import util.CommonLanguageid;

public class ServiceLanguageidImp implements ServiceLanguageid{
	
	private ReadPropLanguageid rp= null;
	private CommonLanguageid common;
	
	//ProcessUtil oProcess = new ProcessUtil(getResourcesPath(),mode,getLogPath(),app);
	ProcessUtilLanguageid oPrecess;
	
	public ServiceLanguageidImp() {
		common = new CommonLanguageid();
	}
	
	public void propSettingService(String filePath) {
		rp = new ReadPropLanguageid(filePath);
	}

	public String LanguageID(String id, String input, String mode, boolean inputFromFileFlag) throws Exception {
		
		common = new CommonLanguageid();
		
		String output = null;
		
		if(mode == null) {
			mode = "polyglot";
		}else {
			if(mode.equals("cld3")) {
				mode = "cld3";
			}
//			else if(mode.equals("cld2")){
//				mode = "cld2";
//			}
			else {
				mode = "polyglot";
			}
		}
		
	
		input = input.replace("\r\n", "\n").replace("\r", "\n");
		input = common.prepareInput(input);
		
		// encrypt blank line
		input = common.encryptBlankLine(input);
		
		String resourcepath = rp.getProp(constantLanguageid.RESULT_PATH);
		oPrecess = new ProcessUtilLanguageid(resourcepath, mode, rp);
		
		output = oPrecess.detectResultFromString(input, mode, inputFromFileFlag);
		
		// Get bat path	
		return output;
	}

	public String LanguageidFromFileForCLD2(String inputFilePath) throws Exception {
		//Preapre Output
		String output = null;
		
		//CLD2
		String mode = "polyglot";
		
		//Get Result Path
		String resourcepath = rp.getProp(constantLanguageid.RESULT_PATH);
		oPrecess = new ProcessUtilLanguageid(resourcepath, mode, rp);
		output = oPrecess.detectResultFromFileCLD2Only(inputFilePath, mode);
		
		return output;
	}
	
	

}
