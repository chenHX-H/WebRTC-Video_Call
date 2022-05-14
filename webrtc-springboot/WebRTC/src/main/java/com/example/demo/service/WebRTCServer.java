package com.example.demo.service;

import cn.hutool.json.JSONUtil;

import com.example.demo.func.RtcRoom;
import com.example.demo.vo.WSCmd;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;


/**
 * @author chenhonxinh
 */
@ServerEndpoint("/rtcServer/{userId}/{objectId}")
@Component
public class WebRTCServer {
    private static HashMap<Integer, Session> webSocketsMap=new HashMap();
    private static HashMap<Integer, RtcRoom> videoRooms=new HashMap<>();

    /**后端向前端发送的指令**/
    private final WSCmd WSCmd_ShowText=new WSCmd("SHOW_TEXT");
    private final WSCmd WSCmd_RemoteSDP=new WSCmd("REMOTE_SDP");
    private final WSCmd WsCmd_ICE =new WSCmd("ICE");
    private final WSCmd Notice_Start_Call=new WSCmd("Call");
    private final WSCmd Notice_Start_Receive=new WSCmd("Receive");



    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId,@PathParam("objectId") Integer objectId ){
        webSocketsMap.put(userId,session);
        System.out.println(userId+"上线");
        System.out.println("当前在线人数: " + webSocketsMap.size());
        WSCmd_ShowText.setData("服务器已连接");
        this.sendMsgToOne(userId,JSONUtil.toJsonStr(WSCmd_ShowText));

    }

    @OnMessage
    public void onMessage(String message, Session session,@PathParam("userId") Integer userId,@PathParam("objectId") Integer otherId) {
        System.out.println("收到消息:selfId="+userId+"  otherId="+otherId);
        WSCmd wsCmd=null;
        try {
             wsCmd=JSONUtil.toBean(message,WSCmd.class);
             dealWithCMD(wsCmd,userId,otherId);
            System.out.println("message = " + wsCmd.getType());
        }catch (Exception e){
            e.printStackTrace();
        }


    }



    @OnError
    public void onError(Session session, Throwable error,@PathParam("userId") Integer userId) {
        System.out.println("连接异常...:"+error);
        error.printStackTrace();
        webSocketsMap.remove(userId);

    }

    @OnClose
    public void onClose(@PathParam("userId") Integer userId) {
        webSocketsMap.remove(userId);
        System.out.println("用户"+userId+"连接关闭");
        System.out.println("当前在线数量:"+webSocketsMap.size());
        webSocketsMap.remove(userId);
    }

    public void startConnect(Integer caller,Integer receiver){
        this.sendMsgToOne(caller,JSONUtil.toJsonStr(Notice_Start_Call));
        this.sendMsgToOne(receiver,JSONUtil.toJsonStr(Notice_Start_Receive));
    }


    /**处理收到的指令**/
    private void  dealWithCMD(WSCmd cmd,Integer selfId,Integer otherId){
        switch (cmd.getType()){
            case "Ready":{
                System.out.println(" ready " );
                this.creatOrJoinRoom(selfId,otherId);
                break;
            }
            case "REMOTE_SDP":{
                System.out.println("REMOTE_SDP开始执行");
                WSCmd_RemoteSDP.setData(cmd.getData());
                this.sendMsgToOne(otherId,JSONUtil.toJsonStr(WSCmd_RemoteSDP));
                break;
            }
            case "ICE":{
                System.out.println("ICE开始执行");
                WsCmd_ICE.setData(cmd.getData());
                this.sendMsgToOne(otherId,JSONUtil.toJsonStr(WsCmd_ICE));
                break;
            }
        }
    }

    /**客户端连接 ws后，调用**/
    private void creatOrJoinRoom(Integer selfId,Integer otherId){
        /**设定，先连接服务器的创建房间，后进的作为被呼叫者**/
        //判断对方是否已经在线，true-->Join Room ;false--> create Room
        Session other = webSocketsMap.get(otherId);
        if(other==null){
            //create Room
            videoRooms.put(selfId,new RtcRoom(selfId,this));
        }else{
            //Join Room
            //房主id为key
            videoRooms.get(otherId).setReceiverId(selfId);
        }
    }

    public void sendMsgToOne(Integer targetId,String message){
        try {
            webSocketsMap.get(targetId).getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}