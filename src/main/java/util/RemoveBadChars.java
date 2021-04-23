package util;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveBadChars {
	private CommonLanguageid oCommon = new CommonLanguageid();
	
	public RemoveBadChars() {
		
	}
	
	public class Replacement {
		public String rowno;
		public String source;
		public String target;
		public String option;
	}
	
	public HashMap<Integer, Object> GetRuleList(String sRuleList) throws Exception {
		HashMap<Integer, Object> hmRules = new HashMap<Integer, Object>();
		String sWordSearch = "";
		try {			
			Pattern tExpression = Pattern.compile("\t", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			sRuleList = oCommon.CheckCarriageReturn(sRuleList);
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

						source = oCommon.ChkNullStrObj(arrWordSearch[0]);
						target = oCommon.ChkNullStrObj(arrWordSearch[1]);

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
	
	public String ReplaceRules(String sInput, HashMap<Integer, Object> hmRules)
			throws Exception {

		if (hmRules != null && hmRules.size() > 0) {
			String sFind, sReplace;
			String sPattern = "";
			String sOption = "";
			int iOption;
			String sInputLower = sInput.toLowerCase();
			boolean bContrain = true;
			for (int i = 0; i < oCommon.GetHashSize(hmRules); i++) {
				Replacement oReplacement = (Replacement) hmRules.get(i);

				sFind = oReplacement.source.toString();
				sReplace = oReplacement.target.toString();

				// case \r\n , \n
				sReplace = oCommon.CheckRegular(sReplace);

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
										bMatchEncrypt = oCommon.IsMatchEncryption(org, "_ao[\\w]*[0-9]{1,}ao_", m.start(), m.group().length());
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
	
}
