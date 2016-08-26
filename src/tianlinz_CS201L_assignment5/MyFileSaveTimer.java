package tianlinz_CS201L_assignment5;

public class MyFileSaveTimer extends Thread{

	private MyTab tab;
	private int interval;
	private boolean run;
	
	public MyFileSaveTimer(MyTab tab, int miliSecondInterval){
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
				tab.autoSave();
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
