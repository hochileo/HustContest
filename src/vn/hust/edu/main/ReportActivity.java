package vn.hust.edu.main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import vn.hust.edu.variable.LoadPreferences;
import vn.hust.edu.variable.Variable;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.widgetcustomlib.FloatingEditText;
import com.example.widgetcustomlib.PaperButton;

public class ReportActivity extends Activity {
	private FloatingEditText subject;
	private FloatingEditText message;
	private PaperButton submit;
	private LinearLayout main_progress;
	private PaperButton choose;
	private RequestQueue mVolleyQueue;
	private Bitmap b = null;
	private static int RESULT_LOAD_IMG = 1;
	String imgDecodableString;
	String feedback = Variable.host + ":" + Variable.port
			+ "/server/newapi/imageupload?user=";
	String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		
		StrictMode.ThreadPolicy policyNews = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policyNews);
		LoadPreferences lp = new LoadPreferences(getApplicationContext());
		feedback += lp.loadString("login") + "&subject=";
		main_progress = (LinearLayout) findViewById(R.id.main_progress);
		subject = (FloatingEditText) findViewById(R.id.editSubject);
		message = (FloatingEditText) findViewById(R.id.editMessage);
		submit = (PaperButton) findViewById(R.id.btnSubmit);
		mVolleyQueue = Volley.newRequestQueue(this);
		choose = (PaperButton) findViewById(R.id.btnChoose);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					System.out.println("check " + imgDecodableString);
					main_progress.setVisibility(View.VISIBLE);
					String s = new String(subject.getText().toString().trim()
							.getBytes("UTF-8"), "iso-8859-1");
					String m = new String(message.getText().toString().trim()
							.getBytes("UTF-8"), "iso-8859-1");
					
					url = feedback + s + "&message=" + m + "&image=";
					System.out.println("Test URL " + url);
					new Taskload1().execute();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadImagefromGallery();
			}
		});
	}

	public void loadImagefromGallery() {
		// Create intent to Open Image applications like Gallery, Google Photos
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// Start the Intent
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		main_progress.setVisibility(View.VISIBLE);
		new Taskload(requestCode, resultCode, data).execute();
	}

	public boolean BitMapToString(Bitmap bitmap) {
		final Random rd = new Random();
		int r = Math.abs(rd.nextInt());
		url+=r;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		try {
			URL url = new URL(this.url.replaceAll(" ", "%20"));
			String response = "";
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// Set connection parameters.
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			// Set content type to PNG
			conn.setRequestProperty("Content-Type", "image/*");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			// Write out the bytes of the content string to the stream.
			out.write(b);
			out.flush();
			out.close();
			// Read response from the input stream.
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String temp;
			while ((temp = in.readLine()) != null) {
				response += temp + "\n";
			}
			temp = null;
			in.close();
			System.out.println("Server response:\n'" + response + "'");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean Upload() {
		
		try {
			JsonObjectRequest jor = new JsonObjectRequest(Method.GET, url, null,
					new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject object) {
							try {
								if (object.getBoolean("status")) {
									main_progress.setVisibility(View.GONE);
									finish();
									Toast.makeText(getApplicationContext(), "Phản hồi thành công",
											Toast.LENGTH_SHORT).show();
								}else{
									main_progress.setVisibility(View.GONE);
									Toast.makeText(getApplicationContext(),
											"Phản hồi không thành công", Toast.LENGTH_SHORT).show();
								}
							
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							main_progress.setVisibility(View.GONE);
							Toast.makeText(getApplicationContext(),
									"Phản hồi không thành công", Toast.LENGTH_SHORT).show();
							error.printStackTrace();
						}
					});

			mVolleyQueue.add(jor);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	class Taskload1 extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean isReturn = false;
			try {
				if (b==null) {
					isReturn = Upload();
				}else{
					isReturn = BitMapToString(b);
					
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return isReturn;
		}

		@Override
		protected void onPostExecute(Boolean bool) {
			super.onPostExecute(bool);
			if (b ==null) {
				
			}else{
			if (bool) {
				main_progress.setVisibility(View.GONE);
				finish();
				Toast.makeText(getApplicationContext(), "Phản hồi thành công",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Phản hồi không thành công", Toast.LENGTH_SHORT).show();
			}
			}
		}
	}

	class Taskload extends AsyncTask<Void, Void, Bitmap> {
		private int requestCode;
		private int resultCode;
		private Intent data;

		public Taskload(int requestCode, int resultCode, Intent data) {
			this.requestCode = requestCode;
			this.resultCode = resultCode;
			this.data = data;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				// When an Image is picked
				if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
						&& null != data) {
					// Get the Image from data

					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };

					// Get the cursor
					Cursor cursor = getContentResolver().query(selectedImage,
							filePathColumn, null, null, null);
					// Move to first row
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					imgDecodableString = cursor.getString(columnIndex);
					cursor.close();

					b = BitmapFactory.decodeFile(imgDecodableString);

				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return b;
		}

		@Override
		protected void onPostExecute(Bitmap b) {
			super.onPostExecute(b);
			ImageView imgView = (ImageView) findViewById(R.id.imageView1);
			if (b != null) {
				imgView.setImageBitmap(b);
			}
			main_progress.setVisibility(View.GONE);
		}
	}

}
