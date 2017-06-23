package zr;

//not useful in elevator 3.0

class Controller {
	protected int code;
	protected ReqLine reql;
	protected Elevator elev;
	
	public Controller()
	{
		code = 0;
	}
	
	int codeGet()
	{
		return code;
	}
/*
	void Schedule()
	{
		reql = new ReqLine();
		boolean valid = reql.Read();
		if (valid)
		{
			elev = new Elevator();
			int i,j,dif;
			for(i=0;i<reql.lengthGet();i++)
			{
				dif = elev.floorGet() - reql.memberGet(i).destGet();
				elev.Start(reql.memberGet(i));
				for(j=0;j<(int)Math.abs(dif);j++)
					if (dif<0)
						elev.Up();
					else
						elev.Down();
				System.out.println(elev);
				elev.Dock();
			}
		}
	}
*/
}

public class Dispatcher extends Controller {
	static final int FR_UP = 1;
	static final int FR_DOWN = -1;
	static final int ER = 0;
	static final int ELEV_STOP = 0;
	static final int ELEV_UP 	 = 1;
	static final int ELEV_DOWN = -1;
	private String str;
	
	public Dispatcher()
	{
		super();
		str = "";
	}
	
	boolean iscmplift(Elevator elev, Request liftr)
	{
		boolean j0=false, j1=false, j2=false ,j3=false;
		
		j0 = liftr.Valid();
		j1 = elev.floorGet()==liftr.destGet();// same  floor
		j2 = //elev.stateGet()==ELEV_STOP |
			 liftr.typeGet()==ER |
			 elev.stateGet()==ELEV_UP & liftr.typeGet()==FR_UP |
			 elev.stateGet()==ELEV_DOWN & liftr.typeGet()==FR_DOWN ; // same orientation
			 
		j3 = elev.timeGet()>=liftr.timeGet();
		return j0 & j1 & j2 & j3;
	}
	
	boolean isdiscmplift(Elevator elev, Request mainr, Request liftr)
	{
		boolean j0=false, j1=false, j2=false ,j3=false;
		
		j0 = liftr.Valid();
		j1 = liftr.typeGet()==ER;
		j2 = //elev.stateGet()==ELEV_STOP |
			 elev.stateGet()==ELEV_UP   & liftr.destGet()>mainr.destGet() |
			 elev.stateGet()==ELEV_DOWN & liftr.destGet()<mainr.destGet() ;
		j3 = elev.timeGet()>=liftr.timeGet();
		return j0 & j1 & j2 & j3;
	}
/*
	void ALS_Schedule()
	{
		reql = new ReqLine();
		boolean valid = reql.Read();
		if (valid)
		{
			boolean haslift = false;
			int i=0,j=0,k=0,l=0; 
			int max_cnt = reql.lengthGet(), cnt_down = reql.lengthGet(), cnt=-1, dif;
			int bkup_cnt=-1, bkupl[] = new int[reql.lengthGet()];
			
			Request r1=null, r2=null;
			elev = new Elevator();
			
			while(cnt_down!=0)
			{
				if (bkup_cnt!=-1){
					for(l=1;l<=bkup_cnt;l++){
						reql.memberGet(bkupl[l]).enable();
					}
					r1 = reql.memberGet(bkupl[0]);
					reql.memberGet(bkupl[0]).disable();
					bkup_cnt = -1;
				}
				else
					do{ 
						r1 = reql.memberGet(++cnt);
					}while(!r1.Valid());
				elev.Start(r1);
				str += r1.oriGet() + "(";
				cnt_down --;
				dif = elev.floorGet() - r1.destGet();
				j = cnt+1;
				while(j<max_cnt&&(reql.memberGet(j).typeGet()==ER||reql.memberGet(j).timeGet()>=r1.timeGet()))
					j++;
				for(i=0;i<=(int)Math.abs(dif);i++)
				{
					//lift
					k = cnt;
					if (i==(int)Math.abs(dif)){
						System.out.print(elev + " ");
						elev.Start(r1);
					}
					haslift = false;
					while(++k<j){
						r2 =  reql.memberGet(k);
						if (iscmplift(elev, r2)){
							haslift = true;
							str += r2.oriGet();
							if (r1.destGet()==elev.floorGet()){
								if (elev.stateGet()==ELEV_STOP)
									switch (r1.typeGet()){
										case FR_UP   :
										case FR_DOWN : elev.Start(r1); break;
										default 	 : elev.Start(r2);
									}
								else
									elev.Start(r1);
							}
							cnt_down --;
							reql.memberGet(k).disable();
						}
						else if (isdiscmplift(elev, r1, r2)){
							str += r2.oriGet();
							bkupl[++bkup_cnt] = k;
							reql.memberGet(k).disable();
						}
					}
					if (haslift){
						if (r1.destGet()!=elev.floorGet())
							System.out.print(elev + " ");
						elev.Dock();
					}
					//operate
					if (i<(int)Math.abs(dif)&&dif<0)
						elev.Up();
					else if (i<(int)Math.abs(dif)&&dif>0)
						elev.Down();
				}
				str += ") ";
				if (haslift==false)
					elev.Dock();
			}
		}
		System.out.println();
	}
*/
	
	void PrintLift(){
		String[] strline = str.trim().split(" ");
		int str_length = strline.length;
		System.out.println();
		for (int i=0;i<str_length;i++)
			System.out.println(strline[i]);
	}
}
