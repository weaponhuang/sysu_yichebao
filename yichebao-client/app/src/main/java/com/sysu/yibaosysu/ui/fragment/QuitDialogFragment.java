package com.sysu.yibaosysu.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.sysu.yibaosysu.LaunchActivity;
import com.sysu.yibaosysu.R;
import com.sysu.yibaosysu.utils.PreferencesUtils;

public class QuitDialogFragment extends DialogFragment implements
		DialogInterface.OnClickListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Dialog dialog = builder.setTitle(getString(R.string.quit))
				.setMessage(getString(R.string.ask_is_quit))
				.setPositiveButton(getString(R.string.confirm), this)
				.setNegativeButton(getString(R.string.cancel), this).create();
		return dialog;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			PreferencesUtils.clear(getActivity());
			startActivity(new Intent(getActivity(), LaunchActivity.class));
			getActivity().finish();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		default:
			break;
		}
		dialog.dismiss();
	}
}
