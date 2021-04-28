package com.omniscien.lslanguageid.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.omniscien.lslanguageid.util.RemoveBadChars.Replacement;



public class CommonLanguageid {
	
	
	
	public CommonLanguageid() {
	
		
	}
	
	public class Replacement {
		public String rowno;
		public String source;
		public String target;
		public String option;
	}
	
	public String trimLog(String content)
	{	
		String out = content.split("\n")[0].substring(0,  content.split("\n")[0].length() > 20 ? 20 : content.split("\n")[0].length());
		if (out.length() == 20) out += "...";
		return out;
	}
	public String normalizeLineFeed(String content)
	{
		return content.replace("\r\n", "\n").replace("\r", "\n");
	}
	public int GetWordCount(String text)
	{
		return CleanAllTag(text).split("\\s+").length;
	}
	   //
    private String CleanAllTag(String contentText)
    {
    	contentText = ReplaceByPattern(contentText, "<.*?>", "", Pattern.DOTALL);
    	contentText = ReplaceByPattern(contentText, "</.*?>", "", Pattern.DOTALL);
    	
    	//unescape more than 1 time to make sure 
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	contentText = StringEscapeUtils.unescapeHtml3(contentText);
    	//
    	
    	contentText = ReplaceByPattern(contentText, ">\\s+<", "><", Pattern.DOTALL);
    	return contentText.replaceAll("\\s+", " ").trim();
    }
    public  String ReplaceByPattern(String input, String pattern, String replacement, int regexOptions)
    {
        String resultString = "";
        Pattern regex = Pattern.compile(pattern, regexOptions);
        Matcher regexMatcher = regex.matcher(input);
        try
        {
            resultString = regexMatcher.replaceAll(replacement);
        }
        catch(IllegalArgumentException ex) { }
        catch(IndexOutOfBoundsException ex) { }
        return resultString;
    }

