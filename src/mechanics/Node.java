package mechanics;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Node {
	private int color;
	private double x, y;
	private Ball ball;
	private Set<Edge> edges;
	private Paint paint;
	Context context;

	public Node(int color, double x, double y, Context context) {
		super();
		this.color = color;
		this.x = x;
		this.y = y;
		this.context = context;
		paint = new Paint();
		paint.setColor(context.getResources().getColor(Util.getColorId(color, 2)));
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		edges = new HashSet<Edge>();
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	// returns true if the node in the argument is directly connected to this
	// node else returns false
	public boolean checkedge(Node n) {
		for (Edge e : edges) {
			if (e.getOtherNode(this) == n) {
				return true;
			}
		}
		return false;
	}

	public Edge getEdge(Node n) {
		for (Edge e : edges) {
			// System.out.println("other node with edge of color "+e.getColor()+" "+e.getOtherNode(this));
			if (e.getOtherNode(this) == n) {
				return e;
			}
		}
		return null;
	}

	public Edge getEdge(Edge e) {
		for (Edge ei : edges) {
			if (ei.getColor() == e.getColor() && e != ei) {
				return ei;
			}
		}
		return null;
	}

	public Node getNode(Node n) {
		Edge e = getEdge(n);
		Edge e1 = n.getEdge(e);
		return e1.getOtherNode(n);
	}

	public void addEdge(Edge e) {
		edges.add(e);
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public void draw(Canvas canvas, double dx, double dy, double mx, double my) {
		canvas.drawCircle((float) (dx + x * mx), (float) (dy + y * my), 30, paint);
	}

	public Ball getBall() {
		return ball;
	}

	public double distance(float x1, float y1) {
		return Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
	}

	public int getColor() {
		return color;
	}

	// returns the angle that the line joining the node and the given point is
	// making
	public double angle(float x1, float y1) {
		// System.out.println("ANGLE object = "+this );
		double dx, dy;
		dx = x1 - x;
		dy = y1 - y;
		// System.out.println("ANGLE dx = "+dx );
		// System.out.println("ANGLE dy = "+dy );
		double slope, angle;
		slope = dy / dx;
		// System.out.println("ANGLE slope = "+slope );
		angle = Math.atan(slope);
		if (dx < 0 && dy > 0) {// second quadrant
			angle = angle + Math.PI;
		} else if (dx < 0 && dy < 0) {// third quadrant
			angle = angle + Math.PI;
		} else if (dx > 0 && dy < 0) {
			angle = angle + 2 * Math.PI;
		}
		return angle;
	}

	@Override
	public String toString() {
		return "("+x+"|"+y+")"+" "+color;
	}
}
