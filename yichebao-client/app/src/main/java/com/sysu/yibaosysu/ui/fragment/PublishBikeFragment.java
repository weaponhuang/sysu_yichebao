package com.sysu.yibaosysu.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sysu.yibaosysu.R;
import com.sysu.yibaosysu.network.NetworkRequest;
import com.sysu.yibaosysu.network.UploadBikeAsyncTask;
import com.sysu.yibaosysu.utils.PreferencesUtils;
import com.sysu.yibaosysu.utils.StringUtils;

public class PublishBikeFragment extends Fragment implements TextWatcher,
		View.OnClickListener {

	EditText bikenameEt;
	EditText labelEt;
	EditText descriptEt;
	WaitingDialogFragment mProgressDialog = new WaitingDialogFragment();

	Button confirmBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_publishbike,
				container, false);
		bikenameEt = (EditText) rootView.findViewById(R.id.add_bike_name);
		labelEt = (EditText) rootView.findViewById(R.id.add_label);
		descriptEt = (EditText) rootView.findViewById(R.id.add_introduce);

		bikenameEt.addTextChangedListener(this);
		labelEt.addTextChangedListener(this);
		descriptEt.addTextChangedListener(this);

		confirmBtn = (Button) rootView.findViewById(R.id.confirm_upload);

		confirmBtn.setOnClickListener(this);
		return rootView;
	}

	/** 检查所有文本框是否已填充 */
	public boolean checkInfoHasCompleted() {
		return !(StringUtils.isEmpty(bikenameEt)
				|| StringUtils.isEmpty(descriptEt) || StringUtils
					.isEmpty(labelEt));
	}

	private void clearAllContent() {
		bikenameEt.setText("");
		descriptEt.setText("");
		labelEt.setText("");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		confirmBtn.setEnabled(checkInfoHasCompleted());
	}

	@Override
	public void onClick(View v) {
		getFragmentManager().beginTransaction().add(mProgressDialog, null)
				.commit();
		int userId = PreferencesUtils.getUserId(getActivity());
		String bikeName = bikenameEt.getText().toString();
		String content = descriptEt.getText().toString();
		String[] label = StringUtils.parseToStringArray(labelEt.getText()
				.toString());

		NetworkRequest.uploadBike(userId, bikeName, content, label,
				new UploadBikeAsyncTask.OnRequestListener() {

					@Override
					public void onUploadBikeSuccess() {
						mProgressDialog.dismiss();
						Toast.makeText(getActivity(), "上传成功",
								Toast.LENGTH_SHORT).show();
						clearAllContent();
					}

					@Override
					public void onUploadBikeFail(String errorMessage) {
						mProgressDialog.dismiss();
						Toast.makeText(getActivity(), errorMessage,
								Toast.LENGTH_SHORT).show();
					}
				});
	}
}
