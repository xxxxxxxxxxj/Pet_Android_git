package com.haotang.pet.view;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
/**
 * Notification类，既可用系统默认的通知布局，也可以用自定义的布局
 * 
 * @author lz
 *
 */
public class MCommonNotification {
    Context mContext;   //Activity或Service上下文
    Notification notification;  //notification
    NotificationManager nm; 
    RemoteViews remoteView=null;  //自定义的通知栏视图
    /**
     * 
     * @param context Activity或Service上下文
     */
    public MCommonNotification(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext=context;
        this.nm=(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE); 
    }
     
    /**
     * 显示自定义通知
     * @param icoId 自定义视图中的图片ID
     * @param titleStr 通知栏标题
     * @param layoutId 自定义布局文件ID
     */
    public void showCustomizeNotification(int icoId,String titleStr,String contentStr,
    		Intent intent,int layoutId) {  
//        notification=new Notification(icoId, titleStr, when);
//        notification.defaults |= Notification.DEFAULT_SOUND; 
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        notification.flags |= Notification.FLAG_HIGH_PRIORITY;
//        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, 0);
//        notification.contentIntent=pi;  
        
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(mContext);
        
        notifBuilder.setTicker(contentStr);//设置提示
        notifBuilder.setSmallIcon(icoId);
        notifBuilder.setContentTitle(titleStr);
        notifBuilder.setContentText(contentStr);
        notifBuilder.setWhen(System.currentTimeMillis());
        notifBuilder.setAutoCancel(true);
//        notifBuilder.setOngoing(true);//设置为常驻通知
        notifBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        notifBuilder.setDefaults(Notification.DEFAULT_ALL);
        int requestCode = (int) SystemClock.uptimeMillis(); 
        PendingIntent pi = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notifBuilder.setContentIntent(pi);
        // 1、创建一个自定义的消息布局 view.xml  
        // 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段  
//        if(remoteView==null)
//        {
//            remoteView = new RemoteViews(mContext.getPackageName(),layoutId);  
//            remoteView.setImageViewResource(R.id.ivNotification,icoId);  
//            remoteView.setTextViewText(R.id.tvTitle, titleStr); 
//            remoteView.setTextViewText(R.id.tvTip, contentStr); 
//        } 
//        NotificationCompat.BigPictureStyle picStyle = new NotificationCompat.BigPictureStyle();
//        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), icoId);
//        picStyle.bigPicture(bmp);
        NotificationCompat.BigTextStyle txtStyle = new NotificationCompat.BigTextStyle();
        txtStyle.bigText(contentStr);
        notifBuilder.setStyle(txtStyle);
//        notifBuilder.setContent(remoteView);
        nm.notify(1, notifBuilder.build());
    }  
    
     
    /**
     * 移除通知
     */
    public void removeNotification()  
    {  
        // 取消的只是当前Context的Notification  
        nm.cancel(1);  
    }  
    
       
}
