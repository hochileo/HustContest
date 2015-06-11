/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package vn.hust.edu.adapter;

import java.util.List;

import vn.hust.edu.gallerylib.FancyCoverFlow;
import vn.hust.edu.gallerylib.FancyCoverFlowAdapter;
import vn.hust.edu.model.GalleryOwn;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GalleryAdapter extends FancyCoverFlowAdapter {
	private List<GalleryOwn> part;
	private int size;

	public GalleryAdapter() {

	}

	public GalleryAdapter(List<GalleryOwn> part) {
		this.part = part;
		this.size = part.size();
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
		TextView textView = null;
		if (reuseableView != null) {
			textView = (TextView) reuseableView;

		} else {
			textView = new TextView(viewGroup.getContext());
			textView.setLayoutParams(new FancyCoverFlow.LayoutParams(200,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			textView.setTextColor(Color.parseColor("#FFFFFF"));
			textView.setPadding(10, 0, 10, 0);
			textView.setGravity(Gravity.CENTER);
		}
		textView.setText(this.getItem(i));
		return textView;
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return part.get(position).getText();
	}
}
