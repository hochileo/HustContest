package vn.hust.edu.variable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class Variable {
	public static String host = "http://192.168.1.4";
	public static int port = 8084;
	public static HashMap<Integer, String> choose_ans = new HashMap<Integer, String>();
	public static int score_Listening = 0;
	public static int score_Reading = 0;
	public static boolean isVisitor = false;

	public static String getJSONFromUrl(String url) {
		InputStream is = null;
		String json = null;
		try {
			// URLConnection conn = new URL(url).openConnection();
			// conn.setConnectTimeout(CONNECT_TIMEOUT);
			// conn.setReadTimeout(READ_TIMEOUT);
			HttpGet httpGet = new HttpGet(url);
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.setParams(httpParameters);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = (InputStream) httpEntity.getContent();
			// is = (InputStream) OpenHttpConnection(url).getContent();
			// is = OpenHttpConnection(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		return json;
	}
}
