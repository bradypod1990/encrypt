package rsa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


public abstract class FileUtil {
	

	public static File getConfieFile(String fileName) {
		String configDirPath = System.getProperty("catalina.home") + File.separator + "apexconf";
		File configFile = new File(configDirPath +  File.separator + fileName);
		if (!configFile.isFile()) {
			new File(configDirPath).mkdirs();
			configFile =  getFileFromResource("/" + fileName);
		}
		return configFile;
	}

	public static String getConfigPath() {
		return System.getProperty("catalina.home") + File.separator + "apexconf";
	}
	
	public static File getFileFromResource(String resource) {
		URL resourceUrl = FileUtil.class.getResource(resource);
		if (resourceUrl == null) {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			resourceUrl = classLoader.getResource(resource);
		}
		return new File(resourceUrl.getFile());
	}

	public static Properties loadProperties(String resource) {
		Properties properties = new Properties();
		try {
			InputStream is = FileUtil.class.getResourceAsStream(resource);
			if (is == null) {
				ClassLoader classLoader = Thread.currentThread()
						.getContextClassLoader();
				is = classLoader.getResourceAsStream(resource);
			}
			if (is != null) {
				properties.load(is);
				is.close();
			}
		} catch (IOException e) {
			return null;
		}
		return properties;
	}
}
