/*
 * *********************************************************
 * Copyright (c) 2012 - 2012, DHBW Mannheim
 * Project: SoS
 * Date: Apr 1, 2012
 * Author(s): andres
 * 
 * *********************************************************
 */
package edu.dhbw.sos.helper;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * TODO andres, add comment!
 * - What should this type do (in one sentence)?
 * - If not intuitive: A simple example how to use this class
 * 
 * @author andres
 * 
 */
public class Messages {
	private static final String			BUNDLE_NAME			= "res.lang.sos";								//$NON-NLS-1$
																																	
	private static final ResourceBundle	RESOURCE_BUNDLE	= ResourceBundle.getBundle(BUNDLE_NAME);
	
	
	private Messages() {
	}
	
	
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
