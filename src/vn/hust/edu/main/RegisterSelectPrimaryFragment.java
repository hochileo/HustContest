package vn.hust.edu.main;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.widgetcustomlib.AbstractWheel;
import com.example.widgetcustomlib.ArrayWheelAdapter;
import com.example.widgetcustomlib.OnWheelChangedListener;
import com.example.widgetcustomlib.OnWheelScrollListener;

public class RegisterSelectPrimaryFragment extends Fragment {
	// public AbstractWheel work;
	public AbstractWheel instutide;
	public AbstractWheel department;
	public AbstractWheel classt;
	private boolean scrolling = false;
	private int mActiveDepartment[];
	private int mActiveClass[];
	private int mActiveInstutide = 0;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.register_select_primary,
				container, false);
		rootView.setBackgroundColor(0xff99cc00);
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

		mActiveDepartment = new int[RegisterActivity.instutide.size()];
		mActiveClass = new int[RegisterActivity.instutide.size()];
		for (int i = 0; i < mActiveDepartment.length; i++) {
			mActiveDepartment[i] = 1;
			mActiveClass[i] = 1;
			i++;
		}
		//
		// work = (AbstractWheel) rootView.findViewById(R.id.work);
		// work.setVisibleItems(3);
		//
		// adapterWork(work, RegisterActivity.works);
		// RegisterActivity.user.setWork(RegisterActivity.worksID.get(work
		// .getCurrentItem()));
		// // work.setCyclic(true);

		instutide = (AbstractWheel) rootView.findViewById(R.id.instutide);
		instutide.setVisibleItems(3);
		adapterWork(instutide, RegisterActivity.instutide);
		RegisterActivity.user.setInstutide(RegisterActivity.instutides.get(
				instutide.getCurrentItem()).getId());
		department = (AbstractWheel) rootView.findViewById(R.id.department);
		department.setVisibleItems(3);
		classt = (AbstractWheel) rootView.findViewById(R.id.classt);
		classt.setVisibleItems(3);
		if (RegisterActivity.departments.get(RegisterActivity.instutide
				.get(mActiveInstutide)) != null
				&& RegisterActivity.departments.get(
						RegisterActivity.instutide.get(mActiveInstutide))
						.size() > 0) {
			adapterWork(department,
					RegisterActivity.departments.get(RegisterActivity.instutide
							.get(mActiveInstutide)));
			RegisterActivity.user.setDepartment(RegisterActivity.instutides
					.get(mActiveInstutide).getDepartments()
					.get(department.getCurrentItem()).getId());
		} else {
			department.setVisibility(View.INVISIBLE);
		}

		if (RegisterActivity.clasts.get(RegisterActivity.instutide
				.get(mActiveInstutide)) != null
				&& RegisterActivity.clasts.get(
						RegisterActivity.instutide.get(mActiveInstutide))
						.size() > 0) {
			adapterWork(classt,
					RegisterActivity.clasts.get(RegisterActivity.instutide
							.get(mActiveInstutide)));
			RegisterActivity.user.setClassst(RegisterActivity.instutides
					.get(mActiveInstutide).getClasssts()
					.get(classt.getCurrentItem()).getId());
		} else {
			classt.setVisibility(View.INVISIBLE);
		}
		// work.addChangingListener(new OnWheelChangedListener() {
		// public void onChanged(AbstractWheel wheel, int oldValue,
		// int newValue) {
		// RegisterActivity.user.setWork(RegisterActivity.worksID
		// .get(newValue));
		// if (RegisterActivity.works.get(newValue).equalsIgnoreCase(
		// "Giảng viên")
		// || RegisterActivity.works.get(newValue)
		// .equalsIgnoreCase("Sinh viên")) {
		// if (RegisterActivity.works.get(newValue).equalsIgnoreCase(
		// "Sinh viên")) {
		// if (RegisterActivity.clasts
		// .get(RegisterActivity.instutide
		// .get(mActiveInstutide)) != null
		// && RegisterActivity.clasts.get(
		// RegisterActivity.instutide
		// .get(mActiveInstutide)).size() > 0) {
		// classt.setVisibility(View.VISIBLE);
		// adapterWork(classt, RegisterActivity.clasts
		// .get(RegisterActivity.instutide
		// .get(mActiveInstutide)));
		// RegisterActivity.user
		// .setClassst(RegisterActivity.instutides
		// .get(mActiveInstutide)
		// .getClasssts()
		// .get(classt.getCurrentItem())
		// .getId());
		// } else {
		// classt.setVisibility(View.INVISIBLE);
		// RegisterActivity.user.setClassst("");
		// }
		// } else {
		// classt.setVisibility(View.INVISIBLE);
		// RegisterActivity.user.setClassst("");
		// }
		// instutide.setVisibility(View.VISIBLE);
		// RegisterActivity.user
		// .setInstutide(RegisterActivity.instutides.get(
		// instutide.getCurrentItem()).getId());
		// if (RegisterActivity.departments
		// .get(RegisterActivity.instutide
		// .get(mActiveInstutide)) != null
		// && RegisterActivity.departments.get(
		// RegisterActivity.instutide
		// .get(mActiveInstutide)).size() > 0) {
		// department.setVisibility(View.VISIBLE);
		// adapterWork(department, RegisterActivity.departments
		// .get(RegisterActivity.instutide
		// .get(mActiveInstutide)));
		// RegisterActivity.user
		// .setDepartment(RegisterActivity.instutides
		// .get(mActiveInstutide).getDepartments()
		// .get(department.getCurrentItem())
		// .getId());
		// } else {
		// department.setVisibility(View.INVISIBLE);
		// RegisterActivity.user.setDepartment("");
		// }
		// } else {
		// instutide.setVisibility(View.INVISIBLE);
		// department.setVisibility(View.INVISIBLE);
		// classt.setVisibility(View.INVISIBLE);
		// RegisterActivity.user.setInstutide("");
		// RegisterActivity.user.setClassst("");
		// RegisterActivity.user.setDepartment("");
		//
		// }
		// }
		// });

		department.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				if (!scrolling) {
					RegisterActivity.user
							.setDepartment(RegisterActivity.instutides
									.get(mActiveInstutide).getDepartments()
									.get(newValue).getId());
					mActiveDepartment[mActiveInstutide] = newValue;

				}
			}
		});
		classt.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				if (!scrolling) {
					RegisterActivity.user
							.setClassst(RegisterActivity.instutides
									.get(mActiveInstutide).getClasssts()
									.get(newValue).getId());
					mActiveClass[mActiveInstutide] = newValue;

				}
			}
		});

		instutide.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue,
					int newValue) {
				if (!scrolling) {
					mActiveInstutide = newValue;
					updateSelecter(department, RegisterActivity.departments,
							newValue);
					updateSelecter(classt, RegisterActivity.clasts, newValue);

				}
			}
		});

		instutide.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				mActiveInstutide = instutide.getCurrentItem();
				updateSelecter(department, RegisterActivity.departments,
						instutide.getCurrentItem());
				updateSelecter(classt, RegisterActivity.clasts,
						instutide.getCurrentItem());
				RegisterActivity.user.setInstutide(RegisterActivity.instutides
						.get(instutide.getCurrentItem()).getId());
			}
		});
		return rootView;
	}

	private void updateSelecter(AbstractWheel wheel,
			HashMap<String, List<String>> items, int index) {
		// mActiveInstutide = index;
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				getActivity(), items.get(RegisterActivity.instutide.get(index)));
		adapter.setTextSize(16);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(mActiveDepartment[index]);
	}

	private void adapterWork(AbstractWheel wheel, List<String> items) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				getActivity(), items);
		adapter.setTextSize(16);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(0);
	}

}
