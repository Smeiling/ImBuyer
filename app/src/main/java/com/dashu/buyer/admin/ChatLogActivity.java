package com.dashu.buyer.admin;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.dashu.buyer.R;
import com.dashu.buyer.adapter.ChatLogAdapter;
import com.dashu.buyer.bean.Conver;

import java.util.ArrayList;
import java.util.List;

public class ChatLogActivity extends Activity {

    private ListView lvChatLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_log);
        lvChatLog = (ListView) findViewById(R.id.lv_chat_log);
        fillContent();
    }

    private void fillContent() {
        List<Conver> list = new ArrayList<>();
        Conver con = new Conver();
        con.setBuyer("丁丁");
        con.setSeller("代购1号");
        list.add(con);
        con = new Conver();
        con.setBuyer("Selly");
        con.setSeller("代购1号");
        list.add(con);
        con = new Conver();
        con.setBuyer("顾客007");
        con.setSeller("代购7号");
        list.add(con);
        con = new Conver();
        con.setBuyer("点点");
        con.setSeller("代购1号");
        list.add(con);
        con = new Conver();
        con.setBuyer("毛毛");
        con.setSeller("代购3号");
        list.add(con);
        ChatLogAdapter adapter = new ChatLogAdapter(this, list);
        lvChatLog.setAdapter(adapter);
    }
}
