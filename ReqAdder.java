package zr;

import java.util.Scanner;

public class ReqAdder extends Thread
{
	private ReqLine reql = null;
	private int base_time;
	
	public ReqAdder(ReqLine reql, String id, int base_time)
	{
		this.reql = reql;
		this.setName(id);
		this.base_time = base_time;
	}
	
	public void run()
	{
		Scanner sc = new Scanner(System.in);
		boolean flag = true;
		Request req = null;
		while(flag) {
			try {
				req = new Request(sc.nextLine(), base_time);
				if (req!=null)
					flag = this.reql.memberAdd(req);
			} catch (Exception e) {}
		}
		sc.close();
	}
}
