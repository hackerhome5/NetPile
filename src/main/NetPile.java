package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import serial.Serial;
import serial.SerialException;
import tcp.TcpClient;

public class NetPile {
	private static TcpClient client;
	private String username;
	private String password;
	private Serial serial;
	private File tempjar;
	
	public NetPile(String ip, int port, String username, String password) throws Exception
	{
		this.username = username;
		this.password = password;
		
		if(username.length() > 16) { this.username = username.substring(0,16); }
		else if(username.length() < 16)
		{ for(int am = 0; am < (16-username.length()); am++) this.username += "!"; 
		}
		
		if(password.length() > 16) { this.password = password.substring(0,16); }
		else if(password.length() < 16)
		{ for(int am = 0; am < (16-password.length()); am++) this.password += "*"; 
		}
		System.out.println("Starting NetPile Client! (Details):\nUsername (16 char): " + this.username
		+ "\nPassword (16 char): " + this.password + "\nip: " + ip
				+"\nPort: " + String.valueOf(port) + "\n");
		
		try {
			serial = new Serial();
		} catch (SerialException e) {
			e.printStackTrace();
		}
		client = new TcpClient(ip, port);
		System.out.println("Client connected!!");
	}
	
	public void pullFile() throws Exception
	{
		this.pullFile(null);
	}
	
	public void pullFile(String path) throws Exception
	{
		System.out.println("Pulling file from server");
		String encrypted = client.recv().trim().replace("null", "").replace(" ", "");
		System.out.println("Decrypting...");
		String decrypted = serial.decryptTemp(this.username, this.password, encrypted);
		System.out.println("Got the file\nSaving the java class...");
		if(path == null)
		{
			try {
				tempjar = File.createTempFile("temp", ".class");
			} catch(IOException ig) {
				throw new IOException("Couldn't create a temp file to save class");
			}
		}
		else
		{
			tempjar = new File(path);
		}
		FileWriter file = new FileWriter(tempjar);
		System.out.println("Saved: " + tempjar.getAbsolutePath());
		file.write(decrypted);
		file.close();
	}
	
	public void runFile() throws Exception {
		System.out.println("Trying to run the jar!");
		tempjar.setExecutable(true);
		String toPrint = "";
		String path = tempjar.getAbsolutePath().replace(".class", "");
		String toExec = String.format("java %s", path);
		Process process = Runtime.getRuntime().exec(toExec);
		BufferedReader inOK = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader inBad = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		
		while((toPrint = inOK.readLine()) != null)
		{
			System.out.println(toPrint);
		}
		toPrint = null;
		while((toPrint = inBad.readLine()) != null)
		{
			System.out.println("ERROR: " + toPrint);
		}
		process.waitFor();
		int exitCode = process.exitValue();
		if(exitCode == 0) {System.out.println("Done running!");}
		else {System.out.println("Error running! (Exit Code): " + exitCode);}
	}
}


