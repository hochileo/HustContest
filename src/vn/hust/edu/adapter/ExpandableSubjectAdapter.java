package vn.hust.edu.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.hust.edu.control.MyAlarmService;
import vn.hust.edu.control.MyReceiver;
import vn.hust.edu.main.MainActivity;
import vn.hust.edu.main.R;
import vn.hust.edu.model.SubSubject;
import vn.hust.edu.model.Subject;
import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.SavePreferences;
import vn.hust.edu.view.AnimatedSubjectExpandableListView.AnimatedExpandableListAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableSubjectAdapter extends AnimatedExpandableListAdapter {

	private Context _context;
	private List<Subject> _listDataHeader;
	LoadPreferences lp;
	SavePreferences sp;
	int check = 100;

	public ExpandableSubjectAdapter(Context context,
			List<Subject> listDataHeader) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		lp = new LoadPreferences(_context);
		sp = new SavePreferences(_context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return _listDataHeader.get(groupPosition).getListSubSubject()
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getRealChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final int temp = groupPosition * check;
		final SubSubject subsubject = (SubSubject) getChild(groupPosition,
				childPosition);
		LayoutInflater layoutInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = layoutInflater.inflate(R.layout.subsubjectlayout, parent,
				false);
		final Button bt_register = (Button) convertView
				.findViewById(R.id.bt_register);

		if (subsubject.getSub_sub_id().toLowerCase().indexOf("test") != -1) {
			bt_register.setVisibility(View.GONE);
		} else {
			bt_register.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					if (MainActivity.db.openDataBase()) {
						ContentValues values = new ContentValues();
						values.put("id", subsubject.getSub_sub_id());
						values.put("name", subsubject.getSub_sub_name());
						values.put("start", subsubject.getTime_start());
						values.put("user", lp.loadString("login"));
						values.put("end", subsubject.getTime_end());
						Cursor c = MainActivity.db.SelectDb("mycontest",
								subsubject.getSub_sub_id(),
								lp.loadString("login"));
						if (c.moveToFirst()) {
							MainActivity.db.DeleteDb("mycontest",
									subsubject.getSub_sub_id());
							bt_register
									.setBackgroundResource(R.drawable.button_reg);
							bt_register.setText("Theo dõi đợt thi");
							SimpleDateFormat formatter = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date d = null;
							try {
								d = formatter.parse(subsubject.getTime_start());
								System.out.println("test date " + d.getMonth()
										+ " - " + (d.getYear() + 1900) + " - "
										+ d.getDate() + " - " + d.getHours()
										+ " - " + d.getMinutes() + " - "
										+ d.getSeconds());
								Calendar calendar = Calendar.getInstance();
								calendar.set(Calendar.MONTH, d.getMonth());
								calendar.set(Calendar.YEAR, d.getYear() + 1900);
								calendar.set(Calendar.DAY_OF_MONTH, d.getDate());

								calendar.set(Calendar.HOUR_OF_DAY, d.getHours());
								calendar.set(Calendar.MINUTE, d.getMinutes());
								calendar.set(Calendar.SECOND, d.getSeconds());

								Intent myIntent = new Intent(_context,
										MyReceiver.class);

								PendingIntent pendingIntent = PendingIntent
										.getBroadcast(
												_context,
												temp + childPosition,
												myIntent,
												PendingIntent.FLAG_CANCEL_CURRENT);
								AlarmManager alarmManager = (AlarmManager) _context
										.getSystemService(Context.ALARM_SERVICE);
								alarmManager.cancel(pendingIntent);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							if (MainActivity.db.InsertDb("mycontest", values) != -1) {
								Toast.makeText(_context, "Đã theo dõi",
										Toast.LENGTH_SHORT).show();
								bt_register
										.setBackgroundResource(R.drawable.button_delete);
								bt_register.setText("Xóa đợt thi");
								SimpleDateFormat formatter = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								Date d = null;
								// TODO Auto-generated catch block
								try {
									d = formatter.parse(subsubject
											.getTime_start());
									System.out.println("test date "
											+ d.getMonth() + " - "
											+ (d.getYear() + 1900) + " - "
											+ d.getDate() + " - "
											+ d.getHours() + " - "
											+ d.getMinutes() + " - "
											+ d.getSeconds());
									Calendar calendar = Calendar.getInstance();
									calendar.set(Calendar.MONTH, d.getMonth());
									calendar.set(Calendar.YEAR,
											d.getYear() + 1900);
									calendar.set(Calendar.DAY_OF_MONTH,
											d.getDate());

									calendar.set(Calendar.HOUR_OF_DAY,
											d.getHours());
									calendar.set(Calendar.MINUTE,
											d.getMinutes());
									calendar.set(Calendar.SECOND,
											d.getSeconds());

									Intent myIntent = new Intent(_context,
											MyReceiver.class);
									myIntent.putExtra("id",
											subsubject.getSub_sub_id());
									myIntent.putExtra("name",
											subsubject.getSub_sub_name());
									myIntent.putExtra("start",
											subsubject.getTime_start());
									myIntent.putExtra("end",
											subsubject.getTime_end());

									PendingIntent pendingIntent = PendingIntent
											.getBroadcast(
													_context,
													temp + childPosition,
													myIntent,
													PendingIntent.FLAG_UPDATE_CURRENT);

									AlarmManager alarmManager = (AlarmManager) _context
											.getSystemService(Context.ALARM_SERVICE);
									alarmManager.set(AlarmManager.RTC_WAKEUP,
											calendar.getTimeInMillis(),
											pendingIntent);
								} catch (ParseException e) {
									e.printStackTrace();
								}
							} else {
								Toast.makeText(_context, "Không thể đăng ký",
										Toast.LENGTH_SHORT).show();
							}
						}
						MainActivity.db.close();
					}
				}
			});
			bt_register.setFocusable(false);
		}
		ImageView status = (ImageView) convertView.findViewById(R.id.status);
		if (subsubject.isStatus()) {
			status.setImageResource(R.drawable.quickaction_slider_presence_active);
			// bt_register.setEnabled(true);
		} else {
			status.setImageResource(R.drawable.quickaction_slider_presence_busy);
			// bt_register.setEnabled(false);
		}

		if (MainActivity.db.openDataBase()) {
			Cursor c = MainActivity.db.SelectDb("mycontest",
					subsubject.getSub_sub_id(), lp.loadString("login"));
			if (c.moveToFirst()) {
				if (!subsubject.isStatus()) {
					MainActivity.db.DeleteDb("mycontest",
							subsubject.getSub_sub_id());
				}
				bt_register.setBackgroundResource(R.drawable.button_delete);
				bt_register.setText("Xóa đợt thi");
				Log.i("check yes", "check yes 2");
			} else {
				Log.i("check yes", "check yes 1");
			}
			c.close();
			MainActivity.db.close();
		}
		Typeface font1 = Typeface.createFromAsset(_context.getAssets(),
				"times.ttf");
		TextView id_subsubject = (TextView) convertView
				.findViewById(R.id.tv_subsubject_id);
		id_subsubject.setTypeface(font1);
		TextView name_subsubject = (TextView) convertView
				.findViewById(R.id.tv_subsubject_name);
		name_subsubject.setTypeface(font1);
		TextView start_subsubject = (TextView) convertView
				.findViewById(R.id.tv_subsubject_time_start);
		start_subsubject.setTypeface(font1);
		TextView end_subsubject = (TextView) convertView
				.findViewById(R.id.tv_subsubject_time_end);
		end_subsubject.setTypeface(font1);
		id_subsubject.setText(subsubject.getSub_sub_id());
		name_subsubject.setText(subsubject.getSub_sub_name());
		start_subsubject.setText(subsubject.getTime_start());
		end_subsubject.setText(subsubject.getTime_end());
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

		Subject subject = (Subject) getGroup(groupPosition);
		LayoutInflater layoutInflater = (LayoutInflater) this._context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = layoutInflater.inflate(R.layout.subject_layout, parent,
				false);
		Typeface font1 = Typeface.createFromAsset(_context.getAssets(),
				"times.ttf");
		TextView id_subject = (TextView) convertView
				.findViewById(R.id.tv_subject_id);
		id_subject.setTypeface(font1);
		TextView name_subject = (TextView) convertView
				.findViewById(R.id.tv_subject_name);
		name_subject.setTypeface(font1);
		TextView instutide_subject = (TextView) convertView
				.findViewById(R.id.tv_subject_instutide);
		instutide_subject.setTypeface(font1);
		TextView department_subject = (TextView) convertView
				.findViewById(R.id.tv_subject_department);
		department_subject.setTypeface(font1);

		id_subject.setText(subject.getId());
		name_subject.setText(subject.getName());
		instutide_subject.setText(subject.getInstutide());
		department_subject.setText(subject.getDepartment());

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
		return _listDataHeader.get(groupPosition).getListSubSubject().size();
	}

}