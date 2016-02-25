package main;

public class testLiav {
	// im going to comment this so i can remember how to do this. we must have
	// documentation later
	public static void main(String... args) {
		try {
			new Thread() {
				@Override
				public void run() {
					try {
						// open server
						final NetPileServer server = new NetPileServer(42666, "liav", "turkia");
						// specifiy what file to send
						server.putJar("D:\\Onedrive\\FIVIK BACKUPS\\FIVIK\\fivik.jar");
						// start transmitting
						server.startLoop();
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}.start();

			// client
			final NetPile pile = new NetPile("localhost", 42666, "liav", "turkia");
			// grab file from server
			pile.pullFile();
			// run the file from the server
			pile.runFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
