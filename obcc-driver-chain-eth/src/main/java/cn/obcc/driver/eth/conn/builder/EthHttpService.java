package cn.obcc.driver.eth.conn.builder;

import cn.obcc.connect.builder.ChainClientBuilder;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.exceptions.ClientConnectionException;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName EthHttpService
 * @desc
 * @data 2019/9/5 15:30
 **/
public class EthHttpService extends HttpService {
    public static final Logger logger = LoggerFactory.getLogger(EthHttpService.class);

    public EthHttpService(String url) {
        super(url);
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

    protected InputStream performIO(String request) throws IOException {
        countSend();
        return super.performIO(request);
    }
}
