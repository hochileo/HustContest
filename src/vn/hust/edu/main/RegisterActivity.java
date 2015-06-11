package vn.hust.edu.main;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.hust.edu.gallerylib.DatePickerDialog;
import vn.hust.edu.gallerylib.DatePickerDialog.OnDateSetListener;
import vn.hust.edu.model.Classst;
import vn.hust.edu.model.Department;
import vn.hust.edu.model.Instutide;
import vn.hust.edu.model.UserRegister;
import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.SavePreferences;
import vn.hust.edu.variable.Variable;
import vn.hust.edu.view.RevealLayout;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
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
import com.example.widgetcustomlib.PaperButton;
import com.example.widgetcustomlib.RetryListener;
import com.google.gson.Gson;

public class RegisterActivity extends FragmentActivity implements
		OnDateSetListener {
	private RequestQueue mVolleyQueue;
	public static RevealLayout mRevealLayout;
	private LinearLayout main_progress;
	private LinearLayout main_error;
	public String DATEPICKER_TAG = "datepicker";
	private RegisterInputPrimaryFragment ripf;
	private RegisterSelectPrimaryFragment rspf;
	private RegisterInputNotPrimaryFragment rinpf;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private int mItem = 0;
	private String url_ins = Variable.host + ":" + Variable.port
			+ "/server/newapi/getinstutide";
	private String url_regis = Variable.host + ":" + Variable.port
			+ "/server/newapi/register/result?new=";
	public static UserRegister user = new UserRegister();
	public static PaperButton btnNext;
	public static PaperButton btnBack;
	public static FragmentManager fm;

	private DatePickerDialog datePickerDialog;
	private ErrorView error;
	private LoadPreferences lp;

	public static List<Instutide> instutides;
	// public static List<String> works;
	// public static List<String> worksID;
	public static List<String> instutide;
	public static boolean isCheckUser;
	public static boolean isCheckMail;
	public static boolean isNew;
	private boolean isPassChange = false;
	public static HashMap<String, List<String>> departments;
	public static HashMap<String, List<String>> clasts;

	public void showDialog() {
		datePickerDialog.setVibrate(false);
		datePickerDialog.setYearRange(1980, 2028);
		datePickerDialog.setCloseOnSingleTapDay(false);
		datePickerDialog.show(fm, DATEPICKER_TAG);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_register);
		lp = new LoadPreferences(RegisterActivity.this);
		Bundle bb = getIntent().getExtras();
		isNew = bb.getBoolean("new");
		url_regis = url_regis + isNew + "&param=";
		// works = new ArrayList<String>();
		// works.add("Quản trị");
		// works.add("Giảng viên");
		// works.add("Sinh viên");
		// works.add("Khách");
		// worksID = new ArrayList<String>();
		// worksID.add("AD");
		// worksID.add("GV");
		// worksID.add("SV");
		// worksID.add("VS");
		fm = getSupportFragmentManager();
		mVolleyQueue = Volley.newRequestQueue(this);
		mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
		main_progress = (LinearLayout) findViewById(R.id.main_progress);
		main_error = (LinearLayout) findViewById(R.id.main_error);
		btnNext = (PaperButton) findViewById(R.id.btnNext);
		btnBack = (PaperButton) findViewById(R.id.btnBack);
		btnNext.setActivated(false);
		btnBack.setVisibility(View.INVISIBLE);
		if (isNew) {
			Calendar calendar = Calendar.getInstance();
			datePickerDialog = DatePickerDialog.newInstance(this,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), true);
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = format.parse(lp.loadString("date"));
				int monthstart = Integer.parseInt((String) DateFormat.format(
						"MM", date)); // 06
				int yearstart = Integer.parseInt((String) DateFormat.format(
						"yyyy", date)); // 2013
				int daystart = Integer.parseInt((String) DateFormat.format(
						"dd", date)); // 20

				datePickerDialog = DatePickerDialog.newInstance(this,
						yearstart, monthstart - 1, daystart, true);
			} catch (ParseException e) {
				e.printStackTrace();
				Calendar calendar = Calendar.getInstance();
				datePickerDialog = DatePickerDialog.newInstance(this,
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH), true);
			}
		}

		ripf = new RegisterInputPrimaryFragment();
		rspf = new RegisterSelectPrimaryFragment();
		rinpf = new RegisterInputNotPrimaryFragment();
		fragments.add(ripf);
		fragments.add(rspf);
		fragments.add(rinpf);
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isOK = true;
				switch (mItem) {
				case 1:
					isOK = CheckPrimary();
					break;
				default:
					break;
				}
				if (isOK && isCheckUser && isCheckMail) {
					if (mItem >= (fragments.size())) {
						user.setAddress(rinpf.address.getText().toString());
						user.setPhone(rinpf.phone.getText().toString());
						user.setName(rinpf.name.getText().toString());
						showDialog();

					} else {
						btnBack.setVisibility(View.VISIBLE);
						getSupportFragmentManager()
								.beginTransaction()
								.addToBackStack(null)
								.hide(fragments.get((mItem % fragments.size()) - 1))
								.add(R.id.container,
										fragments.get(mItem % fragments.size()))
								.commit();
						if (mItem == 2) {
							btnNext.setText("Năm Sinh");
						}
						mItem++;
					}
				}
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});

		mRevealLayout.setContentShown(false);
		mRevealLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						mRevealLayout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						mRevealLayout.postDelayed(new Runnable() {
							@Override
							public void run() {
								mRevealLayout.show();
							}
						}, 50);
					}
				});
		error = (ErrorView) findViewById(R.id.error_view);
		error.setOnRetryListener(new RetryListener() {
			@Override
			public void onRetry() {
				Toast.makeText(RegisterActivity.this, "Retry",
						Toast.LENGTH_SHORT).show();
				main_progress.setVisibility(View.VISIBLE);
				main_error.setVisibility(View.GONE);
				setupData();
			}
		});

		setupData();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mItem--;
		btnNext.setText("Tiếp tục");
		if (mItem == 1) {
			btnBack.setVisibility(View.INVISIBLE);
		}
		System.out.println("mItem " + mItem);
		if (mItem <= 0) {
			finish();
		} else {
			mRevealLayout.getViewTreeObserver().addOnGlobalLayoutListener(
					new ViewTreeObserver.OnGlobalLayoutListener() {
						@SuppressWarnings("deprecation")
						@Override
						public void onGlobalLayout() {
							mRevealLayout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
							mRevealLayout.postDelayed(new Runnable() {
								@Override
								public void run() {
									mRevealLayout.show();
								}
							}, 50);
						}
					});
		}
	}

	private void setupData() {
		instutides = new ArrayList<Instutide>();
		JsonArrayRequest jar = new JsonArrayRequest(url_ins,
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
						main_progress.setVisibility(View.GONE);
						main_error.setVisibility(View.VISIBLE);
					}
				});
		jar.setRetryPolicy(new DefaultRetryPolicy(2000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		mVolleyQueue.add(jar);

	}

	private void getArrayJson(JSONArray response) {

		for (int i = 0; i < response.length(); i++) {
			Instutide ins = new Instutide();
			try {
				JSONObject object = response.getJSONObject(i);
				ins.setId(object.getString("id"));
				ins.setName(object.getString("name"));
				JSONArray array = object.getJSONArray("department");
				List<Department> listDepartments = new ArrayList<Department>();
				for (int j = 0; j < array.length(); j++) {
					Department department = new Department();
					JSONObject sub = array.getJSONObject(j);
					department.setId(sub.getString("id"));
					department.setName(sub.getString("name"));
					listDepartments.add(department);
				}
				ins.setDepartments(listDepartments);
				JSONArray array1 = object.getJSONArray("classst");
				List<Classst> listClass = new ArrayList<Classst>();
				for (int j = 0; j < array1.length(); j++) {
					Classst classst = new Classst();
					JSONObject sub = array1.getJSONObject(j);
					classst.setId(sub.getString("id"));
					classst.setName(sub.getString("name"));
					listClass.add(classst);
				}
				ins.setClasssts(listClass);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			instutides.add(ins);
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
			main_progress.setVisibility(View.GONE);
			main_error.setVisibility(View.GONE);
			instutide = new ArrayList<String>();
			departments = new HashMap<String, List<String>>();
			clasts = new HashMap<String, List<String>>();
			for (Instutide ins : RegisterActivity.instutides) {
				instutide.add(ins.getName());
				List<String> departs = new ArrayList<String>();
				for (Department depart : ins.getDepartments()) {
					departs.add(depart.getName());
				}
				departments.put(ins.getName(), departs);
				List<String> classs = new ArrayList<String>();
				for (Classst clas : ins.getClasssts()) {
					classs.add(clas.getName());
				}
				clasts.put(ins.getName(), classs);
			}
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.container,
							fragments.get(mItem % fragments.size())).commit();
			mItem++;

		}
	}

	private void getData(String json) {
		main_progress.setVisibility(View.VISIBLE);
		JsonObjectRequest jor = new JsonObjectRequest(Method.POST, url_regis
				+ json.replaceAll(" ", "%20"), null,
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

		@Override
		protected Boolean doInBackground(JSONObject... params) {
			boolean status = ParserJsonObject(params[0]);
			return status;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result) {
				mItem = -1;
				if (isNew) {
					Toast.makeText(RegisterActivity.this, "Đăng ký thành công",
							Toast.LENGTH_LONG).show();
					finish();
				} else {
					Toast.makeText(RegisterActivity.this,
							"Chỉnh sửa thành công", Toast.LENGTH_LONG).show();
					SavePreferences sp = new SavePreferences(
							getApplicationContext());
					if (!isPassChange) {
						sp.SavePreferencesString("login", "");
						sp.SavePreferencesString("office", "");
						sp.SavePreferencesString("name", "");
						sp.SavePreferencesString("mail", "");
						sp.SavePreferencesString("phone", "");
						sp.SavePreferencesString("address", "");
						sp.SavePreferencesString("date", "");
						sp.SavePreferencesString("instutide", "");
						sp.SavePreferencesString("department", "");
						sp.SavePreferencesString("class", "");
						sp.SavePreferencesString("isVip", "");
						sp.SavePreferencesString("cash", "");
						sp.SavePreferencesString("pass", "");
						sp.SavePreferencesBool("save", false);
						setResult(2);
						finish();
					} else {
						sp.SavePreferencesString("office", "Sinh viên");
						sp.SavePreferencesString("name", user.getName());
						sp.SavePreferencesString("mail", user.getMail());
						sp.SavePreferencesString("phone", user.getPhone());
						sp.SavePreferencesString("address", user.getAddress());
						sp.SavePreferencesString("date", user.getDate());
						sp.SavePreferencesString("instutide",
								user.getInstutide());
						sp.SavePreferencesString("department",
								user.getDepartment());
						sp.SavePreferencesString("class", user.getClassst());
						setResult(3);
						finish();

					}
				}
			} else {
				main_progress.setVisibility(View.GONE);
				if (isNew) {
					Toast.makeText(RegisterActivity.this, "Không thể đăng ký",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(RegisterActivity.this,
							"Không thể chỉnh sửa", Toast.LENGTH_LONG).show();
				}
			}

		}

	}

	private boolean CheckPrimary() {
		boolean isOK = true;
		String login = ripf.login.getText().toString().trim();
		String pass = ripf.pass.getText().toString();
		String pass1 = ripf.pass1.getText().toString();
		String pass2 = ripf.pass2.getText().toString();
		String mail = ripf.mail.getText().toString();
		if (login.equalsIgnoreCase("")) {
			ripf.login.setValidateResult(false, getString(R.string.err_input));
			Animation shake = AnimationUtils.loadAnimation(
					RegisterActivity.this, R.anim.shake);
			ripf.login.startAnimation(shake);
			isOK = false;
		} else {
			user.setLogin(login);
		}

		if (!isNew) {
			if (pass.equalsIgnoreCase("")) {
				ripf.pass.setValidateResult(false, "Phải nhập mật khẩu");
				Animation shake = AnimationUtils.loadAnimation(
						RegisterActivity.this, R.anim.shake);
				ripf.pass.startAnimation(shake);
				isOK = false;
			} else {
				if (!pass.equals(lp.loadString("pass"))) {
					ripf.pass.setValidateResult(false, "Mật khẩu sai");
					Animation shake = AnimationUtils.loadAnimation(
							RegisterActivity.this, R.anim.shake);
					ripf.pass.startAnimation(shake);
					isOK = false;
				} else {
					if (pass1.equalsIgnoreCase("")) {
						isPassChange = false;
						user.setPass("");
					} else {
						if (pass1.length() < 6) {
							ripf.pass1.setValidateResult(false,
									getString(R.string.err_pass));
							Animation shake = AnimationUtils.loadAnimation(
									RegisterActivity.this, R.anim.shake);
							ripf.pass1.startAnimation(shake);
							isOK = false;
						}
					}
					if (!pass1.equals(pass2)) {
						ripf.pass2.setValidateResult(false,
								"Mật khẩu không khớp");
						Animation shake = AnimationUtils.loadAnimation(
								RegisterActivity.this, R.anim.shake);
						ripf.pass2.startAnimation(shake);
						isOK = false;
					} else {
						isPassChange = true;
						user.setPass(pass2);
					}
				}
			}
		} else {
			if (pass1.equalsIgnoreCase("")) {
				ripf.pass1.setValidateResult(false,
						getString(R.string.err_input));
				Animation shake = AnimationUtils.loadAnimation(
						RegisterActivity.this, R.anim.shake);
				ripf.pass1.startAnimation(shake);
				isOK = false;
			} else {
				if (pass1.length() < 6) {
					ripf.pass1.setValidateResult(false,
							getString(R.string.err_pass));
					Animation shake = AnimationUtils.loadAnimation(
							RegisterActivity.this, R.anim.shake);
					ripf.pass1.startAnimation(shake);
					isOK = false;
				}
			}

			if (!pass1.equals(pass2)) {
				ripf.pass2.setValidateResult(false, "Mật khẩu không khớp");
				Animation shake = AnimationUtils.loadAnimation(
						RegisterActivity.this, R.anim.shake);
				ripf.pass2.startAnimation(shake);
				isOK = false;
			} else {
				user.setPass(pass2);
			}
		}
		if (mail.equalsIgnoreCase("")) {
			ripf.mail.setValidateResult(false, getString(R.string.err_input));
			Animation shake = AnimationUtils.loadAnimation(
					RegisterActivity.this, R.anim.shake);
			ripf.mail.startAnimation(shake);
			isOK = false;
		} else {
			if (mail.indexOf("@") == -1
					|| mail.indexOf("@") == mail.length() - 1) {
				ripf.mail.setValidateResult(false,
						"Sai cú pháp Ex:example@gmail.com");
				Animation shake = AnimationUtils.loadAnimation(
						RegisterActivity.this, R.anim.shake);
				ripf.mail.startAnimation(shake);
				isOK = false;
			} else {
				user.setMail(mail);
			}
		}
		if (!isCheckMail) {
			Animation shake = AnimationUtils.loadAnimation(
					RegisterActivity.this, R.anim.shake);
			ripf.mail.startAnimation(shake);
		}
		if (!isCheckUser) {
			Animation shake = AnimationUtils.loadAnimation(
					RegisterActivity.this, R.anim.shake);
			ripf.login.startAnimation(shake);
		}
		return isOK;
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		if (!isNew) {
			user.setDate(year + "-" + (month + 1) + "-" + day);
		} else {
			user.setDate(year + "-" + month + "-" + day);
		}
		Gson gson = new Gson();
		String json = gson.toJson(user);
		String param = "";
		try {
			param = new String(json.getBytes("UTF-8"), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			param = json;
		}
		System.out.println("check link " + url_regis + param);
		getData(param);

	}
}
