package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Environment;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * 账号相关接口
 */
public class AccountManager {

  private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);
  private static final Admin admin = Admin.build(new HttpService(Environment.RPC_URL));

  public static void main(String[] args) {
    createNewAccount();
    getAccountList();
    unlockAccount();
  }

  /**
   * 创建账号
   */
  private static void createNewAccount() {
    String password = "123456";
    try {
      NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
      String address = newAccountIdentifier.getAccountId();
      logger.info("new account address >>> {}", address);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取账号列表
   */
  private static void getAccountList() {
    try {
      PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
      List<String> addressList;
      addressList = personalListAccounts.getAccountIds();
      logger.info("account size >>> {}", addressList.size());
      for (String address : addressList) {
        logger.info("address >>> {}", address);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 账号解锁
   */
  private static void unlockAccount() {
    String address = "0x80689f3084cf39efbd63cbe77893c2381625676c";
    String password = "123456";
    // 账号解锁持续时间 单位秒
    BigInteger unlockDuration = BigInteger.valueOf(60L);
    try {
      PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password, unlockDuration).send();
      Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
      logger.info("account unlock >>> {}", isUnlocked);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
