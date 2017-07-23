package com.dashu.buyer.util;

import android.content.Context;

import com.dashu.buyer.R;
import com.dashu.buyer.bean.Goods;

import java.util.ArrayList;
import java.util.List;


public class DataUtils {

    String[] picStrings = {
            "http://wenwen.soso.com/p/20100408/20100408171939-719388888.jpg",
            "http://www.jpcai.com/upfiles/photo/200606/20060624180914512.jpg",

    };

    static String[] giftString = {
            "http://img.iv.com.cn/miniblog/201202/2006/75/3cd3058ecae79c542a6d10266251dcc9_big.jpg",
            "http://img.hc360.com/av/info/images/200912/200912211000407450.jpg",
            "http://imgtest.meiliworks.com/pic/_o/a4/87/45db6966b5653bda8386afc98d07_750_750.jpg",
            "http://d06.res.meilishuo.net/pic/_o/f3/77/81f4a5b5f28c6c4304c7bf805deb_599_592.jpg",
            "http://d03.res.meilishuo.net/pic/_o/62/b1/04434ddcbb3bef405937ba35cb2f_500_363.jpeg",
            "http://img.iv.com.cn/miniblog/201202/2006/75/3cd3058ecae79c542a6d10266251dcc9_big.jpg",
            "http://img.hc360.com/av/info/images/200912/200912211000407450.jpg",
            "http://imgtest.meiliworks.com/pic/_o/a4/87/45db6966b5653bda8386afc98d07_750_750.jpg",
            "http://d06.res.meilishuo.net/pic/_o/f3/77/81f4a5b5f28c6c4304c7bf805deb_599_592.jpg",
            "http://d03.res.meilishuo.net/pic/_o/62/b1/04434ddcbb3bef405937ba35cb2f_500_363.jpeg",
            "http://img.iv.com.cn/miniblog/201202/2006/75/3cd3058ecae79c542a6d10266251dcc9_big.jpg",
            "http://img.hc360.com/av/info/images/200912/200912211000407450.jpg",
            "http://imgtest.meiliworks.com/pic/_o/a4/87/45db6966b5653bda8386afc98d07_750_750.jpg",
            "http://d06.res.meilishuo.net/pic/_o/f3/77/81f4a5b5f28c6c4304c7bf805deb_599_592.jpg",
            "http://d03.res.meilishuo.net/pic/_o/62/b1/04434ddcbb3bef405937ba35cb2f_500_363.jpeg",
            "http://img.iv.com.cn/miniblog/201202/2006/75/3cd3058ecae79c542a6d10266251dcc9_big.jpg",
            "http://img.hc360.com/av/info/images/200912/200912211000407450.jpg",
            "http://imgtest.meiliworks.com/pic/_o/a4/87/45db6966b5653bda8386afc98d07_750_750.jpg",
            "http://d06.res.meilishuo.net/pic/_o/f3/77/81f4a5b5f28c6c4304c7bf805deb_599_592.jpg",
            "http://d03.res.meilishuo.net/pic/_o/62/b1/04434ddcbb3bef405937ba35cb2f_500_363.jpeg",};

    static String[] clothStrings = {
            "http://cdn.duitang.com/uploads/item/201202/26/20120226020649_WknMA.jpg",
            "http://pic18.nipic.com/20111213/9012800_192140027338_2.jpg",
            "http://pic1a.nipic.com/2009-01-12/20091123926894_2.jpg",
            "http://pic22.nipic.com/20120719/7892341_141619192135_2.jpg",
            "http://pic2.ooopic.com/10/21/57/75b1OOOPICb3.jpg",
            "http://pic16.nipic.com/20110820/7475577_135430581175_2.jpg",
            "http://pic1a.nipic.com/2009-01-12/20091123926894_2.jpg",
            "http://pic22.nipic.com/20120719/7892341_141619192135_2.jpg",
            "http://pic2.ooopic.com/10/21/57/75b1OOOPICb3.jpg",
            "http://pic16.nipic.com/20110820/7475577_135430581175_2.jpg",
            "http://cdn.duitang.com/uploads/item/201202/26/20120226020649_WknMA.jpg",
            "http://pic18.nipic.com/20111213/9012800_192140027338_2.jpg",
            "http://pic1a.nipic.com/2009-01-12/20091123926894_2.jpg",
            "http://pic22.nipic.com/20120719/7892341_141619192135_2.jpg",
            "http://pic2.ooopic.com/10/21/57/75b1OOOPICb3.jpg",
            "http://pic16.nipic.com/20110820/7475577_135430581175_2.jpg",
            "http://pic1a.nipic.com/2009-01-12/20091123926894_2.jpg",
            "http://pic22.nipic.com/20120719/7892341_141619192135_2.jpg",
            "http://pic2.ooopic.com/10/21/57/75b1OOOPICb3.jpg",
            "http://pic16.nipic.com/20110820/7475577_135430581175_2.jpg",};