	public String round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		//System.out.println(value);
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(bd.doubleValue());
	}
	
	public String getDateString(Calendar cal)
	{	
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
		if (cal != null)
			return format1.format(cal.getTime());
		return "";
	}
	
	
	public String getModelPath(String sModelPath, String sModelName, String sToolType) {
		if (sModelPath == null || sModelPath.trim().length() == 0) {
			String sLocalPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			File fPath = new File(sLocalPath);
			if (fPath.isFile()) {
				sLocalPath = sLocalPath.substring(0, sLocalPath.lastIndexOf("/"));
			} else if (!fPath.exists())
				fPath.mkdirs();	
			
			sLocalPath = sLocalPath.endsWith("/")? sLocalPath : sLocalPath + "/";			
			sModelPath = sLocalPath + "resources/languageid/" + sToolType + "/" + sModelName;
			return sModelPath;
		}
		else
			return sModelPath + sModelName;
	}
	
	public Boolean fileExists(String pathFile) throws Exception {
		Boolean bExist = false;
		try {
			if (isWindows())
				pathFile = pathFile.replace("/", "\\");
			else
				pathFile = pathFile.replace("\\", "/");

			File file = new File(pathFile);
			if (file.isFile() && file.exists()) {
				bExist = true;
			}
		} catch (Exception ex) { }
		return bExist;
	}
	
	public boolean isWindows(){
		 
		String os = System.getProperty("os.name").toLowerCase();
		//windows
	    return (os.indexOf( "win" ) >= 0); 
 
	}
	
	public String ChkNullStrObj(Object obj) {
		try {
			if (obj == null)
				return "";
			else
				return obj.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public int ChkNullIntObj(Object obj) {
		try {
			if (obj == null)
				return 0;
			else if (obj == "")
				return 0;
			else
				return Integer.parseInt(obj.toString());

		} catch (Exception e) {
			return 0;
		}
	}
	public String CheckCarriageReturn(String sData) throws Exception {
		try {
			if (sData != null && sData.trim().length() > 0) {
				sData = sData.replace("\r\n", "\n");
				sData = sData.replace("\r", "\n");
				sData = sData.replace("\n\n", "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sData;
	}

	public int GetHashSize(HashMap hObj) {
		if (hObj == null)
			return 0;
		else
			return hObj.size();
	}
	
	public String CheckRegular(String input) {
		// case \r\n , \n
		if (input == "\\r\\n") {
			input = input.replace("\\r\\n", "\r\n");
			return input;
		} else if (input == "\\n") {
			input = input.replace("\\n", "\n");
			return input;
		} else if (input == "\\\\n") {
			input = input.replace("\\\\n", "\\n");
			return input;
		}

		if (input.contains("\\r\\n")) {
			input = input.replace("\\r\\n", "\r\n");
			return input;
		}
		if (!input.contains("\r") && input.contains("\\n")) {
			input = input.replace("\\n", "\n");
			return input;
		}

		return input;
	}

	public boolean IsMatchEncryption(String input, String sPatternEncrypt, int iIndex, int iLength) {
		boolean bMatchEncrypt = false;
		String sOut = "";
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile(sPatternEncrypt);
		Matcher m = p.matcher(input);
		while (m.find()) {

			int aoStartIndex = m.start();
			int aoEndIndex = aoStartIndex + m.group().length() - 1;
			if (iIndex >= aoStartIndex && iIndex <= aoEndIndex)
				bMatchEncrypt = true;

			sOut = m.group();
			sOut = Matcher.quoteReplacement(sOut);
			m.appendReplacement(sb, sOut);

		}
		m.appendTail(sb);
		input = sb.toString();

		return bMatchEncrypt;
	}

	public String RemoveBOM(String text) {
			String newStr = "";
			try {
				byte[] bArray = text.getBytes();
				if (bArray[0] == -17) {
					byte[] newArray = new byte[bArray.length - 3];
					System.arraycopy(bArray, 3, newArray, 0, bArray.length - 3);
					newStr = new String(newArray);
				} else {
					newStr = text;
				}
			} catch (Exception e) {
			}
			return newStr;
	}
	
    public String getStackTrace(Exception exception) {
		String text = "";
		Writer writer = null;
		try {
			writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			text = writer.toString();
		} catch (Exception e) {

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {

				}
			}
		}
		return text;
	}
    
    public String prepareInput(String input) {
		String temp = input;

		temp = RemoveBOM(temp);
		temp = temp.replace("\r\n", "\n").replace("\r", "\n");

		// TODO: get config path
		String configPath = Constant.CONFIG_PATH_CLEANBADCHARS;
		temp = RemoveBadChars(temp, configPath);

		return temp;
	}
    
	private String RemoveBadChars(String input, String sConfigFilePath) {

		try {
			// read config file in haspmap *** please use HaspMap instead ArrayList
			String sConfigContent = Read(sConfigFilePath);
			// remove BOM *** please check "RemoveBOM" function first. Is it work?
			sConfigContent = RemoveBOM(sConfigContent);

			// start file and replace
			HashMap<Integer, Object> hmRules = GetRuleList(sConfigContent);

			// search and replace text
			String cleanText = ReplaceRules(input, hmRules);

			// write file
			return cleanText;
		} catch (Exception ex) {
			// ex.printStackTrace();
			// throw ex;
		}

		return input;
	}
	
	public String ReplaceRules(String sInput, HashMap<Integer, Object> hmRules)
			throws Exception {

		if (hmRules != null && hmRules.size() > 0) {
			String sFind, sReplace;
			String sPattern = "";
			String sOption = "";
			int iOption;
			String sInputLower = sInput.toLowerCase();
			boolean bContrain = true;
			for (int i = 0; i < GetHashSize(hmRules); i++) {
				Replacement oReplacement = (Replacement) hmRules.get(i);

				sFind = oReplacement.source.toString();
				sReplace = oReplacement.target.toString();

				// case \r\n , \n
				sReplace = CheckRegular(sReplace);

				sOption = oReplacement.option.toString().toLowerCase();
				if (sOption.equals("cs") || sOption.equals("rcs"))
					iOption = Pattern.MULTILINE;
				else
					iOption = Pattern.MULTILINE | Pattern.CASE_INSENSITIVE;

				String sFindPattern = "";
				if (sOption.equals("rcs") || sOption.equals("rci")) {
					sPattern = sFind;
					// if option = regex then set bContrain=true
					bContrain = true;
				} else {
					// if cs,ci then check sInput contain that word or not.
					// if contain then do process, if not contain then skip
					if (sInputLower.indexOf(sFind.toLowerCase()) >= 0)
						bContrain = true;
					else
						bContrain = false;

					// prepare regular pattern before search
					for (char chr : sFind.toCharArray()) {
						if (chr == '^' || chr == '*' || chr == '$' || chr == '[' || chr == ']')
							sFindPattern += "[\\" + chr + "]";
						else if (chr != ' ')
							sFindPattern += "[" + chr + "]";
						else
							sFindPattern += "[ \t]+";
					}

					//Handle Romanize text need space & punctuation around
					sPattern = "(?<=^|([^a-zA-ZΑ]|\\p{P}))(" + sFindPattern + ")(?=((([^a-zA-ZΑ]|\\p{P})($|\\r\\n|\\r|\\n)?)|$|[0-9]))";
				}

				try {
					// rcs,rci: sPattern is original "target" in file
					// cs,ci: sPattern is regular pattern that build from "target" in file
					if (sFind.trim().length() > 0 && bContrain == true) {
						try {
							Pattern pattern = Pattern.compile(sPattern, iOption);
							if (pattern.matcher(sInput).find()) {
								boolean bMatchEncrypt = false;
								String org = sInput, sReturn = "";
								StringBuffer sb = new StringBuffer();
								Matcher m = pattern.matcher(sInput);
								while (m.find()) {
									if (sOption == "rcs" || sOption == "rci") {
										// protect case match result is "_aoxxxao_"
										bMatchEncrypt = IsMatchEncryption(org, "_ao[\\w]*[0-9]{1,}ao_", m.start(), m.group().length());
									}
									if (bMatchEncrypt == false) {
										// match result is not "_aoxxxao_"
										AtomicReference<String> afBefore = new AtomicReference<String>("");
										AtomicReference<String> afAfter = new AtomicReference<String>("");
										String sOutput = sReplace;
										if (sOption == "rcs" || sOption == "rci") {
											String sIn = m.group();

											Pattern p = Pattern.compile(sPattern, iOption);
											sIn = p.matcher(sIn).replaceAll(sReplace);

											sOutput = sIn;

											KeepSpaceBeforeAfter(sOutput, afBefore, afAfter);
											sOutput = sOutput.trim();
										}

										//2019-06-28 fixed bug space lost after replace
										sReturn = afBefore.get() + sOutput + afAfter.get();
										
									} else
										sReturn = m.group();

									sReturn = Matcher.quoteReplacement(sReturn);
									m.appendReplacement(sb, sReturn);
								}

								if (sb.length() > 0) {
									m.appendTail(sb);
									sInput = sb.toString();
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							throw ex;
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					throw ex;
				}
			}
		}
		return sInput;
	}
	
	private void KeepSpaceBeforeAfter(String sInput, AtomicReference<String> oBefore, AtomicReference<String> oAfter) {

		String before = "", after = "";
		for (char ch : sInput.toCharArray()) {
			String charValue = String.valueOf(ch);
			if (charValue.trim().length() == 0)
				before += charValue;
			else
				break;
		}

		if (sInput.equals(before)) {
			//2019/02/26 Fixed bug if text in line only space
			if (sInput.indexOf("\n") == -1 && isMatch(sInput, "^[ ]+$")) {
				oBefore.set(before);
			}
			return;
		}

		for (int i = sInput.length() - 1; i >= 0; i--) {
			String charValue = String.valueOf(sInput.charAt(i));

			if (charValue.trim().length() == 0)
				after = charValue + after;
			else
				break;
		}

		oAfter.set(after);
		oBefore.set(before);

	}
	private boolean isMatch(String input, String pattern) {
		if (pattern.equals(""))
			return false;
		Pattern p = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);

		while (m.find()) {
			return true;
		}
		return false;
	}
	
	public HashMap<Integer, Object> GetRuleList(String sRuleList) throws Exception {
		HashMap<Integer, Object> hmRules = new HashMap<Integer, Object>();
		String sWordSearch = "";
		try {			
			Pattern tExpression = Pattern.compile("\t", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			sRuleList = CheckCarriageReturn(sRuleList);
			String[] saRuleList = sRuleList.split("\n");
			Integer iIndex = 0;
			String source = "", target = "";
			String sOption = "";
			// *** start prepare config before start do process ***
			for (int i = 0; i < saRuleList.length; i++) {
				sWordSearch = saRuleList[i].toString();
				if (sWordSearch.trim().length() > 0) {
					String[] arrWordSearch = tExpression.split(sWordSearch);
					if (arrWordSearch.length >= 2) {

						source = ChkNullStrObj(arrWordSearch[0]);
						target = ChkNullStrObj(arrWordSearch[1]);

						sOption = GetOptionCaseSensitive(arrWordSearch);

						if (arrWordSearch.length > 3 || sOption.trim().length() == 0)
							continue;

						Replacement oReplacement = new Replacement();
						oReplacement.rowno = String.valueOf(iIndex);
						oReplacement.source = source;
						oReplacement.target = target;
						oReplacement.option = sOption;
						hmRules.put(iIndex, oReplacement);

						iIndex++;
					}
				}
			}
			// *** end prepare config before start do process ***
			
		} catch (Exception ex) {
			hmRules = new HashMap<Integer, Object>();
			ex.printStackTrace();
			throw ex;
		}
		return hmRules;
	}
	
	private String GetOptionCaseSensitive(String[] saWordSearch) {
		String sOption = "";
		Integer iOptionIndex = 3;

		if (saWordSearch.length == iOptionIndex) {
			if (saWordSearch[iOptionIndex - 1].toLowerCase().equals("ci"))
				sOption = "ci";

			else if (saWordSearch[iOptionIndex - 1].toLowerCase().equals("cs")
					|| saWordSearch[iOptionIndex - 1].toLowerCase().equals(""))
				sOption = "cs";
			else if (saWordSearch[iOptionIndex - 1].toLowerCase().equals("rci")
					|| saWordSearch[iOptionIndex - 1].toLowerCase().equals(""))
				sOption = "rci";
			else if (saWordSearch[iOptionIndex - 1].toLowerCase().equals("rcs")
					|| saWordSearch[iOptionIndex - 1].toLowerCase().equals(""))
				sOption = "rcs";

		} else if (saWordSearch.length == iOptionIndex - 1) {
			// default value
			sOption = "cs";
		}
		
		return sOption;
	}
	
	private String Read(String FilePath) throws Exception {
		if (new File(FilePath).isFile()) {
			return FileUtils.readFileToString(new java.io.File(FilePath), "UTF-8");
		} else {
			return "";
		}
	}
	
	public String encryptBlankLine(String input) {
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
	
}
