package com.sysu.yibaosysu.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.sysu.yibaosysu.R;
import com.sysu.yibaosysu.model.BikeInfo;
import com.sysu.yibaosysu.network.GetBikeListAsyncTask;
import com.sysu.yibaosysu.network.NetworkRequest;
import com.sysu.yibaosysu.ui.adapter.BikeListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BikeListFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener,
		GetBikeListAsyncTask.OnRequestListener, AbsListView.OnScrollListener {

	private static final int NEWEST = -1;
	private int currentId = NEWEST;
	private static int REQUIRE_SIZE = 5;

	private boolean isGetOlderItem = false;
	private boolean isGetNewerItem = false;

	Context mContext;
	ListView mListView;
	BikeListAdapter adapter;
	SwipeRefreshLayout mSwipeRefreshLayout;

	List<BikeInfo> mDataList = new ArrayList<BikeInfo>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContext = getActivity().getApplicationContext();

		View rootView = inflater.inflate(R.layout.fragment_bike_list,
				container, false);
		mListView = (ListView) rootView.findViewById(R.id.fragment_bike_list);
		adapter = new BikeListAdapter(getActivity(), mDataList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BikeInfo bike = mDataList.get(position);
				getFragmentManager()
						.beginTransaction()
						.replace(R.id.container,
								BikeDetailFragment.newInstance(bike))
						.addToBackStack(null).commit();
			}
		});

		mListView.setOnScrollListener(this);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swiper);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_blue_bright);
		mSwipeRefreshLayout.setOnRefreshListener(this);

		update(NEWEST);

		return rootView;
	}

	private void update(int currentId) {
		mSwipeRefreshLayout.setRefreshing(true);
		NetworkRequest.getBikeList(currentId, REQUIRE_SIZE, this);
	}

	@Override
	public void onRefresh() {
		isGetNewerItem = true;
		isGetOlderItem = false;
		update(NEWEST);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (view.getLastVisiblePosition() == (mDataList.size() - 1)) {
				isGetOlderItem = true;
				isGetNewerItem = false;
				update(currentId);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onGetBikeListSuccess(List<Map<String, Object>> bikeList) {
		List<BikeInfo> tBikeList = BikeInfo.parseList(bikeList);
		if (tBikeList == null || tBikeList.isEmpty()) {
			return;
		}

		else if (mDataList.isEmpty()) {
			mDataList.addAll(tBikeList);
		} else {
			int size = mDataList.size();
			for (BikeInfo tBike : tBikeList) {
				if (!mDataList.contains(tBike))
					mDataList.add(tBike);
			}
			if (mDataList.size() == size) {
				if (isGetNewerItem)
					showMessage("已经是最新数据");
				else if (isGetOlderItem)
					showMessage("没有更多数据");
			}
		}

		((BikeListAdapter) mListView.getAdapter()).notifyDataSetChanged();
		if (currentId == NEWEST) {
			currentId += 1;
			showMessage("刷新成功");
		}

		currentId += REQUIRE_SIZE;

		mSwipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onGetBikeListFail(String errorMessage) {
		mSwipeRefreshLayout.setRefreshing(false);
		showMessage(errorMessage);
	}

	private void showMessage(String errorMessage) {
		Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();

	}
}
