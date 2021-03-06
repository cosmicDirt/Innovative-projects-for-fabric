package com.example.demo.controller;

import com.example.demo.HFJavaExample;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.hyperledger.fabric.sdk.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
public class ProjectController {

    @PostMapping("/createProject")
    @ResponseBody
    public Map<String,Object> createProject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("teacherName")) {
            Gson gson=new Gson();
            Random random=new Random();
            String ProID=String.valueOf(random.nextInt()).replace("-","");

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();
            if(!data.get("teacherName").equals("")) {
                req.setChaincodeID(cid);
                req.setFcn("queryTeacherByName");
                req.setArgs(new String[]{data.get("teacherName")});
                Collection<ProposalResponse> res = channel.queryByChaincode(req);
                for (ProposalResponse pres : res) {
                    stringResponse = new String(pres.getChaincodeActionResponsePayload());
                }
                Map<String, String> result = gson.fromJson(stringResponse, Map.class);

                if (result != null) {
                    TransactionProposalRequest req2 = client.newTransactionProposalRequest();

                    req2.setChaincodeID(cid);
                    req2.setFcn("createProject");
                    req2.setArgs(new String[]{ProID, ProID, data.get("info"), data.get("leaderName"), data.getOrDefault("teacherName", ""),
                            data.get("startTime"), data.get("endTime"), data.get("proName")});
                    Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req2);
                    channel.sendTransaction(res2);
                    status = "right";
                    details = "创建成功";
                    map.put("ProID", ProID);
                } else {
                    status = "wrong";
                    details = "负责人不存在";
                }
            }else{
                TransactionProposalRequest req2 = client.newTransactionProposalRequest();

                req2.setChaincodeID(cid);
                req2.setFcn("createProject");
                req2.setArgs(new String[]{ProID, ProID, data.get("info"), data.get("leaderName"), data.getOrDefault("teacherName", ""),
                        data.get("startTime"), data.get("endTime"), data.get("proName")});
                Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req2);
                channel.sendTransaction(res2);
                status = "right";
                details = "创建成功";
                map.put("ProID", ProID);
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

    @PostMapping("/deleteProject")
    @ResponseBody
    public Map<String,Object> deleteSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("ProID")) {

            Gson gson = new Gson();
            String query = "{\"selector\":{\"sip_pro_id\":\"" + data.get("proID") + "\"}}";

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("getQueryResultForQueryString");
            req.setArgs(new String[]{query});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List<LinkedTreeMap<String, LinkedTreeMap<String, Object>>> result = gson.fromJson(stringResponse, List.class);
            for(int i=0; i<result.size();i++) {

                TransactionProposalRequest req2 = client.newTransactionProposalRequest();

                req2.setChaincodeID(cid);
                req2.setFcn("deleteProject");
                req2.setArgs(new String[] { (String) result.get(i).get("Record").get("sip_id")});
                Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req2);
                channel.sendTransaction(res2);
            }
            TransactionProposalRequest req3 = client.newTransactionProposalRequest();

            req3.setChaincodeID(cid);
            req3.setFcn("deleteProject");
            req3.setArgs(new String[] { data.get("proID") });
            Collection<ProposalResponse> res3 = channel.sendTransactionProposal(req3);
            channel.sendTransaction(res3);

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

        if(data.containsKey("userName")) {

            Gson gson=new Gson();

            HFClient client= HFJavaExample.getClient();
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

            status = "right";
            map.put("result", result);
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
            Gson gson=new Gson();
            Random random=new Random();
            String SipID=String.valueOf(random.nextInt()).replace("-","");

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            Map<String, String> result = null;
            if(data.get("isCheck").equals("1")) {
                QueryByChaincodeRequest req = client.newQueryProposalRequest();

                req.setChaincodeID(cid);
                req.setFcn("queryStudentByName");
                req.setArgs(new String[]{data.get("proID")});
                Collection<ProposalResponse> res = channel.queryByChaincode(req);
                for (ProposalResponse pres : res) {
                    stringResponse = new String(pres.getChaincodeActionResponsePayload());
                }
                result = gson.fromJson(stringResponse, Map.class);
            }
            if(result!=null || data.get("isCheck").equals("0")) {
                TransactionProposalRequest req2 = client.newTransactionProposalRequest();

                req2.setChaincodeID(cid);
                req2.setFcn("AddProMem");
                req2.setArgs(new String[]{SipID, SipID, data.get("proID"), data.get("stuName"), "0", "0"});
                Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req2);
                channel.sendTransaction(res2);

                map.put("SipID", SipID);
                status = "right";
                details = "加入成功";
            }else{
                status = "wrong";
                details = "项目不存在";
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

    @PostMapping("/sesrchProinfo")
    @ResponseBody
    public Map<String,Object> sesrchProinfo(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("ProID")) {

            Gson gson=new Gson();

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryStudentByName");
            req.setArgs(new String[] { data.get("ProID") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String,Object> result = gson.fromJson(stringResponse, Map.class);

            status = "right";
            map.put("result", result);
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

    @PostMapping("/sesrchSIPByStu")
    @ResponseBody
    public Map<String,Object> sesrchSIPByStu(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("stuName")) {
            Gson gson = new Gson();
            String query = "{\"selector\":{\"sip_stu_name\":\"" + data.get("stuName") + "\"}}";

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("getQueryResultForQueryString");
            req.setArgs(new String[]{query});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List<LinkedTreeMap<String, LinkedTreeMap<String, Object>>> result = gson.fromJson(stringResponse, List.class);

            status = "right";
            map.put("Sip", result);

            List<Map<String, Object>> resultinfo = new ArrayList<>();
            for(int i=0; i<result.size();i++) {
                QueryByChaincodeRequest req2 = client.newQueryProposalRequest();

                req2.setChaincodeID(cid);
                req2.setFcn("queryStudentByName");
                req2.setArgs(new String[]{(String) result.get(i).get("Record").get("sip_pro_id")});
                Collection<ProposalResponse> res2 = channel.queryByChaincode(req2);
                for (ProposalResponse pres : res2) {
                    stringResponse = new String(pres.getChaincodeActionResponsePayload());
                }
                Map<String, Object> result2 = gson.fromJson(stringResponse, Map.class);
                resultinfo.add(result2);
            }
            map.put("proInfo", resultinfo);
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/sesrchMemByproID")
    @ResponseBody
    public Map<String,Object> sesrchSIPByproID(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("proID")) {
            Gson gson = new Gson();
            String query = "{\"selector\":{\"sip_pro_id\":\"" + data.get("proID") + "\"}}";

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("getQueryResultForQueryString");
            req.setArgs(new String[]{query});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List<LinkedTreeMap<String, LinkedTreeMap<String, Object>>> result = gson.fromJson(stringResponse, List.class);

            status = "right";
            map.put("Sip", result);

            List<Map<String, Object>> Meminfo = new ArrayList<>();
            for(int i=0; i<result.size();i++) {
                QueryByChaincodeRequest req2 = client.newQueryProposalRequest();

                req2.setChaincodeID(cid);
                req2.setFcn("queryStudentByName");
                req2.setArgs(new String[]{(String) result.get(i).get("Record").get("sip_stu_name")});
                Collection<ProposalResponse> res2 = channel.queryByChaincode(req2);
                for (ProposalResponse pres : res2) {
                    stringResponse = new String(pres.getChaincodeActionResponsePayload());
                }
                Map<String, Object> result2 = gson.fromJson(stringResponse, Map.class);
                Meminfo.add(result2);
            }
            map.put("proInfo", Meminfo);
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/finishPro")
    @ResponseBody
    public Map<String,Object> finishPro(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("proID")) {
            Gson gson = new Gson();
            String query = "{\"selector\":{\"proID\":\"" + data.get("proID") + "\"}}";

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("getQueryResultForQueryString");
            req.setArgs(new String[]{query});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List<LinkedTreeMap<String, LinkedTreeMap<String, Object>>> result = gson.fromJson(stringResponse, List.class);


            Map<String, Float> stuScore=new HashMap<String, Float>();
            for(int i=0;i<result.size();i++){
                List<LinkedTreeMap<String, String>> comments=(List<LinkedTreeMap<String, String>>)result.get(i).get("Record").get("comment");
                if(comments==null)
                    continue;
                List<String> members=(List<String>)result.get(i).get("Record").get("member");
                float score=0;
                for(int j=0;j<comments.size();j++){
                    if(comments.get(j).get("score")!=null)
                        score+=Float.parseFloat(comments.get(j).get("score"));
                }
                score=(score/comments.size())*Float.parseFloat((String) result.get(i).get("Record").get("difficulty"));
                for(int j=0;j<members.size();j++){
                    String stu=members.get(j);
                    if(stuScore.get(stu)!=null){
                        stuScore.put(stu,stuScore.get(stu)+score);
                    }
                    else{
                        stuScore.put(members.get(j),score);
                    }
                }
            }

            String query2 = "{\"selector\":{\"sip_pro_id\":\"" + data.get("proID") + "\"}}";

            QueryByChaincodeRequest req2 = client.newQueryProposalRequest();

            req2.setChaincodeID(cid);
            req2.setFcn("getQueryResultForQueryString");
            req2.setArgs(new String[]{query2});
            Collection<ProposalResponse> res2 = channel.queryByChaincode(req2);
            for (ProposalResponse pres : res2) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List<LinkedTreeMap<String, LinkedTreeMap<String, Object>>> result2 = gson.fromJson(stringResponse, List.class);

            for(int i=0;i<result2.size();i++){
                String stu=(String) result2.get(i).get("Record").get("sip_stu_name");
                if(stuScore.get(stu)!=null){
                    TransactionProposalRequest req3 = client.newTransactionProposalRequest();

                    req3.setChaincodeID(cid);
                    req3.setFcn("AddProMem");
                    req3.setArgs(new String[] { (String) result2.get(i).get("Record").get("sip_id"),
                            (String) result2.get(i).get("Record").get("sip_id"),
                            (String) result2.get(i).get("Record").get("sip_pro_id"),
                            (String) result2.get(i).get("Record").get("sip_stu_name"),
                            "0", stuScore.get(stu).toString() });
                    Collection<ProposalResponse> res3 = channel.sendTransactionProposal(req3);
                    channel.sendTransaction(res3);
                }
            }

            QueryByChaincodeRequest req4 = client.newQueryProposalRequest();
            String query4 = "{\"selector\":{\"sip_pro_id\":\"" + data.get("proID") + "\"}}";
            req4.setChaincodeID(cid);
            req4.setFcn("getQueryResultForQueryString");
            req4.setArgs(new String[]{query4});
            Collection<ProposalResponse> res4 = channel.queryByChaincode(req4);
            for (ProposalResponse pres : res4) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List<LinkedTreeMap<String, LinkedTreeMap<String, Object>>> result4 = gson.fromJson(stringResponse, List.class);
            map.put("result",result4);
            status = "right";
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/score")
    @ResponseBody
    public Map<String,Object> score(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("SipID")) {

            Gson gson=new Gson();

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryStudentByName");
            req.setArgs(new String[] { data.get("SipID") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String, String> result = gson.fromJson(stringResponse, Map.class);

            TransactionProposalRequest req2 = client.newTransactionProposalRequest();

            req2.setChaincodeID(cid);
            req2.setFcn("AddProMem");
            req2.setArgs(new String[]{data.get("SipID"), data.get("SipID"), result.get("sip_pro_id"),
                    result.get("sip_stu_name"), data.get("score"), result.get("relative_score")});
            Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req2);
            channel.sendTransaction(res2);

            status = "right";
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/teacherJoinPro")
    @ResponseBody
    public Map<String,Object> teacherJoinPro(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("proID") && data.containsKey("teacherName")) {
            Gson gson=new Gson();

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryStudentByName");
            req.setArgs(new String[] { data.get("proID") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String, String> result = gson.fromJson(stringResponse, Map.class);

            if(result!=null) {
                TransactionProposalRequest req2 = client.newTransactionProposalRequest();

                req2.setChaincodeID(cid);
                req2.setFcn("createProject");
                req2.setArgs(new String[]{result.get("project_id"), result.get("project_id"),
                        result.get("pro_info"), result.get("pro_leader_name"), data.get("teacherName"),
                        result.get("pro_start_time"), result.get("pro_end_time"), result.get("project_name")});
                Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req2);
                channel.sendTransaction(res2);
                status = "right";
                details = "加入成功";
            } else{
                status = "wrong";
                details = "项目不存在";
            }
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/searchProByTeacher")
    @ResponseBody
    public Map<String,Object> sesrchProByTeacher(@RequestBody Map<String, String> data) throws Exception {
        String status="";
        String details="";
        String stringResponse="";

        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("teacherName")) {
            Gson gson = new Gson();
            String query = "{\"selector\":{\"pro_teacher_name\":\"" + data.get("teacherName") + "\"}}";

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("getQueryResultForQueryString");
            req.setArgs(new String[]{query});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List<Map<String, Object>> results = gson.fromJson(stringResponse, List.class);

            if (results != null) {
                List<Object> proInfo = new ArrayList<>();
                for(Map<String, Object> result:results){
                    proInfo.add(result.get("Record"));
                }
                status = "right";
                details = "查询成功";
                map.put("proInfo", proInfo);
            } else {
                status = "wrong";
                details = "未加入项目";
            }
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/teacherQuitPro")
    @ResponseBody
    public Map<String,Object> teacherQuitPro(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("proID") && data.containsKey("teacherName")) {
            Gson gson=new Gson();

            HFClient client= HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryStudentByName");
            req.setArgs(new String[] { data.get("proID") });
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            Map<String, String> result = gson.fromJson(stringResponse, Map.class);

            if(result!=null) {
                TransactionProposalRequest req2 = client.newTransactionProposalRequest();

                req2.setChaincodeID(cid);
                req2.setFcn("createProject");
                req2.setArgs(new String[]{result.get("project_id"), result.get("project_id"),
                        result.get("pro_info"), result.get("pro_leader_name"), "",
                        result.get("pro_start_time"), result.get("pro_end_time"), result.get("project_name")});
                Collection<ProposalResponse> res2 = channel.sendTransactionProposal(req2);
                channel.sendTransaction(res2);
                status = "right";
                details = "退出成功";
            } else{
                status = "wrong";
                details = "项目不存在";
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
