package zr;

import java.util.Calendar;

public class LocalTime {

	public static int Get()
	{
		Calendar c = Calendar.getInstance();
		int time =  (c.get(Calendar.SECOND) * 10 + c.get(Calendar.MINUTE) * 60 * 10 + c.get(Calendar.MILLISECOND) / 100);
		return time;
	}
	
	public static int relaTime(int t)
	{
		Calendar c = Calendar.getInstance();
		int time =  (c.get(Calendar.SECOND) * 10 + c.get(Calendar.MINUTE) * 60 * 10 + c.get(Calendar.MILLISECOND) / 100);
		if (time<t) time += 36000;
		return time - t;
	}
}
 