package vn.hust.edu.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.hust.edu.image.SmartImage;
import vn.hust.edu.image.SmartImageTask;
import vn.hust.edu.image.WebImage;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SmartImageView extends ImageView {
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;

	public SmartImageView(Context context) {
		super(context);
	}

	public SmartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// Helpers to set image by URL
		public void setImageUrl(String url, final SmartImageView siv) {
			setImage(new WebImage(url), siv);
		}

		public void setImageUrl(String url,
				SmartImageTask.OnCompleteListener completeListener,
				final ProgressBar pb, final SmartImageView siv) {
			setImage(new WebImage(url), completeListener, siv);
		}

		public void setImageUrl(String url, final Integer fallbackResource,
				final ProgressBar pb, final SmartImageView siv) {
			setImage(new WebImage(url), fallbackResource, siv);
		}

		public void setImageUrl(String url, final Integer fallbackResource,
				SmartImageTask.OnCompleteListener completeListener,
				final ProgressBar pb, final SmartImageView siv) {
			setImage(new WebImage(url), fallbackResource, completeListener, siv);
		}

		public void setImageUrl(String url, final Integer fallbackResource,
				final Integer loadingResource, final SmartImageView siv) {
			setImage(new WebImage(url), fallbackResource, loadingResource, siv);
		}

		public void setImageUrl(String url, final Integer fallbackResource,
				final Integer loadingResource,
				SmartImageTask.OnCompleteListener completeListener,
				final SmartImageView siv) {
			setImage(new WebImage(url), fallbackResource, loadingResource,
					completeListener, siv);
		}

		// Helpers to set image by contact address book id
		// public void setImageContact(long contactId) {
		// setImage(new ContactImage(contactId));
		// }
		//
		// public void setImageContact(long contactId, final Integer
		// fallbackResource) {
		// setImage(new ContactImage(contactId), fallbackResource);
		// }
		//
		// public void setImageContact(long contactId, final Integer
		// fallbackResource, final Integer loadingResource) {
		// setImage(new ContactImage(contactId), fallbackResource,
		// fallbackResource);
		// }

		// Set image using SmartImage object
		public void setImage(final SmartImage image, final SmartImageView siv) {
			setImage(image, null, null, null, siv);
		}

		public void setImage(final SmartImage image,
				final SmartImageTask.OnCompleteListener completeListener,
				final SmartImageView siv) {
			setImage(image, null, null, completeListener, siv);
		}

		public void setImage(final SmartImage image,
				final Integer fallbackResource, final SmartImageView siv) {
			setImage(image, fallbackResource, fallbackResource, null, siv);
		}

		public void setImage(final SmartImage image,
				final Integer fallbackResource,
				SmartImageTask.OnCompleteListener completeListener,
				final SmartImageView siv) {
			setImage(image, fallbackResource, fallbackResource, completeListener,
					siv);
		}

		public void setImage(final SmartImage image,
				final Integer fallbackResource, final Integer loadingResource,
				final SmartImageView siv) {
			setImage(image, fallbackResource, loadingResource, null, siv);
		}

		public void setImage(final SmartImage image,
				final Integer fallbackResource, final Integer loadingResource,
				final SmartImageTask.OnCompleteListener completeListener,
				final SmartImageView siv) {
			// Set a loading resource
			if (loadingResource != null) {
				setImageResource(loadingResource);
			}

			// Cancel any existing tasks for this image view
			if (currentTask != null) {
				currentTask.cancel();
				currentTask = null;
			}

			// Set up the new task
			currentTask = new SmartImageTask(getContext(), image);
			currentTask
					.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
						@Override
						public void onComplete(Bitmap bitmap) {
							if (bitmap != null) {
								setImageBitmap(bitmap);
								siv.setVisibility(View.VISIBLE);
							} else {
								if (fallbackResource != null) {
									setImageResource(fallbackResource);
								}
							}
							siv.setVisibility(View.VISIBLE);

							if (completeListener != null) {
								completeListener.onComplete(bitmap);
							}
						}
					});

			// Run the task in a threadpool
			threadPool.execute(currentTask);
		}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}
}