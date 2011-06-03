package com.josephblough.alibris.tasks;

import org.json.JSONObject;

public interface DataReceiver {

    public void dataReceived(JSONObject data);
    public void error(String error);
}
