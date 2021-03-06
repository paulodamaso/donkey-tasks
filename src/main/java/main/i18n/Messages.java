package main.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "i18n.systemTrayApplication"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE =  ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control()); 

	private Messages() {		
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static void setLocale (Locale locale) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale, new UTF8Control());		
	}
	
	public static Locale locale () {
		return RESOURCE_BUNDLE.getLocale();
	}
}