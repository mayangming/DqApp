package com.dq.im.model;

import com.dq.im.util.ImContentFactory;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * 自定义解析器
 */
public class ImContentDeserializer implements JsonDeserializer<P2PMessageBaseModel> {
    private Gson gson = new Gson();
    public ImContentDeserializer(){

    }
    @Override
    public P2PMessageBaseModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        P2PMessageBaseModel p2PMessageBaseModel = gson.fromJson(json, P2PMessageBaseModel.class);
        JsonObject jsonObject = json.getAsJsonObject();
        String content = "";
        IMContentDataModel imContentDataModel = null;
        if (jsonObject.has("contentData")){
            content = jsonObject.get("contentData").toString();
            p2PMessageBaseModel.setSourceContent(content);
        }
        imContentDataModel = ImContentFactory.getIMContentDataModel(p2PMessageBaseModel,content);
        p2PMessageBaseModel.setContentData(imContentDataModel);
        return p2PMessageBaseModel;
    }

}