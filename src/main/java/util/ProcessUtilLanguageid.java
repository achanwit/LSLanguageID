package util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;



public class ProcessUtilLanguageid {
	
	ReadPropLanguageid rp = null;
	//
	DecimalFormat df = new DecimalFormat(constantLanguageid.DECIMAL_FORMAT_TWO_DIGIT); 
	String resourcepath;
	private String mode;
	
	private String debugPath = null;// = rp.getProp(constantLanguageid.DEBUGPATH);
	ServletContext app;
	String languageIDLoadedKey = constantLanguageid.LANGUAGE_ID_LOAD_KEY;
	RemoveBadChars oRemoveBadChars = null;
	CommonLanguageid oCommon = null;
	List<Pattern> lPatternNumPunc = new ArrayList<Pattern>();;
	public ProcessUtilLanguageid() {
		
	}
	public ProcessUtilLanguageid(String _resourcespath,String _mode, ReadPropLanguageid rp) throws Exception {
		this.rp = rp;
		debugPath = rp.getProp(constantLanguageid.DEBUGPATH);
		
		//app = _app;
		resourcepath = _resourcespath;
		mode = _mode;
		//debugPath = _debugPath;
		if (app != null && app.getAttribute(languageIDLoadedKey) != null)
		{
			//DO NOTHING
		}
		else
		{
			//if (!mode.equals("polyglot"))
			//	DetectorFactory.loadProfile(resourcepath + "/languageid/profiles");

			if (app !=null)
				app.setAttribute(languageIDLoadedKey,true);
			

		}

		oRemoveBadChars = new RemoveBadChars();
		oCommon = new CommonLanguageid();
		lPatternNumPunc = new ArrayList<Pattern>();
		
		//TODO: get config path 
		String configNumPunc = _resourcespath +constantLanguageid.CONFIG_NUM_PUNC;
		String numPuncStr = Read(configNumPunc);
		if (numPuncStr.length() > 0) {
			String[] numPuncList = numPuncStr.split(constantLanguageid.NEW_LINE_N);
			for (String numPunc : numPuncList) {
				try {
					Pattern patternNumberPunc = Pattern.compile(numPunc);
					lPatternNumPunc.add(patternNumberPunc);
				}catch(Exception e) {
				}
			}
			
		}
	}
	private boolean isNumPunc(String text) {
		if (lPatternNumPunc != null && lPatternNumPunc.size() > 0) {
			text = text.trim();
			for (Pattern p : lPatternNumPunc) {
				if (p.matcher(text).matches()) {
					return true;
				}
			}
		}
		
		return false;
	}
	private String Read(String FilePath) throws Exception {
		if (new File(FilePath).isFile()) {
			return FileUtils.readFileToString(new java.io.File(FilePath), constantLanguageid.UTF_8);
		}else {
			return "";
		}
	}
	private String RemoveBadChars(String input, String sConfigFilePath) {
		
		try {
			//read config file in haspmap *** please use HaspMap instead ArrayList
			String sConfigContent = Read(sConfigFilePath);
			//remove BOM *** please check "RemoveBOM" function first. Is it work?
			sConfigContent = oCommon.RemoveBOM(sConfigContent);
						
			//start file and replace
			HashMap<Integer, Object> hmRules = oRemoveBadChars.GetRuleList(sConfigContent);
			
			// search and replace text
			String cleanText = oRemoveBadChars.ReplaceRules(input, hmRules);
			
			//write file
			return cleanText;
		} catch (Exception ex) {
			//ex.printStackTrace();
			//throw ex;
		}
		
		return input;
	}
	private String prepareInput(String input){
		String temp = input;

		temp = oCommon.RemoveBOM(temp);
		temp = temp.replace(constantLanguageid.NEW_LINE_RN, constantLanguageid.NEW_LINE_N).replace(constantLanguageid.NEW_LINE_R, constantLanguageid.NEW_LINE_N);

		//TODO: get config path 
		String configPath = rp.getProp(constantLanguageid.RESULT_PATH)+constantLanguageid.CLEAN_BAD_CHAR_CONFIG;
		temp = RemoveBadChars(temp, configPath);
		
		return temp;
	}
	
