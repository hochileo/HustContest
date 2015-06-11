package vn.hust.edu.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.hust.edu.mycontestlib.WeekView;
import vn.hust.edu.mycontestlib.WeekViewEvent;
import vn.hust.edu.variable.LoadPreferences;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014. Website:
 * http://april-shower.com
 */
public class MyContestActivity extends Activity implements
		WeekView.MonthChangeListener, WeekView.EventClickListener,
		WeekView.EventLongPressListener {
	private WeekView mWeekView;
	private LoadPreferences lp;
	private List<MyContest> list;
	private int checkid = 1;
	private Button today;
	private Button view3day;
	private static final int TYPE_DAY_VIEW = 1;
	private static final int TYPE_THREE_DAY_VIEW = 2;
	private int mWeekViewType = TYPE_THREE_DAY_VIEW;
	private int[] color = { R.color.event_color_01, R.color.event_color_02,
			R.color.event_color_03, R.color.event_color_04 };
	private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_contest);
		lp = new LoadPreferences(getApplicationContext());
		list = new ArrayList<MyContest>();
		if (MainActivity.db.openDataBase()) {
			Cursor c = MainActivity.db.SelectDb("mycontest", lp.loadString("login"));
			c.moveToFirst();
			while (c.isAfterLast() == false) {
				MyContest mc = new MyContest();
				mc.setId(c.getString(0));
				mc.setName(c.getString(1));
				mc.setStart(c.getString(2));
				mc.setEnd(c.getString(3));
				list.add(mc);
				c.moveToNext();
			}
			c.close();
			MainActivity.db.close();
		}
		SetEvents();

		// Get a reference for the week view in the layout.
		mWeekView = (WeekView) findViewById(R.id.weekView);
		today = (Button) findViewById(R.id.today);
		view3day = (Button) findViewById(R.id.view3day);
		today.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWeekView.goToToday();
			}
		});
		view3day.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
					view3day.setText("Xem chi tiết");
					mWeekViewType = TYPE_THREE_DAY_VIEW;
					mWeekView.setNumberOfVisibleDays(3);

					// Lets change some dimensions to best fit the view.
					mWeekView.setColumnGap((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
									.getDisplayMetrics()));
					mWeekView.setTextSize((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_SP, 12, getResources()
									.getDisplayMetrics()));
					mWeekView.setEventTextSize((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_SP, 12, getResources()
									.getDisplayMetrics()));
				} else {
					view3day.setText("Xem 3 ngày");
					mWeekViewType = TYPE_DAY_VIEW;
					mWeekView.setNumberOfVisibleDays(1);
					// Lets change some dimensions to best fit the view.
					mWeekView.setColumnGap((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
									.getDisplayMetrics()));
					mWeekView.setTextSize((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_SP, 12, getResources()
									.getDisplayMetrics()));
					mWeekView.setEventTextSize((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_SP, 12, getResources()
									.getDisplayMetrics()));
				}

			}
		});
		// Show a toast message about the touched event.
		mWeekView.setOnEventClickListener(this);

		// The week view has infinite scrolling horizontally. We have to provide
		// the events of a
		// month every time the month changes on the week view.
		mWeekView.setMonthChangeListener(this);

		// Set long press listener for events.
		mWeekView.setEventLongPressListener(this);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

		return events;
	}

	public void SetEvents(){
		Calendar startTime;
		Calendar endTime;
		WeekViewEvent event;
		for (MyContest myContest : list) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				Date date = format.parse(myContest.getStart());
				int monthstart = Integer.parseInt((String) DateFormat.format(
						"MM", date)); // 06
				int yearstart = Integer.parseInt((String) DateFormat.format(
						"yyyy", date)); // 2013
				int daystart = Integer.parseInt((String) DateFormat.format(
						"dd", date)); // 20

				int hourstart = date.getHours();
				int minusstart = Integer.parseInt((String) DateFormat.format(
						"mm", date)); // 10
				System.out.println("date " + daystart + " - " + monthstart
						+ " - " + yearstart + " - " + hourstart + " - "
						+ minusstart);
				Date date1 = format.parse(myContest.getEnd());
				int monthend = Integer.parseInt((String) DateFormat.format(
						"MM", date1)); // 06
				int yearend = Integer.parseInt((String) DateFormat.format(
						"yyyy", date1)); // 2013
				int dayend = Integer.parseInt((String) DateFormat.format("dd",
						date1)); // 20
				int hourend = date1.getHours(); // 9
				int minusend = Integer.parseInt((String) DateFormat.format(
						"mm", date1)); // 10

				startTime = Calendar.getInstance();
				startTime.set(Calendar.HOUR_OF_DAY, hourstart);
				startTime.set(Calendar.MINUTE, minusstart);
				startTime.set(Calendar.MONTH, monthstart - 1);
				startTime.set(Calendar.YEAR, yearstart);
				startTime.set(Calendar.DAY_OF_MONTH, daystart);

				endTime = (Calendar) startTime.clone();
				endTime.set(Calendar.HOUR_OF_DAY, hourend);
				endTime.set(Calendar.MINUTE, minusend);
				endTime.set(Calendar.MONTH, monthend - 1);
				endTime.set(Calendar.YEAR, yearend);
				endTime.set(Calendar.DAY_OF_MONTH, dayend);
				event = new WeekViewEvent(checkid, getEventTitle(startTime,
						myContest.getName(), endTime), startTime, endTime);
				event.setColor(getResources().getColor(
						color[(checkid % color.length) - 1]));
				checkid++;
				events.add(event);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getEventTitle(Calendar start, String title, Calendar end) {
		return String
				.format("Môn thi: "
						+ title
						+ "\nBắt đầu lúc %02d:%02d ngày %d/%s/%d \nKết thúc lúc %02d:%02d ngày %d/%s/%d",
						start.get(Calendar.HOUR_OF_DAY),
						start.get(Calendar.MINUTE),
						start.get(Calendar.DAY_OF_MONTH),
						start.get(Calendar.MONTH) + 1,
						start.get(Calendar.YEAR),
						end.get(Calendar.HOUR_OF_DAY),
						end.get(Calendar.MINUTE),
						end.get(Calendar.DAY_OF_MONTH),
						end.get(Calendar.MONTH) + 1, end.get(Calendar.YEAR));
	}

	@Override
	public void onEventClick(WeekViewEvent event, RectF eventRect) {
		Toast.makeText(MyContestActivity.this, "Clicked " + event.getName(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
		Toast.makeText(MyContestActivity.this,
				"Long pressed event: " + event.getName(), Toast.LENGTH_SHORT)
				.show();
	}

	private class MyContest {
		private String id;
		private String name;
		private String start;
		private String end;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

	}
}
