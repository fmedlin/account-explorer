package org.example.accountexplorer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.example.accountexplorer.R;

public class AccountActivity extends Activity {

	private final String TAG = "AccountActivity";
	private final int AUTH_REQUEST = 0;

	private AccountManager manager;
	private Account account;
	private TextView nameText;
	private TextView statusText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = AccountManager.get(this);
		account = getIntent().getParcelableExtra("account");

		setTitle(account.type);
		setContentView(R.layout.activity_account);
		setupViews();
	}

	private void setupViews() {
		nameText = (TextView) findViewById(R.id.text_name);
		statusText = (TextView) findViewById(R.id.text_status);

		nameText.setText(account.name);
	}

	public void onAuthenticateClick(View v) {
		authenticate();
	}

	public void authenticate() {
		Log.d(TAG, "authenticate");
		try {			
			manager.getAuthToken(
				account,
				"android",					// TODO: Should be authenticator-dependent string (authorization_code) ??
				new Bundle(),
				this,
				new OnTokenAcquired(),
				new Handler(new OnError())	// main handler thread
			);

		}
		catch (Exception e) {
			logError("authenticate:", e);
		}

	}

	private class OnError implements Handler.Callback {
		public boolean handleMessage(Message msg) {
			Log.d(TAG, "OnError: " + msg.toString());
			return true;
		}
	}

	private void handleBundleResult(Bundle bundle) {
		Log.v(TAG, bundle.toString());
		if (bundle.containsKey(AccountManager.KEY_INTENT)) {
			Intent launch = (Intent) bundle.getParcelable(AccountManager.KEY_INTENT);
			if (launch != null) {
				Log.v(TAG, "Starting activity with intent: " + launch.toString());
				startActivityForResult(launch, AUTH_REQUEST);
				return;
			}
		}

		if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
			statusText.setText("authtoken: " + bundle.getString(AccountManager.KEY_AUTHTOKEN));
		}
		else {
			statusText.setText("WTF");
			Log.v(TAG, bundle.toString());
		}		
	}

	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
		public void run(AccountManagerFuture<Bundle> future) {
			Log.d(TAG, "callback : run");
			try {
				handleBundleResult(future.getResult());
			}
			catch(Exception e) {
				logError("callback:", e);
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTH_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				Log.e(TAG, "activity result : success");
				authenticate();
			}
			else {
				Log.e(TAG, "activity result : failed");
			}
		}
	}

	private void logError(String title, Exception e) {
		Log.e(TAG, title, e);
	}
}