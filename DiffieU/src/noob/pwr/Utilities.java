package noob.pwr;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
	public static String GetTimeStamp()
	{
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}
}
