package zr;

public class ReqDispatcher extends Thread
{
	static final int FR_UP = 1;
	static final int FR_DOWN = -1;
	static final int ER = 0;
	static final int ELEV_STOP = 0;
	static final int ELEV_UP 	 = 1;
	static final int ELEV_DOWN = -1;
	
	private ReqLine RLmain = null;
	private ReqLine[] RLbranch = null;
	private Elevator[] elevs = null;
	private int branch_cnt;
	
	public ReqDispatcher(ReqLine RLmain, ReqLine[] RLbranch, Elevator[] elevs, String id)
	{
		this.RLmain = RLmain;
		this.RLbranch = RLbranch;
		this.elevs = elevs;
		this.branch_cnt = RLbranch.length;
		this.setName(id);
	}

	private boolean shftjdg(int elevno, Request r)
	{
		if (elevs[elevno].rqstGet()==null)
			return false;
		boolean judge1 = elevs[elevno].stateGet()==ELEV_UP &
						 r.typeGet()==FR_UP &
						 elevs[elevno].floorGet() <= r.destGet() &
						 elevs[elevno].rqstGet().destGet() >= r.destGet();
		boolean judge2 = elevs[elevno].stateGet()==ELEV_DOWN &
					 	 r.typeGet()==FR_DOWN &
					 	 elevs[elevno].floorGet() >= r.destGet() &
					 	 elevs[elevno].rqstGet().destGet() <= r.destGet(); 
		return judge1 | judge2;
	}
	
	private boolean respjdg(int elevno,Request r)
	{
		boolean judge1 = elevs[elevno].stateGet()==ELEV_STOP ;
		boolean judge2 = elevs[elevno].rqstGet()==null;
		return judge1 & judge2;
	}
	
	public void run()
	{
		Request newreq = null;
		int i,j,k;
		int shiftelevno = -1;
		int respoelevno = -1;
		while(true) {
			//RLmain.stall();
			/* go through RLmain to find possible to-dispatch-request */
			synchronized (RLbranch) {
			for(k=0;k<RLmain.lengthGet();k++){
				newreq = RLmain.memberGet(k);
				if (newreq!=null){	
					/*	first the specific elevator request
					 *  then the least total_mvcnt can-shift elevator
					 *	then the least total_mvcnt can-respond elevator
					 */
					if (newreq.typeGet()==ER) {
						this.RLbranch[newreq.elnoGet()-1].memberAdd(newreq);
						RLmain.del(k);
						System.out.println(newreq +""+ newreq.timeGet() + " add to " + elevs[newreq.elnoGet()-1].nameGet());
						continue;
					}
						
					shiftelevno = -1;
					respoelevno = -1;
					for (i=0;i<branch_cnt;i++) {
						if (shftjdg(i,newreq))
							if (shiftelevno<0) shiftelevno = i;
							else shiftelevno = elevs[shiftelevno].mvcntGet()<elevs[i].mvcntGet() ? shiftelevno : i;
					}
					if (shiftelevno>=0) {
						this.RLbranch[shiftelevno].memberAdd(newreq);
						RLmain.del(k);
						System.out.println(newreq +""+ newreq.timeGet() + " add to " + elevs[shiftelevno].nameGet());
						continue;
					}
					for (j=0;j<branch_cnt;j++){
						if (respjdg(j,newreq))
							if (respoelevno<0) respoelevno = j; 
							else respoelevno = elevs[respoelevno].mvcntGet()<elevs[j].mvcntGet() ? respoelevno : j;
					}
					if (respoelevno>=0) {
						this.RLbranch[respoelevno].memberAdd(newreq);
						RLmain.del(k);
						System.out.println(newreq +""+ newreq.timeGet() + " add to " + elevs[respoelevno].nameGet());
						continue;
					}
				}
			}
			RLmain.disable();
		}
		}
	}
}
