package vn.hust.edu.main;

import vn.edu.hust.materiallib.ButtonFloatSmall;
import vn.edu.hust.materiallib.ToolTip;
import vn.edu.hust.materiallib.ToolTipRelativeLayout;
import vn.edu.hust.materiallib.ToolTipView;
import vn.hust.edu.adapter.PictureAdapter;
import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.SavePreferences;
import vn.hust.edu.variable.Variable;
import vn.hust.edu.view.CirclePageIndicator;
import vn.hust.edu.view.JazzyViewPager;
import vn.hust.edu.view.LinePageIndicator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements View.OnClickListener,
		ToolTipView.OnToolTipViewClickedListener {

	private View convertView;
	private ToolTipView mOrangeToolTipView;
	private ToolTipRelativeLayout mToolTipFrameLayout;
	private TextView hello;
	private TextView logout;
	private TextView report;
	private ButtonFloatSmall settings;
	private JazzyViewPager mainPager;
	private CirclePageIndicator indicator;
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.activity_settings, null);
		mToolTipFrameLayout = (ToolTipRelativeLayout) convertView
				.findViewById(R.id.activity_main_tooltipframelayout);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				addGreenToolTipView();
			}
		}, 500);
		setupView();
		setupData();
		return convertView;
	}

	public void setupView() {
		indicator = (CirclePageIndicator) convertView.findViewById(R.id.indicator);
		mainPager = (JazzyViewPager) convertView.findViewById(R.id.main_pager);
		hello = (TextView) convertView.findViewById(R.id.tv_hello);
		logout = (TextView) convertView.findViewById(R.id.tv_dangxuat);
		report = (TextView) convertView.findViewById(R.id.tv_phanhoi);
		
		report.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ReportActivity.class);
				startActivity(intent);
			}
		});
		
		settings = (ButtonFloatSmall) convertView
				.findViewById(R.id.buttonColorSelector);
		if (!Variable.isVisitor) {

			settings.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mOrangeToolTipView != null) {
						mOrangeToolTipView.remove();
						mOrangeToolTipView = null;
					}
					Intent intent = new Intent(getActivity(),
							InfoAppActivity.class);
					startActivity(intent);
				}
			});
		}
	}

	public void setupData() {
		PictureAdapter pa = new PictureAdapter(getActivity(), mainPager);
		mainPager.setAdapter(pa);
		indicator.setViewPager(mainPager);
		LoadPreferences lp = new LoadPreferences(getActivity());
		final SavePreferences sp = new SavePreferences(getActivity());
		hello.setText("Xin chào: " + lp.loadString("login"));
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sp.SavePreferencesString("pass", "");
				sp.SavePreferencesBool("save", false);
				Intent intent = new Intent(getActivity(), SignInActivity.class);
				startActivity(intent);
				getActivity().finish();

			}
		});
	}

	private void addGreenToolTipView() {
		ToolTip toolTip = new ToolTip().withText("Thông tin ứng dụng")
				.withTextColor(getResources().getColor(R.color.white))
				.withColor(getResources().getColor(R.color.blue_normal))
				.withAnimationType(ToolTip.AnimationType.FROM_TOP);

		mOrangeToolTipView = mToolTipFrameLayout.showToolTipForView(toolTip,
				convertView.findViewById(R.id.buttonColorSelector));
		mOrangeToolTipView.setOnToolTipViewClickedListener(this);
	}

	@Override
	public void onToolTipViewClicked(ToolTipView toolTipView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