	public String detectResultFromFileCLD2Only(String inputFilePath,String mode) {
		String output = "";
		StringBuffer sb = new StringBuffer();
		boolean bError = false;
		String sPattern = constantLanguageid.S_PATTERN;
		Pattern patternLang = Pattern.compile(sPattern, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		
		
		//Prepare for Output
		List<String> inputList = new ArrayList();
		
		try {
	
			// Get Output of file
			String outputScript = callDetectorFromFileCLD2Only(inputFilePath);
			
			//System.out.println("outputScript: "+outputScript);
			
			//Spilt output to Array
			String[] arOutputScript = outputScript.split(constantLanguageid.NEW_LINE_N);
			for (int i = 0; i < arOutputScript.length; i++) {
				
				if (arOutputScript[i].startsWith(constantLanguageid.PREFIX_INPUT)){
					
					String chunk = arOutputScript[i];
					inputList.add(arOutputScript[i]);
					i++;if (i < arOutputScript.length) chunk += constantLanguageid.NEW_LINE_N + arOutputScript[i];
					
					i++;if (i < arOutputScript.length)chunk += constantLanguageid.NEW_LINE_N + arOutputScript[i];
					i++;if (i < arOutputScript.length)chunk += constantLanguageid.NEW_LINE_N + arOutputScript[i];
					i++;if (i < arOutputScript.length)chunk += constantLanguageid.NEW_LINE_N + arOutputScript[i];
					i++;if (i < arOutputScript.length)chunk += constantLanguageid.NEW_LINE_N + arOutputScript[i];
					
					String lang = "";
					
					Matcher m = patternLang.matcher(chunk);
					String code = constantLanguageid.EMPTY_STRING,confidence=constantLanguageid.EMPTY_STRING,bytes=constantLanguageid.EMPTY_STRING;
					
					while (m.find()) {
						code = m.group(constantLanguageid.STRING_CODE) == null ? constantLanguageid.STRING_UNKNOW : m.group(constantLanguageid.STRING_CODE).toString().trim();
						if (code.equals(constantLanguageid.STRING_UN) || code.equals(constantLanguageid.EMPTY_STRING)) code=constantLanguageid.STRING_UNKNOW;
						confidence = m.group(constantLanguageid.STRING_CONFIDENT) == null ? constantLanguageid.STRING_0 : m.group(constantLanguageid.STRING_CONFIDENT).toString().trim();
						
						break;
					}
					
					lang = code.toUpperCase()+constantLanguageid.COLON+(df.format(Double.parseDouble(confidence)))+constantLanguageid.PERCENTAGE_SYMBOL;
					if (output.length() == 0) output = lang; else output += "\n" + lang;
					
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String[] arOutput = (output).split(constantLanguageid.NEW_LINE_N);
		for (int i = 0; i < arOutput.length; i++) {
			//String line = arLine[i];
			String line = inputList.get(i);
			if (bError)
			{
				if (i == 0)
					sb.append("unknown:0%\t"+line);
				else
					sb.append("\nunknown:0%\t"+line);
			}
			else
			{
				if (line.trim().startsWith("_AOBLANKLINEKAO_"))
		
				{
					if (i == 0)
						sb.append("");
					else
						sb.append("\n");
				}
				else
				{
					if (isNumPunc(line)) {
						String lang = "NP:"+(df.format(Double.parseDouble("100")))+"%";
						if (i == 0)
							sb.append(lang+"\t"+line);
						else
							sb.append("\n"+lang+"\t"+line);
					}else {
						String lang = arOutput[i];
						if (i == 0)
							sb.append(lang+"\t"+line.replace("Input: ", ""));
						else
							sb.append("\n"+lang+"\t"+line.replace("Input: ", ""));
					}
				}
			}
		}
		 GetDominantLanguage(sb.toString());
			return sb.toString();
		
		
		
	}
	
	public String detectResultFromString(String input,String mode, boolean inputFromFileFlag)
	{		
		Pattern patternNumberPunc = Pattern.compile("(^([0-9 !\"\\#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]+)$)");
		String sPattern = "Language(?<no>[ ]+[0-9]):[ ]+name:(.*)code:[ ]+(?<code>[a-zA-Z\\_]+)[ ]+confidence:[ ]+(?<confidence>[0-9\\.]+)[ ]+read[ ]+bytes:[ ]+(?<bytes>[0-9]+)";
		Pattern patternLang = Pattern.compile(sPattern, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		
		StringBuffer sb = new StringBuffer();
		//input = input.replace("\r\n", "\n").replace("\r", "\n");
		if(!mode.equals("cld2")) {
			input = prepareInput(input);
		}
		// encrypt blank line
		input = encryptBlankLine(input);
		
		 if (mode.equals("polyglot") && inputFromFileFlag == false)
		{
			String output = "";
			boolean bError = false;
			try {
				
				int chunkcount = 500;
				int linecount = 0;
				StringBuilder sbChunk = new StringBuilder("");
				String[] lines = input.split("\n");
				for (String line : lines) {
					
					sbChunk.append(line);
					linecount++;
					if (linecount < chunkcount) {
						sbChunk.append("\n");
					}else if (linecount >= chunkcount) {

						/**** Detect for CLD2 ********/
						long beginTime = System.currentTimeMillis();
						String outputScript = callDetector(sbChunk.toString());
						long endTime = System.currentTimeMillis();
					//	System.out.println("Used time for CLD2: " + (endTime - beginTime) + " millisec");
						String[] arOutputScript = outputScript.split("\n");
						for (int i = 0; i < arOutputScript.length; i++) {
							if (arOutputScript[i].startsWith("Input: "))
							{
								String chunk = arOutputScript[i];
								//2020-05-02 Add check index is exist
								i++;if (i < arOutputScript.length) chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								
								String lang = "";
//								if (option.length() > 0)
//								{
//									Matcher m = patternLang.matcher(chunk);
//									
//									String no="",code = "",confidence="",bytes="";
//									while (m.find()) {
//										no = m.group("no") == null ? "" : m.group("no").toString().trim();
//										code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
//										if (code.equals("un") || code.equals("")) code="unknown";
//										confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
//
//										if (lang.length() == 0) 
//											lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
//										else if (!code.endsWith("unknown")) 
//											lang += "|" + code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
//									}
//									
//									if (output.length() == 0) output = lang; else output += "\n" + lang;
//								}	
								//else
								//{
									Matcher m = patternLang.matcher(chunk);
									
									String code = "",confidence="",bytes="";
									while (m.find()) {
										code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
										if (code.equals("un") || code.equals("")) code="unknown";
										confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
										
										break;
									}
									
									lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
									if (output.length() == 0) output = lang; else output += "\n" + lang;
								//}
							}
						}
						
						
						linecount = 0;
						sbChunk = new StringBuilder("");
					}
				}
				
				if (sbChunk.toString().length() > 0) {
					//long beginTime = System.currentTimeMillis();
					String outputScript = callDetector(sbChunk.toString());
					//long endTime = System.currentTimeMillis();
					//System.out.println("Used time for CLD2: " + (endTime - beginTime) + " millisec");
					String[] arOutputScript = outputScript.split("\n");
					for (int i = 0; i < arOutputScript.length; i++) {
						if (arOutputScript[i].startsWith("Input: "))
						{
							String chunk = arOutputScript[i];
							//2020-05-02 Add check index is exist
							i++;if (i < arOutputScript.length) chunk += "\n" + arOutputScript[i];
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							
							String lang = "";
//							if (option.length() > 0)
//							{
//								Matcher m = patternLang.matcher(chunk);
//								
//								String no="",code = "",confidence="",bytes="";
//								while (m.find()) {
//									no = m.group("no") == null ? "" : m.group("no").toString().trim();
//									code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
//									if (code.equals("un") || code.equals("")) code="unknown";
//									confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
//
//									if (lang.length() == 0) 
//										lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
//									else if (!code.endsWith("unknown")) 
//										lang += "|" + code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
//								}
//								
//								if (output.length() == 0) output = lang; else output += "\n" + lang;
//							}	
//							else
//							{
								Matcher m = patternLang.matcher(chunk);
								
								String code = "",confidence="",bytes="";
								while (m.find()) {
									code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
									if (code.equals("un") || code.equals("")) code="unknown";
									confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
									
									break;
								}
								
								lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
								if (output.length() == 0) output = lang; else output += "\n" + lang;
							//}
						}
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				bError = true;
			}

			String[] arLine = (input).split("\n");
			String[] arOutput = (output).split("\n");
			for (int i = 0; i < arLine.length; i++) {
				String line = arLine[i];
				if (bError)
				{
					if (i == 0)
						sb.append("unknown:0%\t"+line);
					else
						sb.append("\nunknown:0%\t"+line);
				}
				else
				{
					if (line.trim().startsWith("_AOBLANKLINEKAO_"))
					//if (line.trim().length() == 0)
					{
						if (i == 0)
							sb.append("");
						else
							sb.append("\n");
					}
					else
					{
						if (isNumPunc(line)) {
							String lang = "NP:"+(df.format(Double.parseDouble("100")))+"%";
							if (i == 0)
								sb.append(lang+"\t"+line);
							else
								sb.append("\n"+lang+"\t"+line);
						}else {
							String lang = arOutput[i];
							if (i == 0)
								sb.append(lang+"\t"+line);
							else
								sb.append("\n"+lang+"\t"+line);
						}
					}
				}
			}
			
		}else if(mode.equals("cld2") && inputFromFileFlag == true) {
			
			resourcepath = constantLanguageid.RESULT_PATH;
			
			String output = "";
			boolean bError = false;
			List<String> inputList = new ArrayList();
			try {
				
				int chunkcount = 500;
				int linecount = 0;
				StringBuilder sbChunk = new StringBuilder("");
				String[] lines = input.split("\n");
				for (String line : lines) {
					
					sbChunk.append(line);
					linecount++;
					if (linecount < chunkcount) {
						sbChunk.append("\n");
					}else if (linecount >= chunkcount) {

						/**** Detect for CLD2 ********/
						long beginTime = System.currentTimeMillis();
						String outputScript = callDetectorFromFileCLD2Only(input);
						long endTime = System.currentTimeMillis();
					//	System.out.println("Used time for CLD2: " + (endTime - beginTime) + " millisec");
						String[] arOutputScript = outputScript.split("\n");
						for (int i = 0; i < arOutputScript.length; i++) {
							if (arOutputScript[i].startsWith("Input: "))
							{
								String chunk = arOutputScript[i];
								//2020-05-02 Add check index is exist
								i++;if (i < arOutputScript.length) chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
								
								String lang = "";

									Matcher m = patternLang.matcher(chunk);
									
									String code = "",confidence="",bytes="";
									while (m.find()) {
										code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
										if (code.equals("un") || code.equals("")) code="unknown";
										confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
										
										break;
									}
									
									lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
									if (output.length() == 0) output = lang; else output += "\n" + lang;
								
							}
						}
						
						
						linecount = 0;
						sbChunk = new StringBuilder("");
					}
				}
				
				if (sbChunk.toString().length() > 0) {
					
					String outputScript = callDetectorFromFileCLD2Only(input);
					
					
					String[] arOutputScript = outputScript.split("\n");
					for (int i = 0; i < arOutputScript.length; i++) {
						if (arOutputScript[i].startsWith("Input: "))
						{
							String chunk = arOutputScript[i];
							inputList.add(arOutputScript[i]);
							i++;if (i < arOutputScript.length) chunk += "\n" + arOutputScript[i];
							
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
							
							String lang = "";

								Matcher m = patternLang.matcher(chunk);
								
								String code = "",confidence="",bytes="";
								while (m.find()) {
									code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
									if (code.equals("un") || code.equals("")) code="unknown";
									confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
									
									break;
								}
								
								lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
								if (output.length() == 0) output = lang; else output += "\n" + lang;
							
						}
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				bError = true;
			}

			//String[] arLine = (input).split("\n");
			String[] arOutput = (output).split("\n");
			for (int i = 0; i < arOutput.length; i++) {
				//String line = arLine[i];
				String line = inputList.get(i);
				if (bError)
				{
					if (i == 0)
						sb.append("unknown:0%\t"+line);
					else
						sb.append("\nunknown:0%\t"+line);
				}
				else
				{
					if (line.trim().startsWith("_AOBLANKLINEKAO_"))
			
					{
						if (i == 0)
							sb.append("");
						else
							sb.append("\n");
					}
					else
					{
						if (isNumPunc(line)) {
							String lang = "NP:"+(df.format(Double.parseDouble("100")))+"%";
							if (i == 0)
								sb.append(lang+"\t"+line);
							else
								sb.append("\n"+lang+"\t"+line);
						}else {
							String lang = arOutput[i];
							if (i == 0)
								sb.append(lang+"\t"+line);
							else
								sb.append("\n"+lang+"\t"+line.replace("Input: ", ""));
						}
					}
				}
			}
			
		}
//		 else if (mode.contentEquals("polyglot-notuse")) {
//			String output = "";
//			boolean bError = false;
//			String[] lines = new String[] {};
//			try {
//				
//				lines = input.split("\n");
//				for (int l = 0; l < lines.length; l++) {
//					String line = lines[l].trim();
//					lines[l] = line;
//					
//					if (line.length() == 0) {
//						if (l == 0) output = ""; else output += "\n" + "";
//					}else {
//
//						if (patternNumberPunc.matcher(line).matches()) {
//							String lang = "NP:"+(df.format(Double.parseDouble("100")))+"%";
//							if (output.length() == 0) output = lang; else output += "\n" + lang;
//							continue;
//						}
//						
//						String outputScript = callDetector_notuse(line);
//						String[] arOutputScript = outputScript.split("\n");
//
//						int i=0;
//						if (arOutputScript[i].startsWith("Input: "))
//						{
//							String chunk = arOutputScript[i];
//							//2020-05-02 Add check index is exist
//							i++;if (i < arOutputScript.length) chunk += "\n" + arOutputScript[i];
//							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
//							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
//							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
//							i++;if (i < arOutputScript.length)chunk += "\n" + arOutputScript[i];
//							
//							/*				 
//							Input: China (simplified Chinese: 中国; traditional Chinese: 中國),
//
//							Language: name: English code: en confidence: 71.0 read bytes: 887
//							Prediction is reliable: True
//							Language 1: name: English code: en confidence: 71.0 read bytes: 887
//							Language 2: name: Chinese code: zh_Hant confidence: 11.0 read bytes: 1755
//							Language 3: name: un code: un confidence: 0.0 read bytes: 0
//							
//							*/				
//							
//							String lang = "";
//							if (option.length() > 0)
//							{
//								String pattern = "Language(?<no>[ ]+[0-9]):[ ]+name:(.*)code:[ ]+(?<code>[a-zA-Z\\_]+)[ ]+confidence:[ ]+(?<confidence>[0-9\\.]+)[ ]+read[ ]+bytes:[ ]+(?<bytes>[0-9]+)";
//								Pattern p = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
//								Matcher m = p.matcher(chunk);
//								
//								String no="",code = "",confidence="",bytes="";
//								while (m.find()) {
//									no = m.group("no") == null ? "" : m.group("no").toString().trim();
//									code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
//									if (code.equals("un") || code.equals("")) code="unknown";
//									confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
//
//									if (lang.length() == 0) 
//										lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
//									else if (!code.endsWith("unknown")) 
//										lang += "|" + code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
//								}
//								
//								if (output.length() == 0) output = lang; else output += "\n" + lang;
//							}	
//							else
//							{
//								String pattern = "Language(?<no>[ ]+[0-9]):[ ]+name:(.*)code:[ ]+(?<code>[a-zA-Z\\_]+)[ ]+confidence:[ ]+(?<confidence>[0-9\\.]+)[ ]+read[ ]+bytes:[ ]+(?<bytes>[0-9]+)";
//								Pattern p = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
//								Matcher m = p.matcher(chunk);
//								
//								String code = "",confidence="",bytes="";
//								while (m.find()) {
//									code = m.group("code") == null ? "unknown" : m.group("code").toString().trim();
//									if (code.equals("un") || code.equals("")) code="unknown";
//									confidence = m.group("confidence") == null ? "0" : m.group("confidence").toString().trim();
//									
//									break;
//								}
//								
//								lang = code.toUpperCase()+":"+(df.format(Double.parseDouble(confidence)))+"%";
//								if (output.length() == 0) output = lang; else output += "\n" + lang;
//							}
//						}
//					}
//					
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				bError = true;
//			}
//
//			String[] arLine = (input).split("\n");
//			String[] arOutput = (output).split("\n");
//			for (int i = 0; i < arLine.length; i++) {
//				if (bError)
//				{
//					if (i == 0)
//						sb.append("unknown:0%\t"+arLine[i]);
//					else
//						sb.append("\nunknown:0%\t"+arLine[i]);
//				}
//				else
//				{
//					if (arLine[i].startsWith("_AOBLANKLINEKAO_"))
//					//if (arLine[i].trim().length() == 0)
//					{
//						if (i == 0)
//							sb.append("");
//						else
//							sb.append("\n");
//					}
//					else
//					{
//						if (i == 0)
//							sb.append(arOutput[i]+"\t"+arLine[i]);
//						else
//							sb.append("\n"+arOutput[i]+"\t"+arLine[i]);
//					}
//				}
//			}
//		}
		 else if (mode.equals("cld3")) {
			// System.out.println("CLD3");
			String output = "";
			boolean bError = false;
			String[] lines = new String[] {};
			try {

				DetectLanguage detectLang = null;
				try {
					detectLang = new DetectLanguage();
				} catch (java.lang.UnsatisfiedLinkError e) {
					e.printStackTrace();
					throw e;
				} catch (java.lang.Exception e) {
					e.printStackTrace();
					throw e;
				}

				List<DetectLanguage.LanguageResult> results = null;
				
				lines = input.split("\n");
				for (int i = 0; i < lines.length; i++) {
					String line = lines[i].trim();
					lines[i] = line;

					if (line.length() == 0) {
						
						if (i == 0) output = ""; else output += "\n" + "";
						
					}else {

						if (patternNumberPunc.matcher(line).matches()) {
							String lang = "NP:"+(df.format(Double.parseDouble("100")))+"%";
							if (output.length() == 0) output = lang; else output += "\n" + lang;
							continue;
						}
						
						String lang = "";
						/**** Detect for CLD3 ********/
						//long beginTime = System.currentTimeMillis();
						results = detectLang.find(line);
					//	long endTime = System.currentTimeMillis();
						//System.out.println("Used time for CLD3: " + (endTime - beginTime) + " millisec");
						if (results != null && results.size() > 0) {
							for (DetectLanguage.LanguageResult result : results) {
								/*
								 * if (lang.length() == 0) { lang = result.language.toUpperCase() + ":" +
								 * df.format(result.percent) + "%"; }else { lang += "|" +
								 * result.language.toUpperCase() + ":" + df.format(result.percent) + "%"; }
								 */
								lang = result.language.toUpperCase() + ":" + df.format(result.percent) + "%";
								break;
							}
						}else {
							lang = "UN:" + df.format(100.0) + "%";
						}
						
						if (i == 0) output = lang; else output += "\n" + lang;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				bError = true;
			}

			String[] arLine = (input).split("\n");
			String[] arOutput = (output).split("\n");
			for (int i = 0; i < arLine.length; i++) {
				if (bError) {
					if (i == 0)
						sb.append("unknown:0%\t" + arLine[i]);
					else
						sb.append("\nunknown:0%\t" + arLine[i]);
				} else {
					if (arLine[i].trim().length() == 0) {
						if (i == 0)
							sb.append("");
						else
							sb.append("\n");
					} else {
						if (i == 0)
							sb.append(arOutput[i] + "\t" + arLine[i]);
						else
							sb.append("\n" + arOutput[i] + "\t" + arLine[i]);
					}
				}
			}
		}
		else
		{
			String[] arLine = (input).split("\n");
			for (int i = 0; i < arLine.length; i++) {
				String lang ="";
				try {
					Detector detector = DetectorFactory.create();
					detector.append(arLine[i]);
//					if (option.length() == 0)
//					{
//						ArrayList<Language>prob = detector.getProbabilities();
//						for(Language language :prob){
//							if (lang.length() == 0)
//								lang = language.lang.toUpperCase()+":"+(df.format(language.prob*100))+"%";
//							else
//								lang += "|" + language.lang.toUpperCase()+":"+(df.format(language.prob*100))+"%";
//								
//						}
//					}
//					else
//					{
		
						String s = detector.detect();
						lang = s.toUpperCase()+":"+(df.format(100))+"%";
//					}
				} catch (LangDetectException e) {
					lang ="unknown:0%";
				}
				if (i == 0)
					sb.append(lang + "\t"+arLine[i]);
				else
					sb.append("\n" + lang+"\t"+arLine[i]);
			}
		}
//		GetDominantLanguage(sb.toString(),dominantLang,secondaryLang);
		 GetDominantLanguage(sb.toString());
		return sb.toString();
	}
	
//	public void GetDominantLanguage(String outputText , AtomicReference<String> dominantLang , AtomicReference<String> secondaryLang) 
	public void GetDominantLanguage(String outputText) 
	

	{
		AtomicReference<String> dominantLang = new AtomicReference<String>();
		AtomicReference<String> secondaryLang = new AtomicReference<String>();
		HashMap<String,Double> hashLang = new HashMap<String, Double>();
		
		int lineCount = 0;
		String[] arLine = outputText.split("\n");
		for (int i = 0; i < arLine.length; i++) {
			//count all language
			/*
			String[] arLangDetect = arLine[i].split("\t")[0].split("[|]");
			if (arLine[i].split("\t").length > 1)
			{
				for (int j = 0; j < arLangDetect.length; j++) {
					String[] arLang = arLangDetect[j].split(":");
					String sLang = "";
					double dPercent = Double.parseDouble(arLang[1].replace("%",""));
					if (arLang[0].length() > 2)
						sLang = arLang[0].substring(0, 2).toUpperCase();
					else 
						sLang = arLang[0].toUpperCase();
					
					if (!hashLang.containsKey(sLang))
						hashLang.put(sLang,dPercent);
					else
					{
						double dPercentSum = hashLang.get(sLang)+dPercent;
						hashLang.put(sLang,dPercentSum);
					}
				}
				lineCount++;
			}
			*/
			//count only first language
			String[] arLangDetect = arLine[i].split("\t")[0].split("[|]");
			if (arLine[i].split("\t").length > 1)
			{
				boolean isNP = false;
				for (int j = 0; j < 1; j++) {
					String[] arLang = arLangDetect[j].split(":");
					String sLang = "";
					double dPercent = 100.0;
					if (arLang[0].length() > 2)
						sLang = arLang[0].substring(0, 2).toUpperCase();
					else 
						sLang = arLang[0].toUpperCase();
					
					if (sLang.contentEquals("NP")) {
						isNP = true;
						break;
					}
					if (!hashLang.containsKey(sLang))
						hashLang.put(sLang,dPercent);
					else
					{
						double dPercentSum = hashLang.get(sLang)+dPercent;
						hashLang.put(sLang,dPercentSum);
					}
				}
				if (!isNP)
					lineCount++;
			}
		}	 
	    
		//Get Dominant Language
		double dMatch = 0;
		String sDominantLanguage = "";
		String sSecondaryLanguage = "";
		for (Iterator<Map.Entry<String, Double>> it =  hashLang.entrySet().iterator();
				it.hasNext();) {
			Map.Entry<String, Double> entry = it.next();
			String sKey = entry.getKey();
			double dValue = entry.getValue()/lineCount;
			if (dValue > dMatch)
			{
				dMatch = dValue;
				sDominantLanguage = sKey.toUpperCase();
			}
		}
		
		for (Iterator<Map.Entry<String, Double>> it =  hashLang.entrySet().iterator();
				it.hasNext();) {
			Map.Entry<String, Double> entry = it.next();
			String sKey = entry.getKey();
			if (!sKey.toUpperCase().equals(sDominantLanguage))
			{
				if (sSecondaryLanguage.length() > 0) 
					sSecondaryLanguage+="," + sKey;
				else 
					sSecondaryLanguage = sKey;
			}
		}
		
		if (sDominantLanguage.length() == 0) {
			sDominantLanguage = "UN";
		}
		secondaryLang.set(sSecondaryLanguage);
		dominantLang.set(sDominantLanguage);
		
	}
	
	private String callDetectorFromFileCLD2Only(String inputpath ) throws Exception {

		String output = null;
		//Get bat path
		String batFile = resourcepath + "languageid/detector.sh";
		
		//System.out.println("batFile: "+batFile);

		//Get temp path and temp file
		String sTempPath = debugPath + "temp"; 
		File fTemp = new File(sTempPath);
		if (!fTemp.exists()) fTemp.mkdirs();
		String id = UUID.randomUUID().toString();
		//String inputpath = sTempPath + "/" + id + ".in.txt";
		String outputpath = sTempPath + "/" + id + ".out.txt";
		
	//	System.out.println("outputpath: "+outputpath);

		
		Process p = null;
		
		try {

			// write input file
			//writeFileWithOutBom(inputpath, input);

			File dirBat = new File(resourcepath);
			
			String command = "bash " + batFile + " " + inputpath + " " + outputpath;
			
		//	System.out.println("command: "+command);
		//	System.out.println("resourcepath: "+resourcepath);

			p = Runtime.getRuntime().exec(command, null, dirBat);

			String sTimeout = "";
			Integer timeout = 0;// default 1 hour
			if (sTimeout != null && !sTimeout.equals(""))
				timeout = Integer.parseInt(sTimeout);
			int returnValue = 0;
			if (timeout <= 0) {
				returnValue = p.waitFor();
			} 
			else {
				long now = System.currentTimeMillis();
				long timeoutInMillis = timeout;
				long finish = now + timeoutInMillis;
				while (isAlive(p) && (System.currentTimeMillis() < finish)) {
//					Thread.sleep(10);
				}
				if (isAlive(p)) {
					throw new InterruptedException("Process timeout out after " + timeout + " milliseconds");
				}
				returnValue = p.exitValue();
			}

			p.destroy();

			File oFile = new File(outputpath);
			if (oFile.exists() == false)
				throw new Exception("Detector output file not found");
			output = readFile(outputpath);

		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
//				File oI = new File(inputpath);
//				oI.delete();
//				
				File oO = new File(outputpath);
				oO.delete();
				/**/

				if (p.isAlive()) {
					p.destroy();
				}
				p = null;

			} catch (Exception ex) {
			}
		}
		return output;
	}
	private String callDetector(String input) throws Exception {

		//Get bat path
		String batFile = resourcepath + "languageid/detector.sh";
		
		//Get temp path and temp file
		String sTempPath = debugPath + "temp"; 
		File fTemp = new File(sTempPath);
		if (!fTemp.exists()) fTemp.mkdirs();
		String id = UUID.randomUUID().toString();
		String inputpath = sTempPath + "/" + id + ".in.txt";
		String outputpath = sTempPath + "/" + id + ".out.txt";
		
		Process p = null;
		
		try {

			// encrypt blank line
			input = encryptBlankLine(input);

			// write input file
			writeFileWithOutBom(inputpath, input);

			File dirBat = new File(resourcepath);
			
			String command = "bash " + batFile + " " + inputpath + " " + outputpath;

			p = Runtime.getRuntime().exec(command, null, dirBat);

			String sTimeout = "";
			Integer timeout = 0;// default 1 hour
			if (sTimeout != null && !sTimeout.equals(""))
				timeout = Integer.parseInt(sTimeout);
			int returnValue = 0;
			if (timeout <= 0) {
				returnValue = p.waitFor();
			} 
			else {
				long now = System.currentTimeMillis();
				long timeoutInMillis = timeout;
				long finish = now + timeoutInMillis;
				while (isAlive(p) && (System.currentTimeMillis() < finish)) {
//					Thread.sleep(10);
				}
				if (isAlive(p)) {
					throw new InterruptedException("Process timeout out after " + timeout + " milliseconds");
				}
				returnValue = p.exitValue();
			}

			p.destroy();

			File oFile = new File(outputpath);
			if (oFile.exists() == false)
				throw new Exception("Detector output file not found");
			input = readFile(outputpath);

		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				File oI = new File(inputpath);
				oI.delete();
				
				File oO = new File(outputpath);
				oO.delete();
				/**/

				if (p.isAlive()) {
					p.destroy();
				}
				p = null;

			} catch (Exception ex) {
			}
		}
		return input;
	}
	private String callDetector_notuse(String input) throws Exception {

		//Get bat path
		String batFile = resourcepath + "/languageid/detector_notuse.sh";
		
		//Get temp path and temp file
		Process p = null;
		
		try {

			// encrypt blank line
			//input = encryptBlankLine(input);

			// write input file
			//writeFileWithOutBom(inputpath, input);

			File dirBat = new File(resourcepath);
			String command = "bash " + batFile + " " + input;

			p = Runtime.getRuntime().exec(command, null, dirBat);

			String sTimeout = "600000";
			Integer timeout = 3600000;// default 1 hour
			if (sTimeout != null && !sTimeout.equals(""))
				timeout = Integer.parseInt(sTimeout);
			int returnValue = 0;
			if (timeout <= 0) {
				returnValue = p.waitFor();
			} else {
				long now = System.currentTimeMillis();
				long timeoutInMillis = timeout;
				long finish = now + timeoutInMillis;
				while (isAlive(p) && (System.currentTimeMillis() < finish)) {
					Thread.sleep(10);
				}
				if (isAlive(p)) {
					throw new InterruptedException("Process timeout out after " + timeout + " milliseconds");
				}
				returnValue = p.exitValue();
			}


			// Read result
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			StringBuilder sb = new StringBuilder("");
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line + '\n');
			}
			
			input = sb.toString();

			p.destroy();
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				/*File oI = new File(inputpath);
				oI.delete();
				
				File oO = new File(outputpath);
				oO.delete();*/
				/**/

				if (p.isAlive()) {
					p.destroy();
				}
				p = null;

			} catch (Exception ex) {
			}
		}
		return input;
	}
	private String readFile(String filePath) throws Exception {
		String data = "";
		FileInputStream fin = null;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				fin = new FileInputStream(file);
				byte fileContent[] = new byte[(int) file.length()];
				fin.read(fileContent);
				data = new String(fileContent);
				fin.close();
			} else
				return "";
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			fin.close();
		}

		return data.replace("﻿", "");
	}
	private void writeFileWithOutBom(String path, String content) throws Exception {

		File file = new File(path);
		File dir = new File(file.getParent());
		if (!dir.exists()) {
			// create directory
			boolean cancreate = dir.mkdirs();
		}
		FileOutputStream fO = new FileOutputStream(file);
		OutputStreamWriter oS = new OutputStreamWriter(fO, "UTF-8");
		BufferedWriter out = new BufferedWriter(oS);
		out.write(content);
		out.close();
		oS.close();
		fO.close();
	}
	private String encryptBlankLine(String input) {
		String[] aLine = input.split("\n");
		StringBuilder txt = new StringBuilder();
		for (String line : aLine) {
			if (line.trim().equals("")) {
				txt.append("_AOBLANKLINEKAO_\n");
			} else {
				txt.append(line + "\n");
			}
		}
		input = txt.toString().trim();
		return input;
	}	
	public boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}
}
