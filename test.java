package zr;

import java.util.Calendar;
/*
public class test
{
	public static void main(String[] args)
	{
		tickets t = new tickets(10);
		new adder(t,"adder  1").start();
		new seller(t,"seller 1").start();
		new seller(t,"seller 2").start();
	}
}
*/
class tickets
{
	private int count;
	private int number = 0;
	boolean flag = false;
	
	public tickets(int count)
	{
		this.count = count;
	}
	
	boolean isend()
	{
		return number > count;
	}
	
	void add()
	{
		while (flag)
			try{ wait(); }
			catch(Exception e){}
		System.out.printf(Thread.currentThread().getName() + " add  code " + ++number + " ticket\n");
		flag = true;
		notifyAll();
	}
	
	void sell()
	{
		while (!flag)
			try{ wait(); }
			catch(Exception e){System.out.printf(Thread.currentThread().getName() + " already wait\n");}
		Calendar c = Calendar.getInstance();
		int ms = c.get(Calendar.MILLISECOND);
		System.out.printf(Thread.currentThread().getName() + " sell code " + number + " ticket at " + ms/1000 + "." + ((ms%1000)/50+1)/2 + "(" + ms + "ms)" + "s\n");
		flag = false;
		if (count==number) number = count + 1;
		else notifyAll();
		
	}
}

class adder extends Thread
{
	private tickets t = null;
	
	public adder(tickets t, String id)
	{
		this.t = t;
		this.setName(id);
	}
	
	public void run()
	{
		while(!t.isend())
			synchronized(t){ 
				this.t.add(); 
			}
	}
}

class seller extends Thread
{
	private tickets t = null;
	
	public seller(tickets t, String id)
	{
		this.t = t;
		this.setName(id);
	}
	
	public void run()
	{
		while(!t.isend())
			synchronized(t){ 
				this.t.sell();
			}
	}
}
