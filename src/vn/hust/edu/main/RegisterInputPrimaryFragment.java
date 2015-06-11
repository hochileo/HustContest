package vn.hust.edu.main;

import org.json.JSONException;
import org.json.JSONObject;

import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.Variable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.widgetcustomlib.FloatingEditText;

public class RegisterInputPrimaryFragment extends Fragment {
	public FloatingEditText login;
	public FloatingEditText pass;
	public FloatingEditText pass1;
	public FloatingEditText pass2;
	public FloatingEditText mail;
	private RequestQueue mVolleyQueue;
	private String linkCheckUser = Variable.host + ":" + Variable.port
			+ "/server/newapi/checkuser?login=";
	private String linkCheckMail = Variable.host + ":" + Variable.port
			+ "/server/newapi/checkmail?mail=";
	private LoadPreferences lp;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.register_input_primary,
				container, false);
		login = (FloatingEditText) rootView.findViewById(R.id.editLogin);
		pass = (FloatingEditText) rootView.findViewById(R.id.editPassword);
		pass1 = (FloatingEditText) rootView.findViewById(R.id.editPassword1);
		pass2 = (FloatingEditText) rootView.findViewById(R.id.editPassword2);
		mail = (FloatingEditText) rootView.findViewById(R.id.editMail);
		lp = new LoadPreferences(getActivity());
		if (!RegisterActivity.isNew) {
			login.setText(lp.loadString("login"));
			login.setEnabled(false);
			mail.setText(lp.loadString("mail"));
			RegisterActivity.isCheckUser = true;
			RegisterActivity.isCheckMail = true;
			pass1.setHint("Mật Khẩu Mới");
			pass.setVisibility(View.VISIBLE);
		}

		rootView.setBackgroundColor(0xff008080);
		mVolleyQueue = Volley.newRequestQueue(getActivity());
		RegisterActivity.mRevealLayout.getViewTreeObserver()
				.addOnGlobalLayoutListener(
						new ViewTreeObserver.OnGlobalLayoutListener() {
							@SuppressWarnings("deprecation")
							@Override
							public void onGlobalLayout() {
								RegisterActivity.mRevealLayout
										.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
								RegisterActivity.mRevealLayout.postDelayed(
										new Runnable() {
											@Override
											public void run() {
												RegisterActivity.mRevealLayout
														.show();
											}
										}, 50);
							}
						});

		login.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				getData(linkCheckUser + s.toString().trim(), true);
			}
		});
		mail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				getData(linkCheckMail + s.toString(), false);
			}
		});
		return rootView;
	}

	private void getData(String url, final boolean check) {
		JsonObjectRequest jor = new JsonObjectRequest(Method.POST, url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject object) {
						new GetDataTask(check).execute(object);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});
		jor.setRetryPolicy(new DefaultRetryPolicy(2000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
		private boolean check;

		public GetDataTask(boolean check) {
			this.check = check;
		}

		@Override
		protected Boolean doInBackground(JSONObject... params) {
			boolean status = ParserJsonObject(params[0]);
			return status;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (check) {
				RegisterActivity.isCheckUser = result;
				if (!result) {
					if (!RegisterActivity.isNew) {
						if (!login.getText().toString()
								.equalsIgnoreCase(lp.loadString("login"))) {
							login.setValidateResult(false,
									"Tài khoản đã tồn tại");
						} else {
							RegisterActivity.isCheckUser = true;
						}
					} else {
						login.setValidateResult(false, "Tài khoản đã tồn tại");

					}
					// Animation shake = AnimationUtils.loadAnimation(
					// getActivity(), R.anim.shake);
					// login.startAnimation(shake);
				}
			} else {
				RegisterActivity.isCheckMail = result;
				if (!result) {
					if (!RegisterActivity.isNew) {
						if (!mail.getText().toString()
								.equalsIgnoreCase(lp.loadString("mail"))) {
							mail.setValidateResult(false, "Hòm thư đã tồn tại");
						} else {
							RegisterActivity.isCheckMail = true;
						}
					} else {
						mail.setValidateResult(false, "Hòm thư đã tồn tại");
					}
					// Animation shake = AnimationUtils.loadAnimation(
					// getActivity(), R.anim.shake);
					// mail.startAnimation(shake);
				}
			}
		}
	}
}
