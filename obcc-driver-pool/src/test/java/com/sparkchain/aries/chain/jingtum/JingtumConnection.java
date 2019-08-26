//package com.sparkchain.chain.jingtum;
//
//import com.alibaba.fastjson.JSON;
//import com.blink.jtblc.connection.Connection;
//import com.blink.jtblc.connection.ExecutorPool;
//import com.blink.jtblc.connection.HandleProcessTask;
//import com.blink.jtblc.connection.WebSocket;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
//public class JingtumConnection extends Connection {
//
//	static final Logger logger = LoggerFactory.getLogger(JingtumConnection.class);
//	public JingtumConnection(WebSocket webSocket) {
//		super(webSocket);
//	}
//	JingtumBuilder clientBuilder;
//	public void setClientBuilder(JingtumBuilder clientBuilder) {
//		this.clientBuilder = clientBuilder;
//	}
//
//	/**
//	 *  发送请求
//	 *
//	 * @param params 参数
//	 * @return string
//	 */
//	@Override
//	public String submit(Map<String, Object> params) throws Exception {
//		// System.out.println("submit参数: " + JSON.toJSONString(params) + " 请求地址: "
//		// +clientBuilder.webSocketclient.getURI());
//		logger.debug("submit参数: " + JSON.toJSONString(params) + " 请求地址: " + clientBuilder.webSocketclient.getURI());
//		String result = "";
//		Future<String> future = ExecutorPool.getExecutorPool()
//				.submit(new HandleProcessTask(params, clientBuilder.webSocketclient));
//
//		try {
//			result = future.get(50, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			Thread.interrupted();
//			clientBuilder.ioErrorCountInc();
//			throw new IOException("Interrupted WebSocket request", e);
//		} catch (ExecutionException e) {
//			if (e.getCause() instanceof IOException) {
//				clientBuilder.ioErrorCountInc();
//				throw (IOException) e.getCause();
//			} else {
//				throw new RuntimeException("Unexpected exception", e.getCause());
//			}
//		} catch (TimeoutException e) {
//			clientBuilder.ioErrorCountInc();
//			throw new IOException("Interrupted WebSocket request", e);
//		}
//
//		return result;
//	}
//
//}
