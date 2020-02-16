package com.example.demo;

import org.slf4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;

/**
 * @program: HFJavaExample
 * @description: A simple demo using HF java sdk

 **/
public class HFJavaExample {


    private static final Logger log = LoggerFactory.getLogger(HFJavaExample.class);


    public static void main(String[] args) throws Exception {
        // create fabric-ca client

        HFCAClient caClient = getHfCaClient("http://192.168.0.108:7054", null);

        // enroll or load admin
        HFUser admin = getAdmin(caClient);
        log.info(admin.toString());

        // register and enroll new user
        HFUser hfUser = getUser(caClient, admin, "wesker");
        log.info(hfUser.toString());

        // get HFC client instance
        HFClient client = getHfClient();
        // set user context
        client.setUserContext(admin);

        // get HFC channel using the client
        Channel channel = getChannel(client);
        log.info("Channel: " + channel.getName());

        // query alll account list
        queryBlockChain(client, "queryCar");


        //update
        //invokeBlockChain(client, "update", "ACCOUNT1", "jill_1");

        // query by condition
        //queryBlockChain(client, "query", "ACCOUNT1");


    }

    public static HFClient getClient() throws Exception{
        // create fabric-ca client
        HFCAClient caClient = getHfCaClient("http://192.168.0.108:7054", null);
        // enroll or load admin
        HFUser admin = getAdmin(caClient);
        // get HFC client instance
        HFClient client = getHfClient();
        // set user context
        client.setUserContext(admin);
        // get HFC channel using the client
        Channel channel = getChannel(client);
        return client;
    }
    /***
     * invoke blockChain
     * @param client
     * @param funcName
     * @param args
     * @throws ProposalException
     * @throws InvalidArgumentException
     */
    static void invokeBlockChain(HFClient client, String funcName, String... args) throws ProposalException, InvalidArgumentException {
        Channel channel = client.getChannel("mychannel");
        // 构建proposal
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        // 指定要调用的chaincode
        ChaincodeID cid = ChaincodeID.newBuilder().setName("account").build();
        req.setChaincodeID(cid);
        req.setFcn(funcName);

        if (args.length > 0) {
            req.setArgs(args);
        }
        // 发送proprosal
        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);

        // 提交给orderer节点
        channel.sendTransaction(resps);
    }

    /***
     * query blockChain
     * @param client
     * @param funcName
     * @param args
     * @throws ProposalException
     * @throws InvalidArgumentException
     */
    public static void queryBlockChain(HFClient client, String funcName, String... args) throws ProposalException, InvalidArgumentException {
        // get channel instance from client
        Channel channel = client.getChannel("mychannel");
        // create chaincode request
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        // build cc id providing the chaincode name. Version is omitted here.
        ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();
        String key="CAR1";

        req.setChaincodeID(cid);
        req.setFcn("queryCar");
        req.setArgs(new String[] { key });
        System.out.println("Querying for " + key);
        Collection<ProposalResponse> res = channel.queryByChaincode(req);
        // display response
        for (ProposalResponse pres : res) {
            String stringResponse = new String(pres.getChaincodeActionResponsePayload());
            log.info(stringResponse);
        }
    }


    /***
     * init the channel
     * @param client
     * @return
     * @throws InvalidArgumentException
     * @throws TransactionException
     */
    static Channel getChannel(HFClient client) throws InvalidArgumentException, TransactionException {
        // initialize channel
        // peer name and endpoint in fabcar network
        Peer peer = client.newPeer("peer1", "grpc://192.168.0.108:7051");
        // Peer peer = client.newPeer("peer1", "grpc://192.168.0.108:8051");

        // eventhub name and endpoint in fabcar network
        //EventHub eventHub = client.newEventHub("eventhub01", "grpc://192.168.0.108:7053");
        // orderer name and endpoint in fabcar network
        Orderer orderer = client.newOrderer("orderer", "grpc://192.168.0.108:7050");
        // channel name in fabcar network
        Channel channel = client.newChannel("mychannel");
        channel.addPeer(peer);
        //  channel.addEventHub(eventHub);
        channel.addOrderer(orderer);
        channel.initialize();
        return channel;
    }


    static HFClient getHfClient() throws Exception {
        // initialize default cryptosuite
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        // setup the client
        HFClient client = HFClient.createNewInstance();
        client.setCryptoSuite(cryptoSuite);
        return client;
    }


    static HFUser getUser(HFCAClient caClient, HFUser registrar, String userId) throws Exception {
        HFUser hfUser = tryDeserialize(userId);
        if (hfUser == null) {
            RegistrationRequest rr = new RegistrationRequest(userId, "org1");
            String enrollmentSecret = caClient.register(rr, registrar);
            Enrollment enrollment = caClient.enroll(userId, enrollmentSecret);
            hfUser = new HFUser(userId, "org1", "Org1MSP", enrollment);
            serialize(hfUser);
        }
        return hfUser;
    }


    static HFUser getAdmin(HFCAClient caClient) throws Exception {
        HFUser admin = tryDeserialize("admin");
        if (admin == null) {
            Enrollment adminEnrollment = caClient.enroll("admin", "adminpw");
            admin = new HFUser("admin", "org1", "Org1MSP", adminEnrollment);
            serialize(admin);
        }
        return admin;
    }


    static HFCAClient getHfCaClient(String caUrl, Properties caClientProperties) throws Exception {
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        HFCAClient caClient = HFCAClient.createNewInstance(caUrl, caClientProperties);
        caClient.setCryptoSuite(cryptoSuite);
        return caClient;
    }


    static void serialize(HFUser hfUser) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(
                Paths.get(hfUser.getName() + ".tail")))) {
            oos.writeObject(hfUser);
        }
    }


    static HFUser tryDeserialize(String name) throws Exception {
        if (Files.exists(Paths.get(name + ".tail"))) {
            return deserialize(name);
        }
        return null;
    }

    static HFUser deserialize(String name) throws Exception {
        try (ObjectInputStream decoder = new ObjectInputStream(
                Files.newInputStream(Paths.get(name + ".tail")))) {
            return (HFUser) decoder.readObject();
        }
    }


}