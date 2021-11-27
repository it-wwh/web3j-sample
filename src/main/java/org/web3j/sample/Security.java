package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.utils.Environment;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 公钥私钥相关接口
 */
public class Security {

  private static final Logger logger = LoggerFactory.getLogger(Security.class);

  public static void main(String[] args) {

    exportPrivateKey("D:\\keystore\\0x38aabffc1239788232025b37610dd0fe7eb3ad73.json", "123456");

    importPrivateKey(new BigInteger(Environment.PRIVATE_KEY, 16), "123456", "D:\\keystore");

    exportBip39Wallet("D:\\keystore", "123456");
  }

  /**
   * 导出私钥
   *
   * @param keystorePath 账号的keystore路径
   * @param password     密码
   */
  private static void exportPrivateKey(String keystorePath, String password) {
    try {
      Credentials credentials = WalletUtils.loadCredentials(
          password,
          keystorePath);
      BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
      logger.info("privateKey >>> {}", privateKey.toString(16));
    } catch (IOException | CipherException e) {
      e.printStackTrace();
    }
  }

  /**
   * 导入私钥
   *
   * @param privateKey 私钥
   * @param password   密码
   * @param directory  存储路径 默认测试网络WalletUtils.getTestnetKeyDirectory() 默认主网络 WalletUtils.getMainnetKeyDirectory()
   */
  private static void importPrivateKey(BigInteger privateKey, String password, String directory) {
    ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);
    try {
      String keystoreName = WalletUtils.generateWalletFile(password,
          ecKeyPair,
          new File(directory),
          true);
      logger.info("keystore name >>> {}", keystoreName);
    } catch (CipherException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 生成带助记词的账号
   *
   * @param keystorePath
   * @param password
   */
  private static void exportBip39Wallet(String keystorePath, String password) {
    try {
      Bip39Wallet bip39Wallet = WalletUtils.generateBip39Wallet(password, new File(keystorePath));
    } catch (CipherException | IOException e) {
      e.printStackTrace();
    }
  }

}
