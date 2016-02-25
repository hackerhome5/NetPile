package main;

import java.io.IOException;
import java.net.InetAddress;

import serial.Serial;
import tcp.AcceptListener;
import tcp.ClientThreadsException;
import tcp.ServerException;
import tcp.TcpServer;

public class NetPileServer {
	private static TcpServer server;
	private int port;
	private String username;
	private String password;
	private String compiled;
	private String compiler = null;

	public NetPileServer(int port, String username, String password) throws Exception {
		this.username = username;
		this.password = password;
		this.port = port;

		if (username.length() > 16) {
			this.username = username.substring(0, 16);
		} else if (username.length() < 16) {
			for (int am = 0; am < (16 - username.length()); am++) {
				this.username += " ";
			}
		}

		if (password.length() > 16) {
			this.password = password.substring(0, 16);
		} else if (password.length() < 16) {
			for (int am = 0; am < (16 - password.length()); am++) {
				this.password += " ";
			}
		}
		System.out.println("Starting NetPile Server (Details):\nUsername: " + this.username + "\nPassword: "
				+ this.password + "\nPort: " + String.valueOf(port) + "\n");
	}

	// private String javac(String compilerPath) throws Exception {
	// String system = System.getProperty("os.name").toLowerCase();
	// this.compiler = null;
	// if (compilerPath == null) {
	// if (system.contains("window")) {
	// String root = "C:\\Program Files\\Java\\";
	// String root32 = "C:\\Program Files(x86)\\Java\\";
	// if (new File(root).isDirectory()) {
	// System.out.println("Detected 64bit computer!");
	// File toSee = new File(root);
	// String stuff[] = toSee.list();
	// for (int files = 0; files < stuff.length; files++) {
	// if (stuff[files].contains("jdk")) {
	// this.compiler = root + stuff[files] + "\\bin\\javac.exe";
	// System.out.println("Using: " + stuff[files]);
	// break;
	// }
	// }
	//
	// } else if (new File(root32).isDirectory()) {
	// System.out.println("Detected 32bit computer!");
	// File toSee = new File(root32);
	// String stuff[] = toSee.list();
	// for (int files = 0; files < stuff.length; files++) {
	// if (stuff[files].contains("jdk")) {
	// this.compiler = root + stuff[files] + "\\bin\\javac.exe";
	// System.out.println("Using: " + stuff[files]);
	// break;
	// }
	// }
	// } else {
	// System.err.println("Couldn't find Java folder in either\n" + root + "\n"
	// + root32);
	// }
	// } else if (system.contains("mac")) {
	// String root = "/usr/bin/javac";
	// String root2 = "/Library/Java/JavaVirtualMachines/";
	// if (new File(root).isFile()) {
	// this.compiler = "javac";
	// } else if (new File(root2).isDirectory()) {
	// File toSee = new File(root2);
	// String stuff[] = toSee.list();
	// for (int files = 0; files < stuff.length; files++) {
	// if (stuff[files].contains("jdk")) {
	// this.compiler = root + stuff[files] + "/bin/javac";
	// System.out.println("Using: " + stuff[files]);
	// break;
	// }
	// }
	// } else {
	// System.err.println("Couldn't find Java folder in either\n" + root + "\n"
	// + root2);
	// }
	// } else {
	// String root = "/usr/bin/javac";
	// String root2 = "/usr/lib/jvm/";
	// if (new File(root).isFile()) {
	// this.compiler = "javac";
	// } else if (new File(root2).isDirectory()) {
	// File toSee = new File(root2);
	// String stuff[] = toSee.list();
	// for (int files = 0; files < stuff.length; files++) {
	// if (stuff[files].contains("jdk")) {
	// this.compiler = root + stuff[files] + "/bin/javac";
	// System.out.println("Using: " + stuff[files]);
	// break;
	// }
	// }
	// } else {
	// System.err.println("Couldn't find Java folder in either\n" + root + "\n"
	// + root2);
	// }
	// }
	// } else {
	// return compilerPath;
	// }
	// if (this.compiler == null) {
	// throw new IOException("COULD NOT FIND JAVA JDK/javac compiler!");
	// }
	// return this.compiler;
	// }

