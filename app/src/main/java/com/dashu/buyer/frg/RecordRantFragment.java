package com.dashu.buyer.frg;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;

import cn.bmob.v3.listener.SaveListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordRantFragment extends Fragment {

    EditText houseNameEdit;
    EditText houseCardEdit;
    EditText houseNumberEdit;
    EditText houseMyUserPhoneEdit;
    EditText houseRentStartTimeEdit;
    EditText houseRentEndTimeEdit;
    Button btnSave;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_rent_man, null);
        initView(view);
        initListener();
        return view;
    }


    private void initView(View view) {
        btnSave = (Button) view.findViewById(R.id.button_register_ok);
        houseNameEdit = (EditText) view.findViewById(R.id.editText_input_name);
        houseCardEdit = (EditText) view.findViewById(R.id.editText_input_card);
        
        houseNumberEdit = (EditText) view.findViewById(R.id.editText_input_house);
        
        houseMyUserPhoneEdit = (EditText) view.findViewById(R.id.editText_input_phone);
        
        houseRentStartTimeEdit = (EditText) view.findViewById(R.id.editText_input_start_time);
        houseRentEndTimeEdit = (EditText) view.findViewById(R.id.editText_input_end_time);

    }

    private void initListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(houseCardEdit.getText().toString())) {
                    User man = new User();
                    man.setUsername(houseNameEdit.getText().toString());
                    man.setCard(houseCardEdit.getText().toString());
                    man.setPhone(houseMyUserPhoneEdit.getText().toString());
                    man.setRoleType(StaticUtil.ROLE_USER);
                    man.setPassword("123456");
                    doSave(man);
                }

            }
        });
    }

    private void doSave(final User man) {
        man.save(getActivity(), new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ToastUtils.showTip(getActivity(), "添加成功");
                clearEdit();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.showTip(getActivity(), "添加失败:" + msg);
            }
        });
    }

    private void clearEdit(){
        houseNameEdit.setText("");
        houseCardEdit.setText("");
        houseNumberEdit.setText("");
        houseMyUserPhoneEdit.setText("");
        houseRentStartTimeEdit.setText("");
        houseRentEndTimeEdit.setText("");
    }

}
