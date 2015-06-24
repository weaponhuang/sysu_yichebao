package com.sysu.yibaosysu.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sysu.yibaosysu.R;
import com.sysu.yibaosysu.model.BikeInfo;
import com.sysu.yibaosysu.network.GetBikeListByUserIdAsyncTask.OnRequestListener;
import com.sysu.yibaosysu.network.NetworkRequest;
import com.sysu.yibaosysu.ui.adapter.BikeListAdapter;
import com.sysu.yibaosysu.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonalInfoFragment extends Fragment implements OnRequestListener {

	private static final int REQUIRE_SIZE = 20;
	
	TextView nameTv;
	TextView timeTv;
	ListView mBikeListView;
	List<BikeInfo> mBikeList;
	BikeListAdapter adapter;
	int startId = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_personal_info, null);

		nameTv = (TextView) rootView.findViewById(R.id.user_name);
		timeTv = (TextView) rootView.findViewById(R.id.user_join_time);
		initBikeList();
		mBikeListView = (ListView) rootView
				.findViewById(R.id.user_upload_bike_list);

		mBikeListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						getFragmentManager()
								.beginTransaction()
								.replace(
										R.id.container,
										BikeDetailFragment
												.newInstance(mBikeList
														.get(position)))
								.addToBackStack(null).commit();
					}
				});
		return rootView;
	}

	private void initBikeList() {
		int userId = PreferencesUtils.getUserId(getActivity());
		NetworkRequest.getBikeListByUserId(userId, startId, REQUIRE_SIZE, this);
		Toast.makeText(getActivity(), "正在加载书籍信息", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onGetBikeListByUserIdSuccess(List<Map<String, Object>> bikeList) {
		mBikeList = new ArrayList<BikeInfo>();

		mBikeList = BikeInfo.parseList(bikeList);
		if (mBikeList == null)
			return;
		adapter = new BikeListAdapter(getActivity(), mBikeList);
		mBikeListView.setAdapter(adapter);
		mBikeListView.invalidate();
	}

	@Override
	public void onGetBikeListByUserIdFail(String errorMessage) {
		Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
	}
}
