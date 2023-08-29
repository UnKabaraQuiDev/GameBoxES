package lu.pcy113.pdr.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

public final class Logger {

	private static boolean init = false;
	private static Properties config;
	private static File logFile;
	private static PrintWriter output;
	private static SimpleDateFormat sdf;
	private static String lineFormat, lineRawFormat;

	public static void init(File file) throws FileNotFoundException, IOException {
		if(init)
			close();

		config = new Properties();
		config.load(new FileReader(file));
		
		String dateFormat = config.getProperty("date.format", "dd-MM-yyyy HH:mm:ss");
		sdf = new SimpleDateFormat(dateFormat);
		
		SimpleDateFormat fdf = new SimpleDateFormat(config.getProperty("file.time.format", "dd-MM-yyyy HH-mm-ss"));
		
		String format = config
				.getProperty("file.format", "./logs/log-%CURRENTMS%.txt")
				.replace("%CURRENTMS%", System.currentTimeMillis()+"")
				.replace("%TIME%", fdf.format(Date.from(Instant.now())));

		logFile = new File(format);
		if(!logFile.getParentFile().exists())
			logFile.getParentFile().mkdirs();
		if(!logFile.exists())
			logFile.createNewFile();

		lineFormat = config.getProperty("line.format", "[%TIME%][%LEVEL%](%CLASS%) %MSG%");
		lineRawFormat = config.getProperty("line.rawformat", "[%TIME%][%LEVEL%] %MSG%");

		//output = new SharedPrintStream(System.out, new FileOutputStream(logFile, false), true);
		output = new PrintWriter(new FileOutputStream(logFile), true);
		init = true;
	}

	public static void log(Level lvl, Throwable thr, String msg) {
		log(lvl, msg);
		_log(0, lvl, thr.getClass().getName()+": "+(thr.getLocalizedMessage() != null ? thr.getLocalizedMessage() : thr.getMessage()), true);
		
		StackTraceElement[] el = thr.getStackTrace();
		for(int i = el.length-1; i >= 0; i--) {
			_log(i+1, lvl, el[i].toString(), true);
		}
		
		if(thr.getCause() != null) {
			_log(0 ,lvl, "Caused by: ", true);
			log(lvl, thr.getCause(), msg);
		}
	}
	
	public static void log(Level lvl, String msg) {
		_log(0, lvl, msg, false);
	}
	
	public static void log(Level lvl, String msg, Object... objs) {
		log(lvl, msg);
		for(int i = 0; i < objs.length; i++) {
			_log(i+1, lvl, objs[i].toString(), true);
		}
	}
	
	private static void _log(int depth, Level lvl, String msg, boolean raw) {
		String content = null;
		if(raw)
			content = 
				(lineRawFormat
				.replace("%TIME%", sdf.format(Date.from(Instant.now())))
				.replace("%LEVEL%", lvl.toString())
				.replace("%CLASS%", getCallerClassName(false))
				.replace("%CURRENTMS%", System.currentTimeMillis()+"")
				.replace("%MSG%", (depth > 0 ? indent(depth) : "")+msg));
		else
			content = 
				(lineFormat
				.replace("%TIME%", sdf.format(Date.from(Instant.now())))
				.replace("%LEVEL%", lvl.toString())
				.replace("%CLASS%", getCallerClassName(false))
				.replace("%CURRENTMS%", System.currentTimeMillis()+"")
				.replace("%MSG%", (depth > 0 ? indent(depth) : "")+msg));
		
		System.out.println(content);
		output.println(content);
	}

	private static String indent(int depth) {
		String s = "";
		for(int i = 0; i < Math.max(0, 5-(depth+"").length()); i++)
			s += " ";
		return depth+s;
	}

	public static String getCallerClassName(boolean parent) { 
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i=1; i<stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(Logger.class.getName())/* && ste.getClassName().indexOf("java.lang.Thread")!=0*/) {
				if(!parent)
					return ste.getClassName()+"#"+ste.getMethodName()+"@"+ste.getLineNumber();
				else {
					ste = stElements[i+1];
					return ste.getClassName()+"#"+ste.getMethodName()+"@"+ste.getLineNumber();
				}

			}
		}
		return null;
	 }
	
	public static void close() {
		if(init) {
			output.flush();
			output.close();
			//output.closeSecondary();
			
			logFile = null;
			init = false;
		}
	}
	
	public static void log(Object string) {
		log(Level.FINEST, string == null ? "null" : string.toString());
	}
	public static void log() {
		log(Level.FINEST, "<- "+getCallerClassName(true));
	}

}
