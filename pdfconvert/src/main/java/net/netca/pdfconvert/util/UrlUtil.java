package net.netca.pdfconvert.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class UrlUtil {
	public static boolean isUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
		if (pattern.matcher(url).matches()) {
			return true;
		}
		return false;
	}

	public static boolean isConnect(String url) {
		URL urlConnect;
		InputStream in = null;
		try {
			urlConnect = new URL(url);
			in = urlConnect.openStream();
			return true;
		} catch (Exception e1) {
			url = null;
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
	}
}
