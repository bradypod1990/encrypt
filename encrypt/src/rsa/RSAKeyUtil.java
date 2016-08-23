package rsa;

import sun.security.x509.CertAndKeyGen;
import sun.security.x509.CertificateValidity;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA证书、密钥管理工具类
 *
 * @date 2016-05-23 下午1:49:40
 */
public abstract class RSAKeyUtil {

	private static KeyStore keyStore;
	private static final String DEFAULT_ENC = "UTF-8";
	private final static byte[] hex = "0123456789abcdef".getBytes();
	private static Map<String, String> publicKey = new HashMap<String, String>();
	private static Map<String, String> privateKey = new HashMap<String, String>();


	/**
	 * 根据端标识生成私钥
	 * @param password 调用端标识
	 */
	public static Map<String, String> generateKey(String password) {
		try {
			// 如果服务器上已存在,则直接返回
			Map<String, String> keyMap = RSAKeyUtil.loadKey(password);
			keyMap.remove("privateKey");
			return keyMap;
		} catch (Exception ignored) {
		}
		FileOutputStream fos = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(null, null);
			CertAndKeyGen gen = new CertAndKeyGen("RSA", "MD5withRSA");
			gen.generate(1024);
			PrivateKey key = gen.getPrivateKey();
			Calendar now = Calendar.getInstance();
			Date startDate = now.getTime();
			int keyExpire = CommonConfig.getInt("keyExpire");
			if (keyExpire == 0) {
				keyExpire = 1;
			}
			now.add(Calendar.DATE, keyExpire);

			Date endDate = now.getTime();
			X509Certificate cert = gen.getSelfCertificate(new X500Name("CN=ApexSoft,OU=ApexSoft,O=ApexSoft,ST=FZ,C=CN"), startDate, (endDate.getTime() - startDate.getTime()) / 1000);

			X509Certificate[] chain = new X509Certificate[1];
			chain[0] = cert;
			keyStore.setKeyEntry("privatekey", key, password.toCharArray(), chain);

			fos = new FileOutputStream(password + "_private.jks");
			keyStore.store(fos, password.toCharArray());

			// 返回公钥及过期时间
			String publicKey = KeyStoreUtil.Bytes2HexStringV2(cert.getPublicKey().getEncoded());
			String expireDate = DateUtil.getDateByFormat(endDate, DateUtil.F_DATE10);
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("key", publicKey);
			dataMap.put("expireDate", expireDate);
			return dataMap;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ignored) {
				}
			}
		}
		return null;
	}

	public static Map<String, String> loadKey(String password) throws Exception {
		FileInputStream fis = null;
		try{
			KeyStore keyStore = KeyStore.getInstance("JKS");
			fis = new FileInputStream(password + "_private.jks");
			keyStore.load(fis, password.toCharArray());

			X509CertImpl certificate = (X509CertImpl) keyStore.getCertificate("privatekey");
			// 判断证书是否已过期
			certificate.checkValidity();
			X509CertInfo info = (X509CertInfo) certificate.get(X509CertInfo.IDENT);
			CertificateValidity cerValidity = (CertificateValidity) info.get("validity");
			Date notAfter = (Date) cerValidity.get(CertificateValidity.NOT_AFTER);
			String privateKey = Base64Utils.encode(keyStore.getKey("privatekey", password.toCharArray()).getEncoded());
			String publicKey = KeyStoreUtil.Bytes2HexStringV2(certificate.getPublicKey().getEncoded());
			String expireDate = DateUtil.getDateByFormat(notAfter, DateUtil.F_DATE10);
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("privateKey", privateKey);
			dataMap.put("key", publicKey);
			dataMap.put("expireDate", expireDate);
			return dataMap;
		} catch(Exception ex){
			throw ex;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

}
