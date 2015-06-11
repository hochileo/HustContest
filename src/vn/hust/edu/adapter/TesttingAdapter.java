package vn.hust.edu.adapter;

import java.util.ArrayList;
import java.util.List;

import vn.hust.edu.buttonchooselib.CircleButton;
import vn.hust.edu.control.ButtonClick;
import vn.hust.edu.control.OutlineContainer;
import vn.hust.edu.main.R;
import vn.hust.edu.model.QuesContruct;
import vn.hust.edu.model.Question;
import vn.hust.edu.variable.Variable;
import vn.hust.edu.view.ImageZoom;
import vn.hust.edu.view.JazzyViewPager;
import vn.hust.edu.view.SmartImageView;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TesttingAdapter extends PagerAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Question> groupQuestion;
	private boolean isIntro;
	private JazzyViewPager main_pager;
	private int size = 0;
	private Context mContext;
	private int base3 = 40;
	private int base4 = 70;
	private int base5 = 100;
	private int base6 = 140;
	private int base72 = 152;
	private int base73 = 154;
	private int base74 = 163;
	private int base75 = 175;
	private int part;
	private int[] inro_image = { R.drawable.about_part1,
			R.drawable.about_part2, R.drawable.about_part3,
			R.drawable.about_part4 };

	public TesttingAdapter(Context mContext, List<Question> groupQuestion,
			JazzyViewPager main_pager, int part, boolean isIntro) {

		this.groupQuestion = groupQuestion;
		this.mContext = mContext;
		try {
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.main_pager = main_pager;
		this.part = part;
		this.isIntro = isIntro;
		if (part == 2) {
			this.size = 1;
		} else {
			this.size = groupQuestion.size();
		}

		if (isIntro) {
			this.size = this.size + 1;
		}
	}

	public void update(ArrayList<Question> groupQuestion) {
		this.groupQuestion = groupQuestion;
		size = groupQuestion.size();
		this.notifyDataSetChanged();

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (isIntro) {
			return setTitle(position);
		} else {
			return setTitle(position + 1);
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int pos) {
		View convertView = null;
		int position = 0;
		System.out.println("check " + position + " - " + part + " - " + isIntro
				+ " - " + size);
		if (isIntro) {
			if (pos == 0) {
				convertView = mLayoutInflater.inflate(R.layout.intro, null);
				ImageZoom intro = (ImageZoom) convertView
						.findViewById(R.id.im_intro);
				intro.setImageResource(inro_image[part - 1]);
				intro.setMaxZoom(4f);
				container.addView(convertView);
				main_pager.setObjectForPosition(convertView, pos);
				return convertView;
			} else {
				position = pos - 1;
			}
		} else {
			position = pos;
		}
		if (!groupQuestion.get(position).getLinkImage().equalsIgnoreCase("")
				&& groupQuestion.get(position).getQuestion().get(0)
						.getQuestion().equalsIgnoreCase("")
				&& groupQuestion.get(position).getContent()
						.equalsIgnoreCase("")
				&& !groupQuestion.get(position).getLinkAudio()
						.equalsIgnoreCase("")) {
			convertView = mLayoutInflater.inflate(R.layout.picture_ques, null);
			convertView.setTag(position);
			SmartImageView siv = (SmartImageView) convertView
					.findViewById(R.id.pic_ques);
			siv.setImageUrl(Variable.host
					+ groupQuestion.get(position).getLinkImage(), siv);
			TextView num_ques = (TextView) convertView
					.findViewById(R.id.num_ques);
			num_ques.setText((position + 1) + ". ");

			CircleButton ans_a = (CircleButton) convertView
					.findViewById(R.id.choose_a);
			CircleButton ans_b = (CircleButton) convertView
					.findViewById(R.id.choose_b);
			CircleButton ans_c = (CircleButton) convertView
					.findViewById(R.id.choose_c);
			CircleButton ans_d = (CircleButton) convertView
					.findViewById(R.id.choose_d);
			if (groupQuestion.get(position).getQuestion().get(0).getChooseAns() != null) {
				String choose = groupQuestion.get(position).getQuestion()
						.get(0).getChooseAns();
				String correct = groupQuestion.get(position).getQuestion()
						.get(0).getCorrectAns();

				if (correct.equalsIgnoreCase("A")) {
					ans_a.setColor(Color.parseColor("#ff99cc00"));
				} else if (correct.equalsIgnoreCase("B")) {
					ans_b.setColor(Color.parseColor("#ff99cc00"));
				} else if (correct.equalsIgnoreCase("C")) {
					ans_c.setColor(Color.parseColor("#ff99cc00"));
				} else {
					ans_d.setColor(Color.parseColor("#ff99cc00"));
				}
				if (choose.equalsIgnoreCase(correct)) {
					if (correct.equalsIgnoreCase("A")) {
						ans_a.setColor(Color.parseColor("#ff99cc00"));
						ans_a.setImageResource(R.drawable.ic_action_tick);
					} else if (correct.equalsIgnoreCase("B")) {
						ans_b.setColor(Color.parseColor("#ff99cc00"));
						ans_b.setImageResource(R.drawable.ic_action_tick);
					} else if (correct.equalsIgnoreCase("C")) {
						ans_c.setColor(Color.parseColor("#ff99cc00"));
						ans_c.setImageResource(R.drawable.ic_action_tick);
					} else {
						ans_d.setColor(Color.parseColor("#ff99cc00"));
						ans_d.setImageResource(R.drawable.ic_action_tick);
					}
				} else {
					if (!choose.equalsIgnoreCase("")) {
						if (choose.equalsIgnoreCase("A")) {
							ans_a.setColor(Color.parseColor("#ffff4500"));
							ans_a.setImageResource(R.drawable.ic_action_x);
						} else if (choose.equalsIgnoreCase("B")) {
							ans_b.setColor(Color.parseColor("#ffff4500"));
							ans_b.setImageResource(R.drawable.ic_action_x);
						} else if (choose.equalsIgnoreCase("C")) {
							ans_c.setColor(Color.parseColor("#ffff4500"));
							ans_c.setImageResource(R.drawable.ic_action_x);
						} else {
							ans_d.setColor(Color.parseColor("#ffff4500"));
							ans_d.setImageResource(R.drawable.ic_action_x);
						}
					}
				}
				ans_a.setEnabled(false);
				ans_b.setEnabled(false);
				ans_c.setEnabled(false);
				ans_d.setEnabled(false);
			} else {
				int id = groupQuestion.get(position).getQuestion().get(0)
						.getId();
				ans_a.setOnClickListener(new ButtonClick(convertView, id));
				ans_b.setOnClickListener(new ButtonClick(convertView, id));
				ans_c.setOnClickListener(new ButtonClick(convertView, id));
				ans_d.setOnClickListener(new ButtonClick(convertView, id));
				if (Variable.choose_ans.get(id) != null) {
					if (Variable.choose_ans.get(id).equalsIgnoreCase("A")) {
						ans_a.setColor(Color.parseColor("#ff000000"));
						ans_a.setImageResource(R.drawable.ic_black);
					} else if (Variable.choose_ans.get(id)
							.equalsIgnoreCase("B")) {
						ans_b.setColor(Color.parseColor("#ff000000"));
						ans_b.setImageResource(R.drawable.ic_black);
					} else if (Variable.choose_ans.get(id)
							.equalsIgnoreCase("C")) {
						ans_c.setColor(Color.parseColor("#ff000000"));
						ans_c.setImageResource(R.drawable.ic_black);
					} else {
						ans_d.setColor(Color.parseColor("#ff000000"));
						ans_d.setImageResource(R.drawable.ic_black);
					}
				} else {
					System.out.println("ChooseAns null");
				}
			}
		} else if (groupQuestion.get(position).getLinkImage()
				.equalsIgnoreCase("")
				&& groupQuestion.get(position).getQuestion().get(0)
						.getQuestion().equalsIgnoreCase("")
				&& groupQuestion.get(position).getContent()
						.equalsIgnoreCase("")
				&& !groupQuestion.get(position).getLinkAudio()
						.equalsIgnoreCase("")) {
			convertView = mLayoutInflater.inflate(R.layout.audio_ques_not_text,
					null);
			TableLayout tl = (TableLayout) convertView
					.findViewById(R.id.audio_not_text);
			tl.setStretchAllColumns(true);
			tl.setShrinkAllColumns(true);
			new LoadTask(tl, false, 30, 11, groupQuestion.get(position)
					.getQuestion(), position + 1).execute();
		} else if (groupQuestion.get(position).getLinkImage()
				.equalsIgnoreCase("")
				&& !groupQuestion.get(position).getQuestion().get(0)
						.getQuestion().equalsIgnoreCase("")
				&& !groupQuestion.get(position).getContent()
						.equalsIgnoreCase("")
				&& groupQuestion.get(position).getLinkAudio()
						.equalsIgnoreCase("")) {
			String content_text = groupQuestion.get(position).getContent();
			convertView = mLayoutInflater.inflate(R.layout.ques_yep_text, null);
			TextView title = (TextView) convertView
					.findViewById(R.id.show_title_content);
			TextView content = (TextView) convertView
					.findViewById(R.id.show_all_content);
			title.setVisibility(View.VISIBLE);
			content.setVisibility(View.VISIBLE);
			int character = content_text.indexOf("-");
			title.setText(content_text.substring(1, character));
			content.setText(content_text.substring(character + 1,
					content_text.length()));
			TableLayout tl = (TableLayout) convertView
					.findViewById(R.id.ques_yep_text);
			tl.setStretchAllColumns(true);
			tl.setShrinkAllColumns(true);
			new LoadTask(tl, true, groupQuestion.get(position).getQuestion()
					.size(), 0, groupQuestion.get(position).getQuestion(),
					position + 1).execute();
		} else if (groupQuestion.get(position).getLinkImage()
				.equalsIgnoreCase("")
				&& !groupQuestion.get(position).getQuestion().get(0)
						.getQuestion().equalsIgnoreCase("")
				&& groupQuestion.get(position).getContent()
						.equalsIgnoreCase("")
				&& !groupQuestion.get(position).getLinkAudio()
						.equalsIgnoreCase("")) {
			convertView = mLayoutInflater.inflate(R.layout.ques_yep_text, null);
			TableLayout tl = (TableLayout) convertView
					.findViewById(R.id.ques_yep_text);
			tl.setStretchAllColumns(true);
			tl.setShrinkAllColumns(true);
			new LoadTask(tl, true, groupQuestion.get(position).getQuestion()
					.size(), 0, groupQuestion.get(position).getQuestion(),
					position + 1).execute();
		} else if (groupQuestion.get(position).getLinkImage()
				.equalsIgnoreCase("")
				&& !groupQuestion.get(position).getQuestion().get(0)
						.getQuestion().equalsIgnoreCase("")
				&& groupQuestion.get(position).getContent()
						.equalsIgnoreCase("")
				&& groupQuestion.get(position).getLinkAudio()
						.equalsIgnoreCase("")) {
			convertView = mLayoutInflater.inflate(R.layout.ques_yep_text, null);
			TableLayout tl = (TableLayout) convertView
					.findViewById(R.id.ques_yep_text);
			tl.setStretchAllColumns(true);
			tl.setShrinkAllColumns(true);
			new LoadTask(tl, true, groupQuestion.get(position).getQuestion()
					.size(), 0, groupQuestion.get(position).getQuestion(),
					position + 1).execute();
		} else {
			convertView = mLayoutInflater.inflate(
					R.layout.picture_yep_text_ques, null);
			TableLayout tl = (TableLayout) convertView
					.findViewById(R.id.pic_ques_yep_text);
			SmartImageView siv = (SmartImageView) convertView
					.findViewById(R.id.pic_ques);
			siv.setImageUrl(Variable.host
					+ groupQuestion.get(position).getLinkImage(), siv);
			tl.setStretchAllColumns(true);
			tl.setShrinkAllColumns(true);
			new LoadTask(tl, true, groupQuestion.get(position).getQuestion()
					.size(), 0, groupQuestion.get(position).getQuestion(),
					position + 1).execute();
		}

		((ViewPager) container).addView(convertView, 0);
		main_pager.setObjectForPosition(convertView, pos);
		return convertView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object obj) {
		((ViewPager) container).removeView(main_pager
				.findViewFromObject(position));
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		if (view instanceof OutlineContainer) {
			return ((OutlineContainer) view).getChildAt(0) == obj;
		} else {
			return view == obj;
		}
	}

	private class LoadTask extends AsyncTask<Void, Void, Void> {
		private TableLayout tl;
		private boolean isYepText;
		private List<TableRow> listTR = new ArrayList<TableRow>();
		private int sizefor;
		private int start;
		private int pos;
		private List<QuesContruct> listQuest;

		public LoadTask(TableLayout tl, boolean isYepText, int sizefor,
				int start, List<QuesContruct> listQuest, int pos) {
			this.tl = tl;
			this.isYepText = isYepText;
			this.sizefor = sizefor;
			this.start = start;
			this.listQuest = listQuest;
			this.pos = pos;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (isYepText) {
				for (int i = 0; i < sizefor; i++) {
					TableRow tr = new TableRow(mContext);
					tr.setLayoutParams(new LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT,
							TableRow.LayoutParams.WRAP_CONTENT));
					View v = mLayoutInflater.inflate(R.layout.ans_yep_text,
							null);
					boolean isQuesVisible;
					boolean isAVisible;
					boolean isBVisible;
					boolean isCVisible;
					boolean isDVisible;

					if (!listQuest.get(i).getQuestion().equalsIgnoreCase("")) {
						isQuesVisible = true;
					} else {
						isQuesVisible = false;
					}
					if (!listQuest.get(i).getAnsA().equalsIgnoreCase("")) {
						isAVisible = true;
					} else {
						isAVisible = false;
					}
					if (!listQuest.get(i).getAnsB().equalsIgnoreCase("")) {
						isBVisible = true;
					} else {
						isBVisible = false;
					}
					if (!listQuest.get(i).getAnsC().equalsIgnoreCase("")) {
						isCVisible = true;
					} else {
						isCVisible = false;
					}
					if (!listQuest.get(i).getAnsD().equalsIgnoreCase("")) {
						isDVisible = true;
					} else {
						isDVisible = false;
					}

					TextView ques = (TextView) v.findViewById(R.id.question);
					TextView ans_a = (TextView) v.findViewById(R.id.ans_a);
					TextView ans_b = (TextView) v.findViewById(R.id.ans_b);
					TextView ans_c = (TextView) v.findViewById(R.id.ans_c);
					TextView ans_d = (TextView) v.findViewById(R.id.ans_d);
					if (isQuesVisible) {
						ques.setText(listQuest.get(i).getQuestion());
					} else {
						ques.setVisibility(View.GONE);
					}
					if (isAVisible) {
						ans_a.setText(listQuest.get(i).getAnsA());
					} else {
						ans_a.setVisibility(View.GONE);
					}
					if (isBVisible) {
						ans_b.setText(listQuest.get(i).getAnsB());
					} else {
						ans_b.setVisibility(View.GONE);
					}
					if (isCVisible) {
						ans_c.setText(listQuest.get(i).getAnsC());
					} else {
						ans_c.setVisibility(View.GONE);
					}
					if (isDVisible) {
						ans_d.setText(listQuest.get(i).getAnsD());
					} else {
						ans_d.setVisibility(View.GONE);
					}

					CircleButton bt_ans_a = (CircleButton) v
							.findViewById(R.id.choose_a);
					CircleButton bt_ans_b = (CircleButton) v
							.findViewById(R.id.choose_b);
					CircleButton bt_ans_c = (CircleButton) v
							.findViewById(R.id.choose_c);
					CircleButton bt_ans_d = (CircleButton) v
							.findViewById(R.id.choose_d);
					if (!isAVisible) {

						bt_ans_a.setVisibility(View.GONE);
					}
					if (!isBVisible) {

						bt_ans_b.setVisibility(View.GONE);
					}
					if (!isCVisible) {

						bt_ans_c.setVisibility(View.GONE);
					}
					if (!isDVisible) {

						bt_ans_d.setVisibility(View.GONE);
					}

					if (listQuest.get(i).getChooseAns() != null) {
						String choose = listQuest.get(i).getChooseAns();
						String correct = listQuest.get(i).getCorrectAns();

						if (correct.equalsIgnoreCase("A")) {
							bt_ans_a.setColor(Color.parseColor("#ff99cc00"));
						} else if (correct.equalsIgnoreCase("B")) {
							bt_ans_b.setColor(Color.parseColor("#ff99cc00"));
						} else if (correct.equalsIgnoreCase("C")) {
							bt_ans_c.setColor(Color.parseColor("#ff99cc00"));
						} else {
							bt_ans_d.setColor(Color.parseColor("#ff99cc00"));
						}
						if (choose.equalsIgnoreCase(correct)) {
							if (correct.equalsIgnoreCase("A")) {
								bt_ans_a.setColor(Color.parseColor("#ff99cc00"));
								bt_ans_a.setImageResource(R.drawable.ic_action_tick);
							} else if (correct.equalsIgnoreCase("B")) {
								bt_ans_b.setColor(Color.parseColor("#ff99cc00"));
								bt_ans_b.setImageResource(R.drawable.ic_action_tick);
							} else if (correct.equalsIgnoreCase("C")) {
								bt_ans_c.setColor(Color.parseColor("#ff99cc00"));
								bt_ans_c.setImageResource(R.drawable.ic_action_tick);
							} else {
								bt_ans_d.setColor(Color.parseColor("#ff99cc00"));
								bt_ans_d.setImageResource(R.drawable.ic_action_tick);
							}
						}else {
							if (!choose.equalsIgnoreCase("")) {
								if (choose.equalsIgnoreCase("A")) {
									bt_ans_a.setColor(Color.parseColor("#ffff4500"));
									bt_ans_a.setImageResource(R.drawable.ic_action_x);
								} else if (choose.equalsIgnoreCase("B")) {
									bt_ans_b.setColor(Color.parseColor("#ffff4500"));
									bt_ans_b.setImageResource(R.drawable.ic_action_x);
								} else if (choose.equalsIgnoreCase("C")) {
									bt_ans_c.setColor(Color.parseColor("#ffff4500"));
									bt_ans_c.setImageResource(R.drawable.ic_action_x);
								} else {
									bt_ans_d.setColor(Color.parseColor("#ffff4500"));
									bt_ans_d.setImageResource(R.drawable.ic_action_x);
								}
							}
						}
						bt_ans_a.setEnabled(false);
						bt_ans_b.setEnabled(false);
						bt_ans_c.setEnabled(false);
						bt_ans_d.setEnabled(false);
					} else {
						int id = listQuest.get(i).getId();
						bt_ans_a.setOnClickListener(new ButtonClick(v, id));
						bt_ans_b.setOnClickListener(new ButtonClick(v, id));
						bt_ans_c.setOnClickListener(new ButtonClick(v, id));
						bt_ans_d.setOnClickListener(new ButtonClick(v, id));

						if (Variable.choose_ans.get(id) != null) {
							if (Variable.choose_ans.get(id).equalsIgnoreCase(
									"A")) {
								bt_ans_a.setColor(Color.parseColor("#000000"));
								bt_ans_a.setImageResource(R.drawable.ic_black);
							} else if (Variable.choose_ans.get(id)
									.equalsIgnoreCase("B")) {
								bt_ans_b.setColor(Color.parseColor("#000000"));
								bt_ans_b.setImageResource(R.drawable.ic_black);
							} else if (Variable.choose_ans.get(id)
									.equalsIgnoreCase("C")) {
								bt_ans_c.setColor(Color.parseColor("#ff000000"));
								bt_ans_c.setImageResource(R.drawable.ic_black);
							} else {
								bt_ans_d.setColor(Color.parseColor("#ff000000"));
								bt_ans_d.setImageResource(R.drawable.ic_black);
							}
						}
					}
					int size = groupQuestion.get(pos - 1).getQuestion().size();
					int max = pos * size;
					switch (part) {
					case 0:
						ques.setText("Câu " + pos + ". "
								+ listQuest.get(i).getQuestion());
						break;
					case 3:
						ques.setText((base3 + (max - size + (i + 1))) + ". "
								+ listQuest.get(i).getQuestion());
						break;
					case 4:
						ques.setText((base4 + (max - size + (i + 1))) + ". "
								+ listQuest.get(i).getQuestion());

						break;
					case 5:
						ques.setText((base5 + (max - size + (i + 1))) + ". "
								+ listQuest.get(i).getQuestion());

						break;
					case 6:
						ques.setText((base6 + (max - size + (i + 1))) + ". "
								+ listQuest.get(i).getQuestion());

						break;
					case 7:
						if (pos > 1 && pos < 5) {
							max = (pos - 1) * size;
							ques.setText((base73 + (max - size + (i + 1)))
									+ ". " + listQuest.get(i).getQuestion());
						} else if (pos >= 5 && pos < 8) {
							max = (pos - 4) * size;
							ques.setText((base74 + (max - size + (i + 1)))
									+ ". " + listQuest.get(i).getQuestion());
						} else if (pos >= 8 && pos <= 12) {
							max = (pos - 7) * size;
							ques.setText((base75 + (max - size + (i + 1)))
									+ ". " + listQuest.get(i).getQuestion());
						} else {
							max = pos * size;
							ques.setText((base72 + (max - size + (i + 1)))
									+ ". " + listQuest.get(i).getQuestion());
						}
						break;

					default:
						break;
					}
					tr.addView(v);
					listTR.add(tr);
				}
			} else {
				for (int i = 0; i < sizefor; i = i + 2) {
					TableRow tr = new TableRow(mContext);
					tr.setLayoutParams(new LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT,
							TableRow.LayoutParams.WRAP_CONTENT));

					View v1 = mLayoutInflater.inflate(
							R.layout.audio_ans_not_text, null);
					TextView tv = (TextView) v1.findViewById(R.id.num_ques);
					tv.setText((start++) + ". ");
					CircleButton bt_ans_a = (CircleButton) v1
							.findViewById(R.id.choose_a);
					CircleButton bt_ans_b = (CircleButton) v1
							.findViewById(R.id.choose_b);
					CircleButton bt_ans_c = (CircleButton) v1
							.findViewById(R.id.choose_c);
					if (groupQuestion.get(i).getQuestion().get(0)
							.getChooseAns() != null) {
						String choose = groupQuestion.get(i).getQuestion()
								.get(0).getChooseAns();
						String correct = groupQuestion.get(i).getQuestion()
								.get(0).getCorrectAns();

						if (correct.equalsIgnoreCase("A")) {
							bt_ans_a.setColor(Color.parseColor("#ff99cc00"));
						} else if (correct.equalsIgnoreCase("B")) {
							bt_ans_b.setColor(Color.parseColor("#ff99cc00"));
						} else {
							bt_ans_c.setColor(Color.parseColor("#ff99cc00"));
						}
						if (choose.equalsIgnoreCase(correct)) {
							if (correct.equalsIgnoreCase("A")) {
								bt_ans_a.setColor(Color.parseColor("#ff99cc00"));
								bt_ans_a.setImageResource(R.drawable.ic_action_tick);
							} else if (correct.equalsIgnoreCase("B")) {
								bt_ans_b.setColor(Color.parseColor("#ff99cc00"));
								bt_ans_b.setImageResource(R.drawable.ic_action_tick);
							} else {
								bt_ans_c.setColor(Color.parseColor("#ff99cc00"));
								bt_ans_c.setImageResource(R.drawable.ic_action_tick);
							}
						}else {
							if (!choose.equalsIgnoreCase("")) {
								if (choose.equalsIgnoreCase("A")) {
									bt_ans_a.setColor(Color.parseColor("#ffff4500"));
									bt_ans_a.setImageResource(R.drawable.ic_action_x);
								} else if (choose.equalsIgnoreCase("B")) {
									bt_ans_b.setColor(Color.parseColor("#ffff4500"));
									bt_ans_b.setImageResource(R.drawable.ic_action_x);
								} else  {
									bt_ans_c.setColor(Color.parseColor("#ffff4500"));
									bt_ans_c.setImageResource(R.drawable.ic_action_x);
								} 
							}
						}
						bt_ans_a.setEnabled(false);
						bt_ans_b.setEnabled(false);
						bt_ans_c.setEnabled(false);
					} else {
						int id = groupQuestion.get(i).getQuestion().get(0)
								.getId();
						bt_ans_a.setOnClickListener(new ButtonClick(v1, id));
						bt_ans_b.setOnClickListener(new ButtonClick(v1, id));
						bt_ans_c.setOnClickListener(new ButtonClick(v1, id));

						if (Variable.choose_ans.get(id) != null) {
							if (Variable.choose_ans.get(id).equalsIgnoreCase(
									"A")) {
								bt_ans_a.setColor(Color.parseColor("#000000"));
								bt_ans_a.setImageResource(R.drawable.ic_black);
							} else if (Variable.choose_ans.get(id)
									.equalsIgnoreCase("B")) {
								bt_ans_b.setColor(Color.parseColor("#000000"));
								bt_ans_b.setImageResource(R.drawable.ic_black);
							} else {
								bt_ans_c.setColor(Color.parseColor("#000000"));
								bt_ans_c.setImageResource(R.drawable.ic_black);
							}
						}
					}
					View v2 = mLayoutInflater.inflate(
							R.layout.audio_ans_not_text, null);
					TextView tv1 = (TextView) v2.findViewById(R.id.num_ques);
					tv1.setText((start++) + ". ");
					CircleButton bt_ans_a1 = (CircleButton) v2
							.findViewById(R.id.choose_a);
					CircleButton bt_ans_b1 = (CircleButton) v2
							.findViewById(R.id.choose_b);
					CircleButton bt_ans_c1 = (CircleButton) v2
							.findViewById(R.id.choose_c);
					if (groupQuestion.get(i + 1).getQuestion().get(0)
							.getChooseAns() != null) {
						String choose = groupQuestion.get(i + 1).getQuestion()
								.get(0).getChooseAns();
						String correct = groupQuestion.get(i + 1).getQuestion()
								.get(0).getCorrectAns();

						if (correct.equalsIgnoreCase("A")) {
							bt_ans_a1.setColor(Color.parseColor("#ff99cc00"));
						} else if (correct.equalsIgnoreCase("B")) {
							bt_ans_b1.setColor(Color.parseColor("#ff99cc00"));
						} else {
							bt_ans_c1.setColor(Color.parseColor("#ff99cc00"));
						}
						if (choose.equalsIgnoreCase(correct)) {
							if (correct.equalsIgnoreCase("A")) {
								bt_ans_a1.setColor(Color
										.parseColor("#ff99cc00"));
								bt_ans_a1
										.setImageResource(R.drawable.ic_action_tick);
							} else if (correct.equalsIgnoreCase("B")) {
								bt_ans_b1.setColor(Color
										.parseColor("#ff99cc00"));
								bt_ans_b1
										.setImageResource(R.drawable.ic_action_tick);
							} else {
								bt_ans_c1.setColor(Color
										.parseColor("#ff99cc00"));
								bt_ans_c1
										.setImageResource(R.drawable.ic_action_tick);
							}
						}else {
							if (!choose.equalsIgnoreCase("")) {
								if (choose.equalsIgnoreCase("A")) {
									bt_ans_a.setColor(Color.parseColor("#ffff4500"));
									bt_ans_a.setImageResource(R.drawable.ic_action_x);
								} else if (choose.equalsIgnoreCase("B")) {
									bt_ans_b.setColor(Color.parseColor("#ffff4500"));
									bt_ans_b.setImageResource(R.drawable.ic_action_x);
								} else  {
									bt_ans_c.setColor(Color.parseColor("#ffff4500"));
									bt_ans_c.setImageResource(R.drawable.ic_action_x);
								} 
							}
						}
						bt_ans_a1.setEnabled(false);
						bt_ans_b1.setEnabled(false);
						bt_ans_c1.setEnabled(false);
					} else {
						int id1 = groupQuestion.get(i + 1).getQuestion().get(0)
								.getId();
						bt_ans_a1.setOnClickListener(new ButtonClick(v2, id1));
						bt_ans_b1.setOnClickListener(new ButtonClick(v2, id1));
						bt_ans_c1.setOnClickListener(new ButtonClick(v2, id1));

						if (Variable.choose_ans.get(id1) != null) {
							if (Variable.choose_ans.get(id1).equalsIgnoreCase(
									"A")) {
								bt_ans_a1.setColor(Color.parseColor("#000000"));
								bt_ans_a1.setImageResource(R.drawable.ic_black);
							} else if (Variable.choose_ans.get(id1)
									.equalsIgnoreCase("B")) {
								bt_ans_b1.setColor(Color.parseColor("#000000"));
								bt_ans_b1.setImageResource(R.drawable.ic_black);
							} else {
								bt_ans_c1.setColor(Color.parseColor("#000000"));
								bt_ans_c1.setImageResource(R.drawable.ic_black);
							}
						}
					}
					tr.addView(v1);
					tr.addView(v2);

					listTR.add(tr);
				}
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
		}

	}

	private String setTitle(int pos) {
		if (pos == 0) {
			return "Intro Part" + part;
		}
		int size = groupQuestion.get(pos - 1).getQuestion().size();
		int max = pos * size;
		switch (part) {
		case 1:
			return "Câu " + pos;
		case 2:
			return "Câu 11 - 40";
		case 3:
			int start3 = base3 + (max - size + 1);
			int end3 = base3 + max;
			String tmp3 = "Câu " + start3 + " - " + end3;
			return tmp3;
		case 4:
			int start4 = base4 + (max - size + 1);
			int end4 = base4 + max;
			String tmp4 = "Câu " + start4 + " - " + end4;
			return tmp4;
		case 5:
			int start5 = base5 + (max - size + 1);
			int end5 = base5 + max;
			String tmp5 = "Câu " + start5 + " - " + end5;
			return tmp5;
		case 6:
			int start6 = base6 + (max - size + 1);
			int end6 = base6 + max;
			String tmp6 = "Câu " + start6 + " - " + end6;
			return tmp6;
		case 7:
			int max7 = 0;
			String tmp7 = "";
			if (pos > 1 && pos < 5) {
				max7 = (pos - 1) * size;
				int start7 = base73 + (max7 - size + 1);
				int end7 = base73 + max7;
				tmp7 = "Câu " + start7 + " - " + end7;
			} else if (pos >= 5 && pos < 8) {
				max7 = (pos - 4) * size;
				int start7 = base74 + (max7 - size + 1);
				int end7 = base74 + max7;
				tmp7 = "Câu " + start7 + " - " + end7;
			} else if (pos >= 8 && pos <= this.size) {
				max7 = (pos - 7) * size;
				int start7 = base75 + (max7 - size + 1);
				int end7 = base75 + max7;
				tmp7 = "Câu " + start7 + " - " + end7;
			} else {
				max7 = pos * size;
				int start7 = base72 + (max7 - size + 1);
				int end7 = base72 + max7;
				tmp7 = "Câu " + start7 + " - " + end7;
			}
			return tmp7;

		default:
			return "";
		}
	}

}