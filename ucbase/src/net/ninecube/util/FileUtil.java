/**
 * 
 */
package net.ninecube.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author JXB
 *
 */
public class FileUtil {

	public static InputStream getFileInputStream(String filename) throws IOException {
		InputStream is = FileUtil.class.getResourceAsStream("/"+filename);
        if (is == null) throw new IOException("can't find file:"+filename);
        return is;
	}
	
	public static String getFileContent(String filename) throws IOException {
		return getFileContent(getFileInputStream(filename));
	}
	
	public static String getFileContent(File file) throws IOException {
		return getFileContent(new FileInputStream(file));
	}
	
	public static String getFileContent(InputStream is) throws IOException {
        return new String(getFileBytes(is));
	}
	
	public static byte[] getFileBytes(String filename) throws IOException {
		return getFileBytes(getFileInputStream(filename));
	}
	
	public static byte[] getFileBytes(File file) throws IOException {
		return getFileBytes(new FileInputStream(file));
	}
	
	public static byte[] getFileBytes(InputStream is) throws IOException {
        byte[] bs = new byte[is.available()];
        is.read(bs);
        return bs;
	}
	
	public static String getFileContent(Reader reader) throws IOException {
		StringBuffer ret = new StringBuffer();
		int ch = -1;
		while ((ch = reader.read()) != -1)ret.append((char)ch);
		return ret.toString();
	}
}
