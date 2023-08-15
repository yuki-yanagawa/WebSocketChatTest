package yanagawa.websocket.http;

import java.nio.ByteBuffer;
import java.time.LocalDate;


public class ResponseMessageUtil {
	public static byte[] makeHttpHeader(String data, ContentTypeDef contentType) {
		String responseHeader = "";
		
		responseHeader += "HTTP/1.1 200 OK\r\n";
		responseHeader += "Server: SampleServer\r\n";
		responseHeader += "Date: " + LocalDate.now().toString() + "\r\n";
		responseHeader += "Connection : close\r\n";
		responseHeader += "Content-Type : " + contentType.getContentType() + "\r\n";
		responseHeader += "\r\n";
		responseHeader += data + "\r\n";
		responseHeader += "\r\n";
		
		return responseHeader.getBytes();
	}

	public static byte[] makeHttpHeaderGeneralFile(String data, ContentTypeDef contentType, long size) {
		String responseHeader = "";
		
		responseHeader += "HTTP/1.1 200 OK\r\n";
		responseHeader += "Server: SampleServer\r\n";
		responseHeader += "Date: " + LocalDate.now().toString() + "\r\n";
		responseHeader += "Connection : close\r\n";
		responseHeader += "Content-Type : " + contentType.getContentType() + "\r\n";
		responseHeader += "Content-Length : " + String.valueOf(size) + "\r\n";
		responseHeader += "\r\n";
		responseHeader += data + "\r\n";
		responseHeader += "\r\n";
		
		return responseHeader.getBytes();
	}

	public static byte[] makeHTTPHeaderImgData(byte[] data, ContentTypeDef contentType, long size) {
		String responseHeader = "";
		
		responseHeader += "HTTP/1.1 200 OK\r\n";
		responseHeader += "Server: SampleServer\r\n";
		responseHeader += "Date: " + LocalDate.now().toString() + "\r\n";
		responseHeader += "Connection : close\r\n";
		responseHeader += "Content-Type : " + contentType.getContentType() + "\r\n";
		responseHeader += "Content-Length : " + String.valueOf(size) + "\r\n";
		responseHeader += "\r\n";

		ByteBuffer byteBuf = ByteBuffer.allocate(data.length + 300);
		try {
			byteBuf.put(responseHeader.getBytes(), 0, responseHeader.getBytes().length);
			byteBuf.put(data, 0, (int)size);
			//byteBuf.put(data, responseHeader.getBytes().length, responseHeader.getBytes().length + (int)size);
			byteBuf.put("\r\n\r\n".getBytes(), 0, "\r\n\r\n".getBytes().length);
			//byteBuf.put("\r\n\r\n".getBytes(), responseHeader.getBytes().length + (int)size, responseHeader.getBytes().length + (int)size + "\r\n\r\n".getBytes().length);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return byteBuf.array();
	}

	public static byte[] makeHTTPHeaderVideoData(byte[] data, ContentTypeDef contentType, long size) {
		String responseHeader = "";
		
		responseHeader += "HTTP/1.1 206 Partial Content\r\n";
		responseHeader += "Server: SampleServer\r\n";
		responseHeader += "Date: " + LocalDate.now().toString() + "\r\n";
		responseHeader += "Connection : close\r\n";
		responseHeader += "Content-Type : " + contentType.getContentType() + "\r\n";
		responseHeader += "Content-Length : " + String.valueOf(size) + "\r\n";
		responseHeader += "Content-Range  :  bytes 0-" + String.valueOf(data.length - 1) + "/" + String.valueOf(data.length) + "\r\n";
		responseHeader += "\r\n";

		ByteBuffer byteBuf = ByteBuffer.allocate(data.length + 300);
		try {
			byteBuf.put(responseHeader.getBytes(), 0, responseHeader.getBytes().length);
			byteBuf.put(data, 0, data.length);
			//byteBuf.put(data, responseHeader.getBytes().length, responseHeader.getBytes().length + (int)size);
			byteBuf.put("\r\n\r\n".getBytes(), 0, "\r\n\r\n".getBytes().length);
			//byteBuf.put("\r\n\r\n".getBytes(), responseHeader.getBytes().length + (int)size, responseHeader.getBytes().length + (int)size + "\r\n\r\n".getBytes().length);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return byteBuf.array();
	}

	public static byte[] makeHttpHeaderBad() {
		String responseHeader = "";
		
		responseHeader += "HTTP/1.1 404 BAD\r\n";
		responseHeader += "Server: SampleServer\r\n";
		responseHeader += "Date:" + LocalDate.now().toString() + "\r\n";
		responseHeader += "Connection : close\r\n";
		responseHeader += "\r\n";

		return responseHeader.getBytes();
	}
}
