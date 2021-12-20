package org.web3j.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.model.ERC20;
import org.web3j.model.PancakeRouter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Environment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author honghui 2021/12/15
 */
public class PancakeRouterTask {

  private static final Logger logger = LoggerFactory.getLogger(PancakeRouterTask.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final String pancakeRouterAddress = "0xf7433D0c69bf4C419988A51c5c40B89BE096486a";
  private static final String usdtAddress = "0x1252FC714351e1C1c444EB128B35eAC43f68a054";
  private static final String tokenAddress = "0x815B1c7841D81FDE39D93c5768B1C9ad1B0770De";
  private static final BigInteger usdtApproveAmount = Convert.toWei(BigDecimal.valueOf(100000000), Convert.Unit.ETHER).toBigInteger();
  private static final BigInteger tokenApproveAmount = Convert.toWei(BigDecimal.valueOf(1641963300), Convert.Unit.ETHER).toBigInteger();
  private static final String toAddress = "0x6Db645A875e9d475d824142cA1532C245930be94";

  public static void main(String[] args) throws Exception {
    // buyToken
    // sellToken
    // transfer
  }

  private static void buyToken() throws Exception {
    ERC20 usdtERC20 = ERC20.load(usdtAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    TransactionReceipt usdtApproveTransactionReceipt = usdtERC20.approve(pancakeRouterAddress, usdtApproveAmount).send();
    logger.info("usdtApproveTransactionReceipt >>> {}", usdtApproveTransactionReceipt.toString());
    PancakeRouter pancakeRouter = PancakeRouter.load(pancakeRouterAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    BigInteger deadline = BigInteger.valueOf((System.currentTimeMillis() / 1000) + (600 * 30));
    BigInteger usdtBalance = usdtERC20.balanceOf(toAddress).send();
    logger.info("usdtBalance >>> {}", usdtBalance);
    BigInteger buyUsdtAmount = Convert.toWei(BigDecimal.valueOf(1), Convert.Unit.ETHER).toBigInteger();
    TransactionReceipt transactionReceipt = pancakeRouter.swapExactTokensForTokens(
        buyUsdtAmount, BigInteger.valueOf(1), Arrays.asList(usdtAddress, tokenAddress), toAddress, deadline).send();
    // 获得token的数量
    BigInteger tokenValue = usdtERC20.getTransferEvents(transactionReceipt).get(1).value;
    logger.info("transactionReceipt >>> {}", transactionReceipt.toString());
  }

  private static void sellToken() throws Exception {
    ERC20 tokenERC20 = ERC20.load(tokenAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    TransactionReceipt tokenApproveTransactionReceipt = tokenERC20.approve(pancakeRouterAddress, tokenApproveAmount).send();
    logger.info("tokenApproveTransactionReceipt >>> {}", tokenApproveTransactionReceipt.toString());
    PancakeRouter pancakeRouter = PancakeRouter.load(pancakeRouterAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    BigInteger deadline = BigInteger.valueOf((System.currentTimeMillis() / 1000) + (600 * 30));
    BigInteger tokenBalance = tokenERC20.balanceOf(toAddress).send();
    logger.info("tokenBalance >>> {}", tokenBalance);
    TransactionReceipt tokenSellUsdtTransactionReceipt = pancakeRouter.swapExactTokensForTokens(
        tokenBalance, BigInteger.valueOf(1), Arrays.asList(tokenAddress, usdtAddress), toAddress, deadline).send();
    logger.info("tokenSellUsdtTransactionReceipt >>> {}", tokenSellUsdtTransactionReceipt.toString());
  }

  private static void transfer() throws Exception {
    ERC20 usdtERC20 = ERC20.load(usdtAddress, web3j, Environment.CREDENTIALS, Environment.STATIC_GAS_PROVIDER);
    BigInteger usdtBalance = usdtERC20.balanceOf(toAddress).send();
    logger.info("usdtBalance >>> {}", usdtBalance);
    BigInteger transferAmount = Convert.toWei(BigDecimal.valueOf(1), Convert.Unit.ETHER).toBigInteger();
    TransactionReceipt transferTransactionReceipt = usdtERC20.transfer("0x38AabFFc1239788232025B37610Dd0FE7EB3aD73", transferAmount).send();
    logger.info("transferTransactionReceipt >>> {}", transferTransactionReceipt.toString());
  }

}
