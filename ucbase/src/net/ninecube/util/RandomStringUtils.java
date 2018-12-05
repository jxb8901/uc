package net.ninecube.util;

/**
 * 生成随机数工具类
 */
public class RandomStringUtils {

	/**
	 * RandomStringUtils.java constructor comment.
	 * 
	 * Creation date 2005-3-20
	 */
	private RandomStringUtils() {
		super();
	}

	private static final byte[] NUM = {(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9' };

	private static final byte[] ALPHA =
		{
			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
			(byte) 'A',
			(byte) 'B',
			(byte) 'C',
			(byte) 'D',
			(byte) 'E',
			(byte) 'F',
			(byte) 'G',
			(byte) 'H',
			(byte) 'I',
			(byte) 'J',
			(byte) 'K',
			(byte) 'M',
			(byte) 'N',
			(byte) 'P',
			(byte) 'Q',
			(byte) 'R',
			(byte) 'S',
			(byte) 'T',
			(byte) 'U',
			(byte) 'V',
			(byte) 'W',
			(byte) 'X',
			(byte) 'Y' };

	/**
	 * 随机生成纯数字的字符串
	 * 
	 * Creation date 2005-3-20
	 * @param expectLen 预期返回字符串的长度
	 * @return String
	 */
	public static String randomNumeric(int expectLen) {
		return ((Math.random() * Math.pow(10, expectLen+5)) + "").substring(0, expectLen).replace('.', '8');
	}

	/**
	 * 随机生成总长度为expectLen、数字和英文字母随机混合的字符串。
	 * 
	 * Creation date 2005-3-20
	 * @param expectLen 预期返回字符串的总长度
	 * @return String
	 */
	public static String randomAlphanumeric(int expectLen) {
		byte[] alphas = new byte[expectLen];
		for (int i = 0; i < expectLen; i++) {
			alphas[i] = ALPHA[((int) (Math.random() * 33))];
		}
		return new String(alphas);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 99999; i++) {
			System.out.println(randomNumeric(4));
			//System.out.println(randomAlphanumeric(12));
		}
		//System.out.println(Math.random());
	}
}
