package org.web3j.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Uint;
import org.web3j.model.ERC20;
import org.web3j.model.IPancakeFactory;
import org.web3j.model.PancakeRouter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Convert;
import org.web3j.utils.Environment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory合约监听PairCreated事件
 */
public class PairCreatedEventTask {

  private static final Logger logger = LoggerFactory.getLogger(PairCreatedEventTask.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final String toAddress = "0x6Db645A875e9d475d824142cA1532C245930be94";
  private static final String pancakeFactoryAddress = "0xcA143Ce32Fe78f1f7019d7d551a6402fC5350c73";
  private static final String pancakeRouterAddress = "0x10ED43C718714eb63d5aA57B78B54704E256024E";
  private static final String wbnbAddress = "0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c";
  private static final String busdAddress = "0xe9e7cea3dedca5984780bafc599bd69add087d56";
  private static final String usdtAddress = "0x55d398326f99059ff775485246999027b3197955";
  private static final BigInteger bnbLiquidity = Convert.toWei(BigDecimal.valueOf(300), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger usdtLiquidity = Convert.toWei(BigDecimal.valueOf(200000), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger amount = Convert.toWei(BigDecimal.valueOf(0.01), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger deadline = BigInteger.valueOf((System.currentTimeMillis() / 1000) + (600 * 30));

  public static void main(String[] args) {
    EthFilter filter = new EthFilter(
        DefaultBlockParameterName.LATEST,
        DefaultBlockParameterName.LATEST,
        pancakeFactoryAddress);
    Event event = new Event("PairCreated",
        Arrays.asList(
            new TypeReference<Address>(true) {
            },
            new TypeReference<Address>(true) {
            },
            new TypeReference<Address>(false) {
            },
            new TypeReference<Uint>(false) {
            }
        )
    );
    String topicData = EventEncoder.encode(event);
    filter.addSingleTopic(topicData);
    web3j.ethLogFlowable(filter).subscribe(log -> {
      logger.info("######################## Start ############################");
      EventValues eventValues = Contract.staticExtractEventParameters(event, log);
      Address tokenA = (Address) eventValues.getIndexedValues().get(0);
      Address tokenB = (Address) eventValues.getIndexedValues().get(1);
      Address pair = (Address) eventValues.getNonIndexedValues().get(0);
      Uint allPairLength = (Uint) eventValues.getNonIndexedValues().get(1);
      logger.info("tokenA >>> {} tokenB >>> {} pair >>> {} allPairLength >>> {}",
          tokenA, tokenB, pair, allPairLength.getValue());
      if (!wbnbAddress.equalsIgnoreCase(tokenA.toString()) && !busdAddress.equalsIgnoreCase(tokenA.toString())
          && !usdtAddress.equalsIgnoreCase(tokenA.toString())) {
        pancakePairScan(tokenA.toString());
      } else if (!wbnbAddress.equalsIgnoreCase(tokenB.toString()) && !busdAddress.equalsIgnoreCase(tokenB.toString())
          && !usdtAddress.equalsIgnoreCase(tokenB.toString())) {
        pancakePairScan(tokenB.toString());
      }
      logger.info("######################## End ##############################\n");
    }).isDisposed();
  }


  private static void pancakePairScan(String tokenAddress) {
    IPancakeFactory pancakeFactory = IPancakeFactory.load(pancakeFactoryAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    try {
      List<String> path = new ArrayList<>();
      String wbnbPairAddress = pancakeFactory.getPair(tokenAddress, wbnbAddress).send();
      if (!"0x0000000000000000000000000000000000000000".equals(wbnbPairAddress)) {
        path.add(wbnbAddress);
        path.add(tokenAddress);
        ERC20 wbnbErc20 = ERC20.load(wbnbAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
        BigInteger wbnbBalance = wbnbErc20.balanceOf(wbnbPairAddress).send();
        logger.info("wbnb-token pair create pair_address:{} >>> {} wbnb", wbnbPairAddress, wbnbBalance.divide(BigInteger.TEN.pow(18)));
        if (wbnbBalance.compareTo(bnbLiquidity) < 0) {
          logger.info("wbnb not enough >>> {}", bnbLiquidity.divide(BigInteger.TEN.pow(18)));
        } else {
          logger.info("path:{}", path);
          swapExactETHForTokens(path);
        }
      }
      String busdPairAddress = pancakeFactory.getPair(tokenAddress, busdAddress).send();
      if (!"0x0000000000000000000000000000000000000000".equals(busdPairAddress)) {
        path.add(wbnbAddress);
        path.add(busdAddress);
        path.add(tokenAddress);
        ERC20 busdErc20 = ERC20.load(busdAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
        BigInteger busdBalance = busdErc20.balanceOf(busdPairAddress).send();
        logger.info("busd-token pair create pair_address:{} >>> {} busd", busdPairAddress, busdBalance.divide(BigInteger.TEN.pow(18)));
        if (busdBalance.compareTo(usdtLiquidity) < 0) {
          logger.info("busd not enough >>> {}", usdtLiquidity.divide(BigInteger.TEN.pow(18)));
        } else {
          logger.info("path:{}", path);
          swapExactETHForTokens(path);
        }
      }
      String usdtPairAddress = pancakeFactory.getPair(tokenAddress, usdtAddress).send();
      if (!"0x0000000000000000000000000000000000000000".equals(usdtPairAddress)) {
        path.add(wbnbAddress);
        path.add(usdtAddress);
        path.add(tokenAddress);
        ERC20 usdtErc20 = ERC20.load(usdtAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
        BigInteger usdtBalance = usdtErc20.balanceOf(usdtPairAddress).send();
        logger.info("usdt-token pair create pair_address:{} >>> {} usdt", usdtPairAddress, usdtBalance.divide(BigInteger.TEN.pow(18)));
        if (usdtBalance.compareTo(usdtLiquidity) < 0) {
          logger.info("usdt not enough >>> {}", usdtLiquidity.divide(BigInteger.TEN.pow(18)));
        } else {
          logger.info("path:{}", path);
          swapExactETHForTokens(path);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void swapExactETHForTokens(List<String> path) throws Exception {

    logger.info(">>>>>>>>>>>>>>>>>>>>>>>开始下单>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    PancakeRouter pancakeRouter = PancakeRouter.load(pancakeRouterAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    TransactionReceipt transactionReceipt = pancakeRouter.swapExactETHForTokens(BigInteger.valueOf(1), path, toAddress, deadline, amount).send();
    logger.info("transactionReceipt >>> {}", transactionReceipt.toString());
    logger.info(">>>>>>>>>>>>>>>>>>>>>>>下单完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
  }

}
