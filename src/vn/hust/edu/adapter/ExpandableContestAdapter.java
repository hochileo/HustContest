package vn.hust.edu.adapter;

import java.util.List;

import vn.hust.edu.main.R;
import vn.hust.edu.main.TestActivity;
import vn.hust.edu.model.Contest;
import vn.hust.edu.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import vn.hust.edu.view.AnimatedSubjectExpandableListView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableContestAdapter extends AnimatedExpandableListAdapter {

	private Context _context;
	private List<Contest> _listDataHeader;

	public ExpandableContestAdapter(Context context,
			List<Contest> listDataHeader) {
		this._context = context;
		this._listDataHeader = listDataHeader;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return _listDataHeader.get(groupPosition).getSubjects()
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	@Override
	public View getRealChildView(final int group, final int child,
			boolean isLastChild, View convertView, ViewGroup parent) {

		LayoutInflater layoutInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = layoutInflater.inflate(R.layout.child_layout, parent,
				false);
		final AnimatedSubjectExpandableListView ael = (AnimatedSubjectExpandableListView) convertView
				.findViewById(R.id.elSubject);
		ExpandableSubjectAdapter eca = new ExpandableSubjectAdapter(_context,
				_listDataHeader.get(group).getSubjects());
		ael.setAdapter(eca);
		// ael.setSelector(R.drawable.list_click);

		ael.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (ael.isGroupExpanded(groupPosition)) {
					ael.collapseGroupWithAnimation(groupPosition);
				} else {
					ael.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}
		});
		ael.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (_listDataHeader.get(group).getSubjects().get(groupPosition)
						.getListSubSubject().get(childPosition).isStatus()) {
					Intent intent = new Intent(_context, TestActivity.class);
					intent.putExtra("contest", _listDataHeader.get(group)
							.getId());

					intent.putExtra("subject", _listDataHeader.get(group)
							.getSubjects().get(groupPosition).getId());
					intent.putExtra("subsubject", _listDataHeader.get(group)
							.getSubjects().get(groupPosition)
							.getListSubSubject().get(childPosition)
							.getSub_sub_id());
					_context.startActivity(intent);
				} else {
					Toast.makeText(_context,
							"Đợt thi chưa được kích hoạt hoặc đã hết hạn",
							Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});

		return convertView;

	}

	@Override
	public Object getGroup(int groupPosition) {

		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		Contest contest = (Contest) getGroup(groupPosition);
		Typeface font1 = Typeface.createFromAsset(
				_context.getAssets(), "times.ttf");
		LayoutInflater infalInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = infalInflater.inflate(R.layout.contest_layout, parent,
				false);

		ImageView im_status = (ImageView) convertView
				.findViewById(R.id.im_status);
		
		TextView id_contest = (TextView) convertView
				.findViewById(R.id.tv_id_contest);
		id_contest.setTypeface(font1);
		TextView name_contest = (TextView) convertView
				.findViewById(R.id.tv_name_contest);
		name_contest.setTypeface(font1);
		TextView semeter_contest = (TextView) convertView
				.findViewById(R.id.tv_semeter_contest);
		semeter_contest.setTypeface(font1);
		

		id_contest.setText(contest.getId());
		name_contest.setText(contest.getName());
		semeter_contest.setText(contest.getSemeter());
		if (contest.isStatus()) {
			im_status
					.setImageResource(R.drawable.quickaction_slider_presence_active);
		} else {
			im_status
					.setImageResource(R.drawable.quickaction_slider_presence_busy);
		}

		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

}