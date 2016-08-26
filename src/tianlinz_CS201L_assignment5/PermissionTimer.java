package tianlinz_CS201L_assignment5;

public class PermissionTimer extends Thread{
	
	private MyTab tab;
	private int interval;
	private boolean run;
	
	public PermissionTimer(MyTab tab, int miliSecondInterval){
		this.tab = tab;
		this.interval = miliSecondInterval;
		run = true;
	}
	
	public void terminate(){
		run = false;
	}
	
	
	public void run(){
		while(run){
			try {
				tab.checkPermission();
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
