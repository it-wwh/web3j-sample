package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.model.ERC20;
import org.web3j.model.IPancakeFactory;
import org.web3j.model.PancakeRouter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Environment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PancakePairScanSample {

  private static final Logger logger = LoggerFactory.getLogger(PancakePairScanSample.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final String toAddress = "0x6Db645A875e9d475d824142cA1532C245930be94";
  /**
   * ropsten 测试环境
   * private static final String pancakeFactoryAddress = "0x5C69bEe701ef814a2B6a3EDD4B1652CB9cc5aA6f";
   * private static final String pancakeRouterAddress = "0x7a250d5630B4cF539739dF2C5dAcb4c659F2488D";
   * private static final String wbnbAddress = "0xc778417E063141139Fce010982780140Aa0cD5Ab";
   * private static final String busdAddress = "0x110a13FC3efE6A245B50102D2d79B3E76125Ae83";
   * private static final String usdtAddress = "0x110a13FC3efE6A245B50102D2d79B3E76125Ae83";
   * ST （ropsten）  0x6d3964dD2EAf9214311A0Fc300bb8ea81F8Bfb67
   * private static final String tokenAddress = "0x6d3964dD2EAf9214311A0Fc300bb8ea81F8Bfb67";
   */
  private static final String pancakeFactoryAddress = "0x5C69bEe701ef814a2B6a3EDD4B1652CB9cc5aA6f";
  private static final String pancakeRouterAddress = "0x7a250d5630B4cF539739dF2C5dAcb4c659F2488D";
  private static final String wbnbAddress = "0xc778417E063141139Fce010982780140Aa0cD5Ab";
  private static final String busdAddress = "0x110a13FC3efE6A245B50102D2d79B3E76125Ae83";
  private static final String usdtAddress = "0x110a13FC3efE6A245B50102D2d79B3E76125Ae83";
  private static final String tokenAddress = "0x6d3964dD2EAf9214311A0Fc300bb8ea81F8Bfb67";
  private static final BigInteger bnbLiquidity = Convert.toWei(BigDecimal.valueOf(300), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger usdtLiquidity = Convert.toWei(BigDecimal.valueOf(200000), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger amount = Convert.toWei(BigDecimal.valueOf(0.1), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger deadline = BigInteger.valueOf((System.currentTimeMillis() / 1000) + (600 * 30));

  public static void main(String[] args) {
    pancakePairScan();
  }

  private static void pancakePairScan() {
    IPancakeFactory pancakeFactory = IPancakeFactory.load(pancakeFactoryAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    try {
      List<String> path = new ArrayList<>();
      while (true) {
        String wbnbPairAddress = pancakeFactory.getPair(tokenAddress, wbnbAddress).send();
        if (!"0x0000000000000000000000000000000000000000".equals(wbnbPairAddress)) {
          path.add(wbnbAddress);
          path.add(tokenAddress);
          ERC20 wbnbErc20 = ERC20.load(wbnbAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
          BigInteger wbnbBalance = wbnbErc20.balanceOf(wbnbPairAddress).send();
          logger.info("wbnb-token pair create pair_address:{} >>> {}", wbnbPairAddress, wbnbBalance.divide(BigInteger.TEN.pow(18)));
          if (wbnbBalance.compareTo(bnbLiquidity) > 0) {
            break;
          } else {
            logger.info("wbnb not enough >>> {}", bnbLiquidity.divide(BigInteger.TEN.pow(18)));
          }
        }
        String busdPairAddress = pancakeFactory.getPair(tokenAddress, busdAddress).send();
        if (!"0x0000000000000000000000000000000000000000".equals(busdPairAddress)) {
          path.add(wbnbAddress);
          path.add(busdAddress);
          path.add(tokenAddress);
          ERC20 busdErc20 = ERC20.load(busdAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
          BigInteger busdBalance = busdErc20.balanceOf(busdPairAddress).send();
          logger.info("busd-token pair create pair_address:{} >>> {}", busdPairAddress, busdBalance.divide(BigInteger.TEN.pow(18)));
          if (busdBalance.compareTo(usdtLiquidity) > 0) {
            break;
          } else {
            logger.info("busd not enough >>> {}", usdtLiquidity.divide(BigInteger.TEN.pow(18)));
          }
        }
        String usdtPairAddress = pancakeFactory.getPair(tokenAddress, usdtAddress).send();
        if (!"0x0000000000000000000000000000000000000000".equals(usdtPairAddress)) {
          path.add(wbnbAddress);
          path.add(usdtAddress);
          path.add(tokenAddress);
          ERC20 usdtErc20 = ERC20.load(usdtAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
          BigInteger usdtBalance = usdtErc20.balanceOf(usdtPairAddress).send();
          logger.info("usdt-token pair create pair_address:{} >>> {}", usdtPairAddress, usdtBalance.divide(BigInteger.TEN.pow(18)));
          if (usdtBalance.compareTo(usdtLiquidity) > 0) {
            break;
          } else {
            logger.info("usdt not enough >>> {}", usdtLiquidity.divide(BigInteger.TEN.pow(18)));
          }
        }
        logger.info("token lp not create or liquidity not enough !");
      }
      logger.info("path:{}", path);
      PancakeRouter pancakeRouter = PancakeRouter.load(pancakeRouterAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
      TransactionReceipt transactionReceipt = pancakeRouter.swapExactETHForTokens(BigInteger.valueOf(1), path, toAddress, deadline, amount).send();
      logger.info("transactionReceipt >>> {}", transactionReceipt.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
