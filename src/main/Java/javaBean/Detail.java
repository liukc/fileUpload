package javaBean;

import java.util.HashMap;
import java.util.Map;

public class Detail {
    private int status;
    private String msg;
    private Map map;

    public Detail(){
        map = new HashMap<String, Object>();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
