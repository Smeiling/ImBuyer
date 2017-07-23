package com.dashu.buyer.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dashu.buyer.R;

public class RecordTableFragment extends Fragment {

    ListView  listView;
    TextView emptyTv;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_table, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.person_complain_listview);
        emptyTv = (TextView) view.findViewById(R.id.empty_tv);
        listView.setEmptyView(emptyTv);
    }


}
