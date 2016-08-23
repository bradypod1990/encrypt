package rsa;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 公共配置参数
 * @author dinglei
 *
 */
public class CommonConfig extends PropertyPlaceholderConfigurer {


	/**
	 * 配置是否已经加载过
	 */
	public static boolean isConfigLoad = false;

	/**
	 * esb接口会话ID
	 */
	public static String ESB_INF_SESSIONID = "1234567890";

    /**
     * 默认的系统版本
     */
    public static String VERSION = "common";

    /**
     * 当前运行的系统版本
     */
    public static String RUN_VERSION = null;

	/**
	 * 配置属性对象
	 */
	private static Properties properties = new Properties();

	// 默认的配置文件加载路径
    private String filePath;

	@Override
	public void setLocation(Resource location) {
		String fileName = location.getFilename();
		String path = getFilePath(fileName);
		if (path != null) {
			// 如果web容器目录下存在该文件
			location = new FileSystemResource(path);
		} else {
			fileName = ResourcePatternResolver.CLASSPATH_URL_PREFIX + fileName;
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			location = resolver.getResource(fileName);
		}
        super.setLocation(location);
	}

	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		properties.putAll(props);
	}

	/**
	 * 根据文件名获取web容器目录下的配置文件
	 * @param fileName 配置文件名
	 * @return 文件目录
     */
	private String getFilePath(String fileName) {
		if (this.filePath != null) {
			return filePath;
		}
		String catalinaHome = FileUtil.getConfigPath();
		String filePath = catalinaHome + File.separator + fileName;
		File configFile = new File(filePath);
		if(!configFile.exists()){
			return null;
		}
		this.filePath = filePath;
		return this.filePath;
	}

	/**
	 * 加载配置文件
	 */
	public static void loadConfig(String context){
		String fileName = "config-wsyyt.properties";

		if(isConfigLoad)
			return;
		InputStream in = null;
		try{
			File configFile = FileUtil.getConfieFile(fileName);
			if(!configFile.exists()){
				System.out.println("配置文件："+fileName+";不存在");
			}
			in = new FileInputStream(configFile);
			properties.load(in);
			isConfigLoad = true;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally{
			if(in!=null){
				try{
					in.close();
				}catch(Exception ex){
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}

	public static String getString(String propertiesKey){
		return properties.getProperty(propertiesKey, "");
	}

    public static String getVersion(){
        // 获取配置的系统版本,如果获取不到配置的版本,则取通用版本
        if(RUN_VERSION == null) {
            String version = properties.getProperty("RUN.Version", null);
            RUN_VERSION =  (version == null) ? VERSION : version;
        }
        return RUN_VERSION;
    }

	public static boolean getBoolean(String propertiesKey){
		try{
			return Boolean.parseBoolean(properties.getProperty(propertiesKey, "false"));
		}catch(Exception e){
			return false;
		}
	}

	public static int getInt(String propertiesKey){
		try{
			return Integer.parseInt(properties.getProperty(propertiesKey, "0"));
		}catch(Exception e){
			return 0;
		}
	}

	public static float getFloat(String propertiesKey){
		try{
			return Float.parseFloat(properties.getProperty(propertiesKey, "0.0"));
		}catch(Exception e){
			return 0F;
		}
	}

	public static double getDouble(String propertiesKey){
		try{
			return Double.parseDouble(properties.getProperty(propertiesKey, "0.0"));
		}catch(Exception e){
			return 0L;
		}
	}

}
