//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.alibaba.cloudapi.client;
import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.WebSocketApiType;
import com.alibaba.fastjson.JSONObject;


public class HttpApiClienttest extends ApacheHttpClient{
    public final static String HOST = "e138e00f018f4c578509a4e637c57dee-cn-hangzhou.alicloudapi.com";
    static HttpApiClienttest instance = new HttpApiClienttest();
    public static HttpApiClienttest getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }




    public void 台风预测(String token , String country , ApiCallback callback) {
        String path = "/getTypon/[token]";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("token" , token , ParamPosition.PATH , true);
        request.addParam("country" , country , ParamPosition.HEAD , false);



        sendAsyncRequest(request , callback);
    }

    public ApiResponse 台风预测SyncMode(String token , String country) {
        String path = "/getTypon/[token]";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("token" , token , ParamPosition.PATH , true);
        request.addParam("country" , country , ParamPosition.HEAD , false);



        return sendSyncRequest(request);
    }

}