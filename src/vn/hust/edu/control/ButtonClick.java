package vn.hust.edu.control;

import vn.hust.edu.buttonchooselib.CircleButton;
import vn.hust.edu.main.R;
import vn.hust.edu.variable.Variable;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

@SuppressLint("ResourceAsColor")
public class ButtonClick implements OnClickListener {
	private View view;
	private int id;

	public ButtonClick(View view, int id) {
		this.view = view;
		this.id = id;
	}

	@Override
	public void onClick(View v) {
		CircleButton choose_a = (CircleButton) view.findViewById(R.id.choose_a);
		CircleButton choose_b = (CircleButton) view.findViewById(R.id.choose_b);
		CircleButton choose_c = (CircleButton) view.findViewById(R.id.choose_c);
		CircleButton choose_d = (CircleButton) view.findViewById(R.id.choose_d);

		switch (v.getId()) {
		case R.id.choose_a:
			Variable.choose_ans.put(id, "A");
			choose_a.setImageResource(R.drawable.ic_black);
			choose_b.setImageResource(R.drawable.b);
			choose_c.setImageResource(R.drawable.c);
			choose_d.setImageResource(R.drawable.d);
			break;
		case R.id.choose_b:
			Variable.choose_ans.put(id, "B");
			choose_a.setImageResource(R.drawable.a);
			choose_b.setImageResource(R.drawable.ic_black);
			choose_c.setImageResource(R.drawable.c);
			choose_d.setImageResource(R.drawable.d);
			break;
		case R.id.choose_c:
			Variable.choose_ans.put(id, "C");
			choose_a.setImageResource(R.drawable.a);
			choose_b.setImageResource(R.drawable.b);
			choose_c.setImageResource(R.drawable.ic_black);
			choose_d.setImageResource(R.drawable.d);
			break;
		case R.id.choose_d:
			Variable.choose_ans.put(id, "D");
			choose_a.setImageResource(R.drawable.a);
			choose_b.setImageResource(R.drawable.b);
			choose_c.setImageResource(R.drawable.c);
			choose_d.setImageResource(R.drawable.ic_black);
			break;

		default:
			break;
		}

	}

}
