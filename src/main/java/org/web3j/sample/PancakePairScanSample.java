package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.model.ERC20;
import org.web3j.model.IPancakeFactory;
import org.web3j.protocol.Web3j;
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
  private static final String pancakeFactoryAddress = "0xcA143Ce32Fe78f1f7019d7d551a6402fC5350c73";
  private static final String wbnbAddress = "0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c";
  private static final String busdAddress = "0xe9e7cea3dedca5984780bafc599bd69add087d56";
  private static final String usdtAddress = "0x55d398326f99059ff775485246999027b3197955";
  private static final BigInteger bnbLiquidity = Convert.toWei(BigDecimal.valueOf(300), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger usdtLiquidity = Convert.toWei(BigDecimal.valueOf(200000), Convert.Unit.ETHER).toBigInteger();
  /**
   * RICE 0x338AF54976B9D4F7F41c97dcb60dFEc0694149f9
   * LOA  0x94b69263FCA20119Ae817b6f783Fc0F13B02ad50
   */
  private static final String tokenAddress = "0x338AF54976B9D4F7F41c97dcb60dFEc0694149f9";

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
          ERC20 busdErc20 = ERC20.load(busdPairAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
          BigInteger busdBalance = busdErc20.balanceOf(busdPairAddress).send();
          logger.info("busd-token pair create pair_address:{} >>> {}", wbnbPairAddress, busdBalance.divide(BigInteger.TEN.pow(18)));
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
          ERC20 usdtErc20 = ERC20.load(usdtPairAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
          BigInteger usdtBalance = usdtErc20.balanceOf(busdPairAddress).send();
          logger.info("usdt-token pair create pair_address:{} >>> {}", wbnbPairAddress, usdtBalance.divide(BigInteger.TEN.pow(18)));
          if (usdtBalance.compareTo(usdtLiquidity) > 0) {
            break;
          } else {
            logger.info("usdt not enough >>> {}", usdtLiquidity.divide(BigInteger.TEN.pow(18)));
          }
        }
        logger.info("token lp not create or liquidity not enough !");
      }
      logger.info("path:{}", path);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
