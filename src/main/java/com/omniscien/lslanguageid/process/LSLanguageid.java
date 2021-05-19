package com.omniscien.lslanguageid.process;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.omniscien.lslanguageid.service.ServiceLanguageidImp;
import com.omniscien.lslanguageid.util.ProcessUtilLanguageid;
import com.omniscien.lslanguageid.util.ReadProp;
import com.omniscien.lslanguageid.util.Log4J;
import com.omniscien.lslanguageid.model.LanguageidModel;
import com.omniscien.lslanguageid.model.ServletContextMock;


import LSFileSystemLocal.LSFileSystem;

import com.omniscien.lslanguageid.util.Constant;

public class LSLanguageid {
	
	private ReadProp rp= null;
	//private Log4J oLog = null;
	private ServletContextMock app = new ServletContextMock();
	private String pageName = "LSLanguageID";
	
	
	
	private Pattern opentTagP = Pattern.compile("^[<][p][ ][i][d][=][\\\"][p][i][d][\\d]{0,6}[\"][>]");
	private Pattern closeTagP = Pattern.compile("[<][\\/][p][>]");
	
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
	
	
	private List<String> fileTypeSupportList = Arrays.asList(new String[] {
			"DOC", "DOCX", "XLS", "XLSX", "MSG", "PPT", "PPTX", "PDF", "HTML", "TXT"
	});
	
	private ServiceLanguageidImp langService = new ServiceLanguageidImp();
	
	private long begintime = 0;
	private long endtime = 0;
	private long totaltime =0;
	private String startTimeStr = "";
	private String endTimeStr = "";
	
	
	
	public ReadProp getRp() {
		return rp;
	}

	public void setRp(ReadProp rp) {
		this.rp = rp;
	}

	/*** Constructor ***/
	public LSLanguageid() {
		
	}
	
	public void propertiesSetting(String filePath) {
		rp = new ReadProp(filePath);
		langService = new ServiceLanguageidImp();
		langService.propSettingService(filePath);
		/*** Start comment 2021-05-19 
		if (oLog == null) {
			try {
				oLog = new Log4J();
				oLog.Log4J(app, rp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			oLog.debugMode = true;
			oLog.setDebugPath(rp.getProp(Constant.LOG_PATH));
			oLog.log4JPropertyFile = rp.getProp(Constant.LOG_4J);
		
		}
		*** End comment 2021-05-19 ***/ 
	}


	
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
			writer.write(result+Constant.NEW_LINE_RN);
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
	 * @param languaeidModel 
	 * @throws Exception ***/
	public String GetLanguageIDGeneral(String idStr, String inputStr, String modeStr) throws Exception {
		String result = "";
		
//		ServiceLanguageidImp langService = new ServiceLanguageidImp();
		
		result = langService.LanguageID(idStr, inputStr, modeStr, false);
		
		return result;
		
	}

	
	/*** 2 Set input as string only, by cld2 as default  
	 * @param languaeidModel 
	 * @throws Exception ***/
	public String GetLanguageIDGeneral(String inputStr) throws Exception {
		
		LanguageidModel languaeidModel = new LanguageidModel();
		
		//Initial result;
		String result = new String();
		
		//Prepare id
		String idStr = generateID();
		
		//Prepare mode
		String modeStr = Constant.CLD2_MODE;
		
		//Set result by Call to GetLanguageIDGeneral(String idStr, String inputStr, String modeStr )
		result = GetLanguageIDGeneral(idStr, inputStr, modeStr);
		
		//return result
		return result;
		
	}
	
