package tcp;

public class ClientException extends Exception
{

    private static final long serialVersionUID = 1997753363232807009L;

		public ClientException()
		{
		}

		public ClientException(String message)
		{
			super(message);
		}

		public ClientException(Throwable cause)
		{
			super(cause);
		}

		public ClientException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public ClientException(String message, Throwable cause, 
                                           boolean enableSuppression, boolean writableStackTrace)
		{
			super(message, cause, enableSuppression, writableStackTrace);
		}

}