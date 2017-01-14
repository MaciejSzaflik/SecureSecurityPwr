package noob.pwr;
import java.math.*;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

public class DiffieHellmanProtocol {
	
	public CipherAES cipher;
	private SecretKeySpec createdKey;
	public Steps currentStep;
	public enum Steps{
		Generate,
		WaitForInitial,
		WaitForResponse,
		SendedValues,
		ValuesRecived,
		End,
		Invalid
	}
	
	private BigInteger ourPrime;
	private BigInteger ourGenerator;
	
	private BigInteger mySecret;
	private BigInteger ourSecret;
	private Random rnd;
	
	public DiffieHellmanProtocol(boolean initiator)
	{
		rnd = new Random();
		currentStep = initiator?Steps.Generate:Steps.WaitForInitial;
	}
	
    public boolean HaveFinished()
    {
    	return currentStep == Steps.End;
    }
	
	public String ParseMessage(String message)
	{
		String response = "";
		if(message.equals("Invalid"))
			return null;
		
		switch(currentStep)
		{
		case Generate:
			ourPrime = getPrime();
			ourGenerator = getRandom();
			mySecret = getRandom();
			response = ourPrime.toString() + ";" + ourGenerator.toString();
			currentStep = Steps.WaitForResponse;
			break;
		case WaitForResponse:
			if(message.isEmpty())
				return null;
			currentStep = Steps.ValuesRecived;
			response = calculateToSend().toString();
			CreateKey(new BigInteger(message));
			break;
		case Invalid:
			break;
		case WaitForInitial:
			if(ProccesInputInitial(message))
			{
				currentStep = Steps.SendedValues;
				response = calculateToSend().toString();
			}
			else{
				response = "Invalid";
				currentStep = Steps.Invalid;
			}
			break;
		case SendedValues:
			CreateKey(new BigInteger(message));
			response = "end";
			break;
		default:
			break;
		}
		System.out.println("Diffie: " + response);
		return response;
	}
	
	private boolean ProccesInputInitial(String message)
	{
		String[] values = message.split(";");
		if(values.length != 2)
			return false;
		ourPrime = new BigInteger(values[0]);
		ourGenerator = new BigInteger(values[1]);
		mySecret = getRandom();
		return true;
	}
	private BigInteger calculateToSend()
	{
		return ourGenerator.modPow(mySecret, ourPrime);
	}
	
	private void CreateKey(BigInteger message)
	{
		currentStep = Steps.End;
		ourSecret = message.modPow(mySecret, ourPrime);
		createdKey = AesKeyGenerator.GenerateAesKeyFromInteger(ourSecret);
		System.out.println("key creaded with: " + ourSecret);
		cipher = new CipherAES(createdKey);
	}
	
	
	public void SimulateDiffieHellman(){
		BigInteger p = getPrime();
		BigInteger g = getRandom();
		BigInteger a = getRandom();
		BigInteger b = getRandom();
		BigInteger toSendAlice = g.modPow(a, p);
		BigInteger toSendBob = g.modPow(b, p);
		
		BigInteger aliceResult = toSendBob.modPow(a,p);
		System.out.println(aliceResult.toString(10));
		
		BigInteger bobResult = toSendAlice.modPow(b,p);
		System.out.println(bobResult.toString(10));
	}
	
	
	private BigInteger getRandom()
	{
		return BigInteger.valueOf(rnd.nextLong());
	}
	
	private BigInteger getPrime()
	{
		int bitLength = 128;
		return BigInteger.probablePrime(bitLength, rnd);
	}
}
