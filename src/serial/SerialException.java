package serial;

public class SerialException extends Exception
{

    private static final long serialVersionUID = 1997753363232807009L;

		public SerialException()
		{
		}

		public SerialException(String message)
		{
			super(message);
		}

		public SerialException(Throwable cause)
		{
			super(cause);
		}

		public SerialException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public SerialException(String message, Throwable cause, 
                                           boolean enableSuppression, boolean writableStackTrace)
		{
			super(message, cause, enableSuppression, writableStackTrace);
		}

}