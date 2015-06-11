package vn.hust.edu.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hust.materiallib.ButtonFloatSmall;
import vn.edu.hust.materiallib.ToolTip;
import vn.edu.hust.materiallib.ToolTipRelativeLayout;
import vn.edu.hust.materiallib.ToolTipView;
import vn.hust.edu.adapter.ExpandableContestAdapter;
import vn.hust.edu.model.Contest;
import vn.hust.edu.model.SubSubject;
import vn.hust.edu.model.Subject;
import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.Variable;
import vn.hust.edu.view.AnimatedExpandableListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.widgetcustomlib.ErrorView;
import com.example.widgetcustomlib.RetryListener;

public class ContestFragment extends Fragment implements View.OnClickListener,
		ToolTipView.OnToolTipViewClickedListener {

	private View convertView;
	private LinearLayout main_progress;
	private LinearLayout main_action;
	private LinearLayout main_error;
	private ErrorView error_view;
	private RequestQueue mVolleyQueue;
	private AnimatedExpandableListView elContest;
	private List<Contest> listContest;
	private String url_Contest = Variable.host + ":" + Variable.port
			+ "/server/newapi/getcontest?isVisitor=" + Variable.isVisitor
			+ "&user=";
	private ToolTipView mOrangeToolTipView;
	private ToolTipRelativeLayout mToolTipFrameLayout;
	private ButtonFloatSmall contestHistories;
	private ErrorView error;

	private void addBlueToolTipView() {
		ToolTip toolTip = new ToolTip().withText("Cuộc thi của tôi")
				.withTextColor(getResources().getColor(R.color.white))
				.withColor(getResources().getColor(R.color.purple_progress))
				.withAnimationType(ToolTip.AnimationType.FROM_TOP);

		mOrangeToolTipView = mToolTipFrameLayout.showToolTipForView(toolTip,
				convertView.findViewById(R.id.buttonColorSelector));
		mOrangeToolTipView.setOnToolTipViewClickedListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.activity_contest, null);
		mVolleyQueue = Volley.newRequestQueue(getActivity());
		mToolTipFrameLayout = (ToolTipRelativeLayout) convertView
				.findViewById(R.id.activity_main_tooltipframelayout);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				addBlueToolTipView();
			}
		}, 500);
		setupMenu();
		setupView();
		setupData();
		return convertView;
	}

	public void setupView() {
		main_progress = (LinearLayout) convertView
				.findViewById(R.id.main_progress);
		contestHistories = (ButtonFloatSmall) convertView
				.findViewById(R.id.buttonColorSelector);
		if (!Variable.isVisitor) {

			contestHistories.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mOrangeToolTipView != null) {
						mOrangeToolTipView.remove();
						mOrangeToolTipView = null;
					}
					Intent intent = new Intent(getActivity(),
							MyContestActivity.class);
					startActivity(intent);
				}
			});
		}
		main_action = (LinearLayout) convertView.findViewById(R.id.main_action);
		main_error = (LinearLayout) convertView.findViewById(R.id.main_error);
		error_view = (ErrorView) convertView.findViewById(R.id.error_view);
		elContest = (AnimatedExpandableListView) convertView
				.findViewById(R.id.elContest);
		elContest.setSelector(R.drawable.list_click);

		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		int width = metrics.widthPixels;
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			elContest.setIndicatorBounds(width - GetDipsFromPixel(35), width
					- GetDipsFromPixel(5));
		} else {
			elContest.setIndicatorBoundsRelative(width - GetDipsFromPixel(35),
					width - GetDipsFromPixel(5));
		}

		elContest.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (elContest.isGroupExpanded(groupPosition)) {
					elContest.collapseGroupWithAnimation(groupPosition);
				} else {
					elContest.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}
		});
		elContest.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(getActivity(), "click1", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
		error = (ErrorView) convertView.findViewById(R.id.error_view);
		error.setOnRetryListener(new RetryListener() {
			@Override
			public void onRetry() {
				Toast.makeText(getActivity(), "Retry", Toast.LENGTH_SHORT)
						.show();
				main_action.setVisibility(View.GONE);
				main_progress.setVisibility(View.VISIBLE);
				main_error.setVisibility(View.GONE);
				setupData();
			}
		});
	}

	private void setupMenu() {

	}

	private void setupData() {
		LoadPreferences lp = new LoadPreferences(getActivity());

		listContest = new ArrayList<Contest>();
		JsonArrayRequest jar = new JsonArrayRequest(url_Contest
				+ lp.loadString("login"), new Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				try {
					new SetDataTask().execute(response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				
//				error_view.setErrorTitle(error.getMessage());
				main_action.setVisibility(View.GONE);
				main_progress.setVisibility(View.GONE);
				main_error.setVisibility(View.VISIBLE);
			}
		});
//		jar.setRetryPolicy(new DefaultRetryPolicy(2000,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		mVolleyQueue.add(jar);

	}

	private void getArrayJson(JSONArray response) {

		for (int i = 0; i < response.length(); i++) {
			Contest c = new Contest();
			try {
				JSONObject object = response.getJSONObject(i);
				c.setStatus(object.getBoolean("status"));
				c.setId(object.getString("id"));
				c.setName(object.getString("name"));
				c.setSemeter(object.getString("semeter"));
				JSONArray array = object.getJSONArray("subject");
				List<Subject> listSubjects = new ArrayList<Subject>();
				for (int j = 0; j < array.length(); j++) {
					Subject s = new Subject();
					JSONObject sub = array.getJSONObject(j);
					s.setId(sub.getString("sub_id"));
					s.setName(sub.getString("sub_name"));
					s.setInstutide(sub.getString("sub_instutide"));
					s.setDepartment(sub.getString("sub_department"));
					JSONArray arr = sub.getJSONArray("subsubject");
					List<SubSubject> listSubSubjects = new ArrayList<SubSubject>();
					for (int k = 0; k < arr.length(); k++) {
						JSONObject subsub = arr.getJSONObject(k);
						SubSubject ss = new SubSubject();
						ss.setStatus(subsub.getBoolean("status"));
						ss.setSub_sub_id(subsub.getString("sub_sub_id"));
						ss.setSub_sub_name(subsub.getString("sub_sub_name"));
						ss.setTime_start(subsub.getString("time_start"));
						ss.setTime_end(subsub.getString("time_end"));
						listSubSubjects.add(ss);
					}
					s.setListSubSubject(listSubSubjects);

					listSubjects.add(s);

				}
				c.setSubjects(listSubjects);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listContest.add(c);
		}
	}

	private class SetDataTask extends AsyncTask<JSONArray, Void, Void> {

		@Override
		protected Void doInBackground(JSONArray... params) {
			getArrayJson(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			main_action.setVisibility(View.VISIBLE);
			main_progress.setVisibility(View.GONE);
			main_error.setVisibility(View.GONE);
			ExpandableContestAdapter eca = new ExpandableContestAdapter(
					getActivity(), listContest);
			elContest.setAdapter(eca);
		}
	}

	public int GetDipsFromPixel(float pixels) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (pixels * scale + 0.5f);
	}

	@Override
	public void onToolTipViewClicked(ToolTipView toolTipView) {

	}

	@Override
	public void onClick(View v) {

	}
}
