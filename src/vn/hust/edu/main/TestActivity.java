package vn.hust.edu.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.hust.edu.adapter.GalleryAdapter;
import vn.hust.edu.adapter.TesttingAdapterPart;
import vn.hust.edu.buttonchooselib.CircleButton;
import vn.hust.edu.control.ButtonClick;
import vn.hust.edu.control.ProgressGenerator;
import vn.hust.edu.gallerylib.FancyCoverFlow;
import vn.hust.edu.model.GalleryOwn;
import vn.hust.edu.model.InfoExam;
import vn.hust.edu.model.Part;
import vn.hust.edu.model.QuesContruct;
import vn.hust.edu.model.Question;
import vn.hust.edu.model.TimeTest;
import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.Variable;
import vn.hust.edu.view.JazzyViewPager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.libraryprogressbutton.ActionProcessButton;

public class TestActivity extends FragmentActivity implements
		OnPreparedListener, OnCompletionListener, OnBufferingUpdateListener,
		ProgressGenerator.OnCompleteListener, OnItemClickListener {
	private JazzyViewPager mainPager;
	public static TesttingAdapterPart testting;
	public static LinearLayout main_progress;
	public static TextView num_ques;
	private TextView time_remaining;
	public static InfoExam exam;
	private String url_ques = Variable.host + ":" + Variable.port
			+ "/server/newapi/takeexam?isVisitor=" + Variable.isVisitor;
	private RequestQueue mVolleyQueue;
	private int i = 0;
	private int j = 0;
	private int position = 0;
	private int show_pos = 1;
	private int base1 = 0;
	private int base2 = 10;
	private int base3 = 40;
	private FancyCoverFlow mTrack;
	private int base4 = 70;
	private int position_intro = 0;
	private int[] audio_intro = { R.raw.about_part1, R.raw.about_part2,
			R.raw.about_part3, R.raw.about_part4 };
	public static MediaPlayer mediaPlayer;
	private String uri = "android.resource://vn.hust.edu.main/";
	private ActionProcessButton bt_Submit;
	private ProgressGenerator progressGenerator;
	private List<GalleryOwn> part;
	private int h;
	private int m;
	Dialog dialog;
	private int s;
	private int length;
	private int ch = 1;
	private String user;
	private int size = 0;
	String contest;
	String subject;
	String subsubject;
	boolean isSubmit = false;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.testting);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		LoadPreferences lp = new LoadPreferences(TestActivity.this);
		user = lp.loadString("login");
		Bundle bundle = getIntent().getExtras();
		contest = bundle.getString("contest");
		subject = bundle.getString("subject");
		subsubject = bundle.getString("subsubject");
		url_ques = url_ques + "&contest=" + contest + "&subject=" + subject
				+ "&subsubject=" + subsubject + "&user=" + user;
		System.out.println("check link " + url_ques);

		mVolleyQueue = Volley.newRequestQueue(TestActivity.this);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnPreparedListener(this);
		setupView();
		setupData();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!isSubmit) {
			if (isSubmit) {
				dialog.show();
			} else {
				alertDialog.show();
			}
		} else {
			super.onBackPressed();
			finish();
		}
	}

	private void setupView() {
		mainPager = (JazzyViewPager) findViewById(R.id.main_pager);
		main_progress = (LinearLayout) findViewById(R.id.main_progress);
		num_ques = (TextView) findViewById(R.id.num_ques);
		time_remaining = (TextView) findViewById(R.id.time_remaining);
		if (subject.toLowerCase().indexOf("toeic") !=-1) {
			num_ques.setText("Hướng dẫn Part I.");			
		}else{			
			num_ques.setText("Bắt đầu cuộc thi");			
		}
		mTrack = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
		mTrack.setReflectionEnabled(true);
		mTrack.setReflectionRatio(0.3f);
		mTrack.setReflectionGap(0);
		mTrack.setOnItemClickListener(this);
		bt_Submit = (ActionProcessButton) findViewById(R.id.btnSubmit);
		bt_Submit.setEnabled(false);
		bt_Submit.setMode(ActionProcessButton.Mode.PROGRESS);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("Nộp Bài");
		alertDialogBuilder.setPositiveButton("Okie",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						try {
							stopPlaying();
						} catch (Exception e) {
							e.printStackTrace();
						}
						progressGenerator.start(bt_Submit);
					}
				});
		alertDialogBuilder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		alertDialog = alertDialogBuilder.create();

		bt_Submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSubmit) {
					dialog.show();
				} else {
					alertDialog.show();
				}
				// bt_Submit.setEnabled(false);
			}
		});
	}

	private void setupData() {
		JsonObjectRequest jor = new JsonObjectRequest(Method.GET, url_ques,
				null, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject object) {
						try {
							new SetDataTask().execute(object);
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

	private boolean getArrayJson(JSONObject response) {
		boolean isStatus = false;
		try {

			isStatus = response.getBoolean("status");
			if (isStatus) {
				exam = new InfoExam();
				exam.setStatus(response.getBoolean("status"));
				try {
					exam.setContest(response.getString("contest"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				exam.setSubject(response.getString("subject"));
				exam.setSubsubject(response.getString("subsubject"));
				exam.setExam(response.getString("exam"));
				exam.setLength(response.getInt("length"));
				progressGenerator = new ProgressGenerator(TestActivity.this,
						subject, contest, subsubject, user,
						response.getString("exam"), response.getInt("length"));
				JSONObject timeRemainning = response
						.getJSONObject("timeRemaining");
				TimeTest tRemain = new TimeTest();
				tRemain.setD(timeRemainning.getInt("day"));
				tRemain.setH(timeRemainning.getInt("hour"));
				tRemain.setM(timeRemainning.getInt("minus"));
				tRemain.setS(timeRemainning.getInt("second"));
				if (response.getString("subject").toString().toLowerCase()
						.indexOf("test") != -1) {
					h = 2;
					m = 30;
					s = 0;
				} else {
					h = timeRemainning.getInt("hour");
					m = timeRemainning.getInt("minus");
					s = timeRemainning.getInt("second");
				}
				exam.setTimeRemaining(tRemain);
				JSONObject timeEnd = response.getJSONObject("timeEnd");
				TimeTest tEnd = new TimeTest();
				tEnd.setD(timeEnd.getInt("day"));
				tEnd.setH(timeEnd.getInt("hour"));
				tEnd.setM(timeEnd.getInt("minus"));
				tEnd.setS(timeEnd.getInt("second"));
				exam.setTimeEnd(tEnd);
				List<Part> list_Part = new ArrayList<Part>();
				JSONArray array_Part = response.getJSONArray("listQuested");
				part = new ArrayList<GalleryOwn>();
				for (int i = 0; i < array_Part.length(); i++) {

					JSONObject object_Part = array_Part.getJSONObject(i);
					Part p = new Part();
					p.setPart(object_Part.getString("part"));
					p.setIntro(object_Part.getBoolean("intro"));
					position = 0;
					if (i < 4) {
						if (object_Part.getBoolean("intro")) {
							GalleryOwn go = new GalleryOwn();
							go.setPart(i);
							go.setPosition(position);
							go.setText("Part 0" + (i + 1));
							part.add(go);
							position++;
						}
					}
					List<Question> groupQuestion = new ArrayList<Question>();
					JSONArray array_Group_Question = object_Part
							.getJSONArray("listQuested");

					for (int j = 0; j < array_Group_Question.length(); j++) {
						Question ques = new Question();
						JSONObject object_Group_Question = array_Group_Question
								.getJSONObject(j);
						ques.setId(object_Group_Question.getInt("id"));
						ques.setContent(object_Group_Question
								.getString("content"));
						ques.setLinkAudio(object_Group_Question
								.getString("audio"));
						ques.setLinkImage(object_Group_Question
								.getString("image"));
						ques.setSubject(object_Group_Question
								.getString("subject"));
						List<QuesContruct> question = new ArrayList<QuesContruct>();
						JSONArray array_Question = object_Group_Question
								.getJSONArray("quescontruct");
						for (int k = 0; k < array_Question.length(); k++) {
							GalleryOwn go1 = new GalleryOwn();
							go1.setPart(i);
							go1.setPosition(position);
							if (show_pos < 10) {
								go1.setText("Câu 0" + (show_pos));
							} else {
								go1.setText("Câu " + (show_pos));
							}
							part.add(go1);

							show_pos++;
							JSONObject object_Question = array_Question
									.getJSONObject(k);
							QuesContruct contruct = new QuesContruct();
							contruct.setId(object_Question.getInt("id"));
							contruct.setQuestion(object_Question
									.getString("question"));
							contruct.setAnsA(object_Question.getString("ansA"));
							contruct.setAnsB(object_Question.getString("ansB"));
							contruct.setAnsC(object_Question.getString("ansC"));
							contruct.setAnsD(object_Question.getString("ansD"));
							contruct.setCorrectAns(object_Question
									.getString("correctAns"));
							question.add(contruct);
						}
						position++;
						ques.setQuestion(question);
						groupQuestion.add(ques);
					}
					p.setGroupQuestion(groupQuestion);
					list_Part.add(p);
				}
				exam.setPart(list_Part);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return isStatus;
	}

	private class SetDataTask extends AsyncTask<JSONObject, Void, Boolean> {

		@Override
		protected Boolean doInBackground(JSONObject... params) {
			boolean status = getArrayJson(params[0]);
			return status;
		}

		@Override
		protected void onPostExecute(Boolean isStatus) {
			super.onPostExecute(isStatus);
			if (!isStatus) {
				isSubmit = true;
				onBackPressed();
				Toast.makeText(getApplication(), "Không thể lấy đề thi",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				start();
				main_progress.setVisibility(View.GONE);
				testting = new TesttingAdapterPart(TestActivity.this,
						exam.getPart(), mainPager);
				mainPager.setAdapter(testting);
				bt_Submit.setEnabled(true);
				mTrack.setAdapter(new GalleryAdapter(part));
				position = 1;
				if (exam.getPart().get(position_intro).isIntro()) {
					try {
						mediaPlayer.setDataSource(getApplicationContext(),
								Uri.parse(uri + audio_intro[position_intro]));
						mediaPlayer.prepareAsync();
					} catch (Exception e) {
						e.printStackTrace();
					}
					position_intro++;
				}
			}
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		System.out.println("check i : " + i + " - j = " + j);
		switch (Integer.parseInt(exam.getPart().get(i).getPart())) {
		case 3:
			int current3 = (int) (((float) mediaPlayer.getCurrentPosition() / length) * 100);
//			int pos3 = j;
//			if (j == exam.getPart().get(i).getGroupQuestion().size()) {
//				pos3 = j - 1;
//			}
//			int size3 = exam.getPart().get(i).getGroupQuestion().get(pos3)
//					.getQuestion().size();
			int base3 = (int) 100 / size;
			// System.out.println("current: " + current3 + "% - " + base3);
			if (ch != size) {
				int check = base3 * ch;
				if (current3 <= check + 10 && current3 > check) {
					mTrack.setSelection(position);
					position++;
					ch++;
				}
			}

			break;
		case 4:
			int current = (int) (((float) mediaPlayer.getCurrentPosition() / length) * 100);
//			int pos = j;
//			if (j == exam.getPart().get(i).getGroupQuestion().size()) {
//				pos = j - 1;
//			}
//			int size = exam.getPart().get(i).getGroupQuestion().get(pos)
//					.getQuestion().size();
			int base = (int) 100 / size;
			if (ch != size) {
				int check = base * ch;
				if (current <= check + 10 && current > check) {
					mTrack.setSelection(position);
					position++;
					ch++;
				}
			}

			break;

		default:
			break;
		}
	}

	private void stopPlaying() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		try {
			mediaPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.reset();
		}
		ch = 1;
		if (j < exam.getPart().get(i).getGroupQuestion().size()) {
			mainPager.setCurrentItem(i);
			JazzyViewPager jvp = (JazzyViewPager) mainPager.findViewFromObject(
					i).findViewById(R.id.main_pager);
			jvp.setCurrentItem(j + 1);
			size = exam.getPart().get(i).getGroupQuestion().get(j)
					.getQuestion().size();
			switch (Integer.parseInt(exam.getPart().get(i).getPart())) {
			case 1:
				num_ques.setText("Phần nghe - Câu " + (base1 + (j + 1))
						+ ".");
				mTrack.setSelection(position);
				position++;
				break;
			case 2:
				num_ques.setText("Phần nghe - Câu " + (base2 + (j + 1))
						+ ".");
				mTrack.setSelection(position);
				position++;
				break;
			case 3:
				int max = (j + 1) * size;
				int start3 = base3 + (max - size + 1);
				int end3 = base3 + max;
				String tmp3 = start3 + " - " + end3;
				num_ques.setText("Phần nghe - Câu " + tmp3 + ".");
				mTrack.setSelection(position);
				position++;
				break;
			case 4:
				int max4 = (j + 1) * size;
				int start4 = base4 + (max4 - size + 1);
				int end4 = base4 + max4;
				String tmp4 = start4 + " - " + end4;
				num_ques.setText("Phần nghe - Câu " + tmp4 + ".");
				mTrack.setSelection(position);
				position++;
				break;
			default:
				break;
			}
			try {
				mediaPlayer.setDataSource(Variable.host
						+ exam.getPart().get(i).getGroupQuestion().get(j)
								.getLinkAudio());
				mediaPlayer.prepareAsync();
				j++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (!exam.getPart().get(i + 1).isIntro()) {
				num_ques.setText("Phần đọc");
				mTrack.setSelection(position);
				mainPager.setCurrentItem(i + 1);
				try {
					mediaPlayer.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				mTrack.setSelection(position);
				position++;
				i++;
				j = 0;
				mainPager.setCurrentItem(i);
				JazzyViewPager jvp = (JazzyViewPager) mainPager
						.findViewFromObject(i).findViewById(R.id.main_pager);
				jvp.setCurrentItem(j);
				switch (Integer.parseInt(exam.getPart().get(i).getPart())) {
				case 1:
					num_ques.setText("Hướng dẫn Part I - Phần nghe.");
					break;
				case 2:
					num_ques.setText("Hướng dẫn Part II - Phần nghe.");
					break;
				case 3:
					num_ques.setText("Hướng dẫn Part III - Phần nghe.");
					break;
				case 4:
					num_ques.setText("Hướng dẫn Part IV - Phần nghe.");
					break;
				default:
					break;
				}
				System.out.println("test 1 i= " + i + " j= " + j + " size= ");
				try {
					mediaPlayer.setDataSource(getApplicationContext(),
							Uri.parse(uri + audio_intro[position_intro]));
					mediaPlayer.prepareAsync();
				} catch (Exception e) {
					e.printStackTrace();
				}
				position_intro++;
				
			}
		}

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mediaPlayer.start();
		length = mediaPlayer.getDuration();

	}

	@Override
	public void onComplete(int listening, int reading) {
		isSubmit = true;
		Toast.makeText(TestActivity.this, "OK", Toast.LENGTH_SHORT).show();
		bt_Submit.setText(listening + reading + "đ");
		Variable.choose_ans = new HashMap<Integer, String>();
		Variable.score_Listening = 0;
		Variable.score_Reading = 0;
		dialog = new Dialog(TestActivity.this);
		dialog.setContentView(R.layout.audio_ques_not_text);
		dialog.setTitle("Kết Quả");
		TableLayout tl = (TableLayout) dialog.findViewById(R.id.audio_not_text);
		tl.setStretchAllColumns(true);
		tl.setShrinkAllColumns(true);
		new LoadTask(tl).execute();
	}

	@Override
	public void onComplete(int score) {
		isSubmit = true;
		Toast.makeText(TestActivity.this, "OK", Toast.LENGTH_SHORT).show();
		bt_Submit.setText(score + "đ");
		Variable.choose_ans = new HashMap<Integer, String>();
		Variable.score_Listening = 0;
		Variable.score_Reading = 0;
		dialog = new Dialog(TestActivity.this);
		dialog.setContentView(R.layout.audio_ques_not_text);
		dialog.setTitle("Kết Quả");
		TableLayout tl = (TableLayout) dialog.findViewById(R.id.audio_not_text);
		tl.setStretchAllColumns(true);
		tl.setShrinkAllColumns(true);
		new LoadTask(tl).execute();

	}

	public String getTime() {
		s--;
		if (s < 0) {
			m--;
			if (m < 0) {
				h--;
				if (h < 0) {
					return "End time";
				}
				m = 59;
			}
			s = 59;
		}
		String h = "" + this.h;
		String m = "" + this.m;
		String s = "" + this.s;
		if (this.h < 10) {
			h = "0" + this.h;
		}
		if (this.m < 10) {
			m = "0" + this.m;
		}
		if (this.s < 10) {
			s = "0" + this.s;
		}
		String time = h + ":" + m + ":" + s;
		return time;
	}

	public void start() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isSubmit) {
					time_remaining.setText("Đã nộp bài");
				} else {
					String time = getTime();
					time_remaining.setText(time);
					if (time.equalsIgnoreCase("End time")) {
						try {
							stopPlaying();
						} catch (Exception e) {
							e.printStackTrace();
						}
						progressGenerator.start(bt_Submit);
						time_remaining.setText("Hết giờ");
					} else {
						handler.postDelayed(this, 1000);
					}
				}
			}
		}, 1000);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int part_pos = part.get(position).getPart();
		int child_pos = part.get(position).getPosition();
		mainPager.setCurrentItem(part_pos);
		JazzyViewPager jvp = (JazzyViewPager) mainPager.findViewFromObject(
				part_pos).findViewById(R.id.main_pager);
		jvp.setCurrentItem(child_pos);

	}

	private class LoadTask extends AsyncTask<Void, Void, Void> {
		private TableLayout tl;
		private List<TableRow> listTR = new ArrayList<TableRow>();
		private LayoutInflater mLayoutInflater;
		int start = 1;
		int check = 0;
		TableRow tr;

		public LoadTask(TableLayout tl) {
			mLayoutInflater = getLayoutInflater();
			tr = new TableRow(TestActivity.this);
			tr.setLayoutParams(new LayoutParams(
					TableRow.LayoutParams.MATCH_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT));
			this.tl = tl;
		}

		@Override
		protected Void doInBackground(Void... params) {
			for (Part p : exam.getPart()) {
				for (Question q : p.getGroupQuestion()) {
					for (QuesContruct c : q.getQuestion()) {
						View v1 = mLayoutInflater.inflate(
								R.layout.dialog_ans_not_text, null);
						TextView tv = (TextView) v1.findViewById(R.id.num_ques);
						tv.setText((start++) + ". ");
						CircleButton bt_ans_a = (CircleButton) v1
								.findViewById(R.id.choose_a);
						CircleButton bt_ans_b = (CircleButton) v1
								.findViewById(R.id.choose_b);
						CircleButton bt_ans_c = (CircleButton) v1
								.findViewById(R.id.choose_c);
						CircleButton bt_ans_d = (CircleButton) v1
								.findViewById(R.id.choose_d);
						if (Integer.parseInt(p.getPart()) != 2) {
							bt_ans_d.setVisibility(View.VISIBLE);
						}
						if (c.getChooseAns() != null) {
							String choose = c.getChooseAns();
							String correct = c.getCorrectAns();
							if (choose.equalsIgnoreCase(correct)) {
								if (correct.equalsIgnoreCase("A")) {
									bt_ans_a.setColor(Color
											.parseColor("#ff99cc00"));
									bt_ans_a.setImageResource(R.drawable.ic_action_tick);
								} else if (correct.equalsIgnoreCase("B")) {
									bt_ans_b.setColor(Color
											.parseColor("#ff99cc00"));
									bt_ans_b.setImageResource(R.drawable.ic_action_tick);
								} else if (correct.equalsIgnoreCase("C")) {
									bt_ans_c.setColor(Color
											.parseColor("#ff99cc00"));
									bt_ans_c.setImageResource(R.drawable.ic_action_tick);
								} else if (correct.equalsIgnoreCase("D")) {
									bt_ans_d.setColor(Color
											.parseColor("#ff99cc00"));
									bt_ans_d.setImageResource(R.drawable.ic_action_tick);
								}
							} else {
								if (correct.equalsIgnoreCase("A")) {
									bt_ans_a.setColor(Color
											.parseColor("#ff99cc00"));
								} else if (correct.equalsIgnoreCase("B")) {
									bt_ans_b.setColor(Color
											.parseColor("#ff99cc00"));
								} else if (correct.equalsIgnoreCase("C")) {
									bt_ans_c.setColor(Color
											.parseColor("#ff99cc00"));
								} else if (correct.equalsIgnoreCase("D")) {
									bt_ans_d.setColor(Color
											.parseColor("#ff99cc00"));
								}
								if (!choose.equalsIgnoreCase("")) {

									if (choose.equalsIgnoreCase("A")) {
										bt_ans_a.setColor(Color
												.parseColor("#ffff4500"));
										bt_ans_a.setImageResource(R.drawable.ic_action_x);
									} else if (choose.equalsIgnoreCase("B")) {
										bt_ans_b.setColor(Color
												.parseColor("#ffff4500"));
										bt_ans_b.setImageResource(R.drawable.ic_action_x);
									} else if (choose.equalsIgnoreCase("C")) {
										bt_ans_c.setColor(Color
												.parseColor("#ffff4500"));
										bt_ans_c.setImageResource(R.drawable.ic_action_x);
									} else {
										bt_ans_d.setColor(Color
												.parseColor("#ffff4500"));
										bt_ans_d.setImageResource(R.drawable.ic_action_x);
									}
								}

							}
							bt_ans_a.setEnabled(false);
							bt_ans_b.setEnabled(false);
							bt_ans_c.setEnabled(false);
							bt_ans_d.setEnabled(false);
						}
						// else {
						// int id = c.getId();
						// bt_ans_a.setOnClickListener(new ButtonClick(v1, id));
						// bt_ans_b.setOnClickListener(new ButtonClick(v1, id));
						// bt_ans_c.setOnClickListener(new ButtonClick(v1, id));
						// bt_ans_c.setOnClickListener(new ButtonClick(v1, id));
						//
						// if (Variable.choose_ans.get(id) != null) {
						// if (Variable.choose_ans.get(id)
						// .equalsIgnoreCase("A")) {
						// bt_ans_a.setColor(Color
						// .parseColor("#ff99cc00"));
						// } else if (Variable.choose_ans.get(id)
						// .equalsIgnoreCase("B")) {
						// bt_ans_b.setColor(Color
						// .parseColor("#ff99cc00"));
						// } else {
						// bt_ans_c.setColor(Color
						// .parseColor("#ff99cc00"));
						// }
						// }
						// }

						tr.addView(v1);
						check++;
						if (check >= 2) {
							listTR.add(tr);
							check = 0;
							tr = new TableRow(TestActivity.this);
							tr.setLayoutParams(new LayoutParams(
									TableRow.LayoutParams.MATCH_PARENT,
									TableRow.LayoutParams.WRAP_CONTENT));

						}
					}
				}
			}
			if (check != 0) {
				listTR.add(tr);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			for (TableRow tr : listTR) {
				tl.addView(tr);
			}
			dialog.show();
		}

	}
}
