package com.sysu.yibaosysu.network;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class NetworkRequest {

	public static HttpClient CLIENT = new DefaultHttpClient();

	private static String SERVER_URI = "http://192.168.0.107/yichebao/server.php";

	public static void register(String userName, String password,
			RegisterAsyncTask.OnRequestListener listener) {
		new RegisterAsyncTask(userName, password, listener).execute(SERVER_URI);
	}

	public static void login(String userName, String password,
			LoginAsyncTask.OnRequestListener listener) {
		new LoginAsyncTask(userName, password, listener).execute(SERVER_URI);
	}

	public static void uploadBike(int userId, String bikeName, String content,
			String[] label, UploadBikeAsyncTask.OnRequestListener listener) {
		new UploadBikeAsyncTask(userId, bikeName, content, label, listener)
				.execute(SERVER_URI);
	}

	public static void getBikeList(int startBikeId, int size,
			GetBikeListAsyncTask.OnRequestListener listener) {
		new GetBikeListAsyncTask(startBikeId, size, listener)
				.execute(SERVER_URI);
	}

	public static void getBikeLabel(int bikeId,
			GetLabelAsyncTask.OnRequestListener listener) {
		new GetLabelAsyncTask(bikeId, listener).execute(SERVER_URI);
	}

	public static void getBikeComment(int bikeId,
			GetCommentAsyncTask.OnRequestListener listener) {
		new GetCommentAsyncTask(bikeId, listener).execute(SERVER_URI);
	}

	public static void getBikeListByUserId(int userId, int startBikeId,
			int size, GetBikeListByUserIdAsyncTask.OnRequestListener listener) {
		new GetBikeListByUserIdAsyncTask(userId, startBikeId, size, listener)
				.execute(SERVER_URI);
	}

	public static void getBikeListByBikeName(String bikeName, int startBikeId,
			int size, GetBikeListByBikeNameAsyncTask.OnRequestListener listener) {
		new GetBikeListByBikeNameAsyncTask(bikeName, startBikeId, size,
				listener).execute(SERVER_URI);
	}

	public static void getBikeListByLabel(String label, int startBikeId,
			int size, GetBikeListByLabelAsyncTask.OnRequestListener listener) {
		new GetBikeListByLabelAsyncTask(label, startBikeId, size, listener)
				.execute(SERVER_URI);
	}

	public static void addComment(int bikeId, int userId, String content,
			AddCommentAsyncTask.OnRequestListener listener) {
		new AddCommentAsyncTask(bikeId, userId, content, listener)
				.execute(SERVER_URI);
	}
}
