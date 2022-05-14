package com.example.demo.func;

import com.example.demo.service.WebRTCServer;

/**
 * @author chenhonxinh
 */
public class RtcRoom {
    private Integer callerId;
    private Integer receiverId;
    private WebRTCServer ws;

    public RtcRoom(Integer callerId, WebRTCServer ws) {
        this.callerId = callerId;
        this.ws = ws;
    }

    public Integer getCallerId() {
        return callerId;
    }

    public void setCallerId(Integer callerId) {
        this.callerId = callerId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
        ws.startConnect(callerId,receiverId);
        System.out.println("房间: " + callerId+"已准备就绪,开始视频连接");
    }


}
