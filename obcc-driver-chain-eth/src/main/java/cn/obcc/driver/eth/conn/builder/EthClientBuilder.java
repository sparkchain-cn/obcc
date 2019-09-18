package cn.obcc.driver.eth.conn.builder;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import cn.obcc.config.ObccConfig;
import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.connect.builder.BaseHttpAndWsClientBuilder;
import cn.obcc.connect.pool.ChainClientPoolFactory;
import cn.obcc.utils.base.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class EthClientBuilder extends BaseHttpAndWsClientBuilder<Web3j> implements ChainClientBuilder<Web3j> {
    public static final Logger logger = LoggerFactory.getLogger(EthClientBuilder.class);

    private EthWebSocketClient ethWebSocketClient;

    private WebSocketService websocketService;
    private EthHttpService httpService;

    private Web3j chain3j;


    @Override
    // public Web3j newNativeClient(@NotBlank String url) throws Exception {
    public Web3j newNativeClient() throws Exception {
        if (StringUtils.isNullOrEmpty(url)) {
            throw new RuntimeException("连接url：{" + url + "} 为空，检查数据库或配置文件");
        }

        if (StringUtils.isHttp(url)) {
            httpService = new EthHttpService(url);
            httpService.setClientBuilder(this);
            chain3j = Web3j.build(httpService);
            return chain3j;
        }
        if (StringUtils.isWs(url)) {
            try {
                this.ethWebSocketClient = new EthWebSocketClient(parseUri(url));
                ethWebSocketClient.setClientBuilder(this);
                websocketService = new WebSocketService(ethWebSocketClient, true);

                websocketService.connect();
                chain3j = Web3j.build(websocketService);
            } catch (ConnectException e) {
                e.printStackTrace();
            }
        }
        return chain3j;
    }


    @Override
    public void close(Web3j client) {
        client.shutdown();
    }

    @Override
    public boolean open(Web3j client) throws Exception {
        try {
            if (websocketService != null) {
                websocketService.close();
            }
            client.shutdown();
        } catch (Exception e) {
            logger.error("关闭已经存在的区块链客户端出错。");
        }

        Web3j newChain3j = newNativeClient();
        if (newChain3j == null) {
            return false;
        } else {
            chain3j = newChain3j;
            return true;
        }
    }

    private static URI parseUri(@NotBlank String serverUrl) {
        try {
            return new URI(serverUrl);
        } catch (URISyntaxException var2) {
            throw new RuntimeException(String.format("Failed to parse URL: '%s'", serverUrl), var2);
        }
    }

    // // NOT_YET_CONNECTED, CONNECTING, OPEN, CLOSING, CLOSED
    @Override
    protected boolean isWsDead(Web3j chain3j) {
        if (ethWebSocketClient.getReadyState() == WebSocketClient.READYSTATE.CLOSED
                || ethWebSocketClient.getReadyState() == WebSocketClient.READYSTATE.CLOSING) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isWsOpen(Web3j chain3j) {
        if (ethWebSocketClient.getReadyState() == WebSocketClient.READYSTATE.OPEN) {
            return true;
        }
        return false;
    }


    public static Web3j getClient(@NotNull ObccConfig obccConfig) throws Exception {
        return (Web3j) ChainClientPoolFactory.getClient(obccConfig, EthClientBuilder.class);
    }

    /**
     * 根据上一次的UUid找到指定的连接
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    public static Web3j getClient(@NotNull ObccConfig config, String uuid) throws Exception {
        Web3j remote = ChainClientPoolFactory.getClient(config.getChain().getName(), uuid, EthClientBuilder.class);
        return remote;
    }

}
