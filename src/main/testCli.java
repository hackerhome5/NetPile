package main;

public class testCli {

	public static void main(String[] args) {
		try {
			NetPile client = new NetPile("localhost", 8082, "david", "smerkous");
			client.pullFile();
			client.runFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
