package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Environment;

import java.io.IOException;


/**
 * 快速开始
 */
public class QuickStart {

  private static final Logger logger = LoggerFactory.getLogger(QuickStart.class);

  public static void main(String[] args) {
    Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));

    Web3ClientVersion web3ClientVersion;
    try {
      web3ClientVersion = web3j.web3ClientVersion().send();
      String clientVersion = web3ClientVersion.getWeb3ClientVersion();
      logger.info("clientVersion >>> {}", clientVersion);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
