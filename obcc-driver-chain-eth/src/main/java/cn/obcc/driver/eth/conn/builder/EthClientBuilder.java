package cn.obcc.driver.eth.conn.builder;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import cn.obcc.config.ObccConfig;
import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.connect.builder.HttpAndWsClientBuilder;
import cn.obcc.connect.pool.ChainClientPoolFactory;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.utils.base.StringUtils;

public class EthClientBuilder extends HttpAndWsClientBuilder<Web3j> implements ChainClientBuilder<Web3j> {

	private EthWebSocketClient ethWebSocketClient;

	private WebSocketService websocketService;
	private HttpService httpService;
	private String currentUrl;
	private Web3j chain3j;

	
	@Override
	public Web3j newNativeClient(String url) throws Exception {
		if (StringUtils.isNullOrEmpty(url)) {
			throw new RuntimeException("连接url：{" + url + "} 为空，检查数据库或配置文件");
		}
		

		if (StringUtils.isHttp(url)) {
			httpService = new HttpService(url);
			chain3j = Web3j.build(httpService);
			currentUrl = url;
		} else if (JunctionUtils.isWs(url)) {

			this.ethWebSocketClient = new EthWebSocketClient(parseURI(url));
			ethWebSocketClient.setClientBuilder(this);
			websocketService = new WebSocketService(ethWebSocketClient, true);
			try {
				websocketService.connect();
				chain3j = Web3j.build(websocketService);
				currentUrl = url;
			} catch (ConnectException e) {
				e.printStackTrace();
			}
		}
		return chain3j;
	}

	private static URI parseURI(String serverUrl) {
		try {
			return new URI(serverUrl);
		} catch (URISyntaxException var2) {
			throw new RuntimeException(String.format("Failed to parse URL: '%s'", serverUrl), var2);
		}
	}

	// // NOT_YET_CONNECTED, CONNECTING, OPEN, CLOSING, CLOSED
	@Override
	public boolean isWsDead(Web3j chain3j) {
		if (ethWebSocketClient.getReadyState() == WebSocketClient.READYSTATE.CLOSED
				|| ethWebSocketClient.getReadyState() == WebSocketClient.READYSTATE.CLOSING) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isWsOpen(Web3j chain3j) {
		if (ethWebSocketClient.getReadyState() == WebSocketClient.READYSTATE.OPEN) {
			return true;
		}
		return false;
	}

	@Override
	public void close(Web3j client) {
		client.shutdown();
	}

	@Override
	public boolean open(Web3j client) throws Exception {

		Web3j newChain3j = newNativeClient(currentUrl);
		if (newChain3j == null) {
			return false;
		} else {
			chain3j = newChain3j;
			return true;
		}
	}

	@Override
	public void bizErrorCountInc() {
		// todo: filter the error
		this.bizErrorCountInc();
	}

	
	public static Web3j getClient(ObccConfig obccConfig) throws Exception {
		return (Web3j) ChainClientPoolFactory.getClient(obccConfig, EthClientBuilder.class);
	}

	/**
	 * 根据上一次的UUid找到指定的连接
	 *
	 * @param uuid
	 * @return
	 * @throws Exception
	 */
	public static Web3j getClient(ObccConfig config, String uuid) throws Exception {
		Web3j remote = ChainClientPoolFactory.getClient(config.getChain().name(), uuid, EthClientBuilder.class);

		return remote;
	}

}
