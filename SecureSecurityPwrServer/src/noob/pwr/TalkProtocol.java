package noob.pwr;
 
public class TalkProtocol {
	public String name;
	public String password;
    public TalkResponse processInput(String theInput) {
    	if(name == null)
    	{
    		String[] nameAndPass = theInput.split(";");
    		if(CheckPassword(nameAndPass[0],nameAndPass[1]))
    		{
    			name = nameAndPass[0];
    			password = nameAndPass[1];
    			System.out.println("password ok");
    			return new TalkResponse(ResponseType.NameSet,"Valid");
    		}
    		else
    		{
    			System.out.println("password bad");
    			return new TalkResponse(ResponseType.PasswordInvalid,"InvalidPass");
    		}
    	}
        return new TalkResponse(ResponseType.Talk,theInput);
    }
    
    public boolean CheckPassword(String name,String password)
    {
    	return SimpleDataBase.getInstance().CheckInMap("passwords", name, password);
    }
}

