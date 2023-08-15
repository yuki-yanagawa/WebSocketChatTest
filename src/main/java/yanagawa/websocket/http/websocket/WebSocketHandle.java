package yanagawa.websocket.http.websocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.Callable;

public class WebSocketHandle implements Callable<Boolean> {
	private Socket socket_;
	private String updateKey_;
	public WebSocketHandle(Socket socket, String updateKey) {
		this.socket_ = socket;
		this.updateKey_ = updateKey;
	}
	@Override
	public Boolean call() throws NoSuchAlgorithmException {
		try(InputStream is = this.socket_.getInputStream();
			OutputStream os = this.socket_.getOutputStream()) {
			String connectionSetting = "HTTP/1.1 101 Switching Protocols\r\n";
			connectionSetting += "Connection: Upgrade\r\n";
			connectionSetting += "Upgrade: websocket\r\n";
			connectionSetting += "Sec-WebSocket-Accept: ";
			connectionSetting += Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1")
									.digest((this.updateKey_ + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8"))) + "\r\n\r\n";
			os.write(connectionSetting.getBytes());
			os.flush();
			DataInputStream dis  = new DataInputStream(is);
			DataOutputStream dos = new DataOutputStream(os);
			while(true) {
				String decodedMess = messageDecoder(dis);
				dos.writeByte(0x81);
				if(decodedMess.length() < 126) {
					dos.writeByte(decodedMess.length());
				} else if(decodedMess.length() <= (Short.MAX_VALUE - Short.MIN_VALUE)) {
					dos.writeByte(126);
					dos.writeShort(decodedMess.length());
				} else if (decodedMess.length() <= Long.MAX_VALUE) {
					dos.writeByte(127);
					dos.writeLong(decodedMess.length());
			    }
				dos.write(decodedMess.getBytes("UTF-8"));
				dos.flush();
			}
		} catch(IOException e) {
			
		}
		return true;
	}

	private String messageDecoder(DataInputStream ds) throws IOException {
		byte first = ds.readByte();
		byte second = ds.readByte();
		
		byte opCode = (byte) (first & 0x0F);
		// Check the opCode value
        if (opCode == 1) {
            System.out.println("Start Decode");
        } else if (opCode == 8) {
        	return "";
        }

        // Mask Check
        boolean masked = ((second & 0x80) != 0);
        if (!masked) {
            return "";
        }

        // Data Length Check
        long length = second & 0x7F;
        if (length == 126) {
            length = ds.readUnsignedShort();
        } else if (length == 127) {
            length = ds.readLong();
        }
        System.out.println("Message Length: " + length);
        
        // Read mask
        byte[] mask = new byte[4];
        ds.read(mask);

        // decoder
        byte[] encodedCharArray = new byte[(int) length];
        ds.read(encodedCharArray);
        StringBuilder decoded = new StringBuilder();
        for (int i = 0; i < encodedCharArray.length; i++) {
            char decodedChar = (char) (encodedCharArray[i] ^ mask[i % 4]);
            decoded.append(decodedChar);
        }
        
        System.out.println("Decode Result  " + decoded.toString());
        
        return decoded.toString();
	}
}
