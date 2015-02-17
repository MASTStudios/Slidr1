package mechanics;

public class Tween {
	
	public static final int EASE_SWIFT=1;
	
	private long startTime,duration;
	private int type;
	
	public Tween(long duration,int type){
		startTime=System.currentTimeMillis();
		this.duration=duration;
		this.type=type;
	}
	
	public double getFraction(){
		long curTime=System.currentTimeMillis();
		double timeFraction=(curTime-startTime)/duration;
		if(timeFraction>1){
			return 1;
		}
		switch(type){
		default:
			return timeFraction;
		}
	}
}
