package com.example.demo.controller;

import com.example.demo.HFJavaExample;
import com.example.demo.entity.StudentEntity;
import com.google.gson.Gson;
import org.hyperledger.fabric.sdk.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;

@RestController
public class RegisterController {

    @PostMapping("/register")
    @ResponseBody
    public Map<String,Object> register(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("name")&&data.containsKey("password")) {
            String name=data.get("name");
            Gson gson=new Gson();

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryStudentByName");
            req.setArgs(new String[] { name });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String,String> result=gson.fromJson(stringResponse,Map.class);
            if(result!=null)
            {
                status="wrong";
                details="用户名已存在";
            }
            else {
                req.setFcn("createStudent");
                req.setArgs(new String[]{data.get("name"),data.get("stuNumber"),data.get("password")});
                channel.queryByChaincode(req);
                status="right";
                details="注册成功";
            }
        }
        else
        {
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details",details);
        return map;
    }
}
