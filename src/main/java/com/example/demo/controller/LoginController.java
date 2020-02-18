package com.example.demo.controller;

import com.example.demo.HFJavaExample;
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
        String stringResponse="";

        if (data.containsKey("test")) {
            String name = data.get("test");
            HFClient client=HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("SimpleChainCode").build();
            req.setChaincodeID(cid);
            req.setFcn("query");
            req.setArgs(new String[] { name });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            System.out.println(stringResponse);
           // Gson gson=new Gson();
            //StudentEntity studentEntity=gson.fromJson(stringResponse,StudentEntity.class);
            /*if (list.isEmpty()) {
                status = "wrong";
                details = "用户不存在";
            }*/
          /*  if (!studentEntity.getUserPassword().equals(data.get("password"))) {
                status = "wrong";
                details = "密码错误";
            }
            //用户名与密码正确
            else {
                status = "right";
            }*/
        } else {
            status = "wrong";
            details = "连接失败";
        }

        map.put("status", status);
        map.put("details", details);

        return map;

    }
}
