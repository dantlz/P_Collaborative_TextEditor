package tianlinz_CS201L_assignment5;

public class MyServerTimer extends Thread{

	private MyServer server;
	private int interval;
	
	public MyServerTimer(MyServer server, int miliSecondInterval){
		this.server = server;
		this.interval = miliSecondInterval;
	}
	
	public void run(){
		while(true){
			try {
				server.initiateClientUpdates();
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
