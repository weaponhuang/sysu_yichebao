package com.sysu.yibaosysu.network;

import android.os.AsyncTask;
import android.util.Log;

import com.sysu.yibaosysu.model.BikeInfo;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetBikeListAsyncTask extends AsyncTask<String, Integer, String> {

	private int serviceId = 1;
	private int commandId = 1;
	private int startBikeId;
	private int size;
	private OnRequestListener listener;

	public GetBikeListAsyncTask(int startBikeId, int size,
			OnRequestListener listener) {
		this.startBikeId = startBikeId;
		this.size = size;
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
			msg.put("startBikeId", startBikeId);
			msg.put("size", size);
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
        Log.e("msg", result);
        super.onPostExecute(result);
		if (result != null) {
			try {
				JSONObject msg = new JSONObject(result);
				int requestCode = msg.getInt("returnCode");
				if (requestCode == 1) {
					JSONArray jsonBikeList = msg.getJSONArray("bikeList");
					List<Map<String, Object>> bikeList = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < jsonBikeList.length(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject jsonBike = jsonBikeList.getJSONObject(i);
						map.put(BikeInfo.BIKE_ID,
								Integer.parseInt(jsonBike.getString("bikeId")));
						map.put(BikeInfo.BIKE_NAME,
								jsonBike.getString("bikeName"));
						map.put(BikeInfo.BIKE_CONTENT,
								jsonBike.getString("content"));
						map.put(BikeInfo.CREATE_TIME,
								jsonBike.getString("time"));
						map.put(BikeInfo.AUTHOR_NAME,
								jsonBike.getString("authorName"));
						bikeList.add(map);
					}
					listener.onGetBikeListSuccess(bikeList);
				} else {
					listener.onGetBikeListFail("获取书籍列表失败！");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				listener.onGetBikeListFail("返回错误！");
			}
		} else {
			listener.onGetBikeListFail("网络连接错误！");
		}
	}

	public interface OnRequestListener {
		void onGetBikeListSuccess(List<Map<String, Object>> bikeList);
		void onGetBikeListFail(String errorMessage);
	}
}
