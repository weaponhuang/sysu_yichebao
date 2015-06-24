package com.sysu.yibaosysu.network;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UploadBikeAsyncTask extends AsyncTask<String, Integer, String> {

	private int serviceId = 1;
	private int commandId = 0;
	private int userId;
	private String bikeName;
	private String content;
	private String[] labels;
	private OnRequestListener listener;

	public UploadBikeAsyncTask(int userId, String bikeName, String content,
			String[] label, OnRequestListener listener) {
		this.userId = userId;
		this.bikeName = bikeName;
		this.content = content;
		this.labels = label;
		this.listener = listener;
	}

	@Override
	protected String doInBackground(String... params) {
		HttpPost request = new HttpPost(params[0]);
		HttpResponse response = null;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		JSONObject msg = new JSONObject();
		try {
			msg.put("sid", serviceId);
			msg.put("cid", commandId);
			msg.put("userId", userId);
			msg.put("bikeName", bikeName);
			msg.put("content", content);
			JSONArray labelArray = new JSONArray();
			for (int i = 0; i < labels.length; i++) {
				labelArray.put(labels[i]);
			}
			msg.put("labelArr", labelArray);
			nameValuePair.add(new BasicNameValuePair("msg", msg.toString()));
			request.setEntity(new UrlEncodedFormEntity(nameValuePair,
					HTTP.UTF_8));
			response = NetworkRequest.CLIENT.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
			try {
				JSONObject msg = new JSONObject(result);
				int requestCode = msg.getInt("returnCode");
				if (requestCode == 1) {
					listener.onUploadBikeSuccess();
				} else {
					listener.onUploadBikeFail("上传失败！");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				listener.onUploadBikeFail("返回错误！");
			}
		} else {
			listener.onUploadBikeFail("网络连接错误！");
		}
	}

	public interface OnRequestListener {
		void onUploadBikeSuccess();

		void onUploadBikeFail(String errorMessage);
	}

}
