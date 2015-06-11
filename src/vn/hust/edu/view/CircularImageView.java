package vn.hust.edu.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.hust.edu.image.SmartImage;
import vn.hust.edu.image.SmartImageTask;
import vn.hust.edu.image.WebImage;
import vn.hust.edu.main.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class CircularImageView extends ImageView {
	private int borderWidth;
	private int canvasSize;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;

	public CircularImageView(final Context context) {
		this(context, null);
	}

	public CircularImageView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.circularImageViewStyle);
	}

	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// init paint
		paint = new Paint();
		paint.setAntiAlias(true);

		paintBorder = new Paint();
		paintBorder.setAntiAlias(true);

		// load the styled attributes and set their properties
		TypedArray attributes = context.obtainStyledAttributes(attrs,
				R.styleable.CircularImageView, defStyle, 0);

		if (attributes.getBoolean(R.styleable.CircularImageView_border, true)) {
			int defaultBorderSize = (int) (4 * getContext().getResources()
					.getDisplayMetrics().density + 0.5f);
			setBorderWidth(attributes.getDimensionPixelOffset(
					R.styleable.CircularImageView_border_width,
					defaultBorderSize));
			setBorderColor(attributes.getColor(
					R.styleable.CircularImageView_border_color, Color.WHITE));
		}

		// if (attributes.getBoolean(R.styleable.CircularImageView_shadow,
		// false))
		// addShadow();
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
		this.requestLayout();
		this.invalidate();
	}

	public void setBorderColor(int borderColor) {
		if (paintBorder != null)
			paintBorder.setColor(borderColor);
		this.invalidate();
	}

	@SuppressLint("NewApi")
	public void addShadow() {
		setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
		paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
	}

	@Override
	public void onDraw(Canvas canvas) {
		// load the bitmap
		image = drawableToBitmap(getDrawable());

		// init shader
		if (image != null) {

			canvasSize = canvas.getWidth();
			if (canvas.getHeight() < canvasSize)
				canvasSize = canvas.getHeight();

			BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(
					image, canvasSize, canvasSize, false),
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			paint.setShader(shader);

			// circleCenter is the x or y of the view's center
			// radius is the radius in pixels of the cirle to be drawn
			// paint contains the shader that will texture the shape
			int circleCenter = (canvasSize - (borderWidth * 2)) / 2;
			 canvas.drawCircle(circleCenter + borderWidth, circleCenter
			 + borderWidth, ((canvasSize - (borderWidth * 2)) / 2)
			 + borderWidth - 4.0f, paintBorder);
			canvas.drawCircle(circleCenter + borderWidth, circleCenter
					+ borderWidth,
					((canvasSize - (borderWidth * 2)) / 2) - 4.0f, paint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// The parent has determined an exact size for the child.
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			// The child can be as large as it wants up to the specified size.
			result = specSize;
		} else {
			// The parent has not imposed any constraint on the child.
			result = canvasSize;
		}

		return result;
	}

	private int measureHeight(int measureSpecHeight) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			// The child can be as large as it wants up to the specified size.
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = canvasSize;
		}

		return (result + 2);
	}

	public Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		} else if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	// Helpers to set image by URL
	public void setImageUrl(String url, final CircularImageView siv) {
		setImage(new WebImage(url), siv);
	}

	public void setImageUrl(String url,
			SmartImageTask.OnCompleteListener completeListener,
			final ProgressBar pb, final CircularImageView siv) {
		setImage(new WebImage(url), completeListener, siv);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final ProgressBar pb, final CircularImageView siv) {
		setImage(new WebImage(url), fallbackResource, siv);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			SmartImageTask.OnCompleteListener completeListener,
			final ProgressBar pb, final CircularImageView siv) {
		setImage(new WebImage(url), fallbackResource, completeListener, siv);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource, final CircularImageView siv) {
		setImage(new WebImage(url), fallbackResource, loadingResource, siv);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource,
			SmartImageTask.OnCompleteListener completeListener,
			final CircularImageView siv) {
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
	public void setImage(final SmartImage image, final CircularImageView siv) {
		setImage(image, null, null, null, siv);
	}

	public void setImage(final SmartImage image,
			final SmartImageTask.OnCompleteListener completeListener,
			final CircularImageView siv) {
		setImage(image, null, null, completeListener, siv);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final CircularImageView siv) {
		setImage(image, fallbackResource, fallbackResource, null, siv);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource,
			SmartImageTask.OnCompleteListener completeListener,
			final CircularImageView siv) {
		setImage(image, fallbackResource, fallbackResource, completeListener,
				siv);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource,
			final CircularImageView siv) {
		setImage(image, fallbackResource, loadingResource, null, siv);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource,
			final SmartImageTask.OnCompleteListener completeListener,
			final CircularImageView siv) {
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
							Log.i("ok", "ok Load Image");
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