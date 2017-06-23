package zr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
	static final int FR_UP = 1;
	static final int FR_DOWN = -1;
	static final int ER = 0;
	static int max_floor = 20;
	static long max_time = 999999999;
	static int elevator_count = 3;
	
	private boolean flag;
	private boolean isend;
	private int type;
	private int dest;
	private int elno;
	private int time;
	private String oristr;
	
	public Request()
	{
		flag = false;
		isend = false;
		type = 0;
		dest = 0;
		elno = 0;
		time = 0;
		oristr = "";
	}
	
	public Request(String str_input, int base_time)
	{
		time = LocalTime.relaTime(base_time);
		
		oristr = str_input.trim();
		
		// end signal : .
		
		if (oristr.contentEquals(".")) isend = true;
		
		// floor request    : (FR,#floor,UP/DOWN)

		String regular_fr = "(\\(FR,[0-9]{1,9},(UP|DOWN)\\))";
		Pattern p_fr = Pattern.compile(regular_fr);
		Matcher m_fr =p_fr.matcher(oristr);
		
		// elevator request : (ER,#elevator,#floor)
		
		String regular_er = "(\\(ER,\\#[0-9]{1,9},[0-9]{1,9}\\))";
		Pattern p_er = Pattern.compile(regular_er);
		Matcher m_er =p_er.matcher(oristr);
		
		if (m_fr.matches())
		{
			String[] strlist = oristr.split(",");
			dest = Integer.parseInt(strlist[1]);
			if (strlist[2].equals("DOWN)"))
				type = FR_DOWN;
			else
				type = FR_UP;
			// flag is false when DOWN at L1 or UP at Ltop or destination==0 or destination>max_floor
			flag = !(type==FR_DOWN & dest==1 | type==FR_UP & dest==max_floor) & !(dest==0) & !(dest>max_floor);
		} else if (m_er.matches()) {
			type = ER;
			String[] strlist = oristr.split(",");
			elno = Integer.parseInt(strlist[1].substring(1));
			dest = Integer.parseInt(strlist[2].replace(")",""));
			// flag is false when destination outof max_floor  or elevator number outof elevator_count
			flag = !(dest==0) & !(dest>max_floor) & !(elno==0) & !(elno>elevator_count);
		} else {
			flag = false;
		}
		
		
	}

	boolean Valid(){ return flag; }
	
	boolean isend(){ return isend; }
	
	int typeGet(){ return type; }
	
	int destGet(){ return dest; }
	
	int elnoGet(){ return elno; }
	
	long timeGet(){ return time; }

	String oriGet(){ return oristr; }
	
	public String toString()
	{
		if (flag)
			switch (type) {
				case FR_UP   : return "(FR," + dest + ",UP)";
				case FR_DOWN : return "(FR," + dest + ",DOWN)";
				case ER      : return "(ER,#" + elno + "," + dest + ")";
				default		 : return "error";
			}
		else
			return "invalid request format!";
	}
	
	public boolean equals(Object obj)
	{
		if (obj instanceof Request){
			Request r = (Request) obj;
			return this.type==r.type 
				 & this.flag==r.flag
				 & this.dest==r.dest
				 & this.elno==r.elno
				 & this.time==r.time;
		}
		else
			return false;
	}
	
	public void copy(Request r)
	{
		flag = r.flag;
		isend = r.isend;
		type = r.type;
		dest = r.dest;
		elno = r.elno;
		time = r.time;
		oristr = r.oristr;
	}
}
