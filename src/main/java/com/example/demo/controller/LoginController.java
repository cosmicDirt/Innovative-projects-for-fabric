package com.example.demo.controller;

import com.example.demo.HFJavaExample;
import com.google.gson.Gson;
import org.hyperledger.fabric.sdk.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> doLogin(@RequestBody Map<String, String> data) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String status = "";
        String details = "";
        String stringResponse = "";

        if (data.containsKey("name") && data.containsKey("password") && data.containsKey("identity")) {
            Gson gson = new Gson();

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            if (data.get("identity").equals("student"))
                req.setFcn("queryStudentByName");
            else
                req.setFcn("queryTeacherByName");
            req.setArgs(new String[]{data.get("name")});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String, String> result = gson.fromJson(stringResponse, Map.class);

            if (result.isEmpty()) {
                status = "wrong";
                details = "用户不存在";
            } else if (!result.get("password").equals(data.get("password"))) {
                status = "wrong";
                details = "密码错误";
            }
            //用户名与密码正确
            else {
                status = "right";
            }
        } else {
            status = "wrong";
            details = "连接失败";
        }

        map.put("status", status);
        map.put("details", details);

        return map;

    }
}
