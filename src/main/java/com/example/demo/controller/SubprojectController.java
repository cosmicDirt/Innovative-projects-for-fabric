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
public class SubprojectController {

    @PostMapping("/createSubproject")
    @ResponseBody
    public Map<String,Object> createSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("proID")) {
            Gson gson=new Gson();
            Random random=new Random();
            String subproID=String.valueOf(random.nextInt());

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("createSubproject");
            req.setArgs(new String[] { subproID,subproID,data.get("proID"),data.get("info") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            status="right";
            map.put("subproID",subproID);
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

    @PostMapping("/deleteSubproject")
    @ResponseBody
    public Map<String,Object> deleteSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("subproID")) {

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("deleteSubproject");
            req.setArgs(new String[] { data.get("subproID") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
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

    @PostMapping("/joinSubproject")
    @ResponseBody
    public Map<String,Object> joinSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("subproID")) {

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("joinSubproject");
            req.setArgs(new String[] { data.get("subproID"),data.get("stuName") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
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

    @PostMapping("/quitSubproject")
    @ResponseBody
    public Map<String,Object> quitSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("subproID")) {

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("quitSubproject");
            req.setArgs(new String[] { data.get("subproID"),data.get("stuName") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
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

    @PostMapping("/AddAComment")
    @ResponseBody
    public Map<String,Object> AddAComment(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("subproID")) {

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("AddAComment");
            req.setArgs(new String[] { data.get("subproID"),data.get("userName"),data.get("userAvatar"),data.get("content"),data.get("time") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
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
}

