package org.web3j.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.TokenClient;
import org.web3j.utils.Environment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 批量查询token余额
 */
public class TokenBalanceTask {

  private static final Logger logger = LoggerFactory.getLogger(TokenBalanceTask.class);

  public static class Token {
    public String contractAddress;
    public int decimals;
    public String name;

    public Token(String contractAddress) {
      this.contractAddress = contractAddress;
      this.decimals = 0;
    }

    public Token(String contractAddress, int decimals) {
      this.contractAddress = contractAddress;
      this.decimals = decimals;
    }
  }

  private static Web3j web3j;

  // 要查询的token合约地址
  private static List<Token> tokenList;

  // 要查询的钱包地址
  private static List<String> addressList;

  public static void main(String[] args) {
    web3j = Web3j.build(new HttpService(Environment.RPC_URL));
    loadData();
    requestDecimals();
    requestName();
    processTask();
  }


  private static void loadData() {
    tokenList = Arrays.asList(
        new Token("0x2a8e92800c7b739473ef77c94ca87d9b7219c3d0"),
        new Token("0xdbc941fec34e8965ebc4a25452ae7519d6bdfc4e"));
    addressList = Arrays.asList(
        "0x38AabFFc1239788232025B37610Dd0FE7EB3aD73",
        "0x6Db645A875e9d475d824142cA1532C245930be94");
  }

  private static void requestDecimals() {
    for (Token token : tokenList) {
      token.decimals = TokenClient.getTokenDecimals(web3j, token.contractAddress);
    }
  }

  private static void requestName() {
    for (Token token : tokenList) {
      token.name = TokenClient.getTokenName(web3j, token.contractAddress);
    }
  }

  private static void processTask() {
    for (String address : addressList) {
      for (Token token : tokenList) {
        BigDecimal balance = new BigDecimal(TokenClient.getTokenBalance(web3j, address, token.contractAddress));
        balance = balance.divide(BigDecimal.TEN.pow(token.decimals));
        logger.info("address {} name {} balance {}", address, token.name, balance);
      }
    }
  }

}
