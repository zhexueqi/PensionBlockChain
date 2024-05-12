package com.example.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Http方式请求WeBASE-Front
 */
@Service
public class WeBASEUtils {

    @Value("${project.webase-url}")
    String webaseUrl;

    @Value("${system.contract.PensionInsuranceTransferAddress}")
    String contractAddress;

    public static final String ABI = com.example.utils.IOUtil.readResourceAsString("abi/PensionInsuranceTransfer.abi");

    /**
     * 发送 post 请求调用链接口
     * @param userAddress
     * @param funcName
     * @param funcParam
     * @return 请求结果
     */
    public String funcPost(String userAddress, String funcName, List funcParam) {

        JSONArray abiJSON = JSONUtil.parseArray(ABI);
        //负载
        JSONObject data = JSONUtil.createObj();
        data.set("groupId", "1");
        data.set("contractPath", "/");
        data.set("contractAbi", abiJSON);
        data.set("useAes", false);
        data.set("useCns", false);
        data.set("cnsName", "");
        data.set("user", userAddress);
        data.set("contractAddress", contractAddress);
        data.set("funcName", funcName);
        data.set("funcParam", funcParam);

        String dataString = JSONUtil.toJsonStr(data);

        // 创建httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post请求方式实例
        HttpPost httpPost = new HttpPost(webaseUrl);
        // 设置请求头 发送的是json数据格式
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        // 设置参数---设置消息实体 也就是携带的数据
        StringEntity entity = new StringEntity(dataString, Charset.forName("UTF-8"));
        // 设置编码格式
        entity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        entity.setContentType("application/json");
        // 把请求消息实体塞进去
        httpPost.setEntity(entity);
        // 执行http的post请求
        CloseableHttpResponse httpResponse;
        String result = null;

        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
}
