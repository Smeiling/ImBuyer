package com.dashu.buyer.buyer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dashu.buyer.buyer.GoodsDetailActivity;
import com.dashu.buyer.R;
import com.dashu.buyer.adapter.CollectionGoodsAdapter;
import com.dashu.buyer.bean.CollectionGoods;
import com.dashu.buyer.db.DBHelper;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.view.CustomProgressDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CollectionFragment extends Fragment {

    ListView mListView;
    private List<CollectionGoods> list;
    private List<CollectionGoods> collectionList;
    private CollectionGoodsAdapter collectionAdapter;
    private CustomProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_collection, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        getCollectionList();
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview_collections);
    }

    private void getCollectionList() {
        progressDialog = CustomProgressDialog.createDialog(getActivity());
        progressDialog.show();
        BmobQuery<CollectionGoods> query = new BmobQuery<>();
        query.addWhereEqualTo("ownerObjId", StaticUtil.currentUser.getObjectId());
        query.findObjects(getActivity(), new FindListener<CollectionGoods>() {
            @Override
            public void onSuccess(List<CollectionGoods> list) {
                initListView(list);
            }

            @Override
            public void onError(int i, String s) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                Log.w("TAG", "searchError" + i + s);
            }
        });
    }

    private void initListView(final List<CollectionGoods> mList) {
        list = mList;
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        collectionAdapter = new CollectionGoodsAdapter(getActivity(), mList);
        mListView.setAdapter(collectionAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(getActivity(), GoodsDetailActivity.class);
                mIntent.putExtra("col_data", mList.get(position));
                startActivity(mIntent);
            }
        });

    }


}
