package yanagawa.websocket.http.clienthandle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import yanagawa.websocket.http.HttpRequestAnalyzer;
import yanagawa.websocket.http.ResponseMessageUtil;
import yanagawa.websocket.http.websocket.WebSocketHandle;

public class HTTPClientHandle implements Runnable {
	private static Predicate<String> checkWebSocketUpdate = s -> s.toUpperCase().replaceAll("\\s+", "").equals("UPGRADE:WEBSOCKET");
	private static Predicate<String> checkWebSocketUpdateKey= s -> s.toUpperCase().replaceAll("\\s+", "").matches("SEC-WEBSOCKET-KEY:.+");
	private Socket socket_;
	public HTTPClientHandle(Socket socket) {
		socket_ = socket;
	}
	
	@Override
	public void run() {
		try(InputStream is = socket_.getInputStream();
			OutputStream os = socket_.getOutputStream()) {
			byte[] buf = new byte[1024];
			int readSize = is.read(buf);
			String requestHeader = new String(buf,"UTF-8");
			String[] requestHeaderLine = requestHeader.split("\r\n");
			if(isCheckWebSocketUpdateRequest(requestHeaderLine)) {
				String updateKey = getWebSocketUpdateKeyFromRequestLine(requestHeaderLine);
				try {
					if(startWebSocketConnection(updateKey)) {
						
					} else {
						os.write(ResponseMessageUtil.makeHttpHeaderBad());
					}
				} catch(ExecutionException e) {
					os.write(ResponseMessageUtil.makeHttpHeaderBad());
				} catch(InterruptedException ie) {
					os.write(ResponseMessageUtil.makeHttpHeaderBad());
				}
			} else {
				if(requestHeaderLine[0].split(" ")[0].trim().equals("GET")) {
					if(requestHeaderLine[0].split(" ")[1].trim().matches(".*\\.ico")) {
						//not supported
					} else {
						os.write(HttpRequestAnalyzer.getFileData(requestHeaderLine[0].split(" ")[1].trim()));
						os.flush();
					}
				} else {
					//POST Request not supported
				}
			}
		} catch(IOException e) {
			
		}
	}

	private boolean isCheckWebSocketUpdateRequest(String[] requestParam) {
		return Arrays.stream(requestParam).filter(checkWebSocketUpdate).count() > 0;
	}

	private String getWebSocketUpdateKeyFromRequestLine(String[] requestParam) {
		Optional<String> optStr = Arrays.stream(requestParam).filter(checkWebSocketUpdateKey).findFirst();
		if(optStr.isPresent()) {
			return optStr.get().split(":")[1].trim();
		}
		return "";
	}

	private boolean startWebSocketConnection(String updateKey) throws ExecutionException,InterruptedException {
		if(updateKey.equals("")) {
			return false;
		}
		ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		Future<Boolean> future = executor.submit(new WebSocketHandle(this.socket_, updateKey));
		return future.get();
	}
}
