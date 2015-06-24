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

public class RegisterAsyncTask extends AsyncTask<String, Integer, String>{
	
	private int serviceId = 0;
	private int commandId = 0;
	private String userName;
	private String password;
	private OnRequestListener listener;

	public RegisterAsyncTask(String userName, String password, OnRequestListener listener) {
		this.userName = userName;
		this.password = password;
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
			msg.put("userName", userName);
			msg.put("password", password);
			nameValuePair.add(new BasicNameValuePair("msg", msg.toString()));
			request.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
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
					int userId = msg.getInt("userId");
					listener.onRegisterSuccess(userId);
				}
				else if (requestCode == -1) {
					listener.onRegisterFail("用户名已存在!");
				}
				else {
					listener.onRegisterFail("注册失败！");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				listener.onRegisterFail("返回错误！");
			}
		}
		else {
			listener.onRegisterFail("网络连接错误！");
		}
	}

	public static interface OnRequestListener {
		void onRegisterSuccess(int userId);
		void onRegisterFail(String errorMessage);
	}

}
