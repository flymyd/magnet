package cn.ac.van.magnet.bean;

import java.util.HashMap;

public class NormalResp {
    public static HashMap ok(Object data) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", 0);
        hashMap.put("msg", "成功");
        hashMap.put("data", data);
        return hashMap;
    }

    public static HashMap failed() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", -1);
        hashMap.put("msg", "失败");
        hashMap.put("data", null);
        return hashMap;
    }
}
