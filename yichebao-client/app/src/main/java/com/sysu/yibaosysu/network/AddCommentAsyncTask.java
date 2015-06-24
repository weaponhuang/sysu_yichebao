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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AddCommentAsyncTask extends AsyncTask<String, Integer, String> {
	
	private int serviceId = 2;
	private int commandId = 0;
	private int bikeId;
	private int userId;
	private String content;
	private OnRequestListener listener;

	public AddCommentAsyncTask(int bikeId, int userId, String content, OnRequestListener listener) {
		this.bikeId = bikeId;
		this.userId = userId;
		this.content = content;
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
			msg.put("bikeId", bikeId);
			msg.put("userId", userId);
			msg.put("content", content);
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
					listener.onAddCommentSuccess();
				} else {
					listener.onAddCommentFail("添加评论失败！");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				listener.onAddCommentFail("返回错误！");
			}
		} else {
			listener.onAddCommentFail("网络连接错误！");
		}
	}

	public interface OnRequestListener {
		void onAddCommentSuccess();
		void onAddCommentFail(String errorMessage);
	}
}