	/*** 3 Set input and cld value  
	 * @throws Exception ***/
	public String GetLanguageIDGeneral(String inputStr, String modeStr) throws Exception {
		LanguageidModel languaeidModel = new LanguageidModel();
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
		
		//Prepare Model Result
		com.omniscien.lslanguageid.model.LanguageidModel languaeidModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		
		String tempUUID = generateID();
		
//		oLog.WriteLog(pageName,tempUUID , "Get Language ID From File", "Start",  false);
		//Initial Variable
		String result = null;
		String outputProcessTemp = null;
		Reader inputString = null;
		boolean inputTypeTXT = true;
		String inputStr = null;
		StringBuffer inputBuf = null;
		
		//Get file type
		String fileType = getFileType(filePath).toUpperCase();
//		oLog.WriteLog(pageName,tempUUID , "Get file Type as "+fileType,"", false);
		
		//Prepare Input String 
		if (!fileType.equals("TXT")){
			inputTypeTXT = false;
			//Prepare output file part
			String inputFileName = getFileName(filePath); 
			
			outputProcessTemp = rp.getProp(Constant.PROCESS_TEMP_PATH)+tempUUID+inputFileName+".txt";
			
			LSFileSystem lsFile = new LSFileSystem(null);
			lsFile.File.Convert.FileType(
					//inputFilePath
					filePath, 
					//OutputFilePath
					outputProcessTemp, 
					//FileFormat
					fileType, 
					//OutputFileFormat
					"TXT");
			
			boolean checkSupport = new File(outputProcessTemp).exists();
			if(!checkSupport) {
				System.out.println("Can not support file type: *."+fileType.toLowerCase());
//				oLog.WriteLog(pageName,tempUUID , "Wernning Get Language ID From File", "Can not support file type: *."+fileType.toLowerCase(),  false);
//				oLog.WriteLog(pageName,tempUUID ,  "Get Language ID From File", "End AbNormally", false);
				return "Can not support file type: *."+fileType.toLowerCase();
			}
			inputString = new FileReader(outputProcessTemp);
		}else {
			inputString = new FileReader(filePath);
		}
		
		BufferedReader br = new BufferedReader(inputString);  
	    inputBuf = new StringBuffer();
		
		//Read file
		try {
			String sent = null;
		      while ((sent = br.readLine()) != null){
		    	  sent = findAndReplaceAll(opentTagP, "", sent);
		    	  sent = findAndReplaceAll(closeTagP, "", sent);
		    	  inputBuf.append(sent+" ").append("\n");
//					if(inputBuf.length() > Constant.MAXIMUN_INPUT) {
//						break;
//					}	
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
		
		//Convert source to string
		inputStr = inputBuf.toString();
	
		//get Language id
		result = GetLanguageIDGeneral(inputStr);
		
		//If input not TXT will delete file temp
		if(!inputTypeTXT) {
			deleteFile(outputProcessTemp);
		}
		
//		oLog.WriteLog(pageName,tempUUID , "Get Language ID From File", "End Normally",  false);
		//Return result;
		return result;
		
	}

	private void deleteFile(String outputProcessTemp) {
		File deletefilaPath = new File(outputProcessTemp);
		 if (deletefilaPath.delete()) { 
//		      System.out.println("Deleted the file: " + deletefilaPath.getName());
		    } else {
//		      System.out.println("Failed to delete the file.");
		    } 
		
	}

	private String getFileName(String filePath) {
//		String filePathArr[] = filePath.split("/");
//		int lengthOffilePathArr = filePathArr.length;
//		
//		return filePathArr[lengthOffilePathArr-1];
		File inputFilePath = new File(filePath);
		String filename = inputFilePath.getName();
		return filename;
	}

	private String getContentStringFromFile() {
		// TODO Auto-generated method stub
		return null;
	}

	//Check 
	private String getFileType(String filePath) {
		
		String fileType = null;
		

		
		String fileName = getFileName(filePath);
		
		//Get file type
		String fileNameArr[] = fileName.split("\\.");
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
		//Prepare Model Result
		com.omniscien.lslanguageid.model.LanguageidModel languaeidModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		
		String tempUUID = generateID();
//		oLog.WriteLog(pageName,tempUUID , "Get Language ID From File", "Start",  false);
		
		//Initial variable;
		String result = new String();
		Reader inputString = null;
		boolean inputTypeTXT = true;
		String outputProcessTemp = null;
		
		//Prepare id
		String idStr = generateID();
		
		//Prepare Input 
		String inputStr = new String();
		
		//Get file type
		String fileType = getFileType(filePath).toUpperCase();
//		oLog.WriteLog(pageName,tempUUID , "Get file Type as "+fileType,"", false);
		
		if (!fileType.equals("TXT")){
			inputTypeTXT = false;
			//Prepare output file part
			String inputFileName = getFileName(filePath); 
			
			outputProcessTemp = rp.getProp(Constant.PROCESS_TEMP_PATH)+tempUUID+inputFileName+".txt";
			
			LSFileSystem lsFile = new LSFileSystem(null);
			lsFile.File.Convert.FileType(
					//inputFilePath
					filePath, 
					//OutputFilePath
					outputProcessTemp, 
					//FileFormat
					fileType, 
					//OutputFileFormat
					"TXT");
			boolean checkSupport = new File(outputProcessTemp).exists();
			if(!checkSupport) {
				System.out.println("Can not support file type: *."+fileType.toLowerCase());
//				oLog.WriteLog(pageName,tempUUID , "Wernning Get Language ID From File", "Can not support file type: *."+fileType.toLowerCase(),  false);
//				oLog.WriteLog(pageName,tempUUID ,  "Get Language ID From File", "End AbNormally", false);
				return "Can not support file type: *."+fileType.toLowerCase();
			}
			inputString = new FileReader(outputProcessTemp);
		}else {
			inputString = new FileReader(filePath);
		}
		
	    BufferedReader br = new BufferedReader(inputString);  
	    StringBuffer inputBuf = new StringBuffer();
		try {
			String sent = null;
		      while ((sent = br.readLine()) != null){
		    	  inputBuf.append(sent);
					if(inputBuf.length() > Constant.MAXIMUN_INPUT) {
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
		
		// If input not TXT will delete file temp
		if (!inputTypeTXT) {
			deleteFile(outputProcessTemp);
		}
		
//		oLog.WriteLog(pageName,tempUUID , "Get Language ID From File", "End Normally",  false);
		
		//return result
		return result;
		
	}
	
	/*** 6 Get language id line by line general, used as a base of all line by line method 
	 * @throws Exception ***/
	public String GetLanguageIDLineByLine(String idStr, String inputFilePath, String outFilePath,  String mode) throws Exception{
		//Prepare Model Result
				com.omniscien.lslanguageid.model.LanguageidModel languaeidModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		String tempUUID = null;
		if(idStr == null || idStr.equals("")) {
			tempUUID = generateID();
		}else {
			tempUUID = idStr;
		}
		
//		oLog.WriteLog(pageName,tempUUID , "Get Language ID Line By Line", "Start",  false);
		
		String resultStatus = null;
		String result = null;
		int totalLines = 0;
		boolean inputTypeTXT = true;
		String outputProcessTemp = null;
		BufferedReader readerBuf = null;
		FileInputStream inputString = null;
		Reader fileReaderCLD3 = null;
		
		mode = mode.toLowerCase();
		
		String fileType = getFileType(inputFilePath).toUpperCase();
//		oLog.WriteLog(pageName,tempUUID , "Get file Type as "+fileType,"", false);
		if (!fileType.equals("TXT")){
			inputTypeTXT = false;
			//Prepare output file part
			String inputFileName = getFileName(inputFilePath); 
			
			outputProcessTemp = rp.getProp(Constant.PROCESS_TEMP_PATH)+tempUUID+inputFileName+".txt";
			
			LSFileSystem lsFile = new LSFileSystem(null);
			lsFile.File.Convert.FileType(
					//inputFilePath
					inputFilePath, 
					//OutputFilePath
					outputProcessTemp, 
					//FileFormat
					fileType, 
					//OutputFileFormat
					"TXT");
			boolean checkSupport = new File(outputProcessTemp).exists();
			if(!checkSupport) {
				System.out.println("Can not support file type: *."+fileType.toLowerCase());
//				oLog.WriteLog(pageName,tempUUID , "Wernning Get Language ID Line By Line", "Cannot support file type: *."+fileType.toLowerCase(),  false);
//				oLog.WriteLog(pageName,tempUUID ,  "Get Language ID Line By Line", "End AbNormally", false);
				return "Can not support file type: *."+fileType.toLowerCase();
			}
			readerBuf = new BufferedReader(new FileReader(outputProcessTemp));
			inputString = new FileInputStream(outputProcessTemp);
			fileReaderCLD3 = new FileReader(outputProcessTemp);
			if(!mode.equals("cld3")) {
				result = langService.LanguageidFromFileForCLD2(outputProcessTemp);	
			}
		}else {
			readerBuf = new BufferedReader(new FileReader(inputFilePath));
			inputString = new FileInputStream(inputFilePath);
			fileReaderCLD3 = new FileReader(inputFilePath);
			if(!mode.equals("cld3")) {
				result = langService.LanguageidFromFileForCLD2(inputFilePath);	
			}
		}
		
		
		if(!mode.toUpperCase().equals("CLD3")) {
			
			//Read total line number
//			BufferedReader readerBuf = new BufferedReader(new FileReader(inputFilePath));
			
			while(readerBuf.readLine() != null) {
				totalLines++;
			}
			//System.out.println("Total Line: "+totalLines);
			readerBuf.close();
			
			//Process Big Data
			if(totalLines > Constant.MAX_LINE) {
				
				//Read file
			    StringBuffer inputBuf = new StringBuffer();
				
//				FileInputStream inputString = new FileInputStream(inputFilePath);
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
							if(countLineCondition > Constant.MAX_LINE) {
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
			
//			result = langService.LanguageidFromFileForCLD2(inputFilePath);
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
//			BufferedReader readerBuf = new BufferedReader(new FileReader(inputFilePath));
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
			resultStatus = Constant.EMPTY_STRING;

		    BufferedReader br = new BufferedReader(fileReaderCLD3);  
		    StringBuffer inputBuf = new StringBuffer();
			try {
				String sent = null;
				//process each line	
			      while ((sent = br.readLine()) != null){
			    	  progress++;
			    	  countForWriteFile++;
			    	  String rawResult = GetLanguageIDGeneral(Constant.EMPTY_STRING, sent, mode);
			    	//  TimeUnit.MILLISECONDS.sleep(1);
			    	  
			    	  String[] rawResultArr = rawResult.split(Constant.COLON);
						
						result = rawResultArr[0]+Constant.COLON+rawResultArr[1].replace("\\t", Constant.TAB);
						
						result = result.replace(Constant.REPLACE_ERROR_NO, Constant.EMPTY_STRING);

						if(result.trim().equals(Constant.DETECT_UNKNOW)) {
							result = Constant.UNKNOW_RESULT+Constant.TAB+sent;
						}


						writeFile(sent, result.trim(), outFilePath);		
						
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
		
		// If input not TXT will delete file temp
		if (!inputTypeTXT) {
			deleteFile(outputProcessTemp);
		}
		
//		oLog.WriteLog(pageName,tempUUID , "Get Language ID Line By Line", "End Normally",  false);
		
		return resultStatus;
	}
	
	private String getFileTemp() {
		String sTempPath = rp.getProp(Constant.TEMP_PATH)+ "/temp";
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
		String modeStr = Constant.EMPTY_STRING;
		
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
	
	public String GetLanguageIDSRTFileDefineMaxLine(String inputFilePath, String modeStr, int MaxLine, int outputMode) {
		String jobID = generateID();
		String result = null;
		//Get Total Line
		int totalLines = 0;
		
		boolean getResultFlag = false;
		if(outputMode == 1) {
			getResultFlag = false;
		}else if(outputMode == 2) {
			getResultFlag = true;
		}
		
		BufferedReader readerBuf = null;
		try {
			readerBuf = new BufferedReader(new FileReader(inputFilePath));
			while(readerBuf.readLine() != null) {
				totalLines++;
			}
			readerBuf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		//Preapre subtitelCount
		 int subtitelCount = 0;
		
		//Prepare dialog Count
		int dialogCount = 0;
		
		StringBuffer inputBuf = new StringBuffer();
		String input = new String();
		
		//String indexPattern = "[\\d]{1,5}[a-z]{0,2}[\\n]";
		String indexPattern = "[0-9]";
		String timeLinePattern = "^[\\d]{2}[:][\\d]{2}[:][\\d]{2}[,][\\d]{3}[ ][-][-][\\>][ ][\\d]{2}[:][\\d]{2}[:][\\d]{2}[,][\\d]{3}";
		
		//Prepare get input	
		FileInputStream inputString;
		try {
			inputString = new FileInputStream(inputFilePath);
			DataInputStream in = new DataInputStream(inputString);
			BufferedReader br1 = new BufferedReader(new InputStreamReader(in));
			
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		FileInputStream inputString2;
		try {
			inputString2 = new FileInputStream(inputFilePath);
			DataInputStream in2 = new DataInputStream(inputString2);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
			
			String sent = null;
			int countLintProgress = 0;
		      while ((sent = br2.readLine()) != null){
	
		    	  int inputLength = sent.length();
		    	  
		    	  if(inputLength > 5) {

						if(!sent.matches(timeLinePattern)) {
							inputBuf = inputBuf.append(sent).append("\n");
							countLintProgress++;
//							if(inputBuf.length() > MaxLine) {
//								break;
//							}
						}
					}else if(inputLength != 0){
						String inputCheck = sent.substring(0, inputLength-1);
						try {
						int inputInt = Integer.parseInt(inputCheck);
						}catch(NumberFormatException e) {
							inputBuf = inputBuf.append(inputCheck).append("\n");
							countLintProgress++;
//							if(inputBuf.length() > MaxLine) {
//								break;
//							}
						}
							
						
					}
					if (MaxLine != 0) {
						if (countLintProgress == MaxLine) {
							break;
						}
					}
		      }	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input = new String(inputBuf);
		result = langService.LanguageIDWFS(jobID, input, modeStr, getResultFlag);
		
		
		return result;
	}
	
	/*** 9 Get Language id for SRT file By set mode parameter 
	 * @throws Exception ***/
	public String GetLanguageIDSRTFile(String inputFilePath, String modeStr) throws Exception {
		com.omniscien.lslanguageid.model.LanguageidModel languaeidModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		String result = null;
		//Get Total line
		int totalLines = 0;
		BufferedReader readerBuf = new BufferedReader(new FileReader(inputFilePath));		
		while(readerBuf.readLine() != null) {
			totalLines++;
		}
		readerBuf.close();
		

		
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
							if(inputBuf.length() > Constant.MAXIMUN_INPUT-30) {
								break;
							}
						}
					}else if(inputLength != 0){
						String inputCheck = sent.substring(0, inputLength-1);
						try {
						int inputInt = Integer.parseInt(inputCheck);
						}catch(NumberFormatException e) {
							inputBuf = inputBuf.append(inputCheck).append(" ");
							if(inputBuf.length() > Constant.MAXIMUN_INPUT-30) {
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
		String[] resultArr = result.split(Constant.COLON);
		String[] resultPercentArr = resultArr[1].split("%");
		String resultPercentStr = resultPercentArr[0]+"%";
		
		result = inputFilePath
				+Constant.TAB
				+totalLines+Constant.TAB
				+subtitelCount
				+Constant.TAB
				+dialogCount
				+Constant.TAB
				+resultArr[0].replace(Constant.DOUBLE_QUOTE, Constant.EMPTY_STRING)
				+Constant.COLON
				+resultPercentStr
				+Constant.TAB
				+resultArr[0].replace(Constant.DOUBLE_QUOTE, Constant.EMPTY_STRING);
		
		
		return result;
	}
	
	/*** 10 Get Language id for SRT file By default mode as CLD2 
	 * @throws Exception ***/
	public String GetLanguageIDSRTFile(String inputFilePath) throws Exception {
		String result = new String();
		String modeStr = Constant.CLD2_MODE;
		
		result = GetLanguageIDSRTFile(inputFilePath, modeStr);
		
		return result;
	}
	
	public String GetLanguageIDFromStringWFS(String input, boolean GetResultFlag) {
		String output = null;
		String mode = Constant.CLD2_MODE;
		
		output = GetLanguageIDFromStringWFS(input, mode, GetResultFlag);
		
		return output;
	}

	public String GetLanguageIDFromStringWFS(String input, String mode, boolean GetResultFlag) {
		String output = null;
		
		String jobID = generateID();
		
		output = GetLanguageIDFromStringWFS(jobID, input, mode, GetResultFlag);
		
		return output;
	}

	public String GetLanguageIDFromStringWFS(String jobID, String input, String mode, boolean GetResultFlag) {
		String result = null;
		result = langService.LanguageIDWFS(jobID, input, mode, GetResultFlag);
		return result;
	}
	
	public String GetLanguageIDFromFileWFS(String inputFilePath, boolean GetResultFlag) {
		String output = null;
		String mode = Constant.CLD2_MODE;
		
		output = GetLanguageIDFromFileWFS(inputFilePath, mode, GetResultFlag);
		
		return output;
		
		
	}

	public String GetLanguageIDFromFileWFS(String inputFilePath, String mode, boolean getResultFlag) {
		String output = null;
		
		String jobID = generateID();
		
		output = GetLanguageIDFromFileWFS(jobID, inputFilePath, mode, getResultFlag);
		
		return output;
	}
	
	/*** Start Add max Line 2021-05-11 ***/
	public String GetLanguageIDFromTextWFS(String inputFilePath, int InputFormat, int maxLine, int AnalysisType ,  int OutputMode) {
		com.omniscien.lslanguageid.model.LanguageidModel languaeidModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		
		//Start Time
		begintime = System.currentTimeMillis();
		startTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS")).toString();
		
		boolean getResultFlag = false;
		if(OutputMode == 1) {
			getResultFlag = false;
		}else if(OutputMode == 2) {
			getResultFlag = true;
		}
		
		String mode = null;
		if(AnalysisType == 1) {
			mode = "cld2";
		}else if (AnalysisType == 2){
			mode = "cld3";
		}else {
			
			String result = getErrorJSON(10047);
			return result;
		}
		
		// Initial variable;
		String result = new String();
		
		String jobID = generateID();
		boolean checkSupport = false;
//		oLog.WriteLog(pageName,jobID , "Get Language ID String", "Start",  false);
		
		//Get file type
		String fileType = getFileType(inputFilePath).toLowerCase();
//		oLog.WriteLog(pageName,jobID , "Get file Type as "+fileType,"", false);
		
		//Check support
		String[] filesSupportArr = {"msg", "html", "htm", "txt", "srt", "ttml"};
		for(String fileSupport : filesSupportArr) {
			checkSupport = false;
			if(fileSupport.equals(fileType)) {
				checkSupport = true;
				break;
			}
		}
		
		if (!checkSupport) {
			String resultReturn = getErrorJSON(10100);
			return resultReturn;
		}
		
		if(InputFormat != 4) {
			

			
			boolean inputTypeTXT = true;
			String outputProcessTemp = null;

			// Convert Input file to TXT
			if (!fileType.equals("txt")) {
				inputTypeTXT = false;
				// Prepare output file part
				String inputFileName = getFileName(inputFilePath);

				outputProcessTemp = rp.getProp(Constant.PROCESS_TEMP_PATH) + jobID + inputFileName + ".txt";
				LSFileSystem lsFile = new LSFileSystem(null);
				try {
					lsFile.File.Convert.FileType(
							// inputFilePath
							inputFilePath,
							// OutputFilePath
							outputProcessTemp,
							// FileFormat
							fileType,
							// OutputFileFormat
							"TXT");
					checkSupport = new File(outputProcessTemp).exists();
					if (!checkSupport) {
						return getErrorJSON(10100);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// If maxLine == 0 get All file
			if (maxLine == 0) {
				try {
					if(inputTypeTXT) {
						result = langService.LanguageidFromFileForWFS(inputFilePath, getResultFlag);
					}else {
						result = langService.LanguageidFromFileForWFS(outputProcessTemp, getResultFlag);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// get from MaxLine Input
			else {
				try {
					FileInputStream fstream = null;
					if(inputTypeTXT) {
						fstream = new FileInputStream(inputFilePath);
					}else {
						fstream = new FileInputStream(outputProcessTemp);
					}
					
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					int countLineProgress = 0;

					String strLine;
					StringBuffer inputFromReadTXT = new StringBuffer();
					while ((strLine = br.readLine()) != null) {
						countLineProgress++;
						inputFromReadTXT.append(strLine).append("\n");
						if (countLineProgress == maxLine) {
							break;
						}
					}
					result = langService.LanguageIDWFS(jobID, inputFromReadTXT.toString(), mode, getResultFlag);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			if(fileType.equals("srt")) {
				result = GetLanguageIDSRTFileDefineMaxLine(inputFilePath, mode,  maxLine, OutputMode);
			}else if(fileType.equals("ttml")) {
				result = GetLanguageIDTTMLFile(inputFilePath, mode,  maxLine, OutputMode);
			}else {
				return getErrorJSON(10100);
				
			}
		}

		
		result = prepareFinalResult(result);
		
		return result;
	}
	


	public String GetLanguageIDFromFileWFS(String inputFilePath, int maxLine, int AnalysisType ,  int OutputMode, String OutputFile) {
		com.omniscien.lslanguageid.model.LanguageidModel languaeidModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		
		//Start Time
		begintime = System.currentTimeMillis();
		startTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS")).toString();
		
		boolean getResultFlag = false;
		if(OutputMode == 1) {
			getResultFlag = false;
		}else if(OutputMode == 2) {
			getResultFlag = true;
		}
		
		String mode = null;
		if(AnalysisType == 1) {
			mode = "cld2";
		}else if (AnalysisType == 2){
			mode = "cld3";
		}else {
			String result = getErrorJSON(10047);
			return result;
		}
		
		// Initial variable;
		String result = new String();
		
		String jobID = generateID();
		boolean checkSupport = false;
//		oLog.WriteLog(pageName,jobID , "Get Language ID String", "Start",  false);
		
		//Get file type
		String fileType = getFileType(inputFilePath).toLowerCase();
//		oLog.WriteLog(pageName,jobID , "Get file Type as "+fileType,"", false);
		
		//Check support
		String[] filesSupportArr = {"doc", "docx","xls","xlsx", "ppt", "pptx", "msg","pdf", "html", "htm", "txt", "srt", "ttml"};
		for(String fileSupport : filesSupportArr) {
			checkSupport = false;
			if(fileSupport.equals(fileType)) {
				checkSupport = true;
				break;
			}
		}
		
		if (!checkSupport) {
			String resultReturn = getErrorJSON(10100);
			return resultReturn;
		}
		
		if(fileType.equals("doc") ||
				fileType.equals("docx") || 
				fileType.equals("xls") || 
				fileType.equals("xlsx") || 
				fileType.equals("ppt") || 
				fileType.equals("pptx") || 
				fileType.equals("msg") || 
				fileType.equals("pdf") || 
				fileType.equals("html") || 
				fileType.equals("htm") || 
				fileType.equals("txt")) {
			

			
			boolean inputTypeTXT = true;
			String outputProcessTemp = null;

			// Convert Input file to TXT
			if (!fileType.equals("txt")) {
				inputTypeTXT = false;
				// Prepare output file part
				String inputFileName = getFileName(inputFilePath);

				outputProcessTemp = rp.getProp(Constant.PROCESS_TEMP_PATH) + jobID + inputFileName + ".txt";
				LSFileSystem lsFile = new LSFileSystem(null);
				try {
					lsFile.File.Convert.FileType(
							// inputFilePath
							inputFilePath,
							// OutputFilePath
							outputProcessTemp,
							// FileFormat
							fileType,
							// OutputFileFormat
							"TXT");
					checkSupport = new File(outputProcessTemp).exists();
					if (!checkSupport) {
						String resultReturn = getErrorJSON(10100);
						return resultReturn;	
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// If maxLine == 0 get All file
			if (maxLine == 0) {
				try {
					if(inputTypeTXT) {
						result = langService.LanguageidFromFileForWFS(inputFilePath, getResultFlag);
					}else {
						result = langService.LanguageidFromFileForWFS(outputProcessTemp, getResultFlag);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// get from MaxLine Input
			else {
				try {
					FileInputStream fstream = null;
					if(inputTypeTXT) {
						fstream = new FileInputStream(inputFilePath);
					}else {
						fstream = new FileInputStream(outputProcessTemp);
					}
					
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					int countLineProgress = 0;

					String strLine;
					StringBuffer inputFromReadTXT = new StringBuffer();
					while ((strLine = br.readLine()) != null) {
						countLineProgress++;
						inputFromReadTXT.append(strLine).append("\n");
						if (countLineProgress == maxLine) {
							break;
						}
					}
					result = langService.LanguageIDWFS(jobID, inputFromReadTXT.toString(), mode, getResultFlag);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			if(fileType.equals("srt")) {
				result = GetLanguageIDSRTFileDefineMaxLine(inputFilePath, mode,  maxLine, OutputMode);
			}else if(fileType.equals("ttml")) {
				result = GetLanguageIDTTMLFile(inputFilePath, mode,  maxLine, OutputMode);
			}else {
				String resultReturn = getErrorJSON(10100);
				return resultReturn;
			}
		}
		
		result = prepareFinalResult(result);
		
		if(OutputFile != null) {
			File file = new File(OutputFile);
			if(file.exists()) {
				file.delete();
			}
			writeFile(result, result, OutputFile);
		}
		
		
		return result;
	}
	
	public String GetLanguageIDTTMLFile(String inputFilePath, String mode, int maxLine, int outputMode) {
		String jobID = generateID();
		int lineCountProgress = 0;
		String result = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		boolean getResultFlag = false;
		if(outputMode == 1) {
			getResultFlag = false;
		}else if(outputMode == 2) {
			getResultFlag = true;
		}
		
		StringBuffer inputTemp = new StringBuffer();
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(inputFilePath));
		    NodeList nodeList=document.getElementsByTagName("*");
		    for (int i=0; i<nodeList.getLength(); i++) {
		    	

		        // Get element
		        Element element = (Element)nodeList.item(i);
		        if(element.getNodeName().equals("p")) {
		        	lineCountProgress++;
		        	inputTemp.append(element.getChildNodes().item(0).getNodeValue()).append("\n");
		        }
		        
		    	if(maxLine != 0){
		    		if(lineCountProgress == maxLine) {
		    			break;
		    		}
		    	}
		    }
//		    System.out.println(inputTemp.toString());
		    result = langService.LanguageIDWFS(jobID, inputTemp.toString(), mode, getResultFlag);
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	


	/*** End Add max Line 2021-05-11 ***/
	
	public String GetLanguageIDFromFileWFS(String jobID, String inputFilePath, String mode, boolean getResultFlag) {
		
		com.omniscien.lslanguageid.model.LanguageidModel languaeidModel = new com.omniscien.lslanguageid.model.LanguageidModel();
		
//		oLog.WriteLog(pageName,jobID , "Get Language ID for WFS From File", "Start",  false);
		
		//Initial variable;
		String result = new String();
		Reader inputString = null;
		boolean inputTypeTXT = true;
		String outputProcessTemp = null;
		
		if(jobID == null || jobID.equals("")) {
			jobID = generateID();
		}
		
		//Prepare Input 
		String inputStr = new String();
		
		//Get file type
		String fileType = getFileType(inputFilePath).toUpperCase();
//		oLog.WriteLog(pageName,jobID , "Get file Type as "+fileType,"", false);
		
		if (!fileType.equals("TXT")){
			inputTypeTXT = false;
			//Prepare output file part
			String inputFileName = getFileName(inputFilePath); 
			
			outputProcessTemp = rp.getProp(Constant.PROCESS_TEMP_PATH)+jobID+inputFileName+".txt";
			LSFileSystem lsFile = new LSFileSystem(null);
			try {
				lsFile.File.Convert.FileType(
						//inputFilePath
						inputFilePath, 
						//OutputFilePath
						outputProcessTemp, 
						//FileFormat
						fileType, 
						//OutputFileFormat
						"TXT");
				boolean checkSupport = new File(outputProcessTemp).exists();
				if(!checkSupport) {
					return "{\"errorstatus\":\"yes\",\"dominantlanguage\":\"\",\"dominantlanguagepercent\":\"\",\"secondarylanguage\":\"\",\"secondarylangpercent\":\"\"}";
				}
				result = langService.LanguageidFromFileForWFS(outputProcessTemp, getResultFlag);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				result = langService.LanguageidFromFileForWFS(inputFilePath, getResultFlag);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// If input not TXT will delete file temp
		if (!inputTypeTXT) {
			deleteFile(outputProcessTemp);
		}
		
//		oLog.WriteLog(pageName,jobID , "Get Language ID for WFS From File", "End Normally",  false);
		return result;
	}
	
	private String getErrorJSON(int errorNo) {
		endtime = System.currentTimeMillis();
		totaltime = endtime-begintime;
		endTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS")).toString();
		
		String ErrorMSG = com.omniscien.lib.util.Message.getMessage(errorNo);
		String result = "{\"result\":"
				+ "{\"dominantlanguage\":\"\",\"dominantlanguagepercent\":\"\",\"secondarylanguage\":\"\",\"secondarylangpercent\":\"\"},"
				+ "\"duration\":\""+totaltime+"\","
				+ "\"startdate\":\""+startTimeStr+"\","
				+ "\"errortext\":\""+ErrorMSG+"\","
				+ "\"errorno\":\""+errorNo+"\","
				+ "\"enddate\":\""+endTimeStr+"\","
				+ "\"requestid\":\"\"}";
		
		return result;
	}
	
	private String prepareFinalResult(String result) {
		endtime = System.currentTimeMillis();
		totaltime = endtime-begintime;
		endTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS")).toString();
		String finalResult = "{\"result\":"
				+result+","
						+ ""
						+ "\"duration\":\""+totaltime+"\","
						+ "\"startdate\":\""+startTimeStr+"\","
						+ "\"errortext\":\"\","
						+ "\"errorno\":\"\","
						+ "\"enddate\":\""+endTimeStr+"\","
						+ "\"requestid\":\"\"}";
		return finalResult;
	}
	
}
