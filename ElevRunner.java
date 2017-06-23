package zr;

public class ElevRunner extends Thread
{
	static final int FR_UP = 1;
	static final int FR_DOWN = -1;
	static final int ER = 0;
	static final int ELEV_STOP = 0;
	static final int ELEV_UP 	 = 1;
	static final int ELEV_DOWN = -1;
	
	private Elevator elev;
	private ReqLine reql;
	
	public ElevRunner(String id, Elevator elev, ReqLine reql)
	{
		this.elev = elev;
		this.reql = reql;
		this.setName(id);
	}

	private boolean judgeshift(int i)
	{
		if (i<0 || i>=reql.lengthGet())
			return false;
		else if (reql.memberGet(i)==null)
			return false;
		boolean special_judge = false;
		if (elev.rqstGet()!=null)
			special_judge = elev.floorGet()==elev.rqstGet().destGet() & reql.memberGet(i).destGet()==elev.rqstGet().destGet();
		return reql.memberGet(i).typeGet()==FR_UP   & elev.stateGet()==ELEV_UP   & reql.memberGet(i).destGet()==elev.floorGet() |
			   reql.memberGet(i).typeGet()==FR_DOWN & elev.stateGet()==ELEV_DOWN & reql.memberGet(i).destGet()==elev.floorGet() |
			   reql.memberGet(i).typeGet()==ER & reql.memberGet(i).destGet()==elev.floorGet() |
			   special_judge;
	}
	
	private void doorop()
	{
		int cnt=0;
		boolean opflag = false;
		for(int i=0;i<reql.lengthGet();i++)
			if (elev.rqstGet()!=null)
				if (elev.rqstGet().destGet()==elev.floorGet() || judgeshift(i)){
					opflag = true;
					break;
				}
		if (opflag) {
			elev.Dock();
			if(elev.rqstGet().destGet()==elev.floorGet())
				System.out.println("(" + elev.rqstGet() + "," + elev.nameGet() + ")");
			cnt = 0;
			for(int j=0;j<reql.lengthGet();j++)
				if (judgeshift(j)){
					System.out.println("(" + reql.memberGet(j) + "," + elev.nameGet() + ")");
					cnt ++;
					reql.del(j);
				}
		}
	}
	
	private boolean judgereqset(int i)
	{
		if (i<0 || i>=reql.lengthGet())
			return false;
		else if (reql.memberGet(i)==null)
			return false;
		else if (elev.stateGet()==ELEV_STOP)
			return false;
		else if (elev.stateGet()==ELEV_UP)
			return reql.memberGet(i).typeGet()==ER && reql.memberGet(i).destGet()>elev.rqstGet().destGet();
		else if (elev.stateGet()==ELEV_DOWN)
			return reql.memberGet(i).typeGet()==ER && reql.memberGet(i).destGet()<elev.rqstGet().destGet();
		else 
			return false;
	}
	
	public void run()
	{
		int i,j, tmp;
		Request tmpr = null;
		while(true){
			if (elev.rqstGet()==null) {
				for(j=0;j<reql.lengthGet();j++)
					if (reql.memberGet(j)!=null) {
						elev.reqSet(reql.memberGet(j));
						reql.del(j);
						break;
					}
			} else {
				tmp = -1;
				for(i=0;i<reql.lengthGet();i++)
					if (judgereqset(i)) tmp = i;
				if (tmp>=0) {
					tmpr = elev.rqstGet();
					elev.reqSet(reql.memberGet(tmp));
					reql.memberChange(tmp,tmpr);
				}
			}
			doorop();
			elev.move();
		}
	}
	
}
