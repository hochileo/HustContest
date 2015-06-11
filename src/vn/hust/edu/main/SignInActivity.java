package vn.hust.edu.main;

import org.json.JSONException;
import org.json.JSONObject;

import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.SavePreferences;
import vn.hust.edu.variable.Variable;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.widgetcustomlib.CheckBox;
import com.example.widgetcustomlib.FloatingEditText;
import com.example.widgetcustomlib.PaperButton;

public class SignInActivity extends Activity implements OnClickListener,
		OnTouchListener {

	private String url_Login = Variable.host + ":" + Variable.port
			+ "/server/newapi/result";
	private RequestQueue mVolleyQueue;
	private PaperButton tvVisit;
	private FloatingEditText editLogin;
	private FloatingEditText editPassword;
	private CheckBox saveAccount;
	private TextView tvcheck;
	private PaperButton btnSignIn;
	private PaperButton btnSignOut;
	private boolean isSave;
	private String login;
	private String pass;
	private LinearLayout main_progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_sign_in);
		main_progress = (LinearLayout) findViewById(R.id.main_progress);
		main_progress.setVisibility(View.GONE);
		LoadPreferences lp = new LoadPreferences(SignInActivity.this);
		isSave = lp.loadBool("save");
		if (isSave) {
			Variable.isVisitor = false;
			Intent intent = new Intent(SignInActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		} else {
			mVolleyQueue = Volley.newRequestQueue(this);
			editLogin = (FloatingEditText) findViewById(R.id.editEmail);
			editLogin.setText(lp.loadString("login"));
			editPassword = (FloatingEditText) findViewById(R.id.editPassword);
			tvVisit = (PaperButton) findViewById(R.id.tvVisit);

			saveAccount = (CheckBox) findViewById(R.id.saveAccount);

			btnSignIn = (PaperButton) findViewById(R.id.btnSignIn);
			btnSignOut = (PaperButton) findViewById(R.id.btnSignOut);
			tvcheck = (TextView) findViewById(R.id.tvcheck);

			tvVisit.setOnClickListener(this);
			btnSignIn.setOnClickListener(this);
			btnSignOut.setOnClickListener(this);
			tvcheck.setOnClickListener(this);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		Intent intent = new Intent();
		this.setResult(3, intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSignIn:
			login = editLogin.getText().toString();
			pass = editPassword.getText().toString();
			boolean isOkLogin = false;
			boolean isOkPass = false;
			if (login.equalsIgnoreCase("")) {
				editLogin.setValidateResult(false,
						getString(R.string.err_input));
				Animation shake = AnimationUtils.loadAnimation(this,
						R.anim.shake);
				editLogin.startAnimation(shake);
				isOkLogin = false;
			} else {
				isOkLogin = true;
			}

			if (pass.equalsIgnoreCase("")) {
				editPassword.setValidateResult(false,
						getString(R.string.err_input));
				Animation shake = AnimationUtils.loadAnimation(this,
						R.anim.shake);
				editPassword.startAnimation(shake);
				isOkPass = false;
			} else {
				if (pass.equalsIgnoreCase("admin")) {
					isOkPass = true;
				} else {
					if (pass.length() < 6) {
						editPassword.setValidateResult(false,
								getString(R.string.err_pass));
						Animation shake = AnimationUtils.loadAnimation(this,
								R.anim.shake);
						editPassword.startAnimation(shake);
						isOkPass = false;
					} else {
						isOkPass = true;
					}
				}
			}

			if (isOkLogin && isOkPass) {
				editLogin.setEnabled(false);
				editPassword.setEnabled(false);
				btnSignIn.setEnabled(false);
				getData();
			}
			break;
		case R.id.btnSignOut:
			Toast.makeText(SignInActivity.this, "SignOut", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(SignInActivity.this,
					RegisterActivity.class);
			intent.putExtra("new", true);
			startActivity(intent);
			break;
		case R.id.tvcheck:
			if (saveAccount.isChecked()) {
				System.out.println("check");
				saveAccount.setActivated(false);
			} else {
				System.out.println("check+");
				saveAccount.setActivated(true);
			}
			break;
		case R.id.tvVisit:
			Variable.isVisitor = true;
			Intent intent1 = new Intent(SignInActivity.this, MainActivity.class);
			startActivity(intent1);
//			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Toast.makeText(SignInActivity.this, "touch", Toast.LENGTH_SHORT).show();
		return false;
	}

	private void getData() {
		main_progress.setVisibility(View.VISIBLE);
		JsonObjectRequest jor = new JsonObjectRequest(Method.GET, url_Login
				+ "?login=" + login + "&pass=" + pass, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject object) {
						System.out.println("test + ok");
						new GetDataTask().execute(object);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(SignInActivity.this,
								getString(R.string.err_login),
								Toast.LENGTH_SHORT).show();
						editLogin.setEnabled(true);
						editPassword.setEnabled(true);
						btnSignIn.setEnabled(true);
						main_progress.setVisibility(View.GONE);
						error.printStackTrace();
					}
				});
//		jor.setRetryPolicy(new DefaultRetryPolicy(2000,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mVolleyQueue.add(jor);
	}

	private void ParserJsonObject(JSONObject object) {
		try {
			SavePreferences sp = new SavePreferences(SignInActivity.this);
			sp.SavePreferencesString("login", login);
			sp.SavePreferencesString("office", object.getString("office"));
			sp.SavePreferencesString("name", object.getString("name"));
			sp.SavePreferencesString("mail", object.getString("mail"));
			sp.SavePreferencesString("phone", object.getString("phone"));
			sp.SavePreferencesString("address", object.getString("address"));
			sp.SavePreferencesString("date", object.getString("date"));
			sp.SavePreferencesString("instutide", object.getString("instutide"));
			sp.SavePreferencesString("department",
					object.getString("department"));
			sp.SavePreferencesString("class", object.getString("class"));
			sp.SavePreferencesString("isVip", object.getString("isVip"));
			sp.SavePreferencesString("cash", object.getString("cash"));
			sp.SavePreferencesString("pass", pass);
			sp.SavePreferencesBool("save", saveAccount.isChecked());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class GetDataTask extends AsyncTask<JSONObject, Void, Void> {

		@Override
		protected Void doInBackground(JSONObject... params) {
			ParserJsonObject(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Variable.isVisitor = false;
			Toast.makeText(SignInActivity.this,
					getString(R.string.success_login), Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(SignInActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.setResult(3);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.setResult(3);
	}

}
