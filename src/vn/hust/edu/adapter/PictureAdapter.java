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
import android.widget.ImageView;

public class PictureAdapter extends PagerAdapter {
	private LayoutInflater mLayoutInflater;
	private JazzyViewPager main_pager;
	private int size = 0;
	private int data[] = {R.drawable.xla,R.drawable.ctdlgt, R.drawable.ktlt,
			R.drawable.web, R.drawable.trr,
			R.drawable.ttnt };

	public PictureAdapter(Context mContext, JazzyViewPager main_pager) {
		try {
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.main_pager = main_pager;
		this.size = data.length;
	}

	public void update(List<Part> part) {
		size = data.length;
		this.notifyDataSetChanged();

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View convertView = mLayoutInflater.inflate(R.layout.picture, null);
		ImageView imageZoom = (ImageView) convertView
				.findViewById(R.id.im_intro);
		// child_Pager.setOffscreenPageLimit(30);
		imageZoom.setImageResource(data[position]);
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