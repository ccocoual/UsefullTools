package parsifal.toolbox.business.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.input.BOMInputStream;

public class FileUtil {
	public static HashMap<String, String> fileToHashMap(String content,
			String separator) {

		HashMap<String, String> map = new HashMap<>();
		String[] lineList = content.split("\n");
		for (String line : lineList) {
			String[] data = line.split(separator);
			if (data.length > 1) {
				map.put(data[0], data[1]);
			}

		}

		return map;
	}

	public static String readAllLine(String fileName, Charset charset) {
		try {
			List<String> lineList = Files.readAllLines(Paths.get(fileName),
					charset);
			return StringUtil.fold(lineList, "\n");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	

	public static String convertToUFT8(String text) {
		byte[] converttoBytes;
		try {
			converttoBytes = text.getBytes("UTF-8");
			return new String(converttoBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String readUFT8FileWithBOM(String fileName) {
		StringBuilder builder = new StringBuilder();
		try {
			BOMInputStream bOMInputStream = new BOMInputStream(
					new FileInputStream(fileName));
			InputStreamReader reader = new InputStreamReader(
					new BufferedInputStream(bOMInputStream),
					Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(reader);
			String line = null;
			while ((line = br.readLine()) != null) {
				builder.append(line + "\n");
			}
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String content = builder.toString();
		content = content.replaceAll("é", "e");
		return content;
	}
}
