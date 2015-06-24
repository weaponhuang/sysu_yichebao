package com.sysu.yibaosysu.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sysu.yibaosysu.MainActivity;
import com.sysu.yibaosysu.R;
import com.sysu.yibaosysu.network.NetworkRequest;
import com.sysu.yibaosysu.network.RegisterAsyncTask.OnRequestListener;
import com.sysu.yibaosysu.utils.PreferencesUtils;
import com.sysu.yibaosysu.utils.StringUtils;

public class RegisterFragment extends Fragment implements TextWatcher,
		OnRequestListener, View.OnClickListener {

	EditText usernameEt;
	EditText passwordEt;
	EditText confirmPwdEt;
	Button confirmButton;
	WaitingDialogFragment mProgressDialog = new WaitingDialogFragment();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_register, container,
				false);

		usernameEt = (EditText) rootView.findViewById(R.id.input_user_name);
		passwordEt = (EditText) rootView.findViewById(R.id.input_pwd);
		confirmPwdEt = (EditText) rootView.findViewById(R.id.input_pwd_confirm);

		usernameEt.addTextChangedListener(this);
		passwordEt.addTextChangedListener(this);
		confirmPwdEt.addTextChangedListener(this);

		confirmButton = (Button) rootView
				.findViewById(R.id.btn_confirm_register);

		confirmButton.setOnClickListener(this);
		return rootView;
	}

	private boolean checkInfoHasCompleted() {
		return !(StringUtils.isEmpty(usernameEt)
				|| StringUtils.isEmpty(passwordEt) || StringUtils
					.isEmpty(confirmPwdEt));
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
		confirmButton.setEnabled(checkInfoHasCompleted());
	}

	@Override
	public void onClick(View v) {
		String firstPwd = passwordEt.getText().toString();
		String confirmPwd = confirmPwdEt.getText().toString();
		if (firstPwd.equals(confirmPwd)) {
			getFragmentManager().beginTransaction().add(mProgressDialog, null)
					.commit();
			NetworkRequest.register(usernameEt.getText().toString(), passwordEt
					.getText().toString(), this);
		} else {
			Toast.makeText(getActivity(),
					getString(R.string.error_password_no_the_same),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRegisterSuccess(int userId) {
		mProgressDialog.dismiss();
		PreferencesUtils.saveUserId(getActivity(), userId);
		getActivity().startActivity(
				new Intent(getActivity(), MainActivity.class));
		getActivity().finish();
	}

	@Override
	public void onRegisterFail(String errorMessage) {
		mProgressDialog.dismiss();
		Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
	}
}
