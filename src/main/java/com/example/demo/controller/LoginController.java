package com.example.demo.controller;

import com.example.demo.HFJavaExample;
import com.google.gson.Gson;
import org.hyperledger.fabric.sdk.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
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

            if (result==null) {
                status = "wrong";
                details = "用户不存在";
            } else if (!result.get("password").equals(data.get("password"))) {
                status = "wrong";
                details = "密码错误";
            }
            else if((data.get("identity").equals("student")&&!result.containsKey("stuNumber"))||
                    (data.get("identity").equals("teacher")&&!result.containsKey("teaNumber"))){
                status = "wrong";
                details="身份与账号不一致";
            }
            else {
                status="right";
            }
        } else {
            status = "wrong";
            details = "连接失败";
        }

        map.put("status", status);
        map.put("details", details);

        return map;

    }

    @PostMapping("/fixstuinfo")
    @ResponseBody
    public Map<String, Object> fixstuinfo(@RequestBody Map<String, String> data) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String status = "";
        String details = "";
        String stringResponse = "";

        if (data.containsKey("userName")) {
            Gson gson = new Gson();

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryStudentByName");
            req.setArgs(new String[] { data.get("userName") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String,String> result = gson.fromJson(stringResponse, Map.class);

            TransactionProposalRequest req1 = client.newTransactionProposalRequest();
            req1.setChaincodeID(cid);
            req1.setFcn("fixStuInfo");
            req1.setArgs(new String[]{data.get("userName"),data.get("userName"), data.get("stuNumber"),result.get("password"),data.get("gender"), data.get("uni"),data.get("major"),data.get("phone"),data.get("email")});
            Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req1);
            channel.sendTransaction(res2);

            QueryByChaincodeRequest req3 = client.newQueryProposalRequest();
            req3.setChaincodeID(cid);
            req3.setFcn("queryStudentByName");
            req3.setArgs(new String[] { data.get("userName") });
            Collection<ProposalResponse> res3 = channel.queryByChaincode(req3);
            for (ProposalResponse pres : res3) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String, String> result2 = gson.fromJson(stringResponse, Map.class);

            map.put("result", result2);
            status = "right";
            details = "修改成功";
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }
}
