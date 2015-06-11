package vn.hust.edu.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

public class MyReceiver extends BroadcastReceiver
{
	 
	@Override
	 public void onReceive(Context context, Intent intent)
	{
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(2000);
		Bundle b = intent.getExtras();
	   Intent service1 = new Intent(context, MyAlarmService.class);
	   service1.putExtra("id", b.getString("id"));
	   service1.putExtra("name", b.getString("name"));
	   service1.putExtra("start", b.getString("start"));
	   service1.putExtra("end", b.getString("end"));
	   context.startService(service1);
	   
	 }
	
}
