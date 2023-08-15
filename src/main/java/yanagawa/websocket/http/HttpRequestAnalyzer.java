package yanagawa.websocket.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpRequestAnalyzer {
	public static final String HTML_FILE_PATTERN_REGEX = ".*\\.html$";
	public static final String JASCRIPT_FILE_PATTERN_REGEX = ".*\\.js$";
	public static final String CSS_FILE_PATTERN_REGEX = ".*\\.css$";

	public static byte[] getFileData(String filePath) {
		System.out.println(filePath);
		try {
			ContentTypeDef contentType = null;
			if(filePath.equals("/")) {
				filePath = "html/index.html";
				contentType = ContentTypeDef.HTML;
			} else if(filePath.matches(HTML_FILE_PATTERN_REGEX)) {
				if(filePath.startsWith("^/html/")) {
					filePath = filePath.substring(1);
				} else if(filePath.startsWith("^/")){
					filePath = "html" + filePath;
				} else {
					filePath = "html/" + filePath;
				}
				contentType = ContentTypeDef.HTML;
			} else if(filePath.matches(JASCRIPT_FILE_PATTERN_REGEX)) {
				if(filePath.matches("^/javascript/.*")) {
					filePath = filePath.substring(1);
				} else if(filePath.matches("^javascript/.*")) {
					
				} else if(filePath.matches("^/.*")){
					filePath = "javascript" + filePath;
				} else {
					filePath = "javascript/" + filePath;
				}
				contentType = ContentTypeDef.JAVASCRIPT;
			} else if(filePath.equals("/videoapi")) {
				filePath = "video/mov_hts-samp005.mp4";
				contentType = ContentTypeDef.VIDEOMP4;
				Path p = Paths.get(filePath);
				long size = Files.size(p);
				System.out.println("FilePath : " + filePath + " Size : " + size);
				FileInputStream fis = new FileInputStream(p.toFile());
				byte[] buf = new byte[(int)size];
				fis.read(buf);
				return ResponseMessageUtil.makeHTTPHeaderVideoData(buf, contentType, size);
			} else if(filePath.equals("/imgapi")){
				filePath = "img/test.png";
				contentType = ContentTypeDef.IMGPNG;
				Path p = Paths.get(filePath);
				long size = Files.size(p);
				System.out.println("FilePath : " + filePath + " Size : " + size);
				FileInputStream fis = new FileInputStream(p.toFile());
				byte[] buf = new byte[(int)size];
				fis.read(buf);
				return ResponseMessageUtil.makeHTTPHeaderImgData(buf, contentType, size);
			} else {
				return ResponseMessageUtil.makeHttpHeaderBad();
			}
			Path p = Paths.get(filePath);
			long size = Files.size(p);
			System.out.println("FilePath : " + filePath + " Size : " + size);
			FileInputStream fis = new FileInputStream(p.toFile());
			byte[] buf = new byte[(int)size];
			fis.read(buf);
			return ResponseMessageUtil.makeHttpHeaderGeneralFile(new String(buf), contentType, size);
			
			
		} catch(IOException e) {
			e.printStackTrace();
			return ResponseMessageUtil.makeHttpHeaderBad();
		}
		
	}
}
