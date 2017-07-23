package com.dashu.buyer.db;

import android.content.Context;

import com.dashu.buyer.bean.Address;
import com.dashu.buyer.bean.CollectionGoods;
import com.dashu.buyer.bean.Goods;
import com.dashu.buyer.bean.ShopingCar;
import com.dashu.buyer.bean.ShopingGoods;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class DBHelper {

	public String name = "good_db3";
	private int version = 1;

	DbUtils mDbUtils;

	private static DBHelper dbHelper;

	public static DBHelper getInstance(Context context){
		if(dbHelper == null){
			dbHelper = new DBHelper(context);
		}

		return dbHelper;
	}

	private DBHelper(Context context) {
		// TODO Auto-generated constructor stub
		if (mDbUtils == null) {
			mDbUtils = DbUtils.create(context, name, version,
					new MyDbUpdateListener());
		}
	}
	
	/**
	 * ��ȡ�˺ŵ����е�ַ
	 * @return
	 */
	public List<Address> getAllAddress(String account) {
		return null;
	}
	
	/**
	 * ��ӵ�ַ
	 * @return
	 */
	public boolean addAddress(Address mAddress) {
		try {
			return mDbUtils.saveBindingId(mAddress);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


	public void deleteGoodsByName(String name) {
		try {
			WhereBuilder mwhere = WhereBuilder.b("goods_name", "=",
					name);
			mDbUtils.delete(ShopingGoods.class, mwhere);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteGoodshop(ShopingGoods mGoods) {
		try {
			mDbUtils.delete(mGoods);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearShopgoods(String account){
		try {
			WhereBuilder mwhere = WhereBuilder.b("goods_name", "=",
					account);
			mDbUtils.delete(ShopingGoods.class,mwhere);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearAllShopgoods(){
		try {
			mDbUtils.deleteAll(ShopingGoods.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡtype
	 * ���е�goods
	 * @return
	 */
	public List<Goods> getAllGoods(String type) {
		return null;
	}
	
	/**
	 * ��ȡ���һ�ε�shopcar
	 * @return
	 */
	public ShopingCar getShopingCar() {
		try {
//			WhereBuilder mwhere = WhereBuilder.b("account", "=",
//					account);
			List<ShopingCar> carList = mDbUtils.findAll(ShopingCar.class);
			if(carList!=null&&carList.size()>0){
				return carList.get(0);
			}else{
				return null;
			}
				
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public List<CollectionGoods> getAllCollectionGoods() {
		try {
			return mDbUtils.findAll(CollectionGoods.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * �������һ�ι���
	 * @param mCar
	 */
	public void addOrUpdateCar(ShopingCar mCar){
		try {
			mDbUtils.deleteAll(ShopingCar.class);
			mDbUtils.saveOrUpdate(mCar);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*public List<ShopingGoods> getAllShopingGoods() {
		try {
			return mDbUtils.findAll(ShopingGoods.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	
	/**
	 * ��ȡ���еĹ��ﳵ����Ʒ
	 * @return
	 */
	public List<ShopingGoods> getAllShopingGoodsByAccount(String account) {
//		try {
//			WhereBuilder mwhere = WhereBuilder.b("goods_name", "=",
//					account);
//			return mDbUtils.findAll(ShopingGoods.class,mwhere);
//		} catch (DbException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
		return null;
	}

	public boolean addCollection(CollectionGoods goods) {
		try {
			return mDbUtils.saveBindingId(goods);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * �����Ʒ�����ﳵ
	 * @param mGoods
	 */
	public void addShopGoods(ShopingGoods mGoods){
		try {
			Selector selector = Selector.from(ShopingGoods.class);
			selector.where("goods_name", "=",mGoods.getGoods_name());
			ShopingGoods existObjectGoods = mDbUtils.findFirst(selector);
			if(existObjectGoods!=null){
				//exist
				int tobuynum =  existObjectGoods.getToBuyNum()+mGoods.getToBuyNum();
				double sPrice = existObjectGoods.getsPrice();
				existObjectGoods.setToBuyNum(tobuynum);
				mDbUtils.update(existObjectGoods, "toBuyNum");
			}else{
				boolean isSave = mDbUtils.saveBindingId(mGoods);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean deleteCollectionGoods(CollectionGoods mCollectionGoods) {
		try {
			mDbUtils.delete(mCollectionGoods);
			return true;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	class MyDbUpdateListener implements DbUpgradeListener {

		@Override
		public void onUpgrade(DbUtils arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

}
