package mechanics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {
	private int color;
	private double x, y;
	private Node home;

	private boolean animating;
	private Paint paint;
	private Context context;

	public Ball(int color, double x, double y, Node home, Context context) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.home = home;
		this.context = context;
		paint = new Paint();
		paint.setColor(context.getResources().getColor(Util.getColorId(color, 1)));
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		home.setBall(this);
	}
	
	
	/*
	 * Sets the ball property of the node and the node property of the ball
	 */
	public void setHome(Node home){
		this.home=home;
		home.setBall(this);
	}

	public void draw(Canvas canvas, double dx, double dy, double mx, double my) {
		canvas.drawCircle((float) (dx + x * mx), (float) (dy + y * my), 15, paint);
	}

	public Node getNode() {
		return home;
	}

	/*
	 * This method sets the x and y coordinates of the ball on the specified
	 * edge at the fraction of the distance from the specified node
	 */
	public void setPosition(Edge e, Node from, double fraction) {
		// TODO add ease in the animation. Create a different function for eased
		// fraction for that
		Double x1, x2, y1, y2, dx, dy;
		Node to = e.getOtherNode(from);
		x1 = from.getx();
		x2 = to.getx();
		y1 = from.gety();
		y2 = to.gety();
		dx = x2 - x1;
		dy = y2 - y1;
		x=x1+fraction*dx;
		y=y1+fraction*dy;
	}
	

}
