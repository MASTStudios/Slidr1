package mechanics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.maststudios.slidr.R;

import android.R.fraction;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;

public class Board extends SurfaceView implements OnTouchListener, SurfaceHolder.Callback {

	private List<Ball> balls;
	private List<Node> nodes;
	private List<Edge> edges;

	private String name;

	private Context context;
	private double dx, dy, mx, my;
	private double diffx, diffy, minx, miny;
	private int height, width;

	private boolean interaction; // true if the user can interact with the game

	// animation parameters
	private double animation_fraction;
	private Node animation_n1, animation_n2;

	private GestureDetectorCompat mDetector;

	public Board(Context context, String levelName) {
		super(context);
		getHolder().addCallback(this);
		balls = new ArrayList<Ball>();
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		this.context = context;
		mDetector = new GestureDetectorCompat(context, new GestureListener(this));
		this.setOnTouchListener(this);

		double maxx = Double.MIN_VALUE, maxy = Double.MIN_VALUE, minx = Double.MAX_VALUE, miny = Double.MAX_VALUE;

		// creating level from file
		BufferedReader br;
		String s, arr[];
		try {
			br = new BufferedReader(new InputStreamReader(context.getAssets().open(levelName)));
			while ((s = br.readLine()) != null) {
				// ignoring comments
				// comments are ignored if the first character of the line is #
				if (s.charAt(0) == '#') {
					continue;
				}
				arr = s.split("\\s+");
				if (arr[0].compareToIgnoreCase("name") == 0) {
					name = s.substring(5);
				} else if (arr[0].compareToIgnoreCase("n") == 0) {// for node
																	// and balls
					Node n = new Node(Integer.parseInt(arr[3]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]), context);
					nodes.add(n);
					balls.add(new Ball(Integer.parseInt(arr[3]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]), n, context));

					if (minx > Double.parseDouble(arr[1])) {
						minx = Double.parseDouble(arr[1]);
					}
					if (maxx < Double.parseDouble(arr[1])) {
						maxx = Double.parseDouble(arr[1]);
					}
					if (miny > Double.parseDouble(arr[2])) {
						miny = Double.parseDouble(arr[2]);
					}
					if (maxy < Double.parseDouble(arr[2])) {
						maxy = Double.parseDouble(arr[2]);
					}

				} else if (arr[0].compareToIgnoreCase("e") == 0) {// for edges
					edges.add(new Edge(nodes.get(Integer.parseInt(arr[1])), nodes.get(Integer.parseInt(arr[2])), Integer.parseInt(arr[3]), context));
				}
			}
			diffx = maxx - minx;
			diffy = maxy - miny;
			this.minx = minx;
			this.miny = miny;

		} catch (IOException e) {
			// System.out.println("Did not find the leve in the assets");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Error in the level defined in asets");
			// TODO write code to tell the line number
		}
	}

	/*
	 * Function to draw everything. Avoid adding the code to draw an object
	 * here, create a draw method (described later) in the object and call it
	 * from here instead.
	 * 
	 * -Draw method inside the objects public void draw(Canvas canvas, double
	 * dx, double dy, double mx, double my)
	 */
	public void draw() {
		SurfaceHolder holder = getHolder();
		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(context.getResources().getColor(R.color.background));

		for (Edge e : edges) {
			e.draw(canvas, dx, dy, mx, my);
		}
		for (Node n : nodes) {
			n.draw(canvas, dx, dy, mx, my);
		}
		for (Ball b : balls) {
			b.draw(canvas, dx, dy, mx, my);
		}
		holder.unlockCanvasAndPost(canvas);
	}

	/*
	 * Called when there is a scroll action on the SurfaceView
	 */
	public void onScroll(MotionEvent E1, MotionEvent E2) {
		// find the node that is being touched
		/*
		 * TODO add a condition for the maximum distance x so that if the user
		 * scrolls at a distance greater than x from the nearest node then there
		 * is no scrolling
		 */

		float x1, x2, y1, y2;
		x1 = (float) ((E1.getX() - dx) / mx);
		y1 = (float) ((E1.getY() - dy) / my);
		x2 = (float) ((E2.getX() - dx) / mx);
		y2 = (float) ((E2.getY() - dy) / my);
		double distanceX = x2 - x1;
		double distanceY = y2 - y1;

		double threshold = 500; // TODO set threshold from the dimensions

		double distance = Double.MAX_VALUE;
		Node n1 = null, n2 = null;
		for (Node node : nodes) {
			if (node.distance(x1, y1) < distance) {
				n1 = node;
				distance = node.distance(x1, y1);
			}
		}

		// TODO handle infinite slope
		double slope = distanceY / distanceX;
		double angle = Math.atan(slope), minangle = 2 * Math.PI;
		if (distanceX < 0 && distanceY > 0) {// second quadrant
			angle = Math.PI + angle;
		} else if (distanceX < 0 && distanceY < 0) {// third quadrant
			angle = Math.PI + angle;
		} else if (distanceX > 0 && distanceY < 0) {// fourth quadrant
			angle = 2 * Math.PI + angle;
		}

		for (Node node : nodes) {
			if (n1 != node && Math.abs(n1.angle((float) node.getx(), (float) node.gety()) - angle) < minangle) {
				n2 = node;
				minangle = Math.abs(n1.angle((float) node.getx(), (float) node.gety()) - angle);
			}
		}
		/*
		 * The fraction of animation should be a function of the distance and
		 * the threshold which is an asymptote to fraction = 1
		 */
		double scrollDistance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
		double ratio = scrollDistance / threshold; // the ratio of
													// scrollDistance and
													// threshold
		double pow = Math.pow(2, ratio);
		double fraction = (pow - 1) / pow;
		setBallPosition(n1, n2, fraction);
	}

	/*
	 * Sets the positions of the loop in a transition phase so that the ball
	 * from n1 is going to n2. The fraction specifies the amount of transition
	 * completed.
	 */
	private void setBallPosition(Node n1, Node n2, double fraction) {
		// TODO add support for entangled loops
		// TODO add support for delayed motion in farther balls
		// TODO change the parent of the ball if the fraction reaches 1
		List<Ball> balls = new ArrayList<Ball>();
		List<Node> nodes = new ArrayList<Node>();
		Edge e;
		Node n;
		nodes.add(n1);
		nodes.add(n2);
		balls.add(n1.getBall());
		balls.add(n2.getBall());
		if (n1.checkedge(n2)) {
			e = n1.getEdge(n2);
			while (true) {
				n = nodes.get(nodes.size() - 2).getNode(nodes.get(nodes.size() - 1));
				if (n == n1) {
					break;
				}
				nodes.add(n);
				balls.add(n.getBall());
			}

			// TODO position of balls according to the fraction

			Node from, to;

			int i = 0;
			for (Ball ball : balls) {
				from = ball.getNode();
				if (balls.indexOf(ball) < balls.size() - 1) {
					to = nodes.get(balls.indexOf(ball) + 1);
				} else {
					to = nodes.get(0);
				}
				e = from.getEdge(to);
				ball.setPosition(e, from, fraction);
				i++;
			}
		}
	}

	public void setBallsToBase() {

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		double swidth = width - 2 * context.getResources().getDimension(R.dimen.hpadding);
		double sheight = height - context.getResources().getDimension(R.dimen.tbar) - 2 * context.getResources().getDimension(R.dimen.hpadding);
		mx = swidth / diffx;
		my = sheight / diffy;
		dx = context.getResources().getDimension(R.dimen.hpadding) - minx * mx;
		dy = context.getResources().getDimension(R.dimen.tbar) + context.getResources().getDimension(R.dimen.hpadding) - miny * my;
		setMeasuredDimension(this.width, this.height);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		new updater(this).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return true;
	}

	public void setInteraction(boolean interaction) {
		this.interaction = interaction;
	}

	public boolean isInteractive() {
		return interaction;
	}

}

class updater extends Thread {

	private Board callback;

	public updater(Board callback) {
		this.callback = callback;
	}

	@Override
	public void run() {
		super.run();
		while (true) {
			callback.draw();
			try {
				Thread.sleep(25);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class SetBallsToBase extends Thread {

	private Board callback;

	public SetBallsToBase(Board callback) {
		this.callback = callback;
	}

	public void run() {
		callback.setInteraction(false);
		Tween tween = new Tween(1000, Tween.EASE_SWIFT);
		while (tween.getFraction() < 1) {

		}
		callback.setInteraction(true);
	}

}

class GestureListener extends GestureDetector.SimpleOnGestureListener {

	private Board callback;

	// for scroll customization
	private boolean newScroll;

	public GestureListener(Board callback) {
		this.callback = callback;
		newScroll = true;
	}

	@Override
	public boolean onDown(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		callback.onScroll(e1, e2);
		return true;
	}
}
