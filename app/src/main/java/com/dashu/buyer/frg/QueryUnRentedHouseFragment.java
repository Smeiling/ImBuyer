package com.dashu.buyer.frg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dashu.buyer.R;
import com.dashu.buyer.adapter.EmptyHouseAdapter;
import com.dashu.buyer.bean.Goods;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 */
public class QueryUnRentedHouseFragment extends Fragment {


    public QueryUnRentedHouseFragment() {
        // Required empty public constructor
    }


    ListView listview = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_query_un_rented_house, container, false);
        listview = (ListView) view.findViewById(R.id.listView_empty_house);
        Query();
        return view;
    }

    private void Query(){
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addWhereEqualTo("houseRentNumber", 0);
        //执行查询方法
        query.findObjects(getActivity(), new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0) {
                    //initlistview
                    Log.e("tag","empty house number == "+object.size());
                    initListView(object);

                }else {
                    Log.e("tag", "There is no empty house ");
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
//                ToastUtils.showTip(SystemMainActivity.this, "查询失败：" + msg);

            }
        });
    }

    EmptyHouseAdapter emptyHouseAdapter;
    private void initListView(List<Goods> object){
        emptyHouseAdapter = new EmptyHouseAdapter(getActivity(),object);
        listview.setAdapter(emptyHouseAdapter);
    }

}
