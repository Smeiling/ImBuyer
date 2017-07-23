package com.dashu.buyer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.CartBean;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.db.DBHelper;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.util.StaticUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsAdapter extends BaseAdapter {
    Context mContext;
    List<Goods> mList;
    Holder mHolder;
    LayoutInflater mInflater;
    BitmapUtils mBitmapUtils;

    private int toBuyNum = 1;
    private String objNum;

    public GoodsAdapter(Context mContext, List<Goods> mList2) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.mList = mList2;
        mInflater = LayoutInflater.from(mContext);
        mBitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Goods getItem(int position) {
        // TODO Auto-generated method stub
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.goods_item, null);
            mHolder = new Holder();
            mHolder.iconImageView = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            mHolder.nameTextView = (TextView) convertView
                    .findViewById(R.id.textView1);
            mHolder.priceTextView = (TextView) convertView
                    .findViewById(R.id.textView2);
            mHolder.numberTextView = (TextView) convertView
                    .findViewById(R.id.textView3);
            mHolder.addButton = (TextView) convertView
                    .findViewById(R.id.button_add);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        final Goods mGoods = getItem(position);
        if (mGoods.getGoodImg() == null) {
            mHolder.iconImageView.setImageResource(R.mipmap.product_default);
            mHolder.iconImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            Log.w("TAG", "url" + mGoods.getGoodImg().getUrl());
            //mHolder.iconImageView.setImageURI(Uri.parse(mGoods.getGoodImg().getUrl()));
            Glide.with(mContext).load(mGoods.getGoodImg().getUrl()).into(mHolder.iconImageView);
        }
        //mBitmapUtils.display(mHolder.iconImageView, mGoods.getPngUrl());
        mHolder.nameTextView.setText("名称：" + mGoods.getName());
        mHolder.priceTextView.setText("价格：" + mGoods.getPrice() + " 元");
        mHolder.numberTextView.setText("库存：" + mGoods.getCount());
        if (StaticUtil.currentUser.getRoleType() == StaticUtil.ROLE_SHOPPER) {
            mHolder.addButton.setVisibility(View.INVISIBLE);
        }
        mHolder.addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(mContext, "add " + mGoods.getName(), Toast.LENGTH_SHORT).show();
                checkCart(mGoods);

            }
        });
        return convertView;
    }

    private void checkCart(final Goods mGoods) {
        BmobQuery<CartBean> query = new BmobQuery<>();
        query.addWhereEqualTo("sellerObjId", mGoods.getSellerObjectId());
        query.addWhereEqualTo("buyerObjId", StaticUtil.currentUser.getObjectId());
        query.findObjects(mContext, new FindListener<CartBean>() {
            @Override
            public void onSuccess(List<CartBean> list) {
                if (list.size() > 0) {
                    checkExistGood(mGoods, list.get(0).getObjectId());
                } else {
                    createNewCart(mGoods);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.w(StaticUtil.TAG, "error:" + i + s);
            }
        });
    }

    private void createNewCart(final Goods mGoods) {
        CartBean cart = new CartBean();
        cart.setBuyerObjId(StaticUtil.currentUser.getObjectId());
        cart.setSellerObjId(mGoods.getSellerObjectId());
        cart.setSellerName(mGoods.getSellerName());
        cart.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                checkCart(mGoods);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void checkExistGood(final Goods mGoods, final String cartObjId) {
        toBuyNum = 1;
        BmobQuery<ShopingGoods> query = new BmobQuery<>();
        query.addWhereEqualTo("goodObjId", mGoods.getObjectId());
        query.addWhereEqualTo("ownerObjId", StaticUtil.currentUser.getObjectId());
        query.findObjects(mContext, new FindListener<ShopingGoods>() {
            @Override
            public void onSuccess(List<ShopingGoods> list) {
                Log.w("TAG", "size" + list.size());
                addToBuyNum(list);
                addToCart(mGoods, cartObjId);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("TAG", "error = " + i + s);
            }
        });
    }

    private void addToCart(Goods mGoods, String cartObjId) {
        //checkExistGood(mGoods);
        Log.w("TAG", "toBuyNum" + toBuyNum);
        if (toBuyNum == 1) {
            ShopingGoods mShopingGoods = new ShopingGoods();
            mShopingGoods.setGoods_name(mGoods.getName());
            mShopingGoods.setsPrice(mGoods.getPrice());
            mShopingGoods.setPngUrl(mGoods.getGoodImg() == null ? "" : mGoods.getGoodImg().getUrl());
            mShopingGoods.setToBuyNum(toBuyNum);
            mShopingGoods.setGoodObjId(mGoods.getObjectId());
            mShopingGoods.setSellerObjId(mGoods.getSellerObjectId());
            mShopingGoods.setOwnerObjId(StaticUtil.currentUser.getObjectId());
            mShopingGoods.setCartObjId(cartObjId);
            //DBHelper.getInstance(this).addShopGoods(mShopingGoods);
            mShopingGoods.save(mContext, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(mContext, R.string.add_cart_success, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(mContext, R.string.add_cart_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } else {
            ShopingGoods mShopingGoods = new ShopingGoods();
            mShopingGoods.setToBuyNum(toBuyNum);
            mShopingGoods.update(mContext, objNum, new UpdateListener() {
                @Override
                public void onSuccess() {
                    Log.w("TAG", "update success");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.w("TAG", "update fail");
                }
            });
        }
    }


    private void addToBuyNum(List<ShopingGoods> list) {
        Log.w("TAG", "addToBuyNum" + list.size());
        if (list.size() > 0) {
            toBuyNum = list.get(0).getToBuyNum() + 1;
            objNum = list.get(0).getObjectId();
        }
    }

    class Holder {
        ImageView iconImageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView numberTextView;
        TextView addButton;
    }

}