	// /**
	// * Compiles and loads a java file
	// *
	// * @param path
	// * Directory path of the Java project
	// * @param main
	// * Name of the class with main()
	// * @throws Exception
	// */
	// public void javaFile(String path, String main) throws Exception {
	// this.javaFile(path, main, null);
	// }

	// /**
	// * Command that returns the character used to denote Strings in commands.
	// On
	// * Unix-like systems, it is a single quote ('). On Windows, it is a double
	// * quote (")
	// *
	// * @return
	// */
	// private static String getOSCommandSeperator() {
	// String system = System.getProperty("os.name").toLowerCase();
	// if (system.contains("window")) {
	// return "\"";
	// } else if (system.contains("mac")) {
	// return "\'";
	// }
	// return "\'";
	// }

	/**
	 * Loads a .jar file to be sent to clients
	 * 
	 * @param path
	 *            Path to the jar file
	 * @throws IOException
	 *             if the file can't be found
	 */
	public void putJar(String path) throws IOException {
		System.out.println("Loading jar file located at " + path);
		if (!path.contains(".jar")) {
			throw new IOException("Path doesn't lead to a .jar file!");
		}
		this.compiled = path;
		System.out.println("Encrypting file...");
		Serial.encryptTemp(this.username, this.password, this.compiled);
		System.out.println("Encrypted! (Not yet :( )");
	}

	// public void javaFile(String path, String main, String compilerPath)
	// throws Exception {
	// String outmain = main;
	// final String compiler = javac(compilerPath);
	// String toPrint;
	// System.out.println("Trying to compile: " + path);
	// if (outmain.contains(".java")) {
	// outmain += ".java";
	// }
	// // get the absolute path of the file
	// if (!outmain.contains(path)) {
	// outmain = path + outmain;
	// }
	// // the os command seperator is for directories with spaces in it.
	// final String command = String.format("%s %s", compiler, "-verbose
	// -sourcepath " + getOSCommandSeperator() + path
	// + getOSCommandSeperator() + " " + getOSCommandSeperator() + outmain +
	// getOSCommandSeperator());
	// System.out.println(command);
	// final Process process = Runtime.getRuntime().exec(command);
	// final BufferedReader inOK = new BufferedReader(new
	// InputStreamReader(process.getInputStream()));
	// final BufferedReader inBad = new BufferedReader(new
	// InputStreamReader(process.getErrorStream()));
	//
	// while ((toPrint = inOK.readLine()) != null) {
	// System.out.println(toPrint);
	// }
	// toPrint = null;
	// while ((toPrint = inBad.readLine()) != null) {
	// System.err.println("ERROR: " + toPrint);
	// }
	// process.waitFor();
	// int exitCode = process.exitValue();
	// if (exitCode == 0) {
	// System.out.println("Compiled!");
	// } else {
	// System.err.println("Error compiling! (Exit Code): " + exitCode);
	// }
	// this.compiled = path.replace(".java", ".class");
	// System.out.println("Encrypting compiled file!");
	// serial.encryptTemp(this.username, this.password, this.compiled);
	// System.out.println("Encrypted! (Not yet :( )");
	// }

	public void startLoop() throws ClientThreadsException, ServerException, IOException {
		System.out.println("Preparing to start server!\nChecking NetPile file");
		final String toSend = Serial.readTemp();

		server = new TcpServer(this.port);
		String ip = InetAddress.getLocalHost().getHostAddress();
		System.out.println("Started server on\nip: " + ip + "\nport: " + this.port + "\nWaiting for client...\n\n");
		server.onAccept(new AcceptListener() {

			@Override
			public String[] Modes() {
				String steps[] = { "SEND", "END" };
				return steps;
			}

			@Override
			public void Recieve(String FromClient, int ClientNum, int ArgNum) {
				// System.out.println("Got Client: " + ClientNum);
			}

			@Override
			public String Send(int ClientNum, int ArgNum) {
				System.out.println("Got Client\nNetPiled file being sent!");
				return toSend;
			}
		});
	}
}
