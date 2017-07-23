package com.dashu.buyer.im;

import android.content.Context;
import android.content.Intent;

import com.dashu.buyer.MainActivity;
import com.dashu.buyer.bean.User;
import com.dashu.buyer.util.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**消息接收器
 * @author smile
 * @project DemoMessageHandler
 * @date 2016-03-08-17:37
 */
public class DemoMessageHandler extends BmobIMMessageHandler{

    private Context context;

    public DemoMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //当接收到服务器发来的消息时，此方法被调用
        Logger.i(event.getConversation().getConversationTitle() + "," + event.getMessage().getMsgType() + "," + event.getMessage().getContent());
        excuteMessage(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String,List<MessageEvent>> map =event.getEventMap();
        Logger.i("离线消息属于" + map.size() + "个用户");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list =entry.getValue();
            int size = list.size();
            for(int i=0;i<size;i++){
                excuteMessage(list.get(i));
            }
        }
    }


    /**
     * 处理消息
     * @param event
     */
    private void excuteMessage(final MessageEvent event){
        final BmobIMConversation conversation=event.getConversation();
        final BmobIMUserInfo info =event.getFromUserInfo();
        final BmobIMMessage msg =event.getMessage();
        String username =info.getName();
        String title =conversation.getConversationTitle();
        Logger.i("" + username + "," + title);
        //sdk内部，将新会话的会话标题用objectId表示，因此需要比对用户名和会话标题--单聊，后续会根据会话类型进行判断
        if(!username.equals(title)) {

            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId", info.getUserId());
            query.findObjects(context, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if(list!=null && list.size()>0){
                        User s = list.get(0);
                        String name =s.getUsername();
                        String avatar = s.getAvatar();
                        Logger.i("query success："+name+","+avatar);
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //更新用户资料
                        BmobIM.getInstance().updateUserInfo(info);
                        //更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if(!msg.isTransient()){
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    }else{
                        Logger.e("查无此人");
                    }


                    BmobIMMessage msg = event.getMessage();
                    if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
                        processCustomMessage(msg, event.getFromUserInfo());
                    } else {//SDK内部内部支持的消息类型
                        if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
                            Intent pendingIntent = new Intent(context, MainActivity.class);
                            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
                            BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
                            //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
//                        BmobIMUserInfo info =event.getFromUserInfo();
//                        //这里可以是应用图标，也可以将聊天头像转成bitmap
//                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                                info.getName(),msg.getContent(),"您有一条新消息",pendingIntent);
                        } else {//直接发送消息事件
                            Logger.i("当前处于应用内，发送event");
                            EventBus.getDefault().post(event);
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Logger.e(s);
                }
            });

        }



    }

    /**
     * 处理自定义消息类型
     * @param msg
     */
    private void processCustomMessage(BmobIMMessage msg,BmobIMUserInfo info){
        //自行处理自定义消息类型
        Logger.i(msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra());
//        String type =msg.getMsgType();
//        //发送页面刷新的广播
//        EventBus.getDefault().post(new RefreshEvent());
//        //处理消息
//        if(type.equals("add")){//接收到的添加好友的请求
//            NewFriend friend = AddFriendMessage.convert(msg);
//            //本地好友请求表做下校验，本地没有的才允许显示通知栏--有可能离线消息会有些重复
//            long id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(friend);
//            if(id>0){
//                showAddNotify(friend);
//            }
//        }else if(type.equals("agree")){//接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
//            AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(msg);
//            addFriend(agree.getFromId());//添加消息的发送方为好友
//            //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
//            showAgreeNotify(info,agree);
//        }else{
//            Toast.makeText(context,"接收到的自定义消息："+msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra(),Toast.LENGTH_SHORT).show();
//        }
    }

}