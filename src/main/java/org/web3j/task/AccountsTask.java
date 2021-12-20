package org.web3j.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 批量创建账户，并导出地址、私钥
 * 多线程有点问题，过后在研究
 */
public class AccountsTask {

  private static final Logger logger = LoggerFactory.getLogger(AccountsTask.class);
  private static final String password = "123456";
  private static final String accountsPath = "D:\\accountsTest\\";

  public static void main(String[] args) throws Exception {
    try {
      StringBuilder accounts = new StringBuilder();
      StringBuilder address = new StringBuilder();
      StringBuilder privateKey = new StringBuilder();
      CountDownLatch countDownLatch = new CountDownLatch(100);
      AtomicInteger atomicInteger = new AtomicInteger(1);
      // 开启10个线程
      for (int i = 0; i < 10; i++) {
        new Thread(
            () -> {
              // 每个线程跑10条数据
              for (int j = 0; j < 10; j++) {
                String keystore = "";
                try {
                  keystore = createWallet(password);
                } catch (Exception e) {
                  logger.error("创建账户失败！");
                }
                Account account = decryptWallet(keystore, password);
                address.append(account.getAddress()).append("\n");
                privateKey.append(account.getPrivateKey()).append("\n");
                accounts.append(account.getAddress()).append(" ").append(account.getPrivateKey()).append("\n");
                logger.info("new account {} {} {}", atomicInteger.getAndIncrement(), account.getAddress(), account.getPrivateKey());
                countDownLatch.countDown();
              }
            }
        ).start();
      }
      countDownLatch.await();
      exportTxt(address.toString(), "address.txt");
      exportTxt(privateKey.toString(), "privateKey.txt");
      exportTxt(accounts.toString(), "accounts.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void exportTxt(String accounts, String fileName) throws Exception {
    // 1.使用File类打开一个文件；
    File file = new File(accountsPath + fileName);
    // 2.通过字节流或字符流的子类指定输出的位置
    OutputStream fos = new FileOutputStream(file);
    // 3.进行写操作
    fos.write(accounts.getBytes(StandardCharsets.UTF_8));
    // 4.关闭输出
    fos.close();
  }

  /**
   * 创建钱包
   *
   * @param password 密码
   */
  public static String createWallet(String password) throws Exception {
    ECKeyPair ecKeyPair = Keys.createEcKeyPair();
    WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    return objectMapper.writeValueAsString(walletFile);
  }

  /**
   * 解密keystore 得到私钥
   *
   * @param keystore
   * @param password
   */
  public static Account decryptWallet(String keystore, String password) {
    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    String address = "";
    String privateKey = "";
    try {
      WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
      ECKeyPair ecKeyPair = Wallet.decrypt(password, walletFile);
      privateKey = ecKeyPair.getPrivateKey().toString(16);
      address = walletFile.getAddress();
    } catch (CipherException e) {
      if ("Invalid password provided".equals(e.getMessage())) {
        logger.info("密码错误");
      }
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new Account("0x" + address, privateKey);
  }

  public static class Account {
    private String address;
    private String privateKey;

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getPrivateKey() {
      return privateKey;
    }

    public void setPrivateKey(String privateKey) {
      this.privateKey = privateKey;
    }

    public Account() {
    }

    public Account(String address, String privateKey) {
      this.address = address;
      this.privateKey = privateKey;
    }

    @Override
    public String toString() {
      return "Account{" +
          "address='" + address + '\'' +
          ", privateKey='" + privateKey + '\'' +
          '}';
    }
  }
}
