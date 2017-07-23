package com.dashu.buyer.admin;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dashu.buyer.LoginActivity;
import com.dashu.buyer.R;
import com.dashu.buyer.RegisterActivity;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.bean.UserInfo;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class BuyerActivity extends Activity {

    private ImageView iv_back;
    private TextView tv_add;
    private ListView lv_buyer;

    private List<UserInfo> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryForUser();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        lv_buyer = (ListView) findViewById(R.id.lv_buyer);
        lv_buyer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("user", userList.get(position));
                startActivity(intent);
            }
        });
    }

    private void queryForUser() {
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("roleType", StaticUtil.ROLE_USER);
        query.findObjects(this, new FindListener<UserInfo>() {
            @Override
            public void onSuccess(List<UserInfo> list) {
                if (list != null && list.size() > 0) {
                    Log.d("TAG", "object size = " + list.size());
                    fillUser(list);
                } else {
                    fillUser(list);
                    ToastUtils.showTip(getApplication(), "no user");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void fillUser(List<UserInfo> obj) {
        String[] account = new String[obj.size()];
        String[] nick = new String[obj.size()];
        for (int i = 0; i < obj.size(); i++) {
            account[i] = obj.get(i).getUsername();
            nick[i] = obj.get(i).getNick();
        }
        ArrayList<Map<String, Object>> mData = new ArrayList<>();
        for (int i = 0; i < obj.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("title", account[i]);
            item.put("text", nick[i]);
            mData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, mData, android.R.layout.simple_list_item_2, new String[]{"title", "text"}, new int[]{android.R.id.text1, android.R.id.text2});

        userList = obj;
        lv_buyer.setAdapter(adapter);
    }
}
