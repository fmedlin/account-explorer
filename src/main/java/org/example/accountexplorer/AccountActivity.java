package org.example.accountexplorer;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.example.accountexplorer.R;

public class AccountActivity extends Activity {
	
	Account account;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		account = getIntent().getParcelableExtra("account");
		setTitle(account.type);
		setContentView(R.layout.activity_account);
		setAccountName(account.name);
	}

	private void setAccountName(String name) {
		TextView nameText = (TextView) findViewById(R.id.text_name);
		nameText.setText(name);
	}

	public void onAuthenticateClick(View v) {
		TextView name = (TextView) findViewById(R.id.text_name);
		name.setText("Authenticating!");

	}

}