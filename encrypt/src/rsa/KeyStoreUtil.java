package rsa;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

public class KeyStoreUtil {

	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	private static final String DEFAULT_ENC = "UTF-8";
	private static boolean isLoaded = false;

	/**
	 * @Title: loadKeyStore
	 * @Description: 加载keystore文件
	 * @param filename  文件名
	 * @param psw  密码
	 * @param alias  别名
	 * @param aliasPsw  别名密码
	 * @throws Exception
	 *             void
	 */
	public static void loadKeyStore(String filename, String psw, String alias,
			String aliasPsw) throws Exception {
		// File Jksfile = FileUtil.getConfieFile(filename);
		File Jksfile = new File(filename);
		loadKeyStore(Jksfile, psw, alias, aliasPsw);
	}

	/**
	 * @Title: loadKeyStore
	 * @Description: 加载keystore文件
	 * @param file
	 * @param psw
	 * @param alias
	 * @param aliasPsw
	 * @throws Exception
	 *             void
	 */
	public static void loadKeyStore(File file, String psw, String alias,
			String aliasPsw) throws Exception {
		if (isLoaded)
			return;
		if (!file.exists()) {
			throw new Exception("证书文件:" + file.getAbsolutePath() + "不存在");
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(in, psw.toCharArray());
			Certificate cer = ks.getCertificate(alias);
			publicKey = cer.getPublicKey();
			privateKey = (PrivateKey) ks.getKey(alias, aliasPsw.toCharArray());
		} finally {
			if (in != null) {
				in.close();
			}

		}
		isLoaded = true;

	}

	public void reSetLoadStatus() {
		isLoaded = false;
	}

	/**
	 * @Title: getPublicKey
	 * @Description: 获取公钥信息
	 * @return PublicKey
	 */
	public static PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * @Title: getPrivateKey
	 * @Description: 获取私钥信息
	 * @return PrivateKey
	 */
	public static PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * @Title: getModulus
	 * @Description: 获取加密模数
	 * @return String
	 */
	public static String getModulus() {
		return ((RSAPublicKeyImpl) publicKey).getModulus().toString(16);
	}

	/**
	 * @Title: getPrivateExponent
	 * @Description: 获取私钥串
	 * @return String
	 */
	public static String getPrivateExponent() {
		return ((RSAPrivateCrtKeyImpl) privateKey).getPrivateExponent()
				.toString(16);
	}

	/**
	 * @Title: getPublicExponent
	 * @Description: 获取公钥串
	 * @return String
	 */
	public static String getPublicExponent() {
		return ((RSAPublicKeyImpl) publicKey).getPublicExponent().toString(16);
	}

	public static String encrypt(String data) throws Exception {
		return encrypt(data, DEFAULT_ENC);
	}

	public static String encrypt(String data, String enc) throws Exception {
		byte[] enData = encrypt(data.getBytes(enc));
		return Bytes2HexStringV2(enData);
	}

	public static String decrypt(String data) throws Exception {
		return decrypt(data, DEFAULT_ENC);
	}

	public static String decrypt(String data, String enc) throws Exception {
		byte[] deData = decrypt(HexString2Bytes(data));
		return new StringBuilder(new String(deData, enc)).toString();
	}

	public static String decryptFromJs(String data) throws Exception {
		return decryptFromJs(data, DEFAULT_ENC);
	}

	public static String decryptFromJs(String data, String enc)
			throws Exception {
		byte[] deData = decrypt(HexString2Bytes(data));
		return new StringBuilder(new String(deData, enc)).reverse().toString();
	}

	/**
	 * @Title: encrypt
	 * @Description: 加密
	 * @param data
	 * @return
	 * @throws Exception
	 * 
	 */
	public static byte[] encrypt(byte[] data) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int blockSize = cipher.getBlockSize();
			// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
			// 加密块大小为127
			// byte,加密后为128个byte;因此共有2个加密块，第一个127
			// byte第二个为1个byte
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
					: data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i
							* outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i
							* blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
				// ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
				// OutputSize所以只好用dofinal方法。

				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * @Title: decrypt
	 * @Description: 解密
	 * @param raw
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] raw) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;

			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private final static byte[] hex = "0123456789abcdef".getBytes();

	// 从字节数组到十六进制字符串转换
	public static String Bytes2HexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	// 从字节数组到十六进制字符串转换
	public static String Bytes2HexStringV2(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toHexString((b[i] >> 4) & 0x0f));
			sb.append(Integer.toHexString((b[i] & 0x0f)));
		}
		return sb.toString();
	}

	// 从十六进制字符串到字节数组转换
	public static byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			byte c0 = (byte) Character.digit(hexstr.charAt(j++), 16);
			byte c1 = (byte) Character.digit(hexstr.charAt(j++), 16);
			b[i] = (byte) ((c0 << 4) | c1);
		}
		return b;
	}

	/**
	 * * *
	 * 
	 * @param args
	 *            *
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		
		//生成rsa非对称秘钥keystore命令 :  keytool genkey alias mytest keyalg RSA keysize 1024 keystore mykeystore validity 4000
		
		
		KeyStoreUtil.loadKeyStore("C:/Users/Administrator/mykeystore",
				"zoufengdage", "mytest", "zoufengdage");
		/*
		 * KeyStoreUtil.loadKeyStore( AppConfig.jks_path, AppConfig.storepass,
		 * AppConfig.keyalias, AppConfig.keypass );
		 */

		String test = "123456";
		System.out.println("加密前:" + test);
		System.out.println("公钥:" + KeyStoreUtil.getPublicKey().toString());
		System.out.println("公钥-modulus:" + KeyStoreUtil.getModulus());
		System.out.println("公钥-exponent:" + KeyStoreUtil.getPublicExponent());
		String en_str = encrypt(test);
		System.out.println("加密后的串:" + en_str);
		System.out.println("私钥:" + KeyStoreUtil.getPrivateKey().toString());
		System.out.println("私钥-modulus:" + KeyStoreUtil.getModulus());
		System.out.println("私钥-exponent:" + KeyStoreUtil.getPrivateExponent());
		String de_str = decrypt(en_str);
		System.out.println("解密后的串:" + de_str);

		// String mw =
		// "40a108dc6117209894b6e22fdc5cee50504dd01801b688578f49c2aaed25942605fbf49b129329f10a32fd165de9b344331973938b347ad396f12562e84cc9b1220c5a0bb2f9cffb3acde9f923353576ae081f87e3695cc126f6f0a29b1bf281f229a468133e84484d30e56a3972118fd0f771b769ea913b1aeda1c69756b270";
		
		//获取前端js加密的密码串
		String mw = "93e681b611d6507e0f12246b16d300ac4b9c6d1786d415a47b9e7abe772073ef96f2eaa32acd1699facb51a6845f72a60554e203381b028bdbd8f862058cc308909c73702d45b0a03651ac6b46bef50d3dd6645a107a3e12fbf2a13db3887431fe0b8fae2da4edc7ed764c0527c3a9e1fdee3e3fcde6ed21ac5211db57c22a9c";
		String de_strs = decryptFromJs(mw);
		System.out.println("解密后的串:" + de_strs);
		System.out.println("解密后的串:" + URLDecoder.decode(de_strs, "UTF-8"));
	}

}
