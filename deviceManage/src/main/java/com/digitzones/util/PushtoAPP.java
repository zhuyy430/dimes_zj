package com.digitzones.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;

public class PushtoAPP {    
    //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
	private static String appId = "6IIoU6t3cv95X8wIhUeqw2";
    private static String appKey = "UCFKo9AnjK8Pn8Ro102KX7";
    private static String masterSecret = "B5vo8mAQrl8lJioSVr5Mn3";
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void main(String[] args) throws Exception {
       
    }
    /**
     * 推送方法
     * @param clientIdsList 推送的目标cid
     * @return
     * @throws Exception 
     */
    public static Map<String,Object> pushMessage(List<String> clientIdsList,String title,String content) throws Exception {
    	if(CollectionUtils.isEmpty(clientIdsList)) {
    		throw new RuntimeException("参数不能为空!");
    	}
    	 IGtPush push = new IGtPush(host, appKey, masterSecret);
         NotificationTemplate template = getNotificationTemplate(title,content);
         ListMessage message = new ListMessage();
         message.setData(template);
         message.setOffline(true);
         //离线有效时间，单位为毫秒，可选
         message.setOfflineExpireTime(24 * 1000 * 3600);
         
         List<Target> targetList = new ArrayList<>();
         for(String cid : clientIdsList) {
	         Target target = new Target();
	         target.setAppId(appId);
	         target.setClientId(cid);
	         
	         targetList.add(target);
         }
         String contentId = push.getContentId(message);
         IPushResult ret = push.pushMessageToList(contentId, targetList);
         return ret.getResponse();
    }
    
    @SuppressWarnings("deprecation")
	public static NotificationTemplate getNotificationTemplate(String title,String content) throws Exception {
    	NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTitle(title);
        template.setText(content);
        template.setLogo("icon.png");
        template.setLogoUrl("");
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
       //template.setUrl("http://www.baidu.com");

        return template;
    }
}