package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Environment;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 转账相关接口
 * web3j.ethSendRawTransaction() 发送交易 需要用私钥自签名交易 详见ColdWallet.java
 */
public class TransactionClient {

  private static final Logger logger = LoggerFactory.getLogger(TransactionClient.class);
  private static Web3j web3j;
  private static Admin admin;
  private static final String fromAddress = "0x38AabFFc1239788232025B37610Dd0FE7EB3aD73";
  private static final String toAddress = "0x6Db645A875e9d475d824142cA1532C245930be94";

  public static void main(String[] args) {
    web3j = Web3j.build(new HttpService(Environment.RPC_URL));
    admin = Admin.build(new HttpService(Environment.RPC_URL));
    getBalance(fromAddress);
    getBalance(toAddress);
    sendTransaction();
  }

  /**
   * 获取余额
   *
   * @param address 钱包地址
   */
  private static void getBalance(String address) {
    BigInteger balance = null;
    try {
      EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
      balance = ethGetBalance.getBalance();
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.info("address {} balance {} wei", address, balance);
  }

  /**
   * 生成一个普通交易对象
   *
   * @param fromAddress 放款方
   * @param toAddress   收款方
   * @param nonce       交易序号
   * @param gasPrice    gas 价格
   * @param gasLimit    gas 数量
   * @param value       金额
   * @return 交易对象
   */
  private static Transaction makeTransaction(String fromAddress, String toAddress,
                                             BigInteger nonce, BigInteger gasPrice,
                                             BigInteger gasLimit, BigInteger value) {
    Transaction transaction;
    transaction = Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
    return transaction;
  }

  /**
   * 获取普通交易的gas上限
   *
   * @param transaction 交易对象
   * @return gas 上限
   */
  private static BigInteger getTransactionGasLimit(Transaction transaction) {
    BigInteger gasLimit = BigInteger.ZERO;
    try {
      EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
      gasLimit = ethEstimateGas.getAmountUsed();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return gasLimit;
  }

  /**
   * 获取账号交易次数 nonce
   *
   * @param address 钱包地址
   * @return nonce
   */
  private static BigInteger getTransactionNonce(String address) {
    BigInteger nonce = BigInteger.ZERO;
    try {
      EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
      nonce = ethGetTransactionCount.getTransactionCount();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return nonce;
  }

  /**
   * 发送一个普通交易
   *
   * @return 交易 Hash
   */
  private static String sendTransaction() {
    String password = "123456";
    BigInteger unlockDuration = BigInteger.valueOf(60L);
    BigDecimal amount = new BigDecimal("1");
    String txHash = null;
    try {
      PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(fromAddress, password, unlockDuration).send();
      if (personalUnlockAccount.accountUnlocked()) {
        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        Transaction transaction = makeTransaction(fromAddress, toAddress,
            null, null, null, value);
        // 不是必须的 可以使用默认值
        BigInteger gasLimit = getTransactionGasLimit(transaction);
        // 不是必须的 缺省值就是正确的值
        BigInteger nonce = getTransactionNonce(fromAddress);
        // 该值为大部分矿工可接受的gasPrice
        transaction = makeTransaction(fromAddress, toAddress,
            nonce, Environment.DEFAULT_GAS_PRICE,
            gasLimit, value);
        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
        txHash = ethSendTransaction.getTransactionHash();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.info("tx hash >>> {}" + txHash);
    return txHash;
  }

}
