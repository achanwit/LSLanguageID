package com.omniscien.lslanguageid.process;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.omniscien.lslanguageid.service.ServiceLanguageidImp;
import com.omniscien.lslanguageid.util.ProcessUtilLanguageid;
import com.omniscien.lslanguageid.util.ReadPropLanguageid;
import com.omniscien.lslanguageid.util.constantLanguageid;

public class LSLanguageid {
	
	ReadPropLanguageid rp= null;
	
	Pattern opentTagP = Pattern.compile("^[<][p][ ][i][d][=][\\\"][p][i][d][\\d]{0,6}[\"][>]");
	Pattern closeTagP = Pattern.compile("[<][\\/][p][>]");
	
	private List<String> fileTypeWordList = Arrays.asList(new String[]{
			"doc","docx","dot","dotx","docm","odt","ott","rtf"});	
	private List<String> fileTypeCellList = Arrays.asList(new String[]{
			"xls","xlsx","xlsb","xlsm","xlt","xltx","xltm","xlsm","ods","csv","tsv"});	
	private List<String> fileTypeEmailList = Arrays.asList(new String[]{
			"msg", "pst", "ost", "oft", "olm","eml", "emlx", "mbox"});	
	private List<String> fileTypeSlideList = Arrays.asList(new String[]{
			"ppt","pot","pps", "pptx", "potx", "ppsx", "ppsm", "potm","otp","odp"});
	private List<String> fileTypeOpenofficeList = Arrays.asList(new String[]{
			"odt","ott","ods","odp","otp","odg","otg"});
	
	private ServiceLanguageidImp langService = new ServiceLanguageidImp();
	
	
//	private String host = constant.HOST;
//	private String port = constant.PORT;
	
	//public LSLanguageid instance;
	
	/*** Constructor ***/
	public LSLanguageid() {
		
	}
	
	public void propertiesSetting(String filePath) {
		rp = new ReadPropLanguageid(filePath);
		langService = new ServiceLanguageidImp();
		langService.propSettingService(filePath);;
		
		
}

	/*** Provide instance ***/
//	public LSLanguageid Init(String host, String port) {
//		
//		if(null == instance) {
//			instance = getInstance(host, port);
//		}
//		return instance;	
//	}

	/*** Set initial instance ***/
//	private LSLanguageid getInstance(String hostStr, String portStr) {
//		instance = new LSLanguageid();
//		
//
//		//Setport
//		if(portStr != null && !portStr.equals("")){
//			instance.setPort(portStr);
//		}else {
//			instance.setPort(port);
//		}
//		//Set Host
//		if(hostStr != null && !hostStr.equals("")){
//			instance.setHost(hostStr);
//		}else {
//			instance.setHost(host);
//		}
//		
//		return instance;
//	}
	
	/*** Getter & Setter HOST:PORT ***/
//	public String getHost() {
//		return host;
//	}
//	public void setHost(String host) {
//		this.host = host;
//	}
//	public String getPort() {
//		return port;
//	}
//	public void setPort(String port) {
//		this.port = port;
//	}
	
	/*** Common method for get current date ***/
	private String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = df.format(c.getTime());
		
		return currentDate;
	}

	/*** Common method for get Node result ***/
