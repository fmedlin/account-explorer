package org.example.accountexplorer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.Exception;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ListActivity {

    private static String TAG = "AccountExplorer";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        displayAccounts();
    }

    public void displayAccounts() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccounts();
        getListView().setAdapter(new AccountsAdapter(this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            android.R.id.text2,
            accounts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private static class AccountsAdapter extends ArrayAdapter<Account> {
        Map<String,String> typeToTitle = new HashMap<String,String>();
        int resource;
        int text1ResId;
        int text2ResId;

        public AccountsAdapter(Context context, int resource, int text1ResId, int text2ResId, Account[] accounts) {
            super(context, text1ResId, accounts);
            this.resource = resource;
            this.text1ResId = text1ResId;
            this.text2ResId = text2ResId;

            typeToTitle.put("com.google", "Google");
            typeToTitle.put("com.dropbox.android.account", "Dropbox");
            typeToTitle.put("com.facebook.auth.login", "Facebook");
            typeToTitle.put("com.pindroid", "PinDroid");
            typeToTitle.put("com.meetup.auth", "Meetup");
            typeToTitle.put("com.skype.contacts.sync", "Skype");
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            Account account = getItem(position);
            TextView text1 = (TextView) v.findViewById(text1ResId);
            TextView text2 = (TextView) v.findViewById(text2ResId);

            text1.setText(getTitleFromAccountType(account.type));
            text2.setText(account.name);
            return v;
        }

        private String getTitleFromAccountType(String accountType) {
            String title = typeToTitle.get(accountType);
            return (title == null || title.isEmpty()) ? "Unknown Account" : title;
        }
    }
}

