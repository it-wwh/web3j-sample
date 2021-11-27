package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Environment;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * 基于ERC20的token代币相关
 */
public class TokenClient {

  private static final Logger logger = LoggerFactory.getLogger(TokenClient.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final Admin admin = Admin.build(new HttpService(Environment.RPC_URL));
  private static final String fromAddress = "0x38AabFFc1239788232025B37610Dd0FE7EB3aD73";
  private static final String toAddress = "0x6Db645A875e9d475d824142cA1532C245930be94";
  private static final String contractAddress = "0x2a8e92800c7b739473ef77c94ca87d9b7219c3d0";
  private static final String emptyAddress = "0x0000000000000000000000000000000000000000";

  public static void main(String[] args) {
    getTokenBalance(web3j, fromAddress, contractAddress);
    logger.info("token name >>> {}", getTokenName(web3j, contractAddress));
    logger.info("token decimals >>> {}", getTokenDecimals(web3j, contractAddress));
    logger.info("token symbol >>> {}", getTokenSymbol(web3j, contractAddress));
    logger.info("token total supply >>> {}", getTokenTotalSupply(web3j, contractAddress));
    logger.info("trx hash >>> {}", sendTokenTransaction(
        fromAddress, "123456",
        toAddress, contractAddress,
        BigInteger.valueOf(1)));
  }

  /**
   * 查询代币余额
   */
  public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {
    Function function = new Function("balanceOf",
        Collections.singletonList(new Address(fromAddress)),
        Collections.singletonList(new TypeReference<Uint256>() {
        }));
    Transaction transaction = Transaction.createEthCallTransaction(
        fromAddress,
        contractAddress,
        FunctionEncoder.encode(function));
    BigInteger balanceValue = BigInteger.ZERO;
    try {
      EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
      balanceValue = (BigInteger) FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()).get(0).getValue();
      logger.info("address {} balance {} wei", fromAddress, balanceValue);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return balanceValue;
  }

  /**
   * 查询代币名称
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static String getTokenName(Web3j web3j, String contractAddress) {
    Function function = new Function("name",
        new ArrayList<>(),
        Collections.singletonList(new TypeReference<Utf8String>() {
        }));
    Transaction transaction = Transaction.createEthCallTransaction(
        emptyAddress,
        contractAddress,
        FunctionEncoder.encode(function));
    String name = "";
    try {
      EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      name = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()).get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return name;
  }

  /**
   * 查询代币符号
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static String getTokenSymbol(Web3j web3j, String contractAddress) {
    Function function = new Function("symbol",
        new ArrayList<>(),
        Collections.singletonList(new TypeReference<Utf8String>() {
        }));
    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(emptyAddress, contractAddress, data);
    String symbol = "";
    try {
      EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      symbol = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()).get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return symbol;
  }

  /**
   * 查询代币精度
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static int getTokenDecimals(Web3j web3j, String contractAddress) {
    Function function = new Function("decimals",
        new ArrayList<>(),
        Collections.singletonList(new TypeReference<Uint8>() {
        }));
    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(emptyAddress, contractAddress, data);
    int decimal = 0;
    try {
      EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      decimal = Integer.parseInt(FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()).get(0).getValue().toString());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return decimal;
  }

  /**
   * 查询代币发行总量
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
    Function function = new Function("totalSupply",
        new ArrayList<>(),
        Collections.singletonList(new TypeReference<Uint256>() {
        }));
    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(emptyAddress, contractAddress, data);
    BigInteger totalSupply = BigInteger.ZERO;
    try {
      EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      totalSupply = (BigInteger) FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters()).get(0).getValue();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return totalSupply;
  }

  /**
   * 代币转账
   */
  public static String sendTokenTransaction(String fromAddress, String password, String toAddress, String contractAddress, BigInteger amount) {
    String txHash = null;
    try {
      PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(
          fromAddress, password, BigInteger.valueOf(10)).send();
      if (personalUnlockAccount.accountUnlocked()) {
        Function function = new Function("transfer",
            Arrays.asList(new Address(toAddress), new Uint256(amount)),
            Collections.singletonList(new TypeReference<Bool>() {
            }));
        String data = FunctionEncoder.encode(function);
        EthGetTransactionCount ethGetTransactionCount = web3j
            .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, Environment.DEFAULT_GAS_PRICE,
            Environment.DEFAULT_GAS_LIMIT, contractAddress, data);
        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
        txHash = ethSendTransaction.getTransactionHash();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return txHash;
  }

}
