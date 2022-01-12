package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.model.Verification;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Environment;
import org.web3j.utils.Numeric;

/**
 * ECRecover
 * 前端签名,拿到 message 和 signature 传入后端
 * 后端调用合约的 recover 方法验证签名
 */
public class ECRecoverSample {

  private static final Logger logger = LoggerFactory.getLogger(ECRecoverSample.class);
  private static final String contractAddress = "0xAB9Ec93a5a466DDD7B8a8C6128866fE93F6AA5b9";
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final String message = "0x64e604787cbf194841e7b68d7cd28786f6c9a0a3ab9f8b0a0e87cb4387ab0107";
  private static final String signature = "0xb6ff979cefc43683b1a7b089d8c3541f3ae8ab036e30111fef09a0ad3bf469e5467c495094d0d0ab4a535951c07fe4ecaa15450944abdaec5615dd751d3ca92f1c";

  public static void main(String[] args) throws Exception {
    // https://kovan.etherscan.io/address/0xab9ec93a5a466ddd7b8a8c6128866fe93f6aa5b9
    Verification verification = Verification.load(contractAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    String address = verification.recover(Numeric.hexStringToByteArray(message), Numeric.hexStringToByteArray(signature)).send();
    logger.info("address:{}", address);
  }

}
