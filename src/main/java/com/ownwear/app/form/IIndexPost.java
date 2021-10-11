package com.ownwear.app.form;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.util.JsonFormat;
import com.ownwear.app.model.User;
import net.sf.json.JSON;
import net.sf.json.JsonConfig;

import java.util.HashMap;
import java.util.Map;

public interface IIndexPost {

    Long getPost_id();
    User getUser();
    Map<String, Map> getImgData();

}
