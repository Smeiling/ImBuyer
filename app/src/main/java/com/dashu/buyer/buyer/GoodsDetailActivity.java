package com.dashu.buyer.buyer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dashu.buyer.R;
import com.dashu.buyer.bean.CartBean;
import com.dashu.buyer.bean.CollectionGoods;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.ShopingGoods;
import com.dashu.buyer.im.ui.ChatActivity;
import com.dashu.buyer.util.StaticUtil;
import com.dashu.buyer.util.ToastUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 *
 */
public class GoodsDetailActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "GoodsDetailActivity";

    private String gameid;
    private Goods mGoods;
    private EditText nameTextView;
    private EditText countTextView;
    private EditText innerTextView;
    private EditText priceTextView;
    private EditText addressTextView;
    private EditText timeTextView;
    private EditText contentTextView;
    private TextView sellerTextView;
    private EditText sortTextView;
    private ImageView mImageView;
    private BitmapUtils mBitmapUtils;
    private TextView collectBtn;
    private TextView cartBtn;
    private TextView editBtn;
    private TextView downBtn;
    private TextView saveBtn;
    private CollectionGoods cGood;
    private int toBuyNum = 1;
    private String objNum;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        mBitmapUtils = new BitmapUtils(this);
        mGoods = (Goods) getIntent().getSerializableExtra("data");
        cGood = (CollectionGoods) getIntent().getSerializableExtra("col_data");
        mImageView = (ImageView) findViewById(R.id.imageview_detail);
        //mBitmapUtils.display(mImageView, mGoods.getPngUrl());
        if (cGood != null) {
            searchForGood();
        } else if (mGoods != null) {
            InitTextView();
            initContent();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void searchForGood() {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", cGood.getGoodObjId());
        query.findObjects(this, new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                mGoods = list.get(0);
                InitTextView();
                initContent();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void InitTextView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameTextView = (EditText) findViewById(R.id.textView_name);
        priceTextView = (EditText) findViewById(R.id.textView_price);

        timeTextView = (EditText) findViewById(R.id.textView_date);
        addressTextView = (EditText) findViewById(R.id.textView_address);
        countTextView = (EditText) findViewById(R.id.textView_count);
        innerTextView = (EditText) findViewById(R.id.textView_inner);
        contentTextView = (EditText) findViewById(R.id.textView_content);
        collectBtn = (TextView) findViewById(R.id.add_collection);
        cartBtn = (TextView) findViewById(R.id.add_cart);
        sellerTextView = (TextView) findViewById(R.id.textView_seller);

        saveBtn = (TextView) findViewById(R.id.tv_commit);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoodInfo();
            }
        });
        editBtn = (TextView) findViewById(R.id.tv_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtn.setEnabled(false);
                saveBtn.setVisibility(View.VISIBLE);
                nameTextView.setEnabled(true);
                countTextView.setEnabled(true);
                innerTextView.setEnabled(true);
                priceTextView.setEnabled(true);
                addressTextView.setEnabled(true);
                timeTextView.setEnabled(true);
                contentTextView.setEnabled(true);
                collectBtn.setEnabled(true);
                cartBtn.setEnabled(true);
            }
        });
        downBtn = (TextView) findViewById(R.id.down_btn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoods.delete(getApplicationContext(), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("TAG", "移除收藏成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("TAG", "delete fail");
                    }
                });
            }
        });
        sellerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: chat
                Log.w(StaticUtil.TAG, "onsellertextview click");
                //doChat(mGoods);
            }
        });
        sortTextView = (EditText) findViewById(R.id.textView_sort);
        collectBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);
        if (StaticUtil.currentUser.getRoleType() == 2) {
            collectBtn.setVisibility(View.GONE);
            cartBtn.setVisibility(View.GONE);
        } else {
            downBtn.setVisibility(View.GONE);
        }
        customizeView();
    }

    private void doChat(Goods goods) {

        final BmobIMUserInfo info = new BmobIMUserInfo();
        info.setUserId(goods.getSellerObjectId());
        info.setName(goods.getSellerName());
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    //在此跳转到聊天页面
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(intent, bundle);
                } else {
                    Log.w("TAG", e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });

    }

    private void customizeView() {
        if (StaticUtil.currentUser.getRoleType() == StaticUtil.ROLE_USER) {
            editBtn.setVisibility(View.GONE);
            downBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.INVISIBLE);
            nameTextView.setEnabled(false);
            countTextView.setEnabled(false);
            innerTextView.setEnabled(false);
            priceTextView.setEnabled(false);
            addressTextView.setEnabled(false);
            timeTextView.setEnabled(false);
            contentTextView.setEnabled(false);
            collectBtn.setEnabled(true);
            cartBtn.setEnabled(true);
        } else if (StaticUtil.currentUser.getRoleType() == StaticUtil.ROLE_SHOPPER) {
            editBtn.setVisibility(View.VISIBLE);
            downBtn.setVisibility(View.VISIBLE);
            collectBtn.setVisibility(View.GONE);
            cartBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.INVISIBLE);
            nameTextView.setEnabled(false);
            countTextView.setEnabled(false);
            innerTextView.setEnabled(false);
            priceTextView.setEnabled(false);
            addressTextView.setEnabled(false);
            timeTextView.setEnabled(false);
            contentTextView.setEnabled(false);
            collectBtn.setEnabled(false);
            sortTextView.setEnabled(false);
            cartBtn.setEnabled(false);
        } else {
            editBtn.setVisibility(View.VISIBLE);
            downBtn.setVisibility(View.VISIBLE);
            collectBtn.setVisibility(View.GONE);
            cartBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
            nameTextView.setEnabled(false);
            countTextView.setEnabled(false);
            innerTextView.setEnabled(false);
            priceTextView.setEnabled(false);
            addressTextView.setEnabled(false);
            timeTextView.setEnabled(false);
            contentTextView.setEnabled(false);
            collectBtn.setEnabled(false);
            cartBtn.setEnabled(false);
        }
    }

    private void saveGoodInfo() {
        Goods goods = mGoods;
        goods.setName(nameTextView.getText().toString());
        goods.setWeight(innerTextView.getText().toString());
        goods.setPrice(Float.parseFloat(priceTextView.getText().toString()));
        goods.setDate(timeTextView.getText().toString());
        goods.setAddress(addressTextView.getText().toString());
        goods.setCount(Integer.parseInt(countTextView.getText().toString()));
        goods.setContent(contentTextView.getText().toString());
        goods.update(getApplicationContext(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "商品信息更新成功！", Toast.LENGTH_SHORT).show();
                resetView();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "更新失败！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void resetView() {
        editBtn.setEnabled(true);
        saveBtn.setVisibility(View.INVISIBLE);
        nameTextView.setEnabled(false);
        countTextView.setEnabled(false);
        innerTextView.setEnabled(false);
        priceTextView.setEnabled(false);
        addressTextView.setEnabled(false);
        timeTextView.setEnabled(false);
        contentTextView.setEnabled(false);
        collectBtn.setEnabled(false);
        cartBtn.setEnabled(false);
    }


    private void initContent() {
        nameTextView.setText(mGoods.getName());
        countTextView.setText(String.valueOf(mGoods.getType()));
        innerTextView.setText(mGoods.getWeight());
        priceTextView.setText(String.valueOf(mGoods.getPrice()));
        timeTextView.setText(mGoods.getDate());
        addressTextView.setText(mGoods.getAddress());
        if (mGoods.getGoodImg() != null) {
            Glide.with(this).load(mGoods.getGoodImg().getUrl()).into(mImageView);
        } else {
            mImageView.setImageResource(R.mipmap.product_default);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        contentTextView.setText(mGoods.getContent());
        sellerTextView.setText(mGoods.getSellerName());
        if (mGoods.getType() == 0) {
            sortTextView.setText("彩妆");
        } else if (mGoods.getType() == 1) {
            sortTextView.setText("护肤品");
        } else {
            sortTextView.setText("香水");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void addToBuyNum(List<ShopingGoods> list) {
        if (list.size() > 0) {
            toBuyNum = list.get(0).getToBuyNum() + 1;
            objNum = list.get(0).getObjectId();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_collection:
                Log.w(TAG,"add collection clicked");
                checkExistCollection();
                break;
            case R.id.add_cart:
                Log.w(TAG,"add cart clicked");
                checkCart(mGoods);
                break;
        }
    }

    private void checkCart(final Goods mGoods) {
        BmobQuery<CartBean> query = new BmobQuery<>();
        query.addWhereEqualTo("sellerObjId", mGoods.getSellerObjectId());
        query.addWhereEqualTo("buyerObjId", StaticUtil.currentUser.getObjectId());
        query.findObjects(this, new FindListener<CartBean>() {
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
        cart.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                checkCart(mGoods);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void addCollection(List<CollectionGoods> list) {
        if (list.size() > 0) {
            ToastUtils.showTip(this, "已存在");
            return;
        }
        CollectionGoods mCollectionGoods = new CollectionGoods();
        mCollectionGoods.setName(mGoods.getName());
        mCollectionGoods.setPrice(mGoods.getPrice());
        mCollectionGoods.setPngUrl(mGoods.getGoodImg() == null ? "" : mGoods.getGoodImg().getUrl());
        mCollectionGoods.setOwnerObjId(StaticUtil.currentUser.getObjectId());
        mCollectionGoods.setGoodObjId(mGoods.getObjectId());
        mCollectionGoods.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.w("TAG", "success");
                Toast.makeText(getApplicationContext(), R.string.add_coll_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), R.string.add_coll_fail + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkExistCollection() {
        BmobQuery<CollectionGoods> query = new BmobQuery<>();
        query.addWhereEqualTo("ownerObjId", StaticUtil.currentUser.getObjectId());
        query.addWhereEqualTo("goodObjId", mGoods.getObjectId());
        query.findObjects(this, new FindListener<CollectionGoods>() {
            @Override
            public void onSuccess(List<CollectionGoods> list) {
                Log.d("TAG", "finded");
                addCollection(list);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("TAG", "fail " + i + s);
            }
        });
    }

    private void checkExistGood(final Goods mGoods, final String cartObjId) {
        toBuyNum = 1;
        BmobQuery<ShopingGoods> query = new BmobQuery<>();
        query.addWhereEqualTo("goodObjId", mGoods.getObjectId());
        query.addWhereEqualTo("ownerObjId", StaticUtil.currentUser.getObjectId());
        query.findObjects(this, new FindListener<ShopingGoods>() {
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


    private void addToCart(Goods goods, String cartObjId) {

        if (toBuyNum == 1) {
            ShopingGoods mShopingGoods = new ShopingGoods();
            mShopingGoods.setGoods_name(mGoods.getName());
            mShopingGoods.setsPrice(mGoods.getPrice());
            mShopingGoods.setPngUrl(mGoods.getGoodImg() == null ? "" : mGoods.getGoodImg().getUrl());
            mShopingGoods.setToBuyNum(toBuyNum);
            mShopingGoods.setGoodObjId(mGoods.getObjectId());
            mShopingGoods.setCartObjId(cartObjId);
            mShopingGoods.setSellerObjId(mGoods.getSellerObjectId());
            mShopingGoods.setOwnerObjId(StaticUtil.currentUser.getObjectId());
            //DBHelper.getInstance(this).addShopGoods(mShopingGoods);
            mShopingGoods.save(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), R.string.add_cart_success, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(getApplicationContext(), R.string.add_cart_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } else {
            ShopingGoods mShopingGoods = new ShopingGoods();
            mShopingGoods.setToBuyNum(toBuyNum);
            mShopingGoods.setsPrice(mGoods.getPrice() * toBuyNum);
            mShopingGoods.update(this, objNum, new UpdateListener() {
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GoodsDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
