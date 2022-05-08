package com.ctg.mes.config.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtils {

	private Properties	prop	= null;
	private ClassLoader	classLoader;

	private static HashMap<String, PropertiesUtils> prosMap;


	public static synchronized PropertiesUtils  getInstance(String propName) {
		PropertiesUtils instance = null;

		if(prosMap == null) {
			prosMap = new HashMap<String,PropertiesUtils>();
		}
		
		instance = prosMap.get(propName);

		if(instance == null){
			instance =  new PropertiesUtils(propName);
			prosMap.put(propName, instance);
		}


		return instance;

	}


	//	public static synchronized PropertiesUtils  getInstance(String propName) {
	//		if (instance == null)
	//			instance = new PropertiesUtils(propName);
	//		return instance;
	//	}

	private PropertiesUtils(String propName) {
		classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = PropertiesUtils.class.getClassLoader();
		}

		prop = new Properties();
		addResource(chkPropertiesName(propName));
	}

	public Properties getResource(String name) {
		Properties p = new Properties();
		loadResource(p, name);
		return p;
	}

	private void loadResource(Properties properties, String name) {
		try {
			properties.load(classLoader.getResourceAsStream(name));
		} catch (Exception e) {
			throw new RuntimeException(
					"there is no found resource file of the name [" + name + "]", e);
		}
	}

	public void addResource(String name) {
		loadResource(prop, name);
	}

	public Properties getProperties() {
		return prop;
	}

	public Object set(String key, String value) {
		return prop.setProperty(key, value);
	}

	public String get(String key) {
		return prop.getProperty(key).trim();
	}

	public String get(String key, String defaultValue) {
		return prop.getProperty(key) == null ? defaultValue
				: prop.getProperty(key);
	}

	public int getInt(String key) {
		return Integer.parseInt(prop.getProperty(key).trim());
	}

	public int getInt(String key, int defaultValue) {
		return prop.getProperty(key) == null ? defaultValue
				: Integer.parseInt(prop.getProperty(key).trim());
	}

	public long getLong(String key) {
		return Long.parseLong(prop.getProperty(key).trim());
	}

	public long getLong(String key, long defaultValue) {
		return prop.getProperty(key) == null ? defaultValue
				: Long.parseLong(prop.getProperty(key).trim());
	}
	
	public ConcurrentHashMap<String,String> getMap(String keys,String values){
		ConcurrentHashMap<String,String> map =  new ConcurrentHashMap<String,String>();
		String mapkeys = get(keys,"");
		String mapvalues = get(values,"");
		if(StringUtils.isNotEmpty(mapvalues) && StringUtils.isNotEmpty(mapkeys)){
			for(int i=0;i<mapkeys.split(",").length;i++){
				try{
					map.put(mapkeys.split(",")[i],mapvalues.split(",")[i]);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		return map;
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(prop.getProperty(key).trim());
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return prop.getProperty(key) == null ? defaultValue
				: Boolean.parseBoolean(prop.getProperty(key).trim());
	}

	public static String chkPropertiesName(String fileName) {

		String nameStr = null;

		if(fileName.contains(".") && fileName.endsWith(".properties")) {
			nameStr = fileName;
		} else {
			nameStr = fileName + ".properties";
		}

//		nameStr  = filePath + nameStr;

		return nameStr;

	}
}
