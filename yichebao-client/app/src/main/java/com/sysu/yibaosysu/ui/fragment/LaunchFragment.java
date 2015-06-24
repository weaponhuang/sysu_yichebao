package com.sysu.yibaosysu.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sysu.yibaosysu.R;

public class LaunchFragment extends Fragment {

	Button loginBtn;
	Button registerBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_launch, container, false);

		loginBtn = (Button) view.findViewById(R.id.login);
		registerBtn = (Button) view.findViewById(R.id.register);

		loginBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
						.replace(R.id.launch_container, new LoginFragment())
						.addToBackStack("").commit();
			}
		});

		registerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction()
						.replace(R.id.launch_container, new RegisterFragment())
						.addToBackStack("").commit();
			}
		});
		return view;
	}
}
