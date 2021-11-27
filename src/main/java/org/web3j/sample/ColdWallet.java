package org.web3j.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Environment;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Collections;

/**
 * 冷钱包创建、交易相关
 */
public class ColdWallet {

  private static final Logger logger = LoggerFactory.getLogger(ColdWallet.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final String fromAddress = "0x38AabFFc1239788232025B37610Dd0FE7EB3aD73";
  private static final String toAddress = "0x6Db645A875e9d475d824142cA1532C245930be94";
  private static final String contractAddress = "0x2A8E92800C7B739473Ef77c94cA87D9b7219C3d0";
  // 利用createWallet方法获得 0xeb9f08fd88ed3e6fd91c3d97d1cfddce3bbaf91f
  private static final String keystore = "{\"address\":\"eb9f08fd88ed3e6fd91c3d97d1cfddce3bbaf91f\",\"id\":\"a10004f0-2e4a-46f9-8972-6f10dc057975\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"b73ca3f317adeba65f37a0c55f7a013b58c3808f1f6d6a72ba18b6ab17b6e489\",\"cipherparams\":{\"iv\":\"eee94b092bc045cc7a1355f7d93a4d4d\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"9e00e4163bc93980a472e907479def3f6bbcf93ce6f0a53726f2f97887849763\"},\"mac\":\"62fc71dc2a85cdff11f41676f00381b27ee1433d12c25a71526a3aa6ed204342\"}}";

  public static void main(String[] args) {
    try {
//      createWallet("123456");
//      decryptWallet(keystore, "123456");
//      testTransaction();
      testTokenTransaction();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void testTransaction() {
    EthGetTransactionCount ethGetTransactionCount = null;
    try {
      ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress.toLowerCase(), DefaultBlockParameterName.PENDING).send();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (ethGetTransactionCount == null) return;
    BigInteger nonce = ethGetTransactionCount.getTransactionCount();
    BigInteger value = Convert.toWei(BigDecimal.valueOf(1), Convert.Unit.ETHER).toBigInteger();
    String data = "";
    byte chainId = ChainId.ROPSTEN; // 测试网络
    try {
      String signedData = signTransaction(
          nonce,
          Environment.DEFAULT_GAS_PRICE,
          Environment.DEFAULT_GAS_LIMIT,
          toAddress.toLowerCase(),
          value,
          data,
          chainId,
          Environment.PRIVATE_KEY);
      logger.info("signedData >>> {}", signedData);
      EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
      logger.info("transaction hash >>> {}", ethSendTransaction.getTransactionHash());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void testTokenTransaction() {
    EthGetTransactionCount ethGetTransactionCount = null;
    try {
      ethGetTransactionCount = web3j.ethGetTransactionCount(ColdWallet.fromAddress, DefaultBlockParameterName.PENDING).send();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (ethGetTransactionCount == null) return;
    BigInteger nonce = ethGetTransactionCount.getTransactionCount();
    BigInteger value = BigInteger.ZERO;
    Function function = new Function("transfer",
        Arrays.asList(new Address(ColdWallet.toAddress),
            new Uint256(BigDecimal.valueOf((double) 1).multiply(BigDecimal.TEN.pow(18)).toBigInteger())),
        Collections.singletonList(new TypeReference<Bool>() {
        }));
    String data = FunctionEncoder.encode(function);
    byte chainId = ChainId.ROPSTEN;
    String signedData;
    try {
      signedData = ColdWallet.signTransaction(
          nonce,
          Environment.DEFAULT_GAS_PRICE,
          Environment.DEFAULT_GAS_LIMIT,
          contractAddress,
          value,
          data,
          chainId,
          Environment.PRIVATE_KEY);
      EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
      logger.info("transaction hash >>> {}", ethSendTransaction.getTransactionHash());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * 创建钱包
   *
   * @param password 密码
   */
  public static void createWallet(String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, JsonProcessingException {
    WalletFile walletFile;
    ECKeyPair ecKeyPair = Keys.createEcKeyPair();
    walletFile = Wallet.createStandard(password, ecKeyPair);
    logger.info("address >>> {}", walletFile.getAddress());
    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    String jsonStr = objectMapper.writeValueAsString(walletFile);
    logger.info("keystore json file >>> {}", jsonStr);
  }

  /**
   * 解密keystore 得到私钥
   *
   * @param keystore
   * @param password
   */
  public static void decryptWallet(String keystore, String password) {
    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    try {
      WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
      ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
      String privateKey = ecKeyPair.getPrivateKey().toString(16);
      logger.info("address >>> {}", walletFile.getAddress());
      logger.info("privateKey >>> {}", privateKey);
    } catch (CipherException e) {
      if ("Invalid password provided".equals(e.getMessage())) {
        logger.info("密码错误");
      }
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 签名交易
   */
  public static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                       BigInteger value, String data, byte chainId, String privateKey) throws IOException {
    byte[] signedMessage;
    RawTransaction rawTransaction = RawTransaction.createTransaction(
        nonce,
        gasPrice,
        gasLimit,
        to,
        value,
        data);

    if (privateKey.startsWith("0x")) {
      privateKey = privateKey.substring(2);
    }
    ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
    Credentials credentials = Credentials.create(ecKeyPair);

    if (chainId > ChainId.NONE) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }

    return Numeric.toHexString(signedMessage);
  }

}
