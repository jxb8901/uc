package net.ninecube.util.fileutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import org.apache.log4j.*;

public class TxtFileReader {
	//static Logger log = Logger.getLogger(TxtFileReader.class);

	public static String getContent(String flname) throws IOException {
		StringBuffer strbuf = new StringBuffer();
		try {
			BufferedReader txtrd = new BufferedReader(new InputStreamReader(
					TxtFileReader.class.getResourceAsStream(flname), "utf8"));
			String str;
			while ((str = txtrd.readLine()) != null) {
				strbuf.append(str);
				strbuf.append("\r\n");
			}
		} catch (Exception exc) {
			//log.error(exc);
			throw new IOException(exc.getMessage());
		}

		return strbuf.toString();
	}
}
