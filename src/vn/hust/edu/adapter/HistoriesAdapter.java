package vn.hust.edu.adapter;

import java.util.List;

import vn.hust.edu.main.R;
import vn.hust.edu.model.InfoHistories;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoriesAdapter extends ArrayAdapter<InfoHistories> {
	private Context mContext;
	public List<InfoHistories> histories;

	public HistoriesAdapter(Context mContext, int size,
			List<InfoHistories> histories) {
		super(mContext, size, histories);
		this.mContext = mContext;
		this.histories = histories;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		return position % 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_histories, null);
		Typeface font1 = Typeface.createFromAsset(
				mContext.getAssets(), "times.ttf");
		TextView subject = (TextView) convertView.findViewById(R.id.subject);
		subject.setTypeface(font1);
		TextView subsubject = (TextView) convertView
				.findViewById(R.id.ssubject);
		subsubject.setTypeface(font1);
		TextView exam = (TextView) convertView.findViewById(R.id.exam);
		exam.setTypeface(font1);
		TextView score = (TextView) convertView.findViewById(R.id.score);
		score.setTypeface(font1);
		TextView id = (TextView) convertView.findViewById(R.id.id_sub);
		id.setTypeface(font1);

		subject.setText(histories.get(position).getSubject());
		subsubject.setText(histories.get(position).getSub_subject());
		exam.setText(histories.get(position).getExam());
		score.setText(histories.get(position).getScore()+ " Điểm - "+histories.get(position).getScore_text());
		id.setText(histories.get(position).getSubject_id());
		return convertView;
	}

	public void update() {
		notifyDataSetChanged();
	}

}
