package com.coshling.main;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coshling.connection.UserAPI;
import com.coshling.constants.Constants;

public class LoginActivity extends Activity implements OnClickListener{

	private EditText 	   		emailEditText_ = null;
	private EditText			graduationEditText_ = null;
	private Button				verificationButton_ = null;
	private Button				loginButton_ = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		initView();
		setListener();
		initData();
	}
	
	private void initView() {
		emailEditText_ = (EditText) findViewById(R.id.emailEditText);
		graduationEditText_ = (EditText) findViewById(R.id.graduationEditText);
		verificationButton_ = (Button) findViewById(R.id.verificationButton);
		loginButton_ = (Button) findViewById(R.id.loginButton);
	}
	
	private void setListener() {
		loginButton_.setOnClickListener(this);
		verificationButton_.setOnClickListener(this);
	}
	
	private void initData() {
		String email = Constants.getEmail(LoginActivity.this);
		if (email != null && email.length() > 0) {
			Intent intent = new Intent(LoginActivity.this, SlideMenuWithActivity.class);
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();	
		}
		
	}
	private boolean isValidValues() {
		String email = emailEditText_.getText().toString();
		if (email.length() == 0 || !email.endsWith(".com")) {
			emailEditText_.setError("Please input valid email address");
			return false;
		}
		if (graduationEditText_.getText().toString().length() == 0) {
			graduationEditText_.setError("Please input graduation year");
			return false;
		}
		return true;
	}
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.verificationButton: {
			if (isValidValues()) {
				SendVerificationTask sendVerificationTask = new SendVerificationTask();
				sendVerificationTask.execute(emailEditText_.getText().toString(), graduationEditText_.getText().toString());
			}
		}
		    break;
		case R.id.loginButton: {
			if (isValidValues()) {
				LoginTask loginTask = new LoginTask();
				loginTask.execute(emailEditText_.getText().toString(), graduationEditText_.getText().toString());
			}
		}
		    break;
		}
	}
	private class SendVerificationTask extends AsyncTask<String, Void, String>
	{
		ProgressDialog mProgressDialog ;
		@Override
		protected void onPostExecute( String result )
		{
			mProgressDialog.cancel();
			String msg = "Failed to send verification";
			try {
				JSONObject json = new JSONObject(result);
				String status = json.getString("data");
				if (status.equals("1")) {
					Toast.makeText(LoginActivity.this, "Success to send verification", Toast.LENGTH_LONG).show();
				} else {
					msg = json.getString("message");
					Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
				}
			} catch (Exception e){
				Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute()
		{
			mProgressDialog = new ProgressDialog( LoginActivity.this ) ;
			mProgressDialog.setMessage("please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show() ;
		}

		@Override
		protected String doInBackground( String... params )
		{
			String url = String.format(UserAPI.COSHLING_VERIFICATION_URL, params[0], params[1]);
			return UserAPI.getResponse(url) ;
		}
	}
	private class LoginTask extends AsyncTask<String, Void, String>
	{
		ProgressDialog mProgressDialog ;
		@Override
		protected void onPostExecute( String result )
		{
			mProgressDialog.cancel();
			String msg = "Failed to login";
			try {
				JSONObject json = new JSONObject(result);
				String status = json.getString("data");
				if (status.equals("1")) {
					Constants.setEmail(LoginActivity.this, emailEditText_.getText().toString());
					Intent intent = new Intent(LoginActivity.this, SlideMenuWithActivity.class);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
				} else {
					msg = json.getString("message");
					Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
				}
			} catch (Exception e){
				Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute()
		{
			mProgressDialog = new ProgressDialog( LoginActivity.this ) ;
			mProgressDialog.setMessage("please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show() ;
		}

		@Override
		protected String doInBackground( String... params )
		{
			String url = String.format(UserAPI.COSHLING_LOGIN_URL, params[0], params[1]);
			return UserAPI.getResponse(url) ;
		}
	}
}
