package vn.hust.edu.main;

import vn.edu.hust.materiallib.ButtonFloatSmall;
import vn.edu.hust.materiallib.LayoutRipple;
import vn.edu.hust.materiallib.ToolTip;
import vn.edu.hust.materiallib.ToolTipRelativeLayout;
import vn.edu.hust.materiallib.ToolTipView;
import vn.hust.edu.variable.LoadPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements View.OnClickListener,
		ToolTipView.OnToolTipViewClickedListener {

	int backgroundColor = Color.parseColor("#1E88E5");
	ButtonFloatSmall buttonSelectColor;
	private TextView tvLogin;
	private TextView tvOffice;
	private TextView tvVip;
	private TextView tvName;
	private TextView tvMail;
	private TextView tvPhone;
	private TextView tvAdd;
	private TextView tvDate;
	private TextView tvClass;
	private TextView tvInstutide;
	private TextView tvDepartment;
	private View convertView;
	private LinearLayout main_progress;
	private ToolTipView mOrangeToolTipView;
	private ToolTipRelativeLayout mToolTipFrameLayout;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.activity_profile, null);
		setupView();
		setupData();
		mToolTipFrameLayout = (ToolTipRelativeLayout) convertView
				.findViewById(R.id.activity_main_tooltipframelayout);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					addOrangeToolTipView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 500);
		return convertView;
	}

	private void addOrangeToolTipView() {
		ToolTip toolTip = new ToolTip().withText("Chỉnh sửa")
				.withTextColor(getResources().getColor(R.color.white))
				.withColor(getResources().getColor(R.color.orange))
				.withAnimationType(ToolTip.AnimationType.FROM_TOP);

		mOrangeToolTipView = mToolTipFrameLayout.showToolTipForView(toolTip,
				convertView.findViewById(R.id.buttonColorSelector));
		mOrangeToolTipView.setOnToolTipViewClickedListener(this);
	}

	private void setOriginRiple(final LayoutRipple layoutRipple) {
		layoutRipple.post(new Runnable() {

			@Override
			public void run() {
				@SuppressWarnings("unused")
				View v = layoutRipple.getChildAt(0);
			}
		});

	}

	public void setupView() {
		main_progress = (LinearLayout) convertView
				.findViewById(R.id.main_progress);
		tvLogin = (TextView) convertView.findViewById(R.id.tvLogin);
		tvOffice = (TextView) convertView.findViewById(R.id.tvOffice);
		tvVip = (TextView) convertView.findViewById(R.id.tvVip);
		tvName = (TextView) convertView.findViewById(R.id.tvName);
		tvMail = (TextView) convertView.findViewById(R.id.tvMail);
		tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
		tvAdd = (TextView) convertView.findViewById(R.id.tvAdd);
		tvDate = (TextView) convertView.findViewById(R.id.tvDate);
		tvClass = (TextView) convertView.findViewById(R.id.tvClass);
		tvInstutide = (TextView) convertView.findViewById(R.id.tvInstutide);
		tvDepartment = (TextView) convertView.findViewById(R.id.tvDepartment);

		buttonSelectColor = (ButtonFloatSmall) convertView
				.findViewById(R.id.buttonColorSelector);
		buttonSelectColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getActivity(), "check1", Toast.LENGTH_SHORT)
						.show();
				if (mOrangeToolTipView != null) {
					mOrangeToolTipView.remove();
					mOrangeToolTipView = null;
				}
				Intent intent = new Intent(getActivity(),
						RegisterActivity.class);
				intent.putExtra("new", false);
				getActivity().startActivityForResult(intent, 1);
			}
		});

		LayoutRipple layoutRipple = (LayoutRipple) convertView
				.findViewById(R.id.itemName);

		setOriginRiple(layoutRipple);

		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		layoutRipple = (LayoutRipple) convertView.findViewById(R.id.itemMail);

		setOriginRiple(layoutRipple);

		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		layoutRipple = (LayoutRipple) convertView.findViewById(R.id.itemClass);

		setOriginRiple(layoutRipple);

		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		layoutRipple = (LayoutRipple) convertView
				.findViewById(R.id.itemInstutide);

		setOriginRiple(layoutRipple);

		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		layoutRipple = (LayoutRipple) convertView
				.findViewById(R.id.itemDepartment);

		setOriginRiple(layoutRipple);

		layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

	}

	public void setupData() {
		LoadPreferences lp = new LoadPreferences(getActivity());
		tvLogin.setText(lp.loadString("login"));
		tvOffice.setText(lp.loadString("office"));
		tvName.setText(lp.loadString("name"));
		tvMail.setText(lp.loadString("mail"));
		tvPhone.setText(lp.loadString("phone"));
		tvAdd.setText(lp.loadString("address"));
		tvDate.setText(lp.loadString("date"));
		tvInstutide.setText(lp.loadString("instutide"));
		tvDepartment.setText(lp.loadString("department"));
		tvClass.setText(lp.loadString("class"));
		if (Boolean.parseBoolean(lp.loadString("isVip"))) {
			tvVip.setText("VIP");
		} else {
			tvVip.setText("NP");
		}
		main_progress.setVisibility(View.GONE);
	}

	@SuppressWarnings("unused")
	private void setupMenu() {

	}

	@Override
	public void onToolTipViewClicked(ToolTipView toolTipView) {

	}

	@Override
	public void onClick(View v) {

	}

	// @Override
	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	// System.out.println("result " + requestCode);
	// if (resultCode == 2) {
	// Intent intent = new Intent(getActivity(), SignInActivity.class);
	// startActivityForResult(intent, 1);
	// }
	// if (resultCode == 3) {
	// getActivity().finish();
	// System.exit(0);
	// }
	// }

}
