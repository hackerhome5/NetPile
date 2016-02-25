package main;

public class testServe {

	public static void main(String[] args) throws Exception {
		NetPileServer server;

		try {
			server = new NetPileServer(8082, "david", "smerkous");
			// server.javaFile("C:\\Users\\David\\Desktop\\", "NetPile.java");
			server.startLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Serial o = new Serial();
		// o.encryptTemp("davidsmerkous123", "javapapers",
		// "C:\\Users\\David\\Desktop\\NetPile.class");
		// FileWriter file = new FileWriter(new
		// File("C:\\Users\\David\\Desktop\\NetPile2.class"));
		// file.write(o.decryptTemp("davidsmerkous123", "davidsmerkous126"));
		// file.close();
		;// "C:\\Users\\David\\Desktop\\NetPile2.class");

	}

}
