package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;

import serial.Serial;
import tcp.AcceptListener;
import tcp.TcpServer;

public class NetPileServer {
	private static TcpServer server;
	private static Serial serial;
	private int port;
	private String username;
	private String password;
	private String compiled;
	private String compiler = null;
	
	public NetPileServer(int port, String username, String password) throws Exception
	{
		this.username = username;
		this.password = password;
		this.port = port;
		
		if(username.length() > 16) { this.username = username.substring(0,16); }
		else if(username.length() < 16)
		{ for(int am = 0; am < (16-username.length()); am++) this.username += "!"; 
		}
		
		if(password.length() > 16) { this.password = password.substring(0,16); }
		else if(password.length() < 16)
		{ for(int am = 0; am < (16-password.length()); am++) this.password += "*"; 
		}
		System.out.println("Starting NetPile Server (Details):\nUsername (16 char): " + this.username
		+ "\nPassword (16 char): " + this.password + "\nPort: " + String.valueOf(port) + "\n");
		
		serial = new Serial();
	}
	
	private String javac(String compilerPath) throws Exception
	{
		String system = System.getProperty("os.name").toLowerCase();
		this.compiler = null;
		if(compilerPath == null)
		{
			if(system.contains("window"))
			{
				String root = "C:\\Program Files\\Java\\";
				String root32 = "C:\\Program Files(x86)\\Java\\";
				if(new File(root).isDirectory())
				{
					System.out.println("Detected 64bit computer!");
					File toSee = new File(root);
					String stuff[] = toSee.list();
					for(int files = 0; files < stuff.length; files++)
					{
						if(stuff[files].contains("jdk"))
						{
							this.compiler = root + stuff[files] + "\\bin\\javac.exe";
							System.out.println("Using: " + stuff[files]);
							break;
						}
					}
					
				}
				else if(new File(root32).isDirectory())
				{
					System.out.println("Detected 32bit computer!");
					File toSee = new File(root32);
					String stuff[] = toSee.list();
					for(int files = 0; files < stuff.length; files++)
					{
						if(stuff[files].contains("jdk"))
						{
							this.compiler = root + stuff[files] + "\\bin\\javac.exe";
							System.out.println("Using: " + stuff[files]);
							break;
						}
					}
				}
				else
				{
					System.out.println("Couldn't find Java folder in either\n"
							+ root + "\n"
							+ root32);
				}
			}
			else if(system.contains("mac"))
			{
				String root = "/usr/bin/javac";
				String root2 = "/Library/Java/JavaVirtualMachines/";
				if(new File(root).isFile())
				{
					this.compiler = "javac";
				}
				else if(new File(root2).isDirectory())
				{
					File toSee = new File(root2);
					String stuff[] = toSee.list();
					for(int files = 0; files < stuff.length; files++)
					{
						if(stuff[files].contains("jdk"))
						{
							this.compiler = root + stuff[files] + "/bin/javac";
							System.out.println("Using: " + stuff[files]);
							break;
						}
					}
				}
				else
				{
					System.out.println("Couldn't find Java folder in either\n"
							+ root + "\n"
							+ root2);
				}
			}
			else
			{
				String root = "/usr/bin/javac";
				String root2 = "/usr/lib/jvm/";
				if(new File(root).isFile())
				{
					this.compiler = "javac";
				}
				else if(new File(root2).isDirectory())
				{
					File toSee = new File(root2);
					String stuff[] = toSee.list();
					for(int files = 0; files < stuff.length; files++)
					{
						if(stuff[files].contains("jdk"))
						{
							this.compiler = root + stuff[files] + "/bin/javac";
							System.out.println("Using: " + stuff[files]);
							break;
						}
					}
				}
				else
				{
					System.out.println("Couldn't find Java folder in either\n"
							+ root + "\n"
							+ root2);
				}
			}
		}
		else
		{
			return compilerPath;
		}
		if(this.compiler == null)
			throw new IOException("COULD NOT FIND JAVA JDK/javac compiler!");
		return this.compiler;
	}
	
	public void javaFile(String path) throws Exception
	{
		this.javaFile(path, null);
	}
	
	public void javaFile(String path, String compilerPath) throws Exception
	{
		String compiler = javac(compilerPath);
		String toPrint;
		System.out.println("Trying to compile: " + path);
		String toExec = String.format("%s %s",compiler, path);
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
		if(exitCode == 0) {System.out.println("Compiled!");}
		else {System.out.println("Error compiling! (Exit Code): " + exitCode);}
		this.compiled = path.replace(".java", ".class");
		System.out.println("Encrypting compiled file!");
		serial.encryptTemp(this.username, this.password, this.compiled);
		System.out.println("Encrypted! (Not yet :( )");
	}
	
	public void startLoop() throws Exception
	{
		System.out.println("Preparing to start server!\nChecking NetPile file");
		final String toSend = serial.readTemp();
		
		server = new TcpServer(this.port);
		String ip = Inet4Address.getLocalHost().getHostAddress();
		System.out.println("Started server on\nip: " + ip + "\nport: " + 
				this.port + "\nWaiting for client...\n\n");
		server.onAccept(new AcceptListener(){

			public String[] Modes() {
				String steps[] = {"SEND", "END"};
				return steps;
			}

			public void Recieve(String FromClient, int ClientNum, int ArgNum) {
				//System.out.println("Got Client: " + ClientNum);
			}

			public String Send(int ClientNum, int ArgNum) {
				System.out.println("Got Client\nNetPiled file being sent!");
				return toSend;
			}					
		});
	}
}
