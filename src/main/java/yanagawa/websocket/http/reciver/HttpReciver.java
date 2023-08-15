package yanagawa.websocket.http.reciver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yanagawa.websocket.http.clienthandle.HTTPClientHandle;

public class HttpReciver {
	public static void main(String args[]) {
		ExecutorService executorService = null;
		try(ServerSocket svrsock = new ServerSocket()) {
			svrsock.bind(new InetSocketAddress("127.0.0.1", 9998));
			executorService = Executors.newFixedThreadPool(10);
			while(true) {
		        Socket socket = svrsock.accept();
		        executorService.submit(new HTTPClientHandle(socket));
		    }
		} catch(IOException e) {
			
		}
	}
}
