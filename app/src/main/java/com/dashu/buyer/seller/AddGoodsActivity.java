package com.dashu.buyer.seller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.a.a.V;
import com.dashu.buyer.BaseActivity;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.buyer.BookActivity;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;
import com.dashu.buyer.view.CustomProgressDialog;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 *
 */

public class AddGoodsActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = AddGoodsActivity.class.getSimpleName();
    private static final int RESULT_LOAD_IMAGE = 23;
    private static final int DATE_DIALOG = 1;

    EditText nameEditText;
    EditText priceEditText;
    EditText countEditText;
    EditText weightEditText;
    EditText mDateEditText;
    EditText mAddressEditText;
    EditText mContentEditText;
    Button addBtn;
    ImageView addImgDone;
    ImageView addImg;
    Button cancelButton;
    int roleType = StaticUtil.ROLE_USER;
    String sex;
    RadioGroup radioGroup;
    User rentMan;
    private String account;
    private String objId;
    private String nick;
    private Spinner typeSpinner;

    String picPath = "null";
    private BmobFile bmobFile;
    int mYear, mMonth, mDay;
    private CustomProgressDialog progressDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        mContext = this;
        account = StaticUtil.currentUser.getUsername();
        objId = StaticUtil.currentUser.getObjectId();
        nick = StaticUtil.currentUser.getNick();
        nameEditText = (EditText) findViewById(R.id.add_name);
        priceEditText = (EditText) findViewById(R.id.add_price);
        countEditText = (EditText) findViewById(R.id.add_count);
        weightEditText = (EditText) findViewById(R.id.add_weight);
        mDateEditText = (EditText) findViewById(R.id.add_mdate);
        mDateEditText.setFocusable(false);
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime();
            }
        });
        addImgDone = (ImageView) findViewById(R.id.upload_done);
        mAddressEditText = (EditText) findViewById(R.id.add_maddress);
        mContentEditText = (EditText) findViewById(R.id.add_content);
        typeSpinner = (Spinner) findViewById(R.id.et_type);
        addBtn = (Button) findViewById(R.id.btn_add_new);
        addBtn.setOnClickListener(this);
        addImg = (ImageView) findViewById(R.id.good_img);
        addImg.setOnClickListener(this);

    }

    void chooseTime() {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        Date myDate = new Date();
        d.setTime(myDate);
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dlg = new DatePickerDialog(this, this, year, month, day);
        dlg.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String time = Integer.toString(year) + "-" +
                Integer.toString(month + 1) + "-" +
                Integer.toString(dayOfMonth);
        mDateEditText.setText(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_new:
                if (!nameEditText.getText().toString().isEmpty() && !priceEditText.getText().toString().isEmpty()) {
                    //addNewGood(bmobFile);
                    uploadImg(picPath);
                } else {
                    Toast.makeText(getApplicationContext(), "商品名称或价格不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.good_img:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("TAG", "onActivityResult");

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            if (data != null) {
                Uri selectedImage = data.getData();
                addImg.setImageURI(selectedImage);
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(selectedImage, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    Log.d("TAG", "picPath = " + picPath);
                    cursor.close();
                }
            }
        }
    }

    private void uploadImg(String picPath) {
        Log.d("TAG", "uploadImg");
        if (!"null".equals(picPath)) {
            progressDialog = CustomProgressDialog.createDialog(mContext);
            progressDialog.show();
            bmobFile = new BmobFile(new File(picPath));
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    Log.d("TAG", "onSuccess");
                    addImgDone.setVisibility(View.VISIBLE);
                    Goods nGoods = new Goods(bmobFile);

                    nGoods.setSellerName(nick);
                    nGoods.setSellerObjectId(objId);
                    nGoods.setName(nameEditText.getText().toString());
                    nGoods.setPrice(Float.parseFloat(priceEditText.getText().toString()));
                    nGoods.setType(typeSpinner.getSelectedItemPosition());
                    nGoods.setAddress(mAddressEditText.getText().toString().isEmpty() ? "不详" : mAddressEditText.getText().toString());
                    nGoods.setContent(mContentEditText.getText().toString().isEmpty() ? "无具体描述" : mContentEditText.getText().toString());
                    nGoods.setDate(mDateEditText.getText().toString().isEmpty() ? "不详" : mDateEditText.getText().toString());
                    nGoods.setCount(countEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(countEditText.getText().toString()));
                    nGoods.setWeight(weightEditText.getText().toString().isEmpty() ? "" : weightEditText.getText().toString());
                    nGoods.save(getApplicationContext(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), "添加新品成功！", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            finish();
                            Log.d("TAG", "添加新品成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

                @Override
                public void onProgress(Integer arg0) {
                    // TODO Auto-generated method stub
                    Log.d("TAG", "onProgress");
                }

                @Override
                public void onFailure(int arg0, String arg1) {
                    // TODO Auto-generated method stub
                    Log.w("TAG", "-->uploadMovoieFile-->onFailure:" + arg0 + ",msg = " + arg1);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }

            });
        } else {
            progressDialog = CustomProgressDialog.createDialog(mContext);
            progressDialog.show();
            Goods nGoods = new Goods();
            nGoods.setSellerName(nick);
            nGoods.setSellerObjectId(objId);
            nGoods.setName(nameEditText.getText().toString());
            nGoods.setPrice(Float.parseFloat(priceEditText.getText().toString()));
            nGoods.setType(typeSpinner.getSelectedItemPosition());
            nGoods.setAddress(mAddressEditText.getText().toString().isEmpty() ? "不详" : mAddressEditText.getText().toString());
            nGoods.setContent(mContentEditText.getText().toString().isEmpty() ? "无具体描述" : mContentEditText.getText().toString());
            nGoods.setDate(mDateEditText.getText().toString().isEmpty() ? "不详" : mDateEditText.getText().toString());
            nGoods.setCount(countEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(countEditText.getText().toString()));
            nGoods.setWeight(weightEditText.getText().toString().isEmpty() ? "" : weightEditText.getText().toString());
            nGoods.save(getApplicationContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "添加新品成功！", Toast.LENGTH_SHORT).show();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    finish();
                    Log.d("TAG", "添加新品成功");
                }

                @Override
                public void onFailure(int i, String s) {
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    finish();
                }
            });
        }
    }


    private void addNewGood(BmobFile bombFile) {
        Goods nGoods = new Goods(bombFile);

        nGoods.setSellerName(nick);
        nGoods.setSellerObjectId(objId);
        nGoods.setName(nameEditText.getText().toString());
        nGoods.setPrice(Float.parseFloat(priceEditText.getText().toString()));
        nGoods.setType(typeSpinner.getSelectedItemPosition());
        nGoods.setAddress(mAddressEditText.getText().toString().isEmpty() ? "不详" : mAddressEditText.getText().toString());
        nGoods.setContent(mContentEditText.getText().toString().isEmpty() ? "无具体描述" : mContentEditText.getText().toString());
        nGoods.setDate(mDateEditText.getText().toString().isEmpty() ? "不详" : mDateEditText.getText().toString());
        nGoods.setCount(countEditText.getText().toString().isEmpty() ? 0 : Integer.parseInt(countEditText.getText().toString()));
        nGoods.setWeight(weightEditText.getText().toString().isEmpty() ? "" : weightEditText.getText().toString());
        nGoods.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "添加新品成功！", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private void doUpdate(final User man) {
        man.update(AddGoodsActivity.this, man.getObjectId(), new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ToastUtils.showTip(AddGoodsActivity.this, "修改成功");
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.showTip(AddGoodsActivity.this, "添加失败:" + msg);
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
    private void testSignUp(String username, String password, String card, String phone, boolean sex, String url) {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setMansex(sex ? "1" : "0");
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
