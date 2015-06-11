package vn.hust.edu.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hust.materiallib.ButtonFloatSmall;
import vn.edu.hust.materiallib.ToolTip;
import vn.edu.hust.materiallib.ToolTipRelativeLayout;
import vn.edu.hust.materiallib.ToolTipView;
import vn.hust.edu.adapter.HistoriesAdapter;
import vn.hust.edu.horizontallistview.AdapterView;
import vn.hust.edu.horizontallistview.AdapterView.OnItemClickListener;
import vn.hust.edu.horizontallistview.HListView;
import vn.hust.edu.horizontallistview.util.v11.MultiChoiceModeListener;
import vn.hust.edu.model.Histories;
import vn.hust.edu.model.InfoHistories;
import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.Variable;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.text.Html;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.widgetcustomlib.ErrorView;
import com.example.widgetcustomlib.RetryListener;
import com.google.gson.Gson;

public class HistoryFragment extends Fragment implements View.OnClickListener,
		ToolTipView.OnToolTipViewClickedListener {

	private View convertView;
	private ToolTipView mOrangeToolTipView;
	private ToolTipRelativeLayout mToolTipFrameLayout;
	private String url_his = Variable.host + ":" + Variable.port
			+ "/server/newapi/gethistories?user=";
	private String url_delete = Variable.host + ":" + Variable.port
			+ "/server/newapi/deletehistories?user=";
	private List<Histories> listHistories = new ArrayList<Histories>();
	private LinearLayout main_progress;
	private ViewGroup main_action;
	private LinearLayout main_error;
	private RequestQueue mVolleyQueue;
	private ButtonFloatSmall deleteHistories;
	private ErrorView error;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.activity_history, null);
		mToolTipFrameLayout = (ToolTipRelativeLayout) convertView
				.findViewById(R.id.activity_main_tooltipframelayout);
		mVolleyQueue = Volley.newRequestQueue(getActivity());
		LoadPreferences lp = new LoadPreferences(getActivity());
		String user = lp.loadString("login");
		url_his = url_his + user;
		url_delete = url_delete + user + "&value=";
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
		main_progress = (LinearLayout) convertView
				.findViewById(R.id.main_progress);
		deleteHistories = (ButtonFloatSmall) convertView
				.findViewById(R.id.buttonColorSelector);
		deleteHistories.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOrangeToolTipView != null) {
//					mOrangeToolTipView.remove();
//					mOrangeToolTipView = null;
//					main_action.removeAllViews();
					Intent intent = new Intent(getActivity(), ChartHistoriesActivity.class);
					startActivity(intent);
				}
			}
		});
		main_action = (ViewGroup) convertView.findViewById(R.id.main_histories);
		main_error = (LinearLayout) convertView.findViewById(R.id.main_error);
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

	private void setupData() {
		listHistories = new ArrayList<Histories>();
		JsonArrayRequest jar = new JsonArrayRequest(url_his,
				new Listener<JSONArray>() {
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
						main_action.setVisibility(View.GONE);
						main_progress.setVisibility(View.GONE);
						main_error.setVisibility(View.VISIBLE);
					}
				});

		mVolleyQueue.add(jar);

	}

	private void getArrayJson(JSONArray response) {

		for (int i = 0; i < response.length(); i++) {
			Histories his = new Histories();
			try {
				JSONObject object = response.getJSONObject(i);
				his.setSubject(object.getString("subject"));
				JSONArray array = object.getJSONArray("hs");
				List<InfoHistories> listSubjects = new ArrayList<InfoHistories>();
				for (int j = 0; j < array.length(); j++) {
					JSONObject sub = array.getJSONObject(j);
					InfoHistories infoHis = new InfoHistories();
					infoHis.setId(""+sub.getInt("id"));
					infoHis.setSubject_id(sub.getString("subject_id"));
					infoHis.setSubject(sub.getString("subject"));
					infoHis.setSub_subject(sub.getString("subsubject"));
					infoHis.setExam(sub.getString("exam"));
					infoHis.setScore(sub.getString("score"));
					infoHis.setScore_text(sub.getString("score_text"));
					listSubjects.add(infoHis);
				}
				his.setListHistories(listSubjects);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			listHistories.add(his);
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
			int size = listHistories.size();

			for (int i = 0; i < size; i++) {
				View view = getActivity().getLayoutInflater().inflate(
						R.layout.horizontal_list, null);
				final HListView list = (HListView) view
						.findViewById(R.id.hListView1);
				list.setHeaderDividersEnabled(true);
				list.setFooterDividersEnabled(true);
				list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
				Typeface font1 = Typeface.createFromAsset(
						getActivity().getAssets(), "timesbi.ttf");
				TextView tv = (TextView) view.findViewById(R.id.contest);
				tv.setTypeface(font1);
				tv.setText(Html.fromHtml("<u><b>"
						+ listHistories.get(i).getSubject() + "</u></b>"));
				final HistoriesAdapter ha = new HistoriesAdapter(getActivity(),
						0, listHistories.get(i).getListHistories());
				list.setAdapter(ha);
				list.setTag(view);
				if (list.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE_MODAL) {
					list.setMultiChoiceModeListener(new MultiChoiceModeListener() {

						@Override
						public boolean onPrepareActionMode(ActionMode mode,
								Menu menu) {
							return true;
						}

						@Override
						public void onDestroyActionMode(ActionMode mode) {
						}

						@Override
						public boolean onCreateActionMode(ActionMode mode,
								Menu menu) {
							System.out.println("list create");
							menu.add(0, 0, 0, "Xóa");
							return true;
						}

						@Override
						public boolean onActionItemClicked(ActionMode mode,
								MenuItem item) {
							System.out.println("list click");
							final int itemId = item.getItemId();
							if (itemId == 0) {
								deleteSelectedItems(list, ha);
							}

							mode.finish();
							return false;
						}

						@Override
						public void onItemCheckedStateChanged(ActionMode mode,
								int position, long id, boolean checked) {
							System.out.println("list check ");
							mode.setTitle("Xóa Lịch Sử");
							mode.setSubtitle("Đã chọn: "
									+ list.getCheckedItemCount() + " mục");
						}
					});
				} else if (list.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							System.out.println("list 1 create");

						}
					});
				}

				main_action.addView(view);
			}
		}
	}

	private void deleteSelectedItems(HListView listView,
			HistoriesAdapter adapter) {
		List<InfoHistories> infoH = new ArrayList<InfoHistories>();
		SparseArrayCompat<Boolean> checkedItems = listView
				.getCheckedItemPositions();
		ArrayList<Integer> sorted = new ArrayList<Integer>(checkedItems.size());

		for (int i = 0; i < checkedItems.size(); i++) {
			if (checkedItems.valueAt(i)) {
				sorted.add(checkedItems.keyAt(i));
			}
		}

		Collections.sort(sorted);

		for (int i = sorted.size() - 1; i >= 0; i--) {
			int position = sorted.get(i);
			infoH.add(adapter.histories.get(position));
			adapter.histories.remove(position);
		}
		if (adapter.histories.size() == 0) {
			main_action.removeView((View) listView.getTag());

		} else {
			adapter.update();
		}

		Gson g = new Gson();
		String json = g.toJson(infoH);
		System.out.println("Check link json " + json.replaceAll(" ", "%20"));
		getData(url_delete + json.replaceAll(" ", "%20"));
	}

	private void addGreenToolTipView() {
		ToolTip toolTip = new ToolTip().withText("Bảng điểm của tôi")
				.withTextColor(getResources().getColor(R.color.white))
				.withColor(getResources().getColor(R.color.green_complete))
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

	private void getData(String url) {
		main_progress.setVisibility(View.VISIBLE);
		JsonObjectRequest jor = new JsonObjectRequest(Method.POST, url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject object) {
						new GetDataTask().execute(object);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						main_progress.setVisibility(View.GONE);
						error.printStackTrace();
					}
				});
//		jor.setRetryPolicy(new DefaultRetryPolicy(2000,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mVolleyQueue.add(jor);
	}

	private boolean ParserJsonObject(JSONObject object) {
		try {
			return object.getBoolean("status");
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	private class GetDataTask extends AsyncTask<JSONObject, Void, Boolean> {

		@Override
		protected Boolean doInBackground(JSONObject... params) {
			boolean status = ParserJsonObject(params[0]);
			return status;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			main_progress.setVisibility(View.GONE);
			if (result) {
				Toast.makeText(getActivity(), "Đã Xóa", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getActivity(), "Không thể xóa",
						Toast.LENGTH_LONG).show();
			}

		}

	}

}
