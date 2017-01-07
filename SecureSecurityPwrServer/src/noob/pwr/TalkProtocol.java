package noob.pwr;
 
public class TalkProtocol {
	
	public String name;
	public String password;
	
    public TalkResponse processInput(String theInput) {
    	
    	String[] input = theInput.split(";");
    	String option = input[0];
    	String values = input[1];
    	String message = input[2];
    	ResponseType parsedOption = optionToResponseType(option);
    	//System.out.println(option + " " + values + " " + message); 
    	if(password == null && parsedOption == ResponseType.PasswordInvalid)
    	{
    		return ResponseForPasswordCheck(values);
    	}
    	else if(parsedOption == ResponseType.GetUsers)
    	{
    		return new TalkResponse(ResponseType.GetUsers,ConnectionKeeper.getInstance().GetUsersNames(),null);
    	}
    	else if(parsedOption == ResponseType.Whisper)
    	{
    		return new TalkResponse(ResponseType.Whisper,message,values);
    	}
    	else if(parsedOption == ResponseType.Talk)
    	{
    		return new TalkResponse(ResponseType.Talk,message,null);
    	}
    	else if(parsedOption == ResponseType.History)
    	{
    		return new TalkResponse(ResponseType.History,null,values);
    	}
    	return new TalkResponse(ResponseType.Invalid,theInput,null);
    }
    
    private ResponseType optionToResponseType(String option)
    {
    	switch(option)
    	{
    	case ComConst.USERS:
    		return ResponseType.GetUsers;
    	case ComConst.PASS_AND_NICK:
    		return ResponseType.PasswordInvalid;
    	case ComConst.WHISPER:
    		return ResponseType.Whisper;
    	case ComConst.BROADCAST:
    		return ResponseType.Talk;
    	case ComConst.HISTORY:
    		return ResponseType.History;
		default:
    		return ResponseType.Invalid;
    	}
    }
    
    
    private TalkResponse ResponseForPasswordCheck(String values)
    {
    	String[] nameAndPass = values.split(":");
		if(CheckPassword(nameAndPass[0],nameAndPass[1]))
		{
			name = nameAndPass[0];
			password = nameAndPass[1];
			System.out.println("password ok");
			return new TalkResponse(ResponseType.NameSet,"Valid",null);
		}
		else
		{
			System.out.println("password bad");
			return new TalkResponse(ResponseType.PasswordInvalid,"InvalidPass",null);
		}
    }
    
    public boolean CheckPassword(String name,String password)
    {
    	return SimpleDataBase.getInstance().CheckInMap("passwords", name, password);
    }
}

