package com.example.demo.vo;

/**
 * WebSocket操作类
 * @author chenhonxinh
 */
public class WSCmd {
    private String type;
    private Integer targetId;
    private String  data;

    public WSCmd(String type) {
        this.type = type;
    }

    public WSCmd() {
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WSCmd{" +
                "typeCode=" + type +
                ", data='" + data + '\'' +
                '}';
    }
}
