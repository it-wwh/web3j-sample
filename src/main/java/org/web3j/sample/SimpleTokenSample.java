package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.model.SimpleToken;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Environment;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SimpleTokenSample {

  private static final Logger logger = LoggerFactory.getLogger(SimpleTokenSample.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final String fromAddress = "0x38AabFFc1239788232025B37610Dd0FE7EB3aD73";
  private static final String toAddress = "0x6Db645A875e9d475d824142cA1532C245930be94";
  private static final String contractAddress = "0x6d3964dD2EAf9214311A0Fc300bb8ea81F8Bfb67";

  public static void main(String[] args) {
//    deploy();
    use();
  }

  private static void deploy() {
    RemoteCall<SimpleToken> deploy = SimpleToken.deploy(
        web3j,
        Environment.CREDENTIALS,
        Environment.STATIC_GAS_PROVIDER,
        BigInteger.valueOf(100000000),
        "simple token",
        "ST",
        BigInteger.valueOf(18));
    try {
      SimpleToken simpleToken = deploy.send();
      simpleToken.isValid();
      logger.info("contract address >>> {}", simpleToken.getContractAddress());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void use() {
    SimpleToken simpleToken = SimpleToken.load(contractAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    BigInteger amount = Convert.toWei(BigDecimal.valueOf(10), Convert.Unit.ETHER).toBigInteger();
    try {
      BigInteger balance = simpleToken.balanceOf(fromAddress).send();
      logger.info("address {} balance {} wei", fromAddress, balance);
      TransactionReceipt receipt = simpleToken.transfer(toAddress, amount).send();
      logger.info("receipt >>> {}", receipt.toString());
      BigInteger fromBalance = simpleToken.balanceOf(fromAddress).send();
      logger.info("fromAddress {} balance {} wei", fromAddress, fromBalance);
      BigInteger toBalance = simpleToken.balanceOf(toAddress).send();
      logger.info("toAddress {} balance {} wei", toAddress, toBalance);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
