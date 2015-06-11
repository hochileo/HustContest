package vn.hust.edu.main;

import vn.hust.edu.variable.LoadPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.widgetcustomlib.FloatingEditText;

public class RegisterInputNotPrimaryFragment extends Fragment {
	public FloatingEditText name;
	public FloatingEditText phone;
	public FloatingEditText address;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.register_input_not_primary,
				container, false);
		rootView.setBackgroundColor(0xffaa66cc);
		name = (FloatingEditText) rootView.findViewById(R.id.editName);
		phone = (FloatingEditText) rootView.findViewById(R.id.editPhone);
		address = (FloatingEditText) rootView.findViewById(R.id.editAddress);
		LoadPreferences lp = new LoadPreferences(getActivity());
		if (!RegisterActivity.isNew) {
			name.setText(lp.loadString("name"));
			phone.setText(lp.loadString("phone"));
			address.setText(lp.loadString("address"));
		}
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
		return rootView;
	}


}
