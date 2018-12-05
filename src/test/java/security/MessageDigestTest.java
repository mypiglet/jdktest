package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.testng.annotations.Test;

/**
 * 为应用程序提供信息摘要算法的功能.
 * 
 * <p>
 * 如 MD5 或 SHA 算法。信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
 * 
 * @author mypiglet
 *
 */
public class MessageDigestTest {
	
	private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

	@Test
	public void test() throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.equals("dddfffgggg");
		byte[] hash = md.digest();
		
		System.out.println(toHexString(hash));

	}
	
	private String toHexString(byte[] bytes) {
		char[] hex = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i] & 0xFF;
			hex[i * 2] = HEX_CHARS[b >>> 4];
			hex[i * 2 + 1] = HEX_CHARS[b & 0x0F];
		}
		return new String(hex);
	}

}
