package com.omniscien.lslanguageid.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;

import com.omniscien.lslanguageid.model.ServletContextMock;



public class Log4J {
	public String _sServiceName = "LSRestAPI";

	private ServletContextMock _app = null;
	public String log4JPropertyFile = "";
	public Level lLevel = Level.ERROR;
	public String debugPath = "";
	private String logPath = "";
	public Boolean debugMode = false;
	public String sServerIP = "";
	String loggerDebug = "log-debug";
	private DateFormat dateSQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	


	public Log4J() {
		// TODO Auto-generated constructor stub
	}







	public void Log4J(ServletContextMock app, ReadProp rp) throws Exception {
		try {

			//String sDebugPath = app.getInitParameter("logpath");
			//String sLog4JPropertyFile = app.getInitParameter("log4jfilepath");
			//String sDebugMode = app.getInitParameter("debugmode");
			/*
			if (oCommon.isWindows())
				log4JPropertyFile = "/var/www/lse/log4j.xml";
			else
				log4JPropertyFile = sLog4JPropertyFile;
			*/
			if (log4JPropertyFile == null || log4JPropertyFile.trim().length() == 0)
//				log4JPropertyFile = "/var/www/lse/log4j.xml";
				log4JPropertyFile = rp.getProp(Constant.LOG_4J);
			else if (log4JPropertyFile.trim().length() > 0 && !FileExists(log4JPropertyFile))
//				log4JPropertyFile = "/var/www/lse/log4j.xml";
				log4JPropertyFile = rp.getProp(Constant.LOG_4J);

			_app = app;
			
			//debugMode = sDebugMode;		
			if (debugMode == null)
				debugMode = false;
			
			//debugPath = sDebugPath;
			if (debugPath == null || debugPath.trim().length() == 0) {
//				debugPath = "/var/www/logs/lse-logs/msoffice/";
				debugPath = Constant.LOG_PATH;
				logPath = combine(debugPath, "logs/");
			}
			
			if (logPath == null || logPath.trim().length() == 0)
//				logPath = "/var/www/logs/lse-logs/msoffice/logs/";
				logPath = Constant.LOG_PATH+"logs/";
			
			File dir = new File(debugPath);
			if (!dir.exists()) {
				// create directory
				boolean cancreate = dir.mkdirs();
			}
			initailizelog4j(log4JPropertyFile);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	






	public void setDebugPath(String path) {
		debugPath = path;
		logPath =  combine(debugPath, "logs/");
	}
	
	public String combine(String path1, String path2) {
		String path = "";
		File file1 = new File(path1);
		if (isWindows()){
			if(!path2.startsWith("\\")){
				path2 = "\\"+path2;
			}
			File file2 = new File(file1, path2);
			path = file2.getPath();
			path = path.replace("/", "\\");
		}
		else{
			if(!path2.startsWith("/")){
				path2 = "/"+path2;
			}
			File file2 = new File(file1, path2);
			path = file2.getPath();
			path = path.replace("\\", "/");
		}
		
		return path;
	}
	
	private boolean FileExists(String FilePath) {
		try {
			if (FilePath == null || FilePath.trim().length() == 0)
				return false;
			FilePath = checkPath(FilePath);
			java.io.File oFile = new java.io.File(FilePath);
			return oFile.exists();
		} catch (Exception e) {
			return false;
		}
	}
	
	private String checkPath(String path) {
		if (isWindows())
			path = path.replace("/", "\\");
		else
			path = path.replace("\\", "/");
		return path;
	}
	
	private boolean isWindows() {

		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);

	}
	
	private void initailizelog4j(String log4JPropertyFile) throws Exception {
		String sCacheName = "LSRESTAPI_LOADED";
		if (_app != null && _app.getAttribute(sCacheName) != null) {
			String loaded = _app.getAttribute(sCacheName).toString();
		} else {
			System.setProperty("log4j.configurationFile", log4JPropertyFile);
			_app.setAttribute(sCacheName, "Loaded");
		}
	}

	private void reconfiglog4j(String debugPath, String filename) {
		ThreadContext.put("logPath", debugPath);
		ThreadContext.put("logName", filename);
	}
	
	private void updateLevellog4j(String loggerName, Level level) {
		// update level config
		/*
		 * LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		 * Configuration config = ctx.getConfiguration(); LoggerConfig
		 * loggerConfig = config.getLoggerConfig(loggerName);
		 * loggerConfig.setLevel(level); ctx.updateLoggers(config);
		 */
	}

	public void writeDebugLog(String message) {
		try {
			writeLog(message, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeDebugLog(String message, Exception ex) {
		try {
			if (ex != null)
				message = message + " ExceptionError:" + getStackTrace(ex);
			writeLog(message, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeDebugLog(String message, String id, Exception ex) {
		try {
			if (id != null && id.trim().length() > 0)
				message = "id=" + id + ", " + message;
			
			if (ex != null)
				message = message + " ExceptionError:" + getStackTrace(ex);
			writeLog(message, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeError(String MethodName, String message) {
		try {
			
			message = "MethodName=" + MethodName + "\t" + "Message=" + message;
			
			writeLog(message, true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeError(String MethodName, String id, Exception ex) {
		try {
			
			if (id != null && id.trim().length() > 0)
				MethodName += ", id=" + id;
			
			String message = "MethodName=" + MethodName + "\t" + "Message=" + getStackTrace(ex);
			
			writeLog(message, true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeError(String MethodName, Exception ex) {
		try {
			
			String message = "MethodName=" + MethodName + "\t" + "Message=" + getStackTrace(ex);
			
			writeLog(message, true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeError(String message) {
		try {
			
			writeLog(message, true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeLog(String message, boolean isError) {
		try {
			if (isError)
				error(message);
			else if (lLevel != Level.OFF) {
				if (debugMode)
					debug(message);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// get level from database
	public void debug(String message, String sDebugFolder, String sFileName) throws Exception {
		try {
			if (message != null) {

				reconfiglog4j(sDebugFolder, sFileName);

				Calendar oCal = Calendar.getInstance();
				SimpleDateFormat oDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				
				String sContent = oDateTimeFormat.format(oCal.getTime());
				sContent += ", " + message + "\r\n";
				
				LogManager.getLogger(loggerDebug).debug(sContent);

			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log debug Error=" + ex.toString());

		}
	}
		
	// get level from database
	public void debug(String message) throws Exception {
		try {
			if (message != null) {
				String sFileName = "LSLanguageID-" + getFileName("yyyyMMdd-HH") + "-debug.txt";

				reconfiglog4j(logPath, sFileName);

				Calendar oCal = Calendar.getInstance();
				SimpleDateFormat oDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				
				String sContent = oDateTimeFormat.format(oCal.getTime());
				sContent += ", " + message + "\r\n";
				
				LogManager.getLogger(loggerDebug).debug(sContent);

			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log debug Error=" + ex.toString());

		}
	}

	public void info(String message) throws Exception {
		try {
			if (message != null) {
				String sFileName = "LSLanguageID-" + getFileName("yyyyMMdd-HH") + "-info.txt";

				reconfiglog4j(logPath, sFileName);
				LogManager.getLogger(loggerDebug).info(message + "\n");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log info Error=" + ex.toString());

		}
	}

	public void info(String message, String sFileName) throws Exception {
		try {
			if (message != null) {
				reconfiglog4j(logPath, sFileName);
				LogManager.getLogger(loggerDebug).info(message + "\n");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log info Error=" + ex.toString());

		}
	}

	public void warn(String message) throws Exception {
		try {
			if (message != null) {
				String sFileName = "LSLanguageID-" + getFileName("yyyyMMdd-HH") + "-warn.txt";

				reconfiglog4j(logPath, sFileName);
				LogManager.getLogger(loggerDebug).warn(message + "\n");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log warn Error=" + ex.toString());

		}
	}
	public void error(String message) throws Exception {
		try {
			if (message != null) {
				String sFileName = "LSLanguageID-" + getFileName("yyyyMMdd-HH") + "-error.txt";
					
				reconfiglog4j(logPath, sFileName);
				LogManager.getLogger(loggerDebug).error(message + "\n");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log error Error=" + ex.toString());

		}
	}
	
	public void error(String message, String sDebugFolder, String sFileName) throws Exception {
		try {
			if (message != null) {
//				String sFileName = "error-" + getFileName("yyyyMMdd-HH") + ".txt";

				reconfiglog4j(sDebugFolder, sFileName);
				LogManager.getLogger(loggerDebug).error(message + "\n");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log error Error=" + ex.toString());

		}
	}

	public void errorRetry(String method, String message)
			throws Exception {
		try {
			if (method != null && message != null) {
				if (!method.equals("null")) {
					String errormessage = "Method: " + method + " Error:" + message;

					String sFileName = "error-retry-" + getFileName("yyyyMMdd-HH") + ".txt";

					reconfiglog4j(logPath, sFileName);
					LogManager.getLogger(loggerDebug)
							.error(errormessage + "\n");

				}
			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log error Error=" + ex.toString());

		}
	}

	public void fatal(String message) throws Exception {
		try {
			if (message != null) {
				String sFileName = "LSLanguageID-" + getFileName("yyyyMMdd-HH") + "-fatal.txt";

				reconfiglog4j(logPath, sFileName);
				LogManager.getLogger(loggerDebug).fatal(message + "\n");
			}
		} catch (Exception ex) {
			// TODO: handle exception
			System.out.println("Log fatal Error=" + ex.toString());

		}
	}

	public String getFileName(String pattern) {
		// "yyyyMMdd"
		// "yyyy-MM-dd HH:mm:ss"
		try {
			Calendar oCal = Calendar.getInstance();
			SimpleDateFormat oDateTimeFormat = new SimpleDateFormat(pattern);
			String filename = oDateTimeFormat.format(oCal.getTime());
			return filename;
		} catch (Exception e) {
			// TODO: handle exception

		}
		return "error.txt";
	}

	public String getRequestId(ServletContextMock _application, String serverIP) {
		String sRequestId = "";
		String sRequestIdKey = "LSRestRequestIdKey";
		try {
			//request ID format = yyyyMMdd-HHmmss.SSS-123.456.000001 (datetime+2lastRestHostIP+6digits)
			if (_application != null)
				_app = _application;	
			
			String sLast2SegmentIP = serverIP.replaceAll("(^(\\d{1,3}[.]){2})", "");
			String sDateTimeFormat = getFileName("yyyyMMdd-HHmmss.SSS");
			
			String sOldKey = "", sNumber = "";
			if (_app != null && _app.getAttribute(sRequestIdKey) != null) {
				sOldKey = _app.getAttribute(sRequestIdKey).toString();
				String[] saOldRequestId = sOldKey.split("[-]");
				String sOldDateTimeFormat = saOldRequestId.length > 2 ? saOldRequestId[0] + "-" + saOldRequestId[1]: "";
				String sOldIPNumber = saOldRequestId.length > 2 ? saOldRequestId[2]: "";
				String sOldIP = "", sOldNumber = "";
				if (sOldIPNumber.trim().length() > 0) {
					String[] saIPNumber = sOldIPNumber.split("[.]");
					sOldIP = saIPNumber.length == 3? saIPNumber[0] + "." + saIPNumber[1] : saIPNumber.length == 2? saIPNumber[0] : "";
					sOldNumber = saIPNumber.length == 3? saIPNumber[2] : saIPNumber.length == 2? saIPNumber[1] : "0";
				}
				
				//compare time and ip
				if (sOldDateTimeFormat.trim().length() > 0 && sOldIP.trim().length() > 0 && (sDateTimeFormat + "-" + sLast2SegmentIP).equals(sOldDateTimeFormat + "-" + sOldIP)) {
					sNumber = sOldNumber.trim().length() > 0? sOldNumber : "0";
					sNumber = String.valueOf(Integer.parseInt(sNumber) + 1);
				}
			}
			
			if (sNumber.trim().length() == 0)
				sNumber = "1";
			
			sRequestId = sDateTimeFormat + "-" + sLast2SegmentIP + "." + String.format("%06d", Integer.parseInt(sNumber));
			
			if (_app != null) {
				_app.setAttribute(sRequestIdKey, sRequestId);
			}
			
		} catch (Exception e) {
			UUID uuid = UUID.randomUUID();
			sRequestId = uuid.toString();
		}
		return sRequestId;
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

	public String ResetApplication(javax.servlet.ServletContext application_) throws Exception {
		String sOutput = "";
		try {
			if (application_ == null)
				return "";

			Enumeration<?> e = application_.getAttributeNames();
			while (e.hasMoreElements()) {
				String sKeyName = (String) e.nextElement();

				if (!sKeyName.contains("apache") && !sKeyName.contains("javax")) {
					if (application_.getAttribute(sKeyName) != null) {
						application_.removeAttribute(sKeyName);
					}
				}
			}

			writeDebugLog("Log.java ResetApplication: Reset Application Success");
			
			sOutput = "Reset Application Success";			
			

		} catch (Exception ex) {
			writeLog("Log.java ResetApplication Error=" + getStackTrace(ex), true);
			sOutput = ex.getMessage();
		}
		return sOutput;
	}
	
	public void WriteDebugLogs(String sDebugFolder, String sFileName, String sText, boolean isError)
    {
		try {
			if (isError)
				error(sText, sDebugFolder, sFileName);
			else if (lLevel != Level.OFF) {
				if (debugMode)
					debug(sText, sDebugFolder, sFileName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void WriteLog(String sFileName, String sMethodName, String sMessage, String id, boolean isError) {
		try {
			String sTitle = "";
			if (sFileName != null && sFileName.trim().length() > 0)
				sTitle = sFileName + ": ";
			if (sMethodName != null && sMethodName.trim().length() > 0)
				sTitle += sMethodName + ", ";
			if (id != null && id.trim().length() > 0)
				sTitle += id + " ";
			
			if (isError)
				error(sTitle + sMessage);
			else if (lLevel != Level.OFF) {
				if (debugMode)
					debug(sTitle + sMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
