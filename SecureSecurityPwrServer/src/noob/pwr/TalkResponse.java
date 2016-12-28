package noob.pwr;

public class TalkResponse {
	public ResponseType type;
	public String message;
	public TalkResponse(ResponseType type, String message)
	{
		this.type = type;
		this.message = message;
	}
}
