//package com.sparkchain.chain.jingtum;
//
//import com.blink.jtblc.connection.WebSocket;
//import ClientBuilder;
//
//import java.net.URI;
//import java.nio.channels.NotYetConnectedException;
//
//public class JintumWebSocketclient extends WebSocket {
//
//    public JintumWebSocketclient(URI serverURI) {
//        super(serverURI);
//    }
//
//    ClientBuilder clientBuilder;
//
//    public void setClientBuilder(ClientBuilder clientBuilder) {
//        this.clientBuilder = clientBuilder;
//    }
//
//    public void countSend() {
//        try {
//            clientBuilder.requestCountInc();
//        } catch (Exception e) {
//
//        }
//    }
//
//    public void send(String text) throws NotYetConnectedException {
//        countSend();
//        super.send(text);
//    }
//
//    public void send(byte[] data) throws NotYetConnectedException {
//        countSend();
//        super.send(data);
//    }
//}