//	private String getNodeResult(String rawResult) {
//		String result = new String();
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			JsonNode actualObj = mapper.readTree(rawResult);
//			result = actualObj.get("result").asText();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return result;
//	}

	/*** Common method for create new file ***/
	private void writeFile(String input, String result, String outFilePath) {
		File file = new File(outFilePath);	
		try {
			if(file.createNewFile()) {
				writeLineinFile(result, outFilePath, file);
			}else {
				writeLineinFile(result, outFilePath, file);
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/*** Common method for write file ***/
	private void writeLineinFile(String result, String outFilePath, File file) {
		
		FileWriter writer;
		try {
			writer = new FileWriter(file, true);
			writer.write(result+constantLanguageid.NEW_LINE_RN);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	/*** Common generate id ***/
	public String generateID() {
		String idStr = new String();
		idStr = UUID.randomUUID().toString();
		return idStr;
	}
	
	/*** 1 General get the language id, used as a base of every method. 
	 * @throws Exception ***/
	public String GetLanguageIDGeneral(String idStr, String inputStr, String modeStr ) throws Exception {
		String result = "";
		
//		ServiceLanguageidImp langService = new ServiceLanguageidImp();
		
		result = langService.LanguageID(idStr, inputStr, modeStr, false);
		
		return result;
		
	}
//	public String GetLanguageIDGeneral(String idStr, String inputStr, String modeStr ) {
//		
//		int detectBSFlag = 0;
//		
//		String inputReplace = inputStr;
//		String inputOriginal = inputStr;
//		
//		//Check length of input must <= 2048 charactor
//		int inputLenght = inputStr.length();
//		if(inputLenght > constant.MAXIMUN_INPUT) {
//			return constant.ERROR_MSG_MAIXMUN_INPUT;
//		}
//		
//		//Check backslash
//		String[] splitInputBS = inputStr.split("\\\\");
//		if(splitInputBS.length > 1) {
//			//System.out.println("************** Split BS *****************");
//			detectBSFlag = 1;
//			inputReplace = inputReplace.replace("\\", "");
//		}
//		String[] splitInputBSDBC = inputStr.split("\"");
//		if(splitInputBSDBC.length > 1) {
//			//System.out.println("************** Split DBC *****************");
//			detectBSFlag = 2;
//			inputReplace = inputReplace.replace("\"", "");
//		}
//		
//		String inputRequest = new String();
//		if(detectBSFlag == 0) {
//			inputRequest = inputOriginal;
//		}else {
//			inputRequest = inputReplace;
//		}
//		
////		System.out.println("inputOriginal: "+inputOriginal);
////		System.out.println("inputReplace: "+inputReplace);
//		
//		String resultStr = constant.EMPTY_STRING;
//		OkHttpClient client = new OkHttpClient();
//		Request request = new Request.Builder().header(constant.CONTENT_TYPE, constant.APPLICATION_JSON)
//				              .url(constant.HTTP + host + constant.COLON + port 
//				              +constant.LANGUAGE_LEVEL_PATH_1
//				              +constant.LANGUAGE_LEVEL_PATH_2
//				              +constant.START_RECEIVE_PARAM
//				              +constant.PARAM_ID+idStr
//				              +constant.PARAM_INPUT+inputRequest
//				              +constant.PARAM_MODE+modeStr)
//				              .get().build();
//		LanguageidRespond responseMessage = null;
//		try {
//			Response response = client.newCall(request).execute();
//			
//			ResponseBody responseBody = response.body();
//			String json = new String(responseBody.bytes());
//			resultStr = json;
//			
//			if(detectBSFlag > 0) {
//				resultStr = resultStr.replace(inputReplace, inputOriginal);
//			}
//		}catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return resultStr;
//	}
	
	/*** 2 Set input as string only, by cld2 as default  
	 * @throws Exception ***/
	public String GetLanguageIDGeneral(String inputStr) throws Exception {
		
		//Initial result;
		String result = new String();
		
		//Prepare id
		String idStr = generateID();
		
		//Prepare mode
		String modeStr = constantLanguageid.CLD2_MODE;
		
		//Set result by Call to GetLanguageIDGeneral(String idStr, String inputStr, String modeStr )
		result = GetLanguageIDGeneral(idStr, inputStr, modeStr);
		
		//return result
		return result;
		
	}
	
	/*** 3 Set input and cld value  
	 * @throws Exception ***/
	public String GetLanguageIDGeneral(String inputStr, String modeStr) throws Exception {
		
		//Initial result;
		String result = new String();
		
		//Prepare id
		String idStr = generateID();

		//Set result by Call to GetLanguageIDGeneral(String idStr, String inputStr, String modeStr )
		result = GetLanguageIDGeneral(idStr, inputStr, modeStr);
		
		//return result
		return result;
		
	}
	
	/*** 4 Set input as filePart, by cld2 as default  
	 * @throws Exception ***/
	public String GetLanguageIDGeneralFromFile(String filePath) throws Exception {
		
		//Initial result;
		String result = null;
		
		//Check file type
		String fileType = getFileType(filePath).toLowerCase();
		
		//Prepare Input 
		String inputStr = null;
		StringBuffer inputBuf = null;
		if (fileTypeWordList.contains(fileType)){
			inputStr = getStringContentFromWordAspose(filePath);
		}else if(fileTypeCellList.contains(fileType)) {
			
		}else if(fileTypeSlideList.contains(fileType)) {
			
		}else if(fileTypeEmailList.contains(fileType)) {
			
		}else if(fileType.equals("txt")) {
			Reader inputString = new FileReader(filePath);
		    BufferedReader br = new BufferedReader(inputString);  
		    inputBuf = new StringBuffer();
			
			//Read file
			try {
				String sent = null;
			      while ((sent = br.readLine()) != null){
			    	  sent = findAndReplaceAll(opentTagP, "", sent);
			    	  sent = findAndReplaceAll(closeTagP, "", sent);
			    	  inputBuf.append(sent+" ");
						if(inputBuf.length() > constantLanguageid.MAXIMUN_INPUT) {
							break;
						}	
			      }		
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		
		
		
		
		
		

		inputStr = inputBuf.toString();

		//result = langService.LanguageidFromFileForCLD2(filePath);
		
		//return result
		result = GetLanguageIDGeneral(inputStr);
		return result;
		
	}
	
	private String getStringContentFromWordAspose(String filePath) {
		String output = "";
		try {
//			output = 
		} catch (Exception e) {
			throw e;
		}
		return output;
	}

	private String getContentStringFromFile() {
		// TODO Auto-generated method stub
		return null;
	}

	//Check 
	private String getFileType(String filePath) {
		
		String fileType = null;
		
		//Get file name
		String[] filpathArr = filePath.split("/");
		int filpathArrLength = filpathArr.length;		
		String fileName = filpathArr[filpathArrLength-1];
		
		//Get file type
		String fileNameArr[] = fileName.split(".");
		int fileNameArrLength = fileNameArr.length;
		fileType = fileNameArr[fileNameArrLength-1];
		
		return fileType;
	}

	private String findAndReplaceAll(Pattern opentTagP2, String replace, String sent) {
		// TODO Auto-generated method stub
		 Matcher matcher = opentTagP2.matcher(sent);
		return matcher.replaceAll(replace);
	}

	/*** 5 Set input as filePart and cld value 
	 * @throws Exception ***/
	public String GetLanguageIDGeneralFromFile(String filePath, String modeStr) throws Exception {
		
		//Initial result;
		String result = new String();
		
		//Prepare id
		String idStr = generateID();
		
		//Prepare Input 
		String inputStr = new String();
		
		//Read file
		Reader inputString = new FileReader(filePath);
	    BufferedReader br = new BufferedReader(inputString);  
	    StringBuffer inputBuf = new StringBuffer();
		try {
			String sent = null;
		      while ((sent = br.readLine()) != null){
		    	  inputBuf.append(sent);
					if(inputBuf.length() > constantLanguageid.MAXIMUN_INPUT-1100) {
						break;
					}	
		      }		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
//		File file = new File(filePath);
//		Scanner reader = new Scanner(file);	
//		StringBuffer inputBuf = new StringBuffer();
//		while(reader.hasNextLine()) {
//			inputBuf.append(reader.nextLine());
//			if(inputBuf.length() > constant.MAXIMUN_INPUT-1100) {
//				break;
//			}
//		}
		
		
		//Set input
		inputStr = inputBuf.toString();
		
		//Set result by Call to GetLanguageIDGeneral(String idStr, String inputStr, String modeStr )
		result = GetLanguageIDGeneral(idStr, inputStr, modeStr);
		
		//return result
		return result;
		
	}
	
//	public String GetLanguageIDLineByLineV2(String idStr, String inputFilePath, String outFilePath,  String mode) throws Exception {
//		String result = "";
//		
//		ServiceLanguageidImp langService = new ServiceLanguageidImp();
//		
//		String rawResult = langService.LanguageID(idStr, inputFilePath, "cld2" , true);
//		writeLineinFile(rawResult, null, new File(outFilePath));
//		
//		return result;
//		
//	}
	
	/*** 6 Get language id line by line general, used as a base of all line by line method 
	 * @throws Exception ***/
	public String GetLanguageIDLineByLine(String idStr, String inputFilePath, String outFilePath,  String mode) throws Exception{
		String resultStatus = null;
		String result = null;
		int totalLines = 0;
		
		
		//System.out.println("Mode: "+mode.toUpperCase());
		if(!mode.toUpperCase().equals("CLD3")) {
			
			//Read total line number
			BufferedReader readerBuf = new BufferedReader(new FileReader(inputFilePath));
			
			while(readerBuf.readLine() != null) {
				totalLines++;
			}
			//System.out.println("Total Line: "+totalLines);
			readerBuf.close();
			
			//Process Big Data
			if(totalLines > constantLanguageid.MAX_LINE) {
				
				//Read file
			    StringBuffer inputBuf = new StringBuffer();
				
				FileInputStream inputString = new FileInputStream(inputFilePath);
				DataInputStream in = new DataInputStream(inputString);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				//Prepare progress every 10%
				double tenPD = totalLines*0.1;
				double twentyPD = totalLines*0.2;
				double thirtyPD = totalLines*0.3;
				double fortytyPD = totalLines*0.4;
				double fiftyPD = totalLines*0.5;
				double sixtyPD = totalLines*0.6;
				double seventyPD = totalLines*0.7;
				double eightyPD = totalLines*0.8;
				double ninetyPD = totalLines*0.9;
				double compltePD = totalLines;
				
				//Set target progress as integer every 10%
				int tenPT = (int)Math.round(tenPD);
				int twentyPT = (int)Math.round(twentyPD);
				int thirtyPT = (int)Math.round(thirtyPD);
				int fortytyPT = (int)Math.round(fortytyPD);
				int fiftyPT = (int)Math.round(fiftyPD);
				int sixtyPT = (int)Math.round(sixtyPD);
				int seventyPT = (int)Math.round(seventyPD);
				int eightyPT = (int)Math.round(eightyPD);
				int ninetyPT = (int)Math.round(ninetyPD);
				int compltePT = (int)Math.round(compltePD);

				
				try {
					String line = null;
					int countLineCondition = 0;
					int countLineProgress = 0;
					
					String inputWriteToTempFile = null; 
				      while ((line = br.readLine()) != null){
				    	  //Count Line for condition write temp file
				    	  countLineProgress++;
				    	  countLineCondition++;
				    	  inputBuf.append(line).append("\n");
				    	  
				    	  	//Process Separate temp file to get language id
							if(countLineCondition > constantLanguageid.MAX_LINE) {
								inputWriteToTempFile = new String(inputBuf);
								String inputpathTemp = getFileTemp();
								
								// write input temp file
								File file = new File(inputpathTemp);
								File dir = new File(file.getParent());
								if (!dir.exists()) {
									// create directory
									boolean cancreate = dir.mkdirs();
								}
								
								//Write temp file
								FileOutputStream fO = new FileOutputStream(file);
								OutputStreamWriter oS = new OutputStreamWriter(fO, "UTF-8");
								BufferedWriter out = new BufferedWriter(oS);
								out.write(inputWriteToTempFile);
								out.close();
								oS.close();
								fO.close();

								//Get Language id
								String resultTemp = null;
								resultTemp = langService.LanguageidFromFileForCLD2(inputpathTemp);
								File oI = new File(inputpathTemp);
								oI.delete();
								
								//Write Output File
								String[] arrayResultTemp = resultTemp.split("\n");
								String writeLine = null;
								File outputFile = new File(outFilePath);								
								for(int i = 0; i < arrayResultTemp.length; i++) {
								
									writeLine = arrayResultTemp[i];
									writeLineinFile(writeLine, outFilePath, outputFile);
								}
								
								//Clear variable to initial
								inputBuf = new StringBuffer();
								countLineCondition = 0; 
							}
							
							if(countLineProgress == 1) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 0% Completed.");
							}			
							if(countLineProgress == tenPT) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 10% Completed.");
							}
							if(countLineProgress == twentyPT) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 20% Completed.");
							}
							if(countLineProgress == thirtyPT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 30% Completed.");
							}
							if(countLineProgress == fortytyPT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 40% Completed.");
							}
							if(countLineProgress == fiftyPT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 50% Completed.");
							}
							if(countLineProgress == sixtyPT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 60% Completed.");
							}
							if(countLineProgress == seventyPT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 70% Completed.");
							}
							if(countLineProgress == eightyPT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 80% Completed.");
							}
							if(countLineProgress == ninetyPT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 90% Completed.");
							}
							if(countLineProgress == compltePT ) {
								String currentDate = getCurrentDate();
								System.out.println(currentDate+"::The progress status: 100% Process Completed.");
								resultStatus = "The progress status: 100% Process Completed.";
							}
							
							
				      }		
				} catch (IOException e) {
					e.printStackTrace();
				} finally {

					try {
						if (br != null)
							br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				
			}else{
				
		//	System.out.println("Total line: "+totalLines);
			
			result = langService.LanguageidFromFileForCLD2(inputFilePath);
			String[] arrayResult = result.split("\n");
//			int totalLines = arrayResult.length;
//			System.out.println("Total line: "+totalLines);
			String writeLine = null;
			File outputFile = new File(outFilePath);
			//Prepare progress every 10%
			double tenPD = totalLines*0.1;
			double twentyPD = totalLines*0.2;
			double thirtyPD = totalLines*0.3;
			double fortytyPD = totalLines*0.4;
			double fiftyPD = totalLines*0.5;
			double sixtyPD = totalLines*0.6;
			double seventyPD = totalLines*0.7;
			double eightyPD = totalLines*0.8;
			double ninetyPD = totalLines*0.9;
			double compltePD = totalLines;
			
			//Set target progress as integer every 10%
			int tenPT = (int)Math.round(tenPD);
			int twentyPT = (int)Math.round(twentyPD);
			int thirtyPT = (int)Math.round(thirtyPD);
			int fortytyPT = (int)Math.round(fortytyPD);
			int fiftyPT = (int)Math.round(fiftyPD);
			int sixtyPT = (int)Math.round(sixtyPD);
			int seventyPT = (int)Math.round(seventyPD);
			int eightyPT = (int)Math.round(eightyPD);
			int ninetyPT = (int)Math.round(ninetyPD);
			int compltePT = (int)Math.round(compltePD);
			int progress = 0;
			for(int i = 0; i < totalLines; i++) {
				
				
				progress++;
				//Display log every 10% completed
				if(progress == 1) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 0% Completed.");
				}			
				if(progress == tenPT) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 10% Completed.");
				}
				if(progress == twentyPT) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 20% Completed.");
				}
				if(progress == thirtyPT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 30% Completed.");
				}
				if(progress == fortytyPT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 40% Completed.");
				}
				if(progress == fiftyPT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 50% Completed.");
				}
				if(progress == sixtyPT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 60% Completed.");
				}
				if(progress == seventyPT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 70% Completed.");
				}
				if(progress == eightyPT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 80% Completed.");
				}
				if(progress == ninetyPT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 90% Completed.");
				}
				if(progress == compltePT ) {
					String currentDate = getCurrentDate();
					System.out.println(currentDate+"::The progress status: 100% Process Completed.");
					resultStatus = "The progress status: 100% Process Completed.";
				}
				
				writeLine = arrayResult[i];
				writeLineinFile(writeLine, outFilePath, outputFile);
			}
			//System.out.println("result: "+result);
			}	
		}else {
			//Read total line number
			BufferedReader readerBuf = new BufferedReader(new FileReader(inputFilePath));
			totalLines = 0;
			while(readerBuf.readLine() != null) {
				totalLines++;
			}
			readerBuf.close();
//			System.out.println("Total line: "+totalLines);
			
			//Prepare progress every 10%
			double tenPD = totalLines*0.1;
			double twentyPD = totalLines*0.2;
			double thirtyPD = totalLines*0.3;
			double fortytyPD = totalLines*0.4;
			double fiftyPD = totalLines*0.5;
			double sixtyPD = totalLines*0.6;
			double seventyPD = totalLines*0.7;
			double eightyPD = totalLines*0.8;
			double ninetyPD = totalLines*0.9;
			double compltePD = totalLines;
			
			//Set target progress as integer every 10%
			int tenPT = (int)Math.round(tenPD);
			int twentyPT = (int)Math.round(twentyPD);
			int thirtyPT = (int)Math.round(thirtyPD);
			int fortytyPT = (int)Math.round(fortytyPD);
			int fiftyPT = (int)Math.round(fiftyPD);
			int sixtyPT = (int)Math.round(sixtyPD);
			int seventyPT = (int)Math.round(seventyPD);
			int eightyPT = (int)Math.round(eightyPD);
			int ninetyPT = (int)Math.round(ninetyPD);
			int compltePT = (int)Math.round(compltePD);
			int progress = 0;
			int countForWriteFile = 0;
			
			StringBuffer chunkBufferStringForWriteFile = new StringBuffer();
			String chunkStringForWriteFile = new String();
			
			//Prepare get input
			resultStatus = constantLanguageid.EMPTY_STRING;
			Reader file = new FileReader(inputFilePath);
//			Scanner reader = new Scanner(file);	
			
			//Reader inputString = new StringReader(inputFilePath);
		    BufferedReader br = new BufferedReader(file);  
		    StringBuffer inputBuf = new StringBuffer();
			try {
				String sent = null;
				//process each line	
			      while ((sent = br.readLine()) != null){
			    	  progress++;
			    	  countForWriteFile++;
			    	  String rawResult = GetLanguageIDGeneral(constantLanguageid.EMPTY_STRING, sent, mode);
			    	//  TimeUnit.MILLISECONDS.sleep(1);
			    	  
			    	  String[] rawResultArr = rawResult.split(constantLanguageid.COLON);
						
						result = rawResultArr[0]+constantLanguageid.COLON+rawResultArr[1].replace("\\t", constantLanguageid.TAB);
						
						result = result.replace(constantLanguageid.REPLACE_ERROR_NO, constantLanguageid.EMPTY_STRING);
						//int resulrLength = result.length();
						//StringBuilder resultStrBu = new StringBuilder(result);
						//resultStrBu.setCharAt(0, ' ');
						//resultStrBu.setCharAt(resulrLength-1, ' ');
						
						//result = new String(resultStrBu);
						
						
						//result = result.replace(constant.DOUBLE_QUOTE, constant.EMPTY_STRING);
						
						if(result.trim().equals(constantLanguageid.DETECT_UNKNOW)) {
							result = constantLanguageid.UNKNOW_RESULT+constantLanguageid.TAB+sent;
						}

//						if(countForWriteFile <= 100000) {
//							chunkBufferStringForWriteFile = chunkBufferStringForWriteFile.append(result).append(constant.NEW_LINE_N);
//						}else {
//							
//							chunkStringForWriteFile = new String(chunkBufferStringForWriteFile);
//							chunkBufferStringForWriteFile = new StringBuffer();
//							countForWriteFile = 0;
//							
//							//writ result on a file
//							writeFile(sent, chunkStringForWriteFile.trim(), outFilePath);
//							chunkStringForWriteFile = new String();
//							//TimeUnit.SECONDS.sleep(3);
//						}
						writeFile(sent, result.trim(), outFilePath);
//						System.out.println("Result: "+result.trim());
//						if(progress == 600000) {
//							System.out.println("Sleep");
//							TimeUnit.SECONDS.sleep(10);
//						}
//						if(progress == 1200000) {
//							TimeUnit.SECONDS.sleep(10);
//						}
//						if(progress == 1800000) {
//							TimeUnit.SECONDS.sleep(10);
//						}
//						if(progress == 2400000) {
//							TimeUnit.SECONDS.sleep(10);
//						}
//						if(progress == 3000000) {
//							TimeUnit.SECONDS.sleep(10);
//						}
//						if(progress == 3600000) {
//							TimeUnit.SECONDS.sleep(10);
//						}
						
						
						
//						System.out.println("Line: "+progress);
//						System.out.println("result: "+result);
					
						
						//Display log every 10% completed
						if(progress == 1) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 0% Completed.");
						}			
						if(progress == tenPT) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 10% Completed.");
						}
						if(progress == twentyPT) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 20% Completed.");
						}
						if(progress == thirtyPT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 30% Completed.");
						}
						if(progress == fortytyPT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 40% Completed.");
						}
						if(progress == fiftyPT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 50% Completed.");
						}
						if(progress == sixtyPT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 60% Completed.");
						}
						if(progress == seventyPT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 70% Completed.");
						}
						if(progress == eightyPT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 80% Completed.");
						}
						if(progress == ninetyPT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 90% Completed.");
						}
						if(progress == compltePT ) {
							String currentDate = getCurrentDate();
							System.out.println(currentDate+"::The progress status: 100% Process Completed.");
							resultStatus = "The progress status: 100% Process Completed.";
						}
			    	  
			      }		
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		
	
		
		return resultStatus;
	}
	
	private String getFileTemp() {
		String sTempPath = rp.getProp(constantLanguageid.TEMP_PATH)+ "/temp";
		File fTemp = new File(sTempPath);
		if (!fTemp.exists()) fTemp.mkdirs();
		String id = UUID.randomUUID().toString();
		String inputpath = sTempPath + "/" + id + ".in.txt";
		
		//File tempFile = new File(inputpath);
	return inputpath;
}

	/*** 7 Get language id line.By set the only 
			1) input file path 
			2) output file path
		Default cld as cld2
	 * @throws Exception 
	 */
	public String GetLanguageIDLineByLine(String inputFilePath, String outFilePath) throws Exception {
		//Prepare id
		String idStr = generateID();
		
		//Prepare mode
		String modeStr = constantLanguageid.EMPTY_STRING;
		
		//Process language id
		String resultStatus = GetLanguageIDLineByLine(idStr, inputFilePath, outFilePath, modeStr);
		
		return resultStatus;
	}
	
	/*** 8 Get language id line.By set 
	       1) input file path 
		   2) output file path
		   3) cld
	 * @throws Exception 
	 * ***/ 
	public String GetLanguageIDLineByLine(String inputFilePath, String outFilePath, String modeStr) throws Exception {
		
		//Prepare id
		String idStr = generateID();
		
		//Process language id
		String resultStatus = GetLanguageIDLineByLine(idStr, inputFilePath, outFilePath, modeStr);
		
		return resultStatus;
	}
	
	/*** 9 Get Language id for SRT file By set mode parameter 
	 * @throws Exception ***/
	public String GetLanguageIDSRTFile(String inputFilePath, String modeStr) throws Exception {
		String result = null;
		//Get Total line
		int totalLines = 0;
		BufferedReader readerBuf = new BufferedReader(new FileReader(inputFilePath));		
		while(readerBuf.readLine() != null) {
			totalLines++;
		}
		readerBuf.close();
		
	//	if(!modeStr.toUpperCase().contentEquals("CLD3")) {
			//Preapre subtitelCount
//			 int subtitelCount = 0;
//			
//			//Prepare dialog Count
//			int dialogCount = 0;
//			
//			result = new String();
//			
//			StringBuffer inputBuf = new StringBuffer();
//			String input = new String();
//			
//			//String indexPattern = "[\\d]{1,5}[a-z]{0,2}[\\n]";
//			String indexPattern = "[0-9]";
//			String timeLinePattern = "^[\\d]{2}[:][\\d]{2}[:][\\d]{2}[,][\\d]{3}[ ][-][-][\\>][ ][\\d]{2}[:][\\d]{2}[:][\\d]{2}[,][\\d]{3}";
//			
//			//Prepare get input	
////			Reader inputString = new StringReader(inputFilePath);
//			FileInputStream inputString = new FileInputStream(inputFilePath);
//			DataInputStream in = new DataInputStream(inputString);
//			BufferedReader br1 = new BufferedReader(new InputStreamReader(in));
//			
//			try {
//				String sent = null;
//			      while ((sent = br1.readLine()) != null){
//		
//						int inputLength = sent.length();
//						if(inputLength > 5) {
//							if(sent.matches(timeLinePattern)) {
//								subtitelCount++;
//							}else{
//								dialogCount++;
//							}
//						}
//			      }		
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//
//				try {
//					if (br1 != null)
//						br1.close();
//				} catch (IOException ex) {
//					ex.printStackTrace();
//				}
//			}
//			
//			FileInputStream inputString2 = new FileInputStream(inputFilePath);
//			DataInputStream in2 = new DataInputStream(inputString2);
//			BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
//			
//			
//			try {
//				String sent = null;
//			      while ((sent = br2.readLine()) != null){
//		
//			    	  int inputLength = sent.length();
//			    	  
//			    	  if(inputLength > 5) {
//
//							if(!sent.matches(timeLinePattern)) {
//								inputBuf = inputBuf.append(sent).append(" ");
//								if(inputBuf.length() > constant.MAXIMUN_INPUT-30) {
//									break;
//								}
//							}
//						}else if(inputLength != 0){
//							String inputCheck = sent.substring(0, inputLength-1);
//							try {
//							int inputInt = Integer.parseInt(inputCheck);
//							}catch(NumberFormatException e) {
//								inputBuf = inputBuf.append(inputCheck).append(" ");
//								if(inputBuf.length() > constant.MAXIMUN_INPUT-30) {
//									break;
//								}
//							}
//								
//							
//						}
//			      }		
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//
//				try {
//					if (br2 != null)
//						br2.close();
//				} catch (IOException ex) {
//					ex.printStackTrace();
//				}
//			}
//			
//			input = new String(inputBuf);
			
			
	//	}else {
		
		//Preapre subtitelCount
		 int subtitelCount = 0;
		
		//Prepare dialog Count
		int dialogCount = 0;
		
		result = new String();
		
		StringBuffer inputBuf = new StringBuffer();
		String input = new String();
		
		//String indexPattern = "[\\d]{1,5}[a-z]{0,2}[\\n]";
		String indexPattern = "[0-9]";
		String timeLinePattern = "^[\\d]{2}[:][\\d]{2}[:][\\d]{2}[,][\\d]{3}[ ][-][-][\\>][ ][\\d]{2}[:][\\d]{2}[:][\\d]{2}[,][\\d]{3}";
		
		//Prepare get input	
		FileInputStream inputString = new FileInputStream(inputFilePath);
		DataInputStream in = new DataInputStream(inputString);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(in));
		//BufferedReader br2 = new BufferedReader(new InputStreamReader(in));
		
		try {
			String sent = null;
		      while ((sent = br1.readLine()) != null){
	
					int inputLength = sent.length();
					if(inputLength > 5) {
						if(sent.matches(timeLinePattern)) {
							subtitelCount++;
						}else{
							dialogCount++;
						}
					}
		      }		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (br1 != null)
					br1.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		
		
		FileInputStream inputString2 = new FileInputStream(inputFilePath);
		DataInputStream in2 = new DataInputStream(inputString2);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
		

		
		try {
			String sent = null;
		      while ((sent = br2.readLine()) != null){
	
		    	  int inputLength = sent.length();
		    	  
		    	  if(inputLength > 5) {

						if(!sent.matches(timeLinePattern)) {
							inputBuf = inputBuf.append(sent).append(" ");
							if(inputBuf.length() > constantLanguageid.MAXIMUN_INPUT-30) {
								break;
							}
						}
					}else if(inputLength != 0){
						String inputCheck = sent.substring(0, inputLength-1);
						try {
						int inputInt = Integer.parseInt(inputCheck);
						}catch(NumberFormatException e) {
							inputBuf = inputBuf.append(inputCheck).append(" ");
							if(inputBuf.length() > constantLanguageid.MAXIMUN_INPUT-30) {
								break;
							}
						}
							
						
					}
		      }		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (br2 != null)
					br2.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		input = new String(inputBuf);

		
		result = GetLanguageIDGeneral(input, modeStr);
		String[] resultArr = result.split(constantLanguageid.COLON);
		String[] resultPercentArr = resultArr[1].split("%");
		String resultPercentStr = resultPercentArr[0]+"%";
		
		result = inputFilePath
				+constantLanguageid.TAB
				+totalLines+constantLanguageid.TAB
				+subtitelCount
				+constantLanguageid.TAB
				+dialogCount
				+constantLanguageid.TAB
				+resultArr[0].replace(constantLanguageid.DOUBLE_QUOTE, constantLanguageid.EMPTY_STRING)
				+constantLanguageid.COLON
				+resultPercentStr
				+constantLanguageid.TAB
				+resultArr[0].replace(constantLanguageid.DOUBLE_QUOTE, constantLanguageid.EMPTY_STRING);
		
		
		return result;
	}
	
	/*** 10 Get Language id for SRT file By default mode as CLD2 
	 * @throws Exception ***/
	public String GetLanguageIDSRTFile(String inputFilePath) throws Exception {
		String result = new String();
		String modeStr = constantLanguageid.CLD2_MODE;
		
		result = GetLanguageIDSRTFile(inputFilePath, modeStr);
		
		return result;
	}
	

//	private void writeFileWithOutBom(String path, String content) throws Exception {
//
//		File file = new File(path);
//		File dir = new File(file.getParent());
//		if (!dir.exists()) {
//			// create directory
//			boolean cancreate = dir.mkdirs();
//		}
//		FileOutputStream fO = new FileOutputStream(file);
//		OutputStreamWriter oS = new OutputStreamWriter(fO, "UTF-8");
//		BufferedWriter out = new BufferedWriter(oS);
//		out.write(content);
//		out.close();
//		oS.close();
//		fO.close();
//	}

	
	

	
	
	
}