    static String[] homeStrings = {
            "http://img03.tooopen.com/images/20130522/tooopen_09040920.jpg",
            "http://pic1a.nipic.com/2009-02-17/200921785019121_2.jpg",
            "http://pic.sosucai.com/b/2010_01/b_36860_jpg_20100116114412.jpg",
            "http://img4.duitang.com/uploads/item/201407/22/20140722150622_wusvw.thumb.700_0.jpeg",
            "http://pic9.nipic.com/20100808/4800623_121420073862_2.jpg",
            "http://img03.tooopen.com/images/20130522/tooopen_09040920.jpg",
            "http://pic1a.nipic.com/2009-02-17/200921785019121_2.jpg",
            "http://pic.sosucai.com/b/2010_01/b_36860_jpg_20100116114412.jpg",
            "http://img4.duitang.com/uploads/item/201407/22/20140722150622_wusvw.thumb.700_0.jpeg",
            "http://pic9.nipic.com/20100808/4800623_121420073862_2.jpg",
            "http://img03.tooopen.com/images/20130522/tooopen_09040920.jpg",
            "http://pic1a.nipic.com/2009-02-17/200921785019121_2.jpg",
            "http://pic.sosucai.com/b/2010_01/b_36860_jpg_20100116114412.jpg",
            "http://img4.duitang.com/uploads/item/201407/22/20140722150622_wusvw.thumb.700_0.jpeg",
            "http://pic9.nipic.com/20100808/4800623_121420073862_2.jpg",
            "http://img03.tooopen.com/images/20130522/tooopen_09040920.jpg",
            "http://pic1a.nipic.com/2009-02-17/200921785019121_2.jpg",
            "http://pic.sosucai.com/b/2010_01/b_36860_jpg_20100116114412.jpg",
            "http://img4.duitang.com/uploads/item/201407/22/20140722150622_wusvw.thumb.700_0.jpeg",
            "http://pic9.nipic.com/20100808/4800623_121420073862_2.jpg",

    };

    public static List<Goods> getGoods(Context context, int type) {
        List<Goods> mGoodList = new ArrayList<Goods>();
        for (int i = 0; i < 10; i++) {
            Goods mGoods = new Goods();
            mGoods.setAddress("city,street,house");
            mGoods.setPrice(10f + i * 0.1f);

            mGoods.setDate("20140922");
            mGoods.setWeight((12 * i) + "g");
            mGoods.setContent("content");
            String name = "";
            switch (type) {
                case 1:
                    name = context.getString(R.string.type_home);
                    mGoods.setPngUrl(homeStrings[i]);
                    break;
                case 2:
                    name = context.getString(R.string.type_clothes);
                    mGoods.setPngUrl(clothStrings[i]);
                    break;
                case 3:
                    name = context.getString(R.string.type_gift);
                    mGoods.setPngUrl(giftString[i]);
                    break;
            }
            mGoods.setName(name + i);
            mGoods.setType(-1);
            mGoods.setSellerObjectId("2dc8f7d7a0");
            mGoods.setSellerName("13632772064");
            mGoodList.add(mGoods);

        }

        return mGoodList;

    }


}
