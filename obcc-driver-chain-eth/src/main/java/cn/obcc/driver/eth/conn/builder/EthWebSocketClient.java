package cn.obcc.driver.eth.conn.builder;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.Map;

import org.web3j.protocol.websocket.WebSocketClient;

import cn.obcc.connect.builder.ChainClientBuilder;

public class EthWebSocketClient extends WebSocketClient {
    public EthWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public EthWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    ChainClientBuilder clientBuilder;

    public void setClientBuilder(ChainClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    public void countSend() {
        try {
            clientBuilder.requestCountInc();
        } catch (Exception e) {

        }
    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        //   engine.send( text );
        countSend();
        super.send(text);
    }

    /**
     * Sends binary <var> data</var> to the connected webSocket server.
     *
     * @param data The byte-Array of data to send to the WebSocket server.
     */
    @Override
    public void send(byte[] data) throws NotYetConnectedException {
        // engine.send( data );
        countSend();
        super.send(data);
    }

}
