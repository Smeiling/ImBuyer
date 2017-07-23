package com.dashu.buyer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.dashu.buyer.bean.User;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;

import cn.bmob.v3.listener.UpdateListener;


/**
 *
 */

public class UpdateUserActivity extends BaseActivity implements View.OnClickListener{
	EditText nameEditText;
	EditText cardEditText;
	EditText phoneEditText;
	EditText passwordEditText;
	EditText double_passEditText;
	Button registerButton;
	Button cancelButton;
	boolean sex;
	RadioGroup radioGroup;
	String userId;
	User user;

	String name;
	String card;
	String phone;

	int roleType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_update);
		roleType = StaticUtil.currentUser.getRoleType();
		user = StaticUtil.currentUser;

		name = user.getUsername();
		card = user.getCard();
		phone = user.getPhone();
		userId = user.getObjectId();

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup_tab);
		nameEditText = (EditText)findViewById(R.id.editText_input_name);

		cardEditText = (EditText)findViewById(R.id.editText_input_card);
		phoneEditText = (EditText)findViewById(R.id.editText_input_phone);
		passwordEditText = (EditText)findViewById(R.id.editText_input_password);
		double_passEditText = (EditText)findViewById(R.id.editText_input_double_pass);
		registerButton = (Button)findViewById(R.id.button_register_ok);
		cancelButton = (Button)findViewById(R.id.button_register_cancel);
		registerButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);


		nameEditText.setText(name);
		nameEditText.setEnabled(false);
		cardEditText.setText(card);
		phoneEditText.setText(phone);


		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
							case R.id.radiobutton0:
								sex = true;
								break;
							case R.id.radiobutton1:
								sex = false;
								break;
						}

					}
				});


	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_register_ok:
			String account = nameEditText.getText().toString().trim();
			String card = cardEditText.getText().toString().trim();
			String phone = phoneEditText.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
			String repass = double_passEditText.getText().toString().trim();
			if (account.equals("") || password.equals("")||repass.equals("")
					|| card.equals("")||phone.equals("")) {
				ToastUtils.showTip(UpdateUserActivity.this, "用户名或者密码不能为空！");
				return;
			}

			if (!password.equals(repass)) {
				ToastUtils.showTip(UpdateUserActivity.this, "密码不一致！");
				return;
			}

			doUpdate(account, password,card,phone,sex);
			break;
		case R.id.button_register_cancel:
			finish();
			break;

		default:
			break;
		}
	}

	void doUpdate(String account, String password,String card,String phone,boolean sex) {
		if(roleType==StaticUtil.ROLE_MANAGER){
			testSignUp(account,password,card,phone,sex);
		}else{
			updateRentman(account,password,card,phone,sex);
		}

	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}

	/**
	 * 注册用户
	 */
	@SuppressLint("UseValueOf")
	private void testSignUp(String username,String password,String card,String phone,boolean sex) {
		final User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMansex(sex?"1":"0");
		user.setCard(card);
		user.setPhone(phone);
		user.setRoleType(roleType);
		user.update(this, userId, new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("更新成功:" + user.getUsername());
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("更新失败:" + msg);
			}
		});
	}


	private void updateRentman(String username,String password,String card,String phone,boolean sex) {
		final User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMansex(sex?"1":"0");
		user.setCard(card);
		user.setPhone(phone);
		user.update(this, userId, new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("更新成功:");
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("更新失败:" + msg);
			}
		});
	}


}
