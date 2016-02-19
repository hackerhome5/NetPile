package tcp;

public class ClientThreadsException extends Exception
{

    private static final long serialVersionUID = 1997753363232807009L;

		public ClientThreadsException()
		{
		}

		public ClientThreadsException(String message)
		{
			super(message);
		}

		public ClientThreadsException(Throwable cause)
		{
			super(cause);
		}

		public ClientThreadsException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public ClientThreadsException(String message, Throwable cause, 
                                           boolean enableSuppression, boolean writableStackTrace)
		{
			super(message, cause, enableSuppression, writableStackTrace);
		}

}