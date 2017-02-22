package com.jaernest.reader;

import java.io.IOException;
import java.util.Properties;

/*  
 * @author Rishi Khanna
 * @version 2.0
 * @Team:DaVita MCOE
 * @Email:rishi.khanna@davita.com
 * @Company:CitiusTech
 */
public class PropertyManager {

	private static final Properties FRAMEWORKPROPERTY = new Properties();
	private static final Properties APPLICATIONPROPERTY = new Properties();
	private static final String APPLICATIONPROPERTIESPATH = "../../../../../application.properties";
	private static final String FRAMEWORKPROPERTIESPATH = "../../../../../framework.properties";
	//private static final Logger LOGGER = Logg.createLogger();

	private PropertyManager() {
	}

	public static Properties loadFrameworkPropertyFile(String propertyToLoad) {
		try {
			FRAMEWORKPROPERTY.load(PropertyManager.class
					.getResourceAsStream(FRAMEWORKPROPERTIESPATH));
		} catch (IOException io) {
			//LOGGER.info(
			//		"IOException in the loadFrameworkPropertyFile() method of the PropertyManager class",
			//		io);
			Runtime.getRuntime().halt(0);
		}
		return FRAMEWORKPROPERTY;
	}

	public static Properties loadApplicationPropertyFile(String propertyToLoad) {
		try {
			APPLICATIONPROPERTY.load(PropertyManager.class
					.getResourceAsStream(APPLICATIONPROPERTIESPATH));
		} catch (IOException io) {
			//LOGGER.info(
			//		"IOException in the loadApplicationPropertyFile() method of the PropertyManager class",
			//		io);
			//Runtime.getRuntime().halt(0);
		}
		return APPLICATIONPROPERTY;
	}
}