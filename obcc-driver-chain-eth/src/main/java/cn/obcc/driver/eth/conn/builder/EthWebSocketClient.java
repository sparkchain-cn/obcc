package cn.obcc.driver.eth.conn.builder;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.Map;

import cn.obcc.driver.base.BaseChainDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.websocket.WebSocketClient;

import cn.obcc.connect.builder.ChainClientBuilder;

public class EthWebSocketClient extends WebSocketClient {
    public static final Logger logger = LoggerFactory.getLogger(BaseChainDriver.class);

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
            if (clientBuilder.getCallback() != null) {
                clientBuilder.getCallback().requestCountInc();
            }
        } catch (Exception e) {
            logger.error("clientBuilder.requestCountInc error.", e);
        }
    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        countSend();
        super.send(text);
    }

    @Override
    public void send(byte[] data) throws NotYetConnectedException {
        countSend();
        super.send(data);
    }

    public void onError(Exception e) {
        if (clientBuilder.getCallback() != null) {
            clientBuilder.getCallback().ioErrorCountInc();
        }
        super.onError(e);
    }


}
