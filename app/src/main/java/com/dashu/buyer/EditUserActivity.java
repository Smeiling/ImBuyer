package com.dashu.buyer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dashu.buyer.bean.User;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 *
 */

public class EditUserActivity extends BaseActivity implements View.OnClickListener{
	EditText nameEditText;
	EditText cardEditText;
	EditText phoneEditText;
	EditText houseNumberEdit;
	EditText houseRentStartTimeEdit;
	EditText houseRentEndTimeEdit;
	Button registerButton;
	Button cancelButton;
	int roleType = StaticUtil.ROLE_USER;
	String sex;
	RadioGroup radioGroup;
	User rentMan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_edit);
		rentMan = (User) getIntent().getSerializableExtra("data");
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup_tab);
		nameEditText = (EditText)findViewById(R.id.editText_input_name);
		cardEditText = (EditText)findViewById(R.id.editText_input_card);
		phoneEditText = (EditText)findViewById(R.id.editText_input_phone);
		houseNumberEdit = (EditText) findViewById(R.id.editText_input_house);

		houseRentStartTimeEdit = (EditText) findViewById(R.id.editText_input_start_time);
		houseRentEndTimeEdit = (EditText) findViewById(R.id.editText_input_end_time);


		registerButton = (Button)findViewById(R.id.button_register_ok);
		cancelButton = (Button)findViewById(R.id.button_register_cancel);

		registerButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		nameEditText.setText(rentMan.getUsername());
		cardEditText.setText(rentMan.getCard());
		phoneEditText.setText(rentMan.getPhone());
		if("1".equals(rentMan.getMansex())){
			((RadioButton)radioGroup.findViewById(R.id.radiobutton0)).setChecked(true);
		}else{
			((RadioButton)radioGroup.findViewById(R.id.radiobutton1)).setChecked(true);
		}


		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
							case R.id.radiobutton0:
								sex = "1";
								break;
							case R.id.radiobutton1:
								sex = "0";
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
			String houseNumber = houseNumberEdit.getText().toString().trim();
			String startDate = houseRentStartTimeEdit.getText().toString().trim();
			String endDate = houseRentEndTimeEdit.getText().toString().trim();
			if (account.equals("") || startDate.equals("")||endDate.equals("")
					|| houseNumber.equals("")
					|| card.equals("")||phone.equals("")) {
				ToastUtils.showTip(EditUserActivity.this, "各项输入不为空");
				return;
			}

			User man = new User();
			man.setUsername(account);
			man.setCard(card);
			man.setPhone(phone);
			man.setMansex(sex);
			man.setObjectId(rentMan.getObjectId());
			doUpdate(man);

			break;
		case R.id.button_register_cancel:
			finish();
			break;

		default:
			break;
		}
	}


	private void doUpdate(final User man) {
		man.update(EditUserActivity.this,man.getObjectId(), new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ToastUtils.showTip(EditUserActivity.this, "修改成功");
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				ToastUtils.showTip(EditUserActivity.this, "添加失败:" + msg);
			}
		});
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
	private void testSignUp(String username,String password,String card,String phone,boolean sex,String url) {
		final User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMansex(sex?"1":"0");
		user.setCard(card);
		user.setPhone(phone);
		user.setRoleType(roleType);

		user.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				toast("添加成功:" + user.getUsername());
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("添加失败:" + msg);
			}
		});
	}


}
