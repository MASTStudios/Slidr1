package mechanics;

import com.maststudios.slidr.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Edge {
	private Node n1, n2;
	private int color;
	private Paint paint;
	private Context context;

	public Edge(Node n1, Node n2, int color,Context context) {
		this.n1 = n1;
		this.n2 = n2;
		this.color = color;
		n1.addEdge(this);
		n2.addEdge(this);
		paint=new Paint();
		this.context=context;
		paint.setColor(context.getResources().getColor(Util.getColorId(color, 3)));
		paint.setStrokeWidth(context.getResources().getDimension(R.dimen.edgewidth));
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	public void draw(Canvas canvas, double dx, double dy, double mx, double my) {
		canvas.drawLine((float) (dx + n1.getx() * mx), (float) (dy + n1.gety() * my), (float) (dx + n2.getx() * mx), (float) (dy + n2.gety() * my), paint);
	}
	
	public Node getOtherNode(Node n){
		if(n==n1){
			return n2;
		}else if(n==n2){
			return n1;
		}else{
			return null;
		}
	}
	
	public int getColor(){
		return color;
	}
}
