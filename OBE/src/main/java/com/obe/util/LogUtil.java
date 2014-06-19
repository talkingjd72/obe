package com.obe.util;

import org.jboss.logging.Logger;

public class LogUtil {

	public void warn(String message) {
		logger.warn(message);
	}
	
	public void error(String message) {
		logger.error(message);
	}
	
	public void info(String message)	{
		logger.info(message);
	}
	
	public void trace(String message) {
		logger.trace(message);
	}
	
	private static Logger logger = Logger.getLogger("obe logger");
	private static final String WARN = "WARN";
	private static final String ERROR = "ERROR";
	private static final String INFO = "INFO";
	private static final String TRACE = "TRACE";
	
}
