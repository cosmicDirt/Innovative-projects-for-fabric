package com.example.demo.controller;

import com.example.demo.HFJavaExample;
import com.google.gson.Gson;
import org.hyperledger.fabric.sdk.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
public class SubprojectController {

    @PostMapping("/createSubproject")
    @ResponseBody
    public Map<String, Object> createSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("proID")) {
            Gson gson = new Gson();
            Random random = new Random();
            String subproID = String.valueOf(random.nextInt()).replace("-","");
            String timeStamp = String.valueOf(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String startTime = df.format(new Date());

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("createSubproject");
            req.setArgs(new String[]{subproID, data.get("proID"), startTime, data.get("endTime"), data.get("difficulty"), data.get("info"),data.get("subproName"),data.get("userName")});
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);
            status = "right";
            map.put("subproID", subproID);
            map.put("startTime", startTime);
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/deleteSubproject")
    @ResponseBody
    public Map<String, Object> deleteSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("subproID")) {

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("deleteSubproject");
            req.setArgs(new String[]{data.get("subproID")});
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);

            status = "right";
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/joinSubproject")
    @ResponseBody
    public Map<String, Object> joinSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("subproID")) {

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("joinSubproject");
            req.setArgs(new String[]{data.get("subproID"), data.get("stuName")});
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);

            status = "right";
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/quitSubproject")
    @ResponseBody
    public Map<String, Object> quitSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("subproID")) {

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("quitSubproject");
            req.setArgs(new String[]{data.get("subproID"), data.get("stuName")});
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);

            status = "right";
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/addComment")
    @ResponseBody
    public Map<String, Object> addComment(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("subproID")) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("addComment");
            req.setArgs(new String[]{data.get("subproID"), data.get("userName"), data.get("userAvatar"), data.get("content"), time, data.get("score")});
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            channel.sendTransaction(res);

            status = "right";
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/queryHistoryOfSubproject")
    @ResponseBody
    public Map<String, Object> queryHistoryOfSubproject(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("subproID")) {
            Gson gson = new Gson();

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryHistoryByKey");
            req.setArgs(new String[]{data.get("subproID")});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List result = gson.fromJson(stringResponse, List.class);
            status = "right";
            map.put("result", result);
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/querySubOfPro")
    @ResponseBody
    public Map<String, Object> querySubOfPro(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("proID")) {
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
            status = "right";
            List result = gson.fromJson(stringResponse, List.class);
            map.put("result", result);
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/queryAllAppendixForSub")
    @ResponseBody
    public Map<String, Object> queryAllAppendixForSub(@RequestBody Map<String, String> data) throws Exception {
        String status = "";
        String details = "";
        String stringResponse = "";

        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("subproID")) {
            Gson gson = new Gson();

            HFClient client = HFJavaExample.getClient();
            Channel channel = client.getChannel("mychannel");
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

            req.setChaincodeID(cid);
            req.setFcn("queryAllAppendixForSub");
            req.setArgs(new String[]{data.get("subproID")});
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            for (ProposalResponse pres : res) {
                stringResponse = new String(pres.getChaincodeActionResponsePayload());
            }
            List result = gson.fromJson(stringResponse, List.class);
            status = "right";
            map.put("result", result);
        } else {
            status = "wrong";
            details = "连接失败";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    //上传文件
    @PostMapping("/uploadAppendixForSub")
    public Map<String, Object> uploadFile(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        String status = "";
        String details = "";

        Map<String, Object> map = new HashMap<String, Object>();
        request.setCharacterEncoding("UTF-8");
        String user = request.getParameter("userName");
        String description = request.getParameter("description");
        String subproID = request.getParameter("subproID");

        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String path = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = df.format(new Date());
            String type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            String stamp = String.valueOf(df.parse(time).getTime() / 1000);

            if (type != null) {
                // 项目在容器中实际发布运行的根路径
                String realPath = request.getSession().getServletContext().getRealPath("/");
                // 自定义的文件名称
                String trueFileName = user + "_" + stamp + "_" + fileName;

                // 设置存放文件的路径
                path = "E:\\tmpStore\\" + trueFileName;
                File dest = new File(path);
                //判断文件父目录是否存在
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();
                }
                file.transferTo(dest);

                HFClient client = HFJavaExample.getClient();
                Channel channel = client.getChannel("mychannel");
                TransactionProposalRequest req = client.newTransactionProposalRequest();
                ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();

                req.setChaincodeID(cid);
                req.setFcn("uploadAppendixForSub");
                req.setArgs(new String[]{subproID, fileName, path, description, user, time});
                Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
                channel.sendTransaction(res);

                status = "right";
                map.put("fileName", fileName);
                map.put("upTime", time);
            } else {
                status = "wrong";
                details = "无法识别的文件类型";
            }
        } else {
            status = "wrong";
            details = "文件为空";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    @RequestMapping("/downloadAppendixForSub")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        String downloadFilePath = "E:\\tmpStore\\";//被下载的文件在服务器中的路径,
        String fileName = request.getParameter("fileName");//被下载文件的名称
        String upUser = request.getParameter("upUser");
        String upTime = request.getParameter("upTime");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String stamp = String.valueOf(df.parse(upTime).getTime() / 1000);

        File file = new File(downloadFilePath + upUser + "_" + stamp + "_" + fileName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

