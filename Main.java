package zr;

public class Main {
	public static void main(String[] args)
	{
		boolean DEBUG = true;
		
		int base_time = LocalTime.Get();
		
		ReqLine RLmain = new ReqLine("RLmain", !DEBUG);
		int elcnt = 3;
		ReqLine[] RLbranch = new ReqLine[elcnt];
		ElevRunner[] erun = new ElevRunner[elcnt];
		Elevator[] elev = new Elevator[elcnt];
		for(int i=0;i<elcnt;i++) {
			RLbranch[i] = new ReqLine("RLbranch"+(i+1),!DEBUG);
			elev[i] = new Elevator("#"+(i+1), base_time, !DEBUG);
			erun[i] = new ElevRunner("#erunner"+(i+1),elev[i], RLbranch[i]);
		}
		ReqAdder amain = new ReqAdder(RLmain, "#addermain", base_time);
		ReqDispatcher d1 = new ReqDispatcher(RLmain, RLbranch, elev, "#dispatcher1");
		
		amain.start();
		d1.start();
		for(int j=0;j<elcnt;j++)
			erun[j].start();
	} 
}
