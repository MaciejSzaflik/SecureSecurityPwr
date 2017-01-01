package noob.pwr;

public class TalkResponse {
	public ResponseType type;
	public String message;
	public String reciver;
	public TalkResponse(ResponseType type, String message,String reciver)
	{
		this.type = type;
		this.message = message;
		this.reciver = reciver;
	}
}
