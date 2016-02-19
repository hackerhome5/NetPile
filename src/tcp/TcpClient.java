package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient {
	
	private Socket client;
	
	public TcpClient(String ip, int port) throws ClientException
	{
		try {
			client = new Socket(ip, port);
		} catch (IOException e) {
			try {
				this.client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ClientException("Error connecting to Server");
		}
	}
	
	public void send(String toSend) throws ClientException
	{
		try {
			PrintWriter out = new PrintWriter(this.client.getOutputStream());
			out.write(toSend+"\n");
			out.close();
		} catch (IOException e) {
			try {
				this.client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ClientException("Error sending message to server");
		}
	}
	
	public String recv() throws ClientException
	{
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			String toRet = in.readLine();
			in.close();
			return toRet;
		} catch (IOException e) {
			try {
				this.client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new ClientException("Error recieving from server");
		}
	}
	
	public void close() throws ClientException 
	{
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ClientException("Error closing client connection with server");
		}
	}
	
}
