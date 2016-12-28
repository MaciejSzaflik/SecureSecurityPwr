package noob.pwr;
 
public class TalkProtocol {
	public String name;
    public TalkResponse processInput(String theInput) {
    	if(name == null)
    	{
    		name = theInput;
    		return new TalkResponse(ResponseType.NameSet,null);
    	}
        return new TalkResponse(ResponseType.Talk,theInput);
    }
}

