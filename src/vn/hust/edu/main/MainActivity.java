package vn.hust.edu.main;

import vn.edu.hust.materiallib.ResideMenu;
import vn.edu.hust.materiallib.ResideMenuItem;
import vn.hust.edu.control.DataBaseHelper;
import vn.hust.edu.menufloatbuttonlib.FloatingActionButton;
import vn.hust.edu.menufloatbuttonlib.FloatingActionsMenu;
import vn.hust.edu.variable.Variable;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private FloatingActionsMenu menu;
	private FloatingActionButton settings;
	private FloatingActionButton profile;
	private FloatingActionButton history;
	private FloatingActionButton contest;

	private ResideMenu resideMenu;
	private ResideMenuItem itemProfile;
	private ResideMenuItem itemHistory;
	private ResideMenuItem itemContest;
	private ResideMenuItem itemSettings;
	public static DataBaseHelper db;
	private boolean isExit = false;
	private int check = 0;

	// private int id_click_privious;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DataBaseHelper(getApplicationContext());
		try {
			db.createDataBase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		menu = (FloatingActionsMenu) findViewById(R.id.action_menu);
		settings = (FloatingActionButton) findViewById(R.id.action_settings);
		profile = (FloatingActionButton) findViewById(R.id.action_profile);
		history = (FloatingActionButton) findViewById(R.id.action_history);
		contest = (FloatingActionButton) findViewById(R.id.action_contest);
		setupMenu();
		settings.setOnClickListener(this);
		profile.setOnClickListener(this);
		history.setOnClickListener(this);
		contest.setOnClickListener(this);
		if (Variable.isVisitor) {
			menu.setVisibility(View.GONE);
			// resideMenu.set
			changeFragment(new ContestFragment());
		} else {
			changeFragment(new ProfileFragment());
		}
		// id_click_privious = R.id.action_profile;
	}

	private void changeFragment(Fragment targetFragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		check++;
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				check=0;
			}
		}, 3000);
		
		if (check == 2) {
			isExit = true;
		}
		if (isExit) {
			super.onBackPressed();
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "Hãy bấm nút quay lại 1 lần nữa để thoát!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View view) {
		menu.collapse();
		resideMenu.closeMenu();
		int id = view.getId();
		// if (id != id_click_privious) {
		if ((id == R.id.action_profile)) {
			changeFragment(new ProfileFragment());
		} else if ((id == R.id.action_history)) {
			changeFragment(new HistoryFragment());
		} else if ((id == R.id.action_contest)) {
			changeFragment(new ContestFragment());
		} else if ((id == R.id.action_settings)) {
			changeFragment(new SettingsFragment());
		}
		// id_click_privious = id;

		// }

	}

	private void setupMenu() {
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.bg_4);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		resideMenu.setScaleValue(0.6f);

		if (!Variable.isVisitor) {
			itemProfile = new ResideMenuItem(this, R.drawable.menu_p, "Cá nhân");
			itemHistory = new ResideMenuItem(this, R.drawable.menu_h, "Lịch sử");
			itemContest = new ResideMenuItem(this, R.drawable.menu_t,
					"Cuộc thi");
			itemSettings = new ResideMenuItem(this, R.drawable.menu_s,
					"Cài đặt");

			itemProfile.setOnClickListener(this);
			itemHistory.setOnClickListener(this);
			itemContest.setOnClickListener(this);
			itemSettings.setOnClickListener(this);
			itemProfile.setId(R.id.action_profile);
			itemHistory.setId(R.id.action_history);
			itemContest.setId(R.id.action_contest);
			itemSettings.setId(R.id.action_settings);
			resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
			resideMenu.addMenuItem(itemHistory, ResideMenu.DIRECTION_LEFT);
			resideMenu.addMenuItem(itemContest, ResideMenu.DIRECTION_RIGHT);
			resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

		}

		findViewById(R.id.title_left).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});
		findViewById(R.id.title_right).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
					}
				});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {

		}

		@Override
		public void closeMenu() {

		}
	};

	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	@Override
	protected void onActivityResult(int request, int result, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(request, result, arg2);
		System.out.println("check result " + result);
		if (result == 2) {
			Intent intent = new Intent(MainActivity.this, SignInActivity.class);
			startActivity(intent);
			finish();
		}

		if (result == 3) {
			changeFragment(new ProfileFragment());
			// id_click_privious = R.id.action_profile;
		}
	}

}
