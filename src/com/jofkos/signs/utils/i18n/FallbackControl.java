package com.jofkos.signs.utils.i18n;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class FallbackControl extends ResourceBundle.Control {
	
	public static FallbackControl CONTROL = new FallbackControl();
			
	public List<String> getFormats(String string) {
		return FORMAT_PROPERTIES;
	}
	
	@Override
	public Locale getFallbackLocale(String baseName, Locale locale) {
		return Locale.ROOT;
	}
}