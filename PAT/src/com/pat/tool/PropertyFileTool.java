package com.pat.tool;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertyFileTool {
	
	private PropertiesConfiguration config = null;
	
	public PropertyFileTool() {
		this(".\\src\\conf.properties");
	}
	
	public PropertyFileTool(String configUrl) {
		try {
			this.config = new PropertiesConfiguration(configUrl);
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}
		this.config.setAutoSave(true);
	}
	
	public void setProperty(String key, String value) {
		this.config.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return this.config.getString(key);
	}
	
	public static void main(String[] args) {
		PropertyFileTool pros = new PropertyFileTool();
		for(int i = 1; i <= Integer.parseInt( pros.getProperty( "total" ) ) ; ++ i ) {
			pros.setProperty(String.format("%04d", 1000 + i) + "_latest", "2000/01/01 00:00:00");
		}
	}
}
