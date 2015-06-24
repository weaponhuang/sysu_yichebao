package com.sysu.yibaosysu.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import android.os.AsyncTask;

public class GetLabelAsyncTask extends AsyncTask<String, Integer, String> {
	
	private int serviceId = 1;
	private int commandId = 2;
	private int bikeId;
	private OnRequestListener listener;

	public GetLabelAsyncTask(int bikeId, OnRequestListener listener) {
		this.bikeId = bikeId;
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
					JSONArray jsonLabels = msg.getJSONArray("labelArr");
					List<String> labels = new ArrayList<String>();
					for (int i = 0; i < jsonLabels.length(); i++) {
						labels.add(jsonLabels.getString(i));
					}
					listener.onGetLabelSuccess(labels);
				} else {
					listener.onGetLabelFail("获取标签失败！");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				listener.onGetLabelFail("返回错误！");
			}
		} else {
			listener.onGetLabelFail("网络连接错误！");
		}
	}

	public interface OnRequestListener {
		void onGetLabelSuccess(List<String> labels);
		void onGetLabelFail(String errorMessage);
	}
	
}
