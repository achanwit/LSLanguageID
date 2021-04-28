package com.omniscien.lslanguageid.util;

import java.io.File;


public class constantLanguageid {
	
	public static final String MEDIA_TYPE = "application/json; charset=utf-8";
	
	public static final String PREFIX_INPUT = "Input: ";
	
	public static final String STRING_CODE = "code";
	
	public static final String STRING_UNKNOW = "unknown";
	
	public static final String STRING_UN = "un";
	
	public static final String STRING_CONFIDENT = "confidence";
	
	public static final String STRING_0 = "0";
	
	public static final String S_PATTERN = "Language(?<no>[ ]+[0-9]):[ ]+name:(.*)code:[ ]+(?<code>[a-zA-Z\\_]+)[ ]+confidence:[ ]+(?<confidence>[0-9\\.]+)[ ]+read[ ]+bytes:[ ]+(?<bytes>[0-9]+)";
	
	public static final String HOST = "localhost";
	
	public static final String PORT = "8080";
	
	public static final String CONTENT_TYPE = "Content-Type";
	
	public static final String APPLICATION_JSON = "application/json";
	
	public static final String HTTP = "http://";
	
	public static final String COLON = ":";
	
	public static final String PERCENTAGE_SYMBOL = "%";
	
	public static final String EMPTY_STRING = "";
	
	public static final String LANGUAGE_LEVEL_PATH_1 = "/LSLanguageIDService";
	
	public static final String LANGUAGE_LEVEL_PATH_2 = "/LanguageID.jsp";
	
	public static final String START_RECEIVE_PARAM = "?";
	
	public static final String PARAM_ID = "id=";
	
	public static final String PARAM_INPUT = "&input=";
	
	public static final String PARAM_MODE = "&mode=";
	
	public static final String NEW_LINE_RN = "\r\n";
	
	public static final String NEW_LINE_N = "\n";
	
	public static final String NEW_LINE_R = "\r";
	
	public static final String UTF_8 = "UTF-8";
	
	public static final String CLAW_BRACKET_OPEN = "[";
	
	public static final String CLAW_BRACKET_CLOASE = "]";
	
	public static final String CLD2_MODE = "cld2";
	
	public static final int MAXIMUN_INPUT =50000;
	
	public static final String ERROR_MSG_MAIXMUN_INPUT = "Status: 400 bad_request [Request header is too large because the input length is larger than the maximum acceptable value. Please reduce input to less than 1948 characters.]";
	
	public static final String REPLACE_ERROR_NO = ","+"\""+"errorno"+"\"";
	
	public static final String DETECT_UNKNOW = "\""+":"+"\""+"-1"+"\""+","+"\""+"enddate";
	
	public static final String UNKNOW_RESULT = "UNKNOWN:0.00%";
	
	public static final String TAB = "\t";
	
	public static final String DOUBLE_QUOTE = "\"";
	
	public static final String CONFIG_PATH_CLEANBADCHARS = "CONFIG_PATH_CLEANBADCHARS";
	
	public static final String CLEAN_BAD_CHAR_CONFIG = "CLEAN_BAD_CHAR_CONFIG";
	
	public static final String CONFIG_NUM_PUNC = "CONFIG_NUM_PUNC";
	
	public static final String RESULT_PATH = "RESULT_PATH";
	
	public static final String TEMP_PATH = "TEMP_PATH";
	
	public static final String DEBUGPATH = "DEBUGPATH"; 
	
	public static final int MAX_LINE = 20001;
	
	public static final int MAX_LANGTH = 400000;
	
	public static final String DECIMAL_FORMAT_TWO_DIGIT = "0.00";
	
	public static final String LANGUAGE_ID_LOAD_KEY = "AOLanguageIDLoaded";
}
