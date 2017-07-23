package com.dashu.buyer.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.util.ToastUtils;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/11/19.
 */
public class RecordHouseFrg extends Fragment {

    EditText houseNameEdit;
    EditText houseEreaEdit;
    EditText houseNumberEdit;
    Button btnSave;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_house,null);
        initView(view);
        initListener();
        return view;
    }


    private void initView(View view){
        btnSave = (Button) view.findViewById(R.id.btn_select_save);
        houseNameEdit = (EditText) view.findViewById(R.id.edit_house_name);
        houseEreaEdit = (EditText) view.findViewById(R.id.edit_house_erea);
        houseNumberEdit = (EditText) view.findViewById(R.id.edit_house_num);
    }

    private void initListener(){
    }

    private void clearValues(){
         houseNameEdit.setText("");
         houseEreaEdit.setText("");
         houseNumberEdit.setText("");
    }

    private void doSave(final Goods goods){
        goods.save(getActivity(), new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                ToastUtils.showTip(getActivity(),"添加成功");
                clearValues();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ToastUtils.showTip(getActivity(),"添加失败:" + msg);
            }
        });
    }
}
