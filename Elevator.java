package zr;

interface ElevatorAction
{
	final int ELEV_STOP = 0;
	final int ELEV_UP 	 = 1;
	final int ELEV_DOWN = -1;
	
	abstract int floorGet();
	abstract int timeGet();
	abstract int stateGet();
	abstract void Stop();
	abstract void Up();
	abstract void Down();
	abstract void Dock();
	abstract boolean reqSet(Request r);
	abstract void move();
}

class Elevator implements ElevatorAction{
	private String name;
	private boolean doorop;
	private int state;
	private int current_floor;
	private int current_time;
	private Request current_request;
	private long total_mvcnt;
	private int base_time;
	private boolean debug;
	
	public Elevator(String name, int base_time, boolean DEBUG)
	{
		this.name = name;
		doorop = false;
		current_floor = 1;
		current_time = 0;
		current_request = null;
		state = ELEV_STOP;
		total_mvcnt = 0;
		this.base_time = base_time;
		debug = DEBUG;
	}
	
	public String nameGet(){ return name; }
	
	public int floorGet(){ return current_floor; }
	
	public int timeGet(){ return current_time; }
	
	public Request rqstGet(){ return current_request; }
	
	public int stateGet(){ return state; }
	
	private String statePrint()
	{
		switch (state){
			case ELEV_UP   : return "UP"  ;
			case ELEV_DOWN : return "DOWN";
			default		   : return "IDLE";
		}
	}
	
	public long mvcntGet(){ return total_mvcnt; }
	
	private String tPrint()
	{
		int min,sec,mil;
		min = current_time/600;
		sec = current_time%600/10;
		mil = current_time%10;
		if (min<10 && sec<10)
			return "0" + min + "\"" + "0" + sec + "." + mil +"\'";
		else if (min<10)
			return "0" + min + "\"" +     + sec + "." + mil +"\'";
		else if (sec<10)
			return     + min + "\"" + "0" + sec + "." + mil +"\'";
		else
			return     + min + "\"" +     + sec + "." + mil +"\'";

	}
	
	private String statusPrint()
	{
		if (current_floor<10)
			return "(" + this.name + ",#0" + current_floor + "," + statePrint() + "," + total_mvcnt + "," + tPrint() + ")";
		else
			return "(" + this.name + ",#"  + current_floor + "," + statePrint() + "," + total_mvcnt + "," + tPrint() + ")";
	}
	
	public synchronized void Stop()
	{
		if (debug) System.out.println(this.name + " wait at " + current_floor + "," + tPrint());
		state = ELEV_STOP;
	}
	
	public synchronized void Up()
	{
		if (debug) System.out.println(this.name + " up" + ":" + tPrint());
		state = ELEV_UP;
		current_floor ++;
		total_mvcnt ++;
		current_time += 30;
		try{ Thread.sleep(1000*3); } catch(Exception e){}
	}
	
	
	public synchronized void Down()
	{
		if (debug) System.out.println(this.name + " down" + ":" + tPrint());
		state = ELEV_DOWN;
		current_floor --;
		total_mvcnt ++;
		current_time += 30;
		try{ Thread.sleep(1000*3); } catch(Exception e){}
	}
	
	public synchronized void Dock()
	{
		if (debug) System.out.println(name + " open door at " + current_floor + ":" + tPrint());
		if (!doorop) System.out.println(statusPrint());
		current_time += 60;
		if (!doorop) try{ doorop = true;Thread.sleep(1000*6); } catch(Exception e){}
	}

	public String toString()
	{
		String state_output;
		
		switch (state)
		{
			case ELEV_STOP :
				state_output = "STOP";
				break;
			case ELEV_UP :
				state_output = "UP";
				break;
			case ELEV_DOWN :
				state_output = "DOWN";
				break;
			default : 
				state_output = "";
				break;
		}
		return  "(" + name + " at " + current_floor + "," + state_output + "," + tPrint() + ")";
	}
	
	public synchronized boolean reqSet(Request request)
	{
		if (request==null) {
			return false;
		} else {
			current_request = request;
			if (request.destGet()>current_floor) state = ELEV_UP;
			else if (request.destGet()<current_floor) state = ELEV_DOWN;
			else state = ELEV_STOP;
			current_time = LocalTime.relaTime(base_time);
			if (debug) System.out.println(this.name + "(" + statePrint() + ")activated by" + request + " at :" + tPrint());
			return true;
		}
	}
	
	public synchronized void move()
	{
		if (current_request!=null) {
			doorop = false;
			if (current_request.destGet()<current_floor) {
				this.Down();
			} else if (current_request.destGet()==current_floor) {
				this.Stop();
				this.current_request = null;
			} else {
				this.Up();
			}
		}
	}
	
}