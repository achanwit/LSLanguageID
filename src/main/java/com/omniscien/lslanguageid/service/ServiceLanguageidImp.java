package com.omniscien.lslanguageid.service;

import java.io.File;
import java.util.regex.Pattern;

import com.omniscien.lslanguageid.model.LanguageidModel;
import com.omniscien.lslanguageid.model.LanguageidRespond;
import com.omniscien.lslanguageid.util.CommonLanguageid;
import com.omniscien.lslanguageid.util.ProcessUtilLanguageid;
import com.omniscien.lslanguageid.util.ReadProp;
import com.omniscien.lslanguageid.util.Constant;

public class ServiceLanguageidImp implements ServiceLanguageid{
	
	private ReadProp rp= null;
	private CommonLanguageid common;
	
	//ProcessUtil oProcess = new ProcessUtil(getResourcesPath(),mode,getLogPath(),app);
	ProcessUtilLanguageid oPrecess;
	
	public ServiceLanguageidImp() {
		common = new CommonLanguageid();
	}
	
	public void propSettingService(String filePath) {
		rp = new ReadProp(filePath);
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
		
		String resourcepath = rp.getProp(Constant.RESULT_PATH);
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
		String resourcepath = rp.getProp(Constant.RESULT_PATH);
		oPrecess = new ProcessUtilLanguageid(resourcepath, mode, rp);
		output = oPrecess.detectResultFromFileCLD2Only(inputFilePath, mode);
		
		return output;
	}

	public String LanguageIDWFS(String id, String input, String mode, boolean GetResultFlag) {
		common = new CommonLanguageid();
		
		com.omniscien.lslanguageid.model.LanguageidModel languageidModel = new LanguageidModel();
		
		String output = null;
		
		if(mode == null) {
			mode = "polyglot";
		}else {
			if(mode.equals("cld3")) {
				mode = "cld3";
			}
			else {
				mode = "polyglot";
			}
		}
		
	
		input = input.replace("\r\n", "\n").replace("\r", "\n");
		input = common.prepareInput(input);
		
		// encrypt blank line
		input = common.encryptBlankLine(input);
		
		String resourcepath = rp.getProp(Constant.RESULT_PATH);
		try {
			oPrecess = new ProcessUtilLanguageid(resourcepath, mode, rp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		languageidModel = oPrecess.detectResultFromStringWFS(input, mode, languageidModel);
		
		output = getResultFromModelToJSONString(languageidModel, GetResultFlag); 
		
		// Get bat path	
		return output;
	}

	private String getResultFromModelToJSONString(LanguageidModel languageidModel, boolean GetResultFlag) {
		String output = null;
		String result = null;
		String errorStatus = null;
		String dominantlanguage = null;
		String dominantLangPercent = null;
		String secondarylanguage = null;
		String secondaryLangPercent = null;
		
	
		errorStatus = languageidModel.getErrortext();
		dominantlanguage = languageidModel.getDominantlanguage();
		dominantLangPercent = languageidModel.getDominantLangPercent();
		secondarylanguage = languageidModel.getSecondarylanguage();
		secondaryLangPercent = languageidModel.getSecondaryLangPercent();
		
		if(dominantlanguage.equals("UN") && 
				dominantLangPercent.equals("") &&
				secondarylanguage.equals("") &&
				secondaryLangPercent.equals("")
				) {
			output = "{\"errorstatus\":\"yes\",\"dominantlanguage\":\"UN\",\"dominantlanguagepercent\":\"\",\"secondarylanguage\":\"\",\"secondarylangpercent\":\"\"}\n";
					
		}else {
			if(GetResultFlag) {
				result = languageidModel.getResult();
				output = "{\"detail\":\""+result+"\",\"dominantlanguage\":\""+dominantlanguage+"\",\"dominantlanguagepercent\":\""+dominantLangPercent+"\",\"secondarylanguage\":\""+secondarylanguage+"\",\"secondarylangpercent\":\""+secondaryLangPercent+"\",\"errorstatus\":\""+errorStatus+"\"}";
				
			}else {
				output = "{\"dominantlanguage\":\""+dominantlanguage+"\",\"dominantlanguagepercent\":\""+dominantLangPercent+"\",\"secondarylanguage\":\""+secondarylanguage+"\",\"secondarylangpercent\":\""+secondaryLangPercent+"\",\"errorstatus\":\""+errorStatus+"\"}";
				
			}
		}
		
		
		
		return output;
		
	}
	
	public String LanguageidFromFileForWFS(String inputFilePath, boolean getResultFlag) throws Exception {
		//Preapre Output
		
		com.omniscien.lslanguageid.model.LanguageidModel languageModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		String output = null;
		
		//CLD2
		String mode = "polyglot";
		
		//Get Result Path
		String resourcepath = rp.getProp(Constant.RESULT_PATH);
		oPrecess = new ProcessUtilLanguageid(resourcepath, mode, rp);
		languageModel = oPrecess.detectResultFromFileForWFS(inputFilePath, mode, languageModel);
		
		output = getResultFromModelToJSONString(languageModel, getResultFlag);
		
		return output;
	}
	
	

}
