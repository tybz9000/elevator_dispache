package zr;

public class ReqLine {
	static int max_length = 50;

	private String name;
	private Request[] rline;
	private boolean[] vline;
	private int rlen;
	private int num;
	private boolean debug;
	private boolean available;
	private Request return_r;
	
	public ReqLine(String name, boolean DEBUG)
	{
		this.name = name;
		rline = new Request[max_length];
		vline = new boolean[max_length];
		for(int i=0;i<max_length;i++) vline[i] = false;
		rlen = -1;
		num = 0;
		available = true;
		return_r = new Request();
		debug = DEBUG;
	}
	
	String nameGet(){ return name; }

	int lengthGet(){ return rlen + 1; }
	
	int numGet(){ return rlen+1 - num; }
	
	boolean exist(Request r)
	{
		for (int i=0;i<=rlen;i++)
			if (rline[i].equals(r))
				return true;
		return false;
	}
	
	public synchronized void del(int i)
	{
		if (i>=0 && i<=rlen && vline[i]){
			if (debug) System.out.println(rline[i] + "(" + rline[i].timeGet() +"/" + i + ")" + " taken from " + this.nameGet());
			num ++;
			vline[i] = false;
		}
	}
	
	public synchronized boolean memberAdd(Request req)
	{
		if (req!=null){
			if (req.Valid() && !exist(req)) {
				if (rlen+1>max_length-1){
					System.out.println("error : ReqLine overflow!" + "(>" + max_length + ")");
					notifyAll();
					return false;
				} else {
					rlen ++;
					rline[rlen] = req;
					vline[rlen] = true;
					if (debug) System.out.println(req + "(" + req.timeGet() +"/" + rlen + ")" + " add to " + this.nameGet());
				}
			} else if (req.isend()) {
				notifyAll();
				return false;
			} else {
				System.out.println("warning : has invalid input");
			}
		}
		notifyAll();
		return true;
	}
/*
	public synchronized Request memberGet()
	{
		
		while (!available)
			try{ 
				if (debug) System.out.println(Thread.currentThread().getName() + " waits");
				wait(); 
			} catch(Exception e){}
		if (debug) System.out.println(Thread.currentThread().getName() + " starts");
		
		
		if (!available) {
			System.out.println(return_r + " gives");
			return return_r;
		}
		int FstVld;
		for(FstVld=0;FstVld<=rlen;FstVld++)
			if (vline[FstVld]) break;
		if (FstVld<=rlen && vline[FstVld]) {
			if (debug) System.out.println(rline[FstVld] + "(" + rline[FstVld].timeGet() +"/" + FstVld + ")" + " taken from " + this.nameGet());
			vline[FstVld] = false;
			num ++;
			notifyAll();
			return rline[FstVld];
		} else {
			notifyAll();
			return null;
		}
	}
*/
	public synchronized Request memberGet(int i)
	{
		/*
		while (this.num>this.rlen)
			try{ 
				if (debug) System.out.println(Thread.currentThread().getName() + " waits");
				wait(); 
			} catch(Exception e){}
		if (debug) System.out.println(Thread.currentThread().getName() + " starts");
		*/
		if (i>=0 && i<=rlen && vline[i]) {
			return rline[i];
		} else {
			return null;
		}
	}
	
	public synchronized void memberChange(int i, Request r)
	{
		if (i>=0 && i<=rlen && vline[i]){
			rline[i] = r;
			vline[i] = true;
		}
	}
	
	public synchronized void enable()
	{
		available = true;
	}
	
	public synchronized void disable()
	{
		available = false;
	}
	
	public synchronized void stall()
	{
		if(!available){
			try{
				Thread.sleep((long)(1000*Math.random()));
			} catch (Exception e){ }
		}
	}
	
	public synchronized void AV()
	{
		if (!available) {
			System.out.println(return_r + " passes");
			available = true;
		}
	}
}
