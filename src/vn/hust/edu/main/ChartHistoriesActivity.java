package vn.hust.edu.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.hust.edu.adapter.HistoriesAdapter;
import vn.hust.edu.horizontallistview.AdapterView;
import vn.hust.edu.horizontallistview.AdapterView.OnItemClickListener;
import vn.hust.edu.horizontallistview.HListView;
import vn.hust.edu.horizontallistview.util.v11.MultiChoiceModeListener;
import vn.hust.edu.model.Histories;
import vn.hust.edu.model.InfoHistories;
import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.Variable;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.widgetcustomlib.ErrorView;
import com.example.widgetcustomlib.RetryListener;

public class ChartHistoriesActivity extends Activity {
	private List<InfoHistories> listInfoHistories;
	private String url_his = Variable.host + ":" + Variable.port
			+ "/server/newapi/getcontrainhistories?user=";
	private LinearLayout main_progress;
	private LinearLayout main_action;
	private LinearLayout main_error;
	private ErrorView error;
	private RequestQueue mVolleyQueue;
	private TableLayout table;
	private LayoutInflater li;
	private List<TableRow> list_TR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_his);
		li = getLayoutInflater();
		mVolleyQueue = Volley.newRequestQueue(getApplicationContext());
		LoadPreferences lp = new LoadPreferences(ChartHistoriesActivity.this);
		String user = lp.loadString("login");
		url_his = url_his + user;
		setupView();
		setupData();
	}

	public void setupView() {
		table = (TableLayout) findViewById(R.id.table);
		main_progress = (LinearLayout) findViewById(R.id.main_progress);
		main_action = (LinearLayout) findViewById(R.id.main_action);
		main_error = (LinearLayout) findViewById(R.id.main_error);
		error = (ErrorView) findViewById(R.id.error_view);
		error.setOnRetryListener(new RetryListener() {
			@Override
			public void onRetry() {
				Toast.makeText(getApplicationContext(), "Retry",
						Toast.LENGTH_SHORT).show();
				main_action.setVisibility(View.GONE);
				main_progress.setVisibility(View.VISIBLE);
				main_error.setVisibility(View.GONE);
				setupData();
			}
		});
	}

	private void setupData() {
		listInfoHistories = new ArrayList<InfoHistories>();
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

		try {
			for (int j = 0; j < response.length(); j++) {
				JSONObject sub = response.getJSONObject(j);
				InfoHistories infoHis = new InfoHistories();
				infoHis.setId("" + sub.getInt("id"));
				infoHis.setSubject_id(sub.getString("subject_id"));
				infoHis.setSubject(sub.getString("subject"));
				infoHis.setContest(sub.getString("contest"));
				infoHis.setSub_subject(sub.getString("subsubject"));
				infoHis.setExam(sub.getString("exam"));
				infoHis.setSemeter(sub.getString("semetter"));
				infoHis.setScore(sub.getString("score"));
				infoHis.setScore_text(sub.getString("score_text"));
				listInfoHistories.add(infoHis);
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
			int check = 1;
			for (InfoHistories ih : listInfoHistories) {
				View v = li.inflate(R.layout.chart_his_item, null);
				TableRow tr = (TableRow) v.findViewById(R.id.tableRow);
				if (check % 2 != 0) {
					v.setBackgroundColor(0xff6F9C33);
				} else {
					v.setBackgroundColor(0xff92C94A);
				}
				TextView tv_hocky = (TextView) tr.findViewById(R.id.tv_hocky);
				tv_hocky.setText(ih.getSemeter());
				TextView tv_mahocphan = (TextView) tr
						.findViewById(R.id.tv_mahocphan);
				tv_mahocphan.setText(ih.getSubject_id());
				TextView tv_tenhocphan = (TextView) tr
						.findViewById(R.id.tv_tenhocphan);
				tv_tenhocphan.setText(ih.getSubject());
				TextView tv_tenkythi = (TextView) tr
						.findViewById(R.id.tv_tenkythi);
				tv_tenkythi.setText(ih.getContest());
				TextView tv_dotthi = (TextView) tr.findViewById(R.id.tv_dotthi);
				tv_dotthi.setText(ih.getSub_subject());
				TextView tv_dethi = (TextView) tr.findViewById(R.id.tv_dethi);
				tv_dethi.setText(ih.getExam());
				TextView tv_diemso = (TextView) tr.findViewById(R.id.tv_diemso);
				tv_diemso.setText(ih.getScore());
				TextView tv_diemchu = (TextView) tr
						.findViewById(R.id.tv_diemchu);
				tv_diemchu.setText(ih.getScore_text());
				table.addView(v);
				check++;
			}
			main_action.setVisibility(View.VISIBLE);
			main_progress.setVisibility(View.GONE);
			main_error.setVisibility(View.GONE);

		}
	}

}
