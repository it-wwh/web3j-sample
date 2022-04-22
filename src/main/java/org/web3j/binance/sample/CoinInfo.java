package org.web3j.binance.sample;

import com.binance.connector.client.impl.SpotClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.utils.Environment;

import java.util.LinkedHashMap;

public class CoinInfo {
    private static final Logger logger = LoggerFactory.getLogger(CoinInfo.class);
    public static void main(String[] args) {
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");
        LinkedHashMap<String,Object> parameters = new LinkedHashMap<>();

        SpotClientImpl client = new SpotClientImpl(Environment.BINANCE_API_KEY, Environment.BINANCE_SECRET_KEY);
        String result = client.createWallet().coinInfo(parameters);
        logger.info(result);
    }
}
