package vn.hust.edu.adapter;

import java.util.List;

import vn.hust.edu.control.OutlineContainer;
import vn.hust.edu.main.R;
import vn.hust.edu.model.Part;
import vn.hust.edu.view.JazzyViewPager;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TesttingAdapterPart extends PagerAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Part> part;
	private JazzyViewPager main_pager;
	private int size = 0;
	private Context mContext;

	public TesttingAdapterPart(Context mContext, List<Part> part,
			JazzyViewPager main_pager) {
		this.part = part;
		this.mContext = mContext;
		try {
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.main_pager = main_pager;
		this.size = part.size();
	}

	public void update(List<Part> part) {
		this.part = part;
		size = part.size();
		this.notifyDataSetChanged();

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Part " + (position + 1);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View convertView = mLayoutInflater
				.inflate(R.layout.testting_part, null);
		JazzyViewPager child_Pager = (JazzyViewPager) convertView
				.findViewById(R.id.main_pager);
		// child_Pager.setOffscreenPageLimit(30);
		TesttingAdapter testting = new TesttingAdapter(mContext, part.get(
				position).getGroupQuestion(), child_Pager,
				Integer.parseInt(part.get(position).getPart()), part.get(
						position).isIntro());
		child_Pager.setAdapter(testting);

		((ViewPager) container).addView(convertView, 0);
		main_pager.setObjectForPosition(convertView, position);
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

}