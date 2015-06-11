package vn.hust.edu.control;

import org.json.JSONObject;

import vn.hust.edu.main.TestActivity;
import vn.hust.edu.model.Part;
import vn.hust.edu.model.QuesContruct;
import vn.hust.edu.model.Question;
import vn.hust.edu.variable.Variable;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.libraryprogressbutton.ProcessButton;

public class ProgressGenerator {

	public interface OnCompleteListener {

		public void onComplete(int listening, int reading);

		public void onComplete(int score);
	}

	private OnCompleteListener mListener;
	private int mProgress;
	private RequestQueue mVolleyQueue;
	private Context mContext;
	private String url_score = Variable.host + ":" + Variable.port
			+ "/server/newapi/takescore?isVisitor=" + Variable.isVisitor
			+ "&subject=";
	private String subject;
	private String login;
	private String subsubject;
	private String contest;
	private String exam;
	private int length;

	public ProgressGenerator(OnCompleteListener listener, String subject,
			String contest, String subsubject, String login, String exam,
			int length) {
		mListener = listener;
		this.mContext = (Context) listener;
		this.subject = subject;
		this.contest = contest;
		this.subsubject = subsubject;
		this.login = login;
		this.exam = exam;
		this.length = length;
		url_score = url_score + subject + "&login=" + login + "&contest="
				+ contest + "&exam=" + exam + "&subsubject=" + subsubject + "&";
		System.out.println("link " + url_score );
	}

	public void start(final ProcessButton button) {
		Variable.score_Listening = 0;
		Variable.score_Reading = 0;
		mVolleyQueue = Volley.newRequestQueue(mContext);
		for (Part p : TestActivity.exam.getPart()) {
			int part = Integer.parseInt(p.getPart());
			for (Question q : p.getGroupQuestion()) {
				for (QuesContruct ques : q.getQuestion()) {
					int id = ques.getId();
					String correct_ans = ques.getCorrectAns();
					if (Variable.choose_ans.get(id) != null) {
						String choose_ans = Variable.choose_ans.get(id);
						ques.setChooseAns(choose_ans);
						if (choose_ans.equalsIgnoreCase(correct_ans)) {
							if (part <= 4) {
								Variable.score_Listening++;
							} else {
								Variable.score_Reading++;
							}
						}
					} else {
						ques.setChooseAns("");
					}
					mProgress++;
					button.setProgress(mProgress);
				}
			}

		}
		if (subject.toLowerCase().indexOf("toeic") != -1) {
			url_score = url_score + "listening=" + Variable.score_Listening
					+ "&reading=" + Variable.score_Reading;
		} else {
			float score = (float) Variable.score_Listening;
			float size = (float) length;
			int score_final = (int) ((score / size) * 100);
			url_score = url_score + "listening=" + score_final + "&reading=0";
		}
		System.out.println("check link " + url_score + " - test= " + subject.toLowerCase().indexOf("toeic"));
		JsonObjectRequest jor = new JsonObjectRequest(Method.GET, url_score,
				null, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject object) {
						try {

							if (subject.toLowerCase().indexOf("toeic") != -1) {
								mListener.onComplete(
										object.getInt("listening"),
										object.getInt("reading"));
							} else {
								mListener.onComplete(object.getInt("score"));

							}
							TestActivity.testting.update(TestActivity.exam
									.getPart());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});
		// jor.setRetryPolicy(new DefaultRetryPolicy(2000,
		// DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
		// DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mVolleyQueue.add(jor);
	}
}
