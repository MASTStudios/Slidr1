package mechanics;

import com.maststudios.slidr.R;

import android.content.Context;
import android.graphics.Paint;

public class Util {

	public Context context;

	public static int getColorId(int color, int darkness) {
			switch (color+darkness*100) {
			case 101:
				return R.color.A1;
			case 102:
				return R.color.A2;
			case 103:
				return R.color.A3;
			case 104:
				return R.color.A4;
			case 105:
				return R.color.A5;
			case 201:
				return R.color.B1;
			case 202:
				return R.color.B2;
			case 203:
				return R.color.B3;
			case 204:
				return R.color.B4;
			case 205:
				return R.color.B5;
			case 301:
				return R.color.C1;
			case 302:
				return R.color.C2;
			case 303:
				return R.color.C3;
			case 304:
				return R.color.C4;
			case 305:
				return R.color.C5;
			}
		return 0;
	}
}
