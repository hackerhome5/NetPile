package tcp;

public interface AcceptListener {
	
	/*
	 * Select what modes to be in what order/send/recv/end
	 * This must be returned in a String array
	 * @return String Array
	 */
	
	public String[] Modes();
	
	/*
	 * To receive data from the client (Not just one but multiple)
	 * @param FromClient Full line (\n) from the Client
	 * @param ClientNum Current client num/thread
	 * @param ArgNum The Current mode in the array it's at
	 * @return@param ArgNum The Current mode in the array it's at void
	 */
	
	public void Recieve(String FromClient, int ClientNum, int ArgNum);
	
	/*
	 * This is to send data to the current threaded client
	 * @param ClientNum Current client num/thread
	 * @param ArgNum The Current mode in the array it's at
	 * @return String that gets sent to the client
	 */
	
	public String Send(int ClientNum, int ArgNum);
	
}
