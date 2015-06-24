package com.sysu.yibaosysu.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sysu.yibaosysu.R;
import com.sysu.yibaosysu.model.BikeInfo;
import com.sysu.yibaosysu.network.GetBikeListByBikeNameAsyncTask;
import com.sysu.yibaosysu.network.GetBikeListByLabelAsyncTask;
import com.sysu.yibaosysu.network.NetworkRequest;
import com.sysu.yibaosysu.ui.adapter.BikeListAdapter;
import com.sysu.yibaosysu.utils.StringUtils;

import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements
		GetBikeListByLabelAsyncTask.OnRequestListener,
		GetBikeListByBikeNameAsyncTask.OnRequestListener {

	private static final String LABEL_STRING = "标签";
	private static final String BIKE_STRING = "品牌";

	private int startBikeId = -1;
	private int size = 5;

	TextView mNoResultTv;
	ListView mResultList;
	Spinner mTypeSpinner;
	Button searchBtn;
	EditText searchEt;

	List<BikeInfo> mBikeList;
	BikeListAdapter adapter;

	WaitingDialogFragment mProgressDialog = new WaitingDialogFragment();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container,
				false);

		mNoResultTv = (TextView) rootView.findViewById(R.id.no_result);
		mResultList = (ListView) rootView.findViewById(R.id.search_result_list);
		mTypeSpinner = (Spinner) rootView
				.findViewById(R.id.search_condition_spinner);
		searchBtn = (Button) rootView.findViewById(R.id.search_button);
		searchEt = (EditText) rootView.findViewById(R.id.search_content);

		mResultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getFragmentManager()
						.beginTransaction()
						.replace(
								R.id.container,
								BikeDetailFragment.newInstance(mBikeList
										.get(position))).addToBackStack(null)
						.commit();
			}
		});
		initSearchAction();
		return rootView;
	}

	private void initSearchAction() {
		searchEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				searchBtn.setEnabled(!StringUtils.isEmpty(searchEt));
			}
		});
		searchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSearch(mTypeSpinner.getSelectedItem().toString(), searchEt
						.getText().toString());
			}
		});
	}

	private void startSearch(String type, String content) {
		getFragmentManager().beginTransaction().add(mProgressDialog, null)
				.commit();
		if (type.equals(LABEL_STRING)) {
			NetworkRequest.getBikeListByLabel(content, startBikeId, size, this);
		} else if (type.equals(BIKE_STRING)) {
			NetworkRequest.getBikeListByBikeName(content, startBikeId, size,
					this);
		}
	}

	@Override
	public void onGetBikeListByLabelSuccess(List<Map<String, Object>> bikeList) {
		showBikeList(bikeList);
	}

	@Override
	public void onGetBikeListByLabelFail(String errorMessage) {
		showErrorToast(errorMessage);
	}

	@Override
	public void onGetBikeListByBikeNameSuccess(
			List<Map<String, Object>> bikeList) {
		showBikeList(bikeList);
	}

	@Override
	public void onGetBikeListByBikeNameFail(String errorMessage) {
		showErrorToast(errorMessage);
	}

	private void showErrorToast(String errorMessage) {
		mProgressDialog.dismiss();
		Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
	}

	private void showBikeList(List<Map<String, Object>> bikeList) {
		mProgressDialog.dismiss();
		mBikeList = BikeInfo.parseList(bikeList);
		if (mBikeList == null || mBikeList.isEmpty()) {
			mNoResultTv.setVisibility(View.VISIBLE);
			mResultList.setVisibility(View.GONE);
		} else {
			mNoResultTv.setVisibility(View.GONE);
			mResultList.setVisibility(View.VISIBLE);
			adapter = new BikeListAdapter(getActivity(), mBikeList);
			mResultList.setAdapter(adapter);
			mResultList.invalidate();
		}
	}

}
