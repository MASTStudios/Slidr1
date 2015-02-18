package mechanics;

public class Tween {

	public static final int EASE_SWIFT = 1;

	private long startTime, duration;
	private int type;

	public Tween(long duration, int type, double fraction) {
		startTime = System.currentTimeMillis();
		this.duration = duration;
		this.type = type;
		if (fraction > 1) {
			startTime = System.currentTimeMillis() - duration;
		} else {
			startTime = (long) (System.currentTimeMillis() - duration * fraction);
		}
	}

	public double getFraction() {
		long curTime = System.currentTimeMillis();
		double timeFraction = (double) (curTime - startTime) / (double) duration;
		if (timeFraction > 1) {
			return 1;
		}
		switch (type) {
		default:
			return timeFraction;
		}
	}

	public boolean alive() {
		long curTime = System.currentTimeMillis();
		if (curTime - startTime > duration) {
			return false;
		} else {
			return true;
		}
	}
}
