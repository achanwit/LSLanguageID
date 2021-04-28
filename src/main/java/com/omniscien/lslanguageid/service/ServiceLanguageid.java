package com.omniscien.lslanguageid.service;

import com.omniscien.lslanguageid.model.LanguageidModel;

public interface ServiceLanguageid {
	
	/*** Service for input String
	 * @throws Exception ***/
	public String LanguageID(String id, String input, String mode, boolean inputFromFileFlag) throws Exception;
	
	/*** Service for input File Path 
	 * @throws Exception ***/
	public String LanguageidFromFileForCLD2(String inputFilePath) throws Exception;
	
	public String LanguageIDWFS(String id, String input, String mode, boolean GetResultFlag);
	

}
