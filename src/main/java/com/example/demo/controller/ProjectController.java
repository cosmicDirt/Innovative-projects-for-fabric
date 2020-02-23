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
import java.util.Random;

@RestController
public class ProjectController {

    @PostMapping("/createProject")
    @ResponseBody
    public Map<String,Object> createProject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("info")) {
            Gson gson=new Gson();
            Random random=new Random();
            String ProID=String.valueOf(random.nextInt());

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("createProject");
            req.setArgs(new String[] { ProID,ProID,data.get("info"),data.get("leaderName"),data.get("teacherName"),
                    data.get("startTime"),data.get("endTime")});
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);
            status="right";
            map.put("ProID",ProID);
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

    @PostMapping("/deleteProject")
    @ResponseBody
    public Map<String,Object> deleteSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("ProID")) {


            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("deleteProject");
            req.setArgs(new String[] { data.get("proID") });
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);

            status="right";
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

    @PostMapping("/searchStu")
    @ResponseBody
    public Map<String,Object> searchStu(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("stuName")) {

            Gson gson=new Gson();

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryStudentByName");
            req.setArgs(new String[] { data.get("stuName") });
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String, String> result = gson.fromJson(stringResponse, Map.class);

            if (result.isEmpty()) {
                status = "wrong";
                details = "用户不存在";
            }
            else {
                map.put("stuNumber", result.get("stuName"));
                map.put("gender", result.get("gender"));
                map.put("email", result.get("email"));
                map.put("phone", "phone");
                status = "right";
            }
            status="right";
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

    @PostMapping("/quitProject")
    @ResponseBody
    public Map<String,Object> quitProject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("SipID")) {

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("quitProject");
            req.setArgs(new String[] { data.get("SipID") });
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);

            status="right";
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

    @PostMapping("/AddProMember")
    @ResponseBody
    public Map<String,Object> AddProMember(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("proID")) {

            Random random=new Random();
            String SipID=String.valueOf(random.nextInt());

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("AddProMem");
            req.setArgs(new String[] { SipID,SipID,data.get("proID"),data.get("userName") });
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);

            map.put("SipID", SipID);
            status="right";
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
