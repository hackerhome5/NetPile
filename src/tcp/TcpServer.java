package tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import tcp.ClientThreadsException;

public class TcpServer {
	
	private ServerSocket server;
	private int port = 8081;
	private Threads threads;
	
	/*
	 * @param port The crap
	 */
	
	public TcpServer(int port) throws ServerException, ClientThreadsException
	{
		this(port, 0);
	}
	
	public TcpServer(int port, int MaxClients) throws ServerException, ClientThreadsException
	{
		try {
			this.port = port;
			this.server = new ServerSocket();
			this.server.setReuseAddress(true);
			this.server.setReceiveBufferSize(1024);
			this.server.bind(new InetSocketAddress(this.port));
			threads = new Threads(MaxClients);
		} catch (IOException e) {
			try {
				this.close();
			} catch(NullPointerException ignored){}
			throw new ServerException("Could not create server on port: "+ String.valueOf(this.port));
		}
	}

	public void close()
	{
		try {
			this.server.close();
		} catch (IOException | NullPointerException ignored) {}
	}
	
	public void onAccept(AcceptListener listener) throws ClientThreadsException, ServerException
	{
		try {
			while(true) threads.bind(server.accept(), listener);
		} catch (IOException e) {
			throw new ServerException("Couldn't bind accept to threads!");
		}
	}
}

class Threads {
	
	private int MaxThreads = 0; 
	public int current = 0;
	public boolean threads[];

	public Threads(int maxthread) throws ClientThreadsException
	{
		if(maxthread < 0)
		{
			throw new ClientThreadsException("Max Clients needs to be equal/above 0!");
		}
		this.MaxThreads = maxthread;
	}

	public void bind(Socket onAccept, AcceptListener listener) throws ClientThreadsException
	{
		this.current = (this.current == 0) ? -1 : this.current;
		if((this.current += 1) > this.MaxThreads)
		{
			throw new ClientThreadsException("Too many clients! Max: " + this.MaxThreads);
		}
		
		new ClientThread(this.current, onAccept, listener).start();
	}
}

class ClientThread extends Thread {
	
	private Socket client;
	private int num;
	private int coms;
	private AcceptListener toCall;
	private BufferedReader in;
	private PrintWriter out;
	
	public ClientThread(int num, Socket serveSock, AcceptListener toCall)
	{
		this.num = num;
		this.client = serveSock;
		this.toCall = toCall;
		this.coms = 0;
	}
	
	public void run()
	{
			String modes[] = this.toCall.Modes();
			if(modes.length > 30)
			{
				try {
					this.client.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				try {
					throw new ClientThreadsException("Too many args up to 30 (client): " + this.num); //30 will lag and be overkill
				} catch (ClientThreadsException e) {
					e.printStackTrace();
				}
			}
			if(Arrays.toString(modes).toUpperCase().contains("RECV")) {
				try {
					in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
				} catch (IOException e) {
					try {
						this.client.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					try {
						throw new ClientThreadsException("Error getting input stream (client): " + this.num);
					} catch (ClientThreadsException e1) {
						e1.printStackTrace();
					}
				}
			}
			if(Arrays.toString(modes).toUpperCase().contains("SEND")) {
				try {
					out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(this.client.getOutputStream())), true);
				} catch (IOException e) {
					try {
						this.client.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					try {
						throw new ClientThreadsException("Error getting output stream! (client): " + this.num);
					} catch (ClientThreadsException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			for(int amount = 0; amount < modes.length; amount++)
			{
				String mode = modes[amount].toUpperCase();
				if(mode.contains("RECV"))
				{
					try {
						this.toCall.Recieve(in.readLine(), this.num, this.coms);
					} catch (IOException e) {
						try {
							this.client.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						try {
							throw new ClientThreadsException("Error recieving from client: " + this.num);
						} catch (ClientThreadsException e1) {
							e1.printStackTrace();
						}
					}
					this.coms += 1;
				}
				else if(mode.contains("SEND"))
				{
					try {
						out.println(this.toCall.Send(this.num, this.coms));
						this.coms += 1;
					} catch (Exception e) {
						try {
							this.client.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
						try {
							throw new ClientThreadsException("Error sending to client: " + this.num);
						} catch (ClientThreadsException e1) {
							e1.printStackTrace();
						}
					}
				}
				else if(mode.contains("END"))
				{
					try {
						this.client.close();
					} catch (IOException e) {
						try {
							throw new ClientThreadsException("Error closing the client: " + this.num);
						} catch (ClientThreadsException e1) {
							e1.printStackTrace();
						}
					}
					break;
				}
				else
				{
					try {
						this.client.close();
					} catch (IOException e) {
						try {
							throw new ClientThreadsException("Mode: " + mode + " Not found! (client): " + this.num);
						} catch (ClientThreadsException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
	}
}