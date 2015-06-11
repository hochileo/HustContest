package vn.hust.edu.control;

import vn.hust.edu.main.MainActivity;
import vn.hust.edu.main.R;
import vn.hust.edu.main.SignInActivity;
import vn.hust.edu.main.TestActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class MyAlarmService extends Service

{
	private NotificationManager mManager;
	String id = "mã đợt thi: ";
	String name = "tên đợt thi: ";
	String start = "bắt đầu: ";
	String end = "kết thúc: ";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Bundle b = intent.getExtras();
		id = "Mã ĐT: "+ b.getString("id");
		name ="Tên ĐT: " + b.getString("name");
		start ="Bắt đầu lúc: "+ b.getString("start");
		end ="Kết thúc lúc: "+ b.getString("end");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(),
				SignInActivity.class);

		Notification notification = new Notification(R.drawable.ic_launcher, id
				+ " - " + name + "\n"+start +"\n"+end, System.currentTimeMillis());

		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				"Hust Contest", id + " - " + name  + "\n"+start +"\n"+end,
				pendingNotificationIntent);

		mManager.notify(0, notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}