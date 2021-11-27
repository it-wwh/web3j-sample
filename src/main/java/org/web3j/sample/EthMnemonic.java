package org.web3j.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * 以太坊助记词
 * 用到了比特币的jar包 org.bitcoinj
 */
public class EthMnemonic {

  /**
   * 通用的以太坊基于bip44协议的助记词路径 （imToken、Metamask）
   */
  private static final String ETH_TYPE = "m/44'/60'/0'/0/0";
  private static final Logger logger = LoggerFactory.getLogger(EthMnemonic.class);
  private static final SecureRandom secureRandom = new SecureRandom();
  private static final String password = "123456";

  public static void main(String[] args) {
    // 生成助记词
//    generateMnemonic();
    // 导入助记词 由generateMnemonic方法生成
    List<String> list = Arrays.asList("receive", "regular", "review", "census", "dash", "mammal", "broccoli", "distance", "change", "sorry", "license", "obey");
    importMnemonic(list);
  }

  public static void generateMnemonic() {
    String[] pathArray = ETH_TYPE.split("/");
    if (pathArray.length <= 1) {
      logger.info("内容不对");
      return;
    }
    String passphrase = "";
    DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase);

    createEthWallet(ds, pathArray);
  }

  private static void importMnemonic(List<String> list) {
    String[] pathArray = EthMnemonic.ETH_TYPE.split("/");
    if (pathArray.length <= 1) {
      logger.info("内容不对");
      return;
    }
    String passphrase = "";
    long creationTimeSeconds = System.currentTimeMillis() / 1000;
    DeterministicSeed ds = new DeterministicSeed(list, null, passphrase, creationTimeSeconds);

    createEthWallet(ds, pathArray);
  }

  private static void createEthWallet(DeterministicSeed ds, String[] pathArray) {
    // 根私钥
    byte[] seedBytes = ds.getSeedBytes();
    logger.info("根私钥 >>> {}", Arrays.toString(seedBytes));
    // 助记词
    List<String> mnemonic = ds.getMnemonicCode();
    assert mnemonic != null;
    logger.info("助记词 >>> {}", Arrays.toString(mnemonic.toArray()));

    try {
      // 助记词种子
      byte[] mnemonicSeedBytes = MnemonicCode.INSTANCE.toEntropy(mnemonic);
      logger.info("助记词种子 >>> {}", Arrays.toString(mnemonicSeedBytes));
      ECKeyPair mnemonicKeyPair = ECKeyPair.create(mnemonicSeedBytes);
      WalletFile walletFile = Wallet.createLight(EthMnemonic.password, mnemonicKeyPair);
      ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
      // 存这个keystore 用完后删除
      String jsonStr = objectMapper.writeValueAsString(walletFile);
      logger.info("mnemonic keystore >>> {}", jsonStr);
      // 验证
      WalletFile checkWalletFile = objectMapper.readValue(jsonStr, WalletFile.class);
      ECKeyPair ecKeyPair = Wallet.decrypt(EthMnemonic.password, checkWalletFile);
      byte[] checkMnemonicSeedBytes = Numeric.hexStringToByteArray(ecKeyPair.getPrivateKey().toString(16));
      logger.info("验证助记词种子 >>> {}", Arrays.toString(checkMnemonicSeedBytes));
      List<String> checkMnemonic = MnemonicCode.INSTANCE.toMnemonic(checkMnemonicSeedBytes);
      logger.info("验证助记词 >>> {}", Arrays.toString(checkMnemonic.toArray()));
    } catch (MnemonicException.MnemonicLengthException | MnemonicException.MnemonicWordException | MnemonicException.MnemonicChecksumException | CipherException | IOException e) {
      e.printStackTrace();
    }

    if (seedBytes == null)
      return;
    DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
    for (int i = 1; i < pathArray.length; i++) {
      ChildNumber childNumber;
      if (pathArray[i].endsWith("'")) {
        int number = Integer.parseInt(pathArray[i].substring(0,
            pathArray[i].length() - 1));
        childNumber = new ChildNumber(number, true);
      } else {
        int number = Integer.parseInt(pathArray[i]);
        childNumber = new ChildNumber(number, false);
      }
      dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
    }
    logger.info("path >>> {}", dkKey.getPathAsString());

    ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
    logger.info("eth privateKey >>> {}", keyPair.getPrivateKey().toString(16));
    logger.info("eth publicKey >>> {}", keyPair.getPublicKey().toString(16));

    try {
      WalletFile walletFile = Wallet.createLight(EthMnemonic.password, keyPair);
      logger.info("eth address >>> 0x{}", walletFile.getAddress());
      ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
      String jsonStr = objectMapper.writeValueAsString(walletFile);
      logger.info("eth keystore >>> {}", jsonStr);
      EthHDWallet ethHDWallet = new EthHDWallet(keyPair.getPrivateKey().toString(16),
          keyPair.getPublicKey().toString(16),
          mnemonic, dkKey.getPathAsString(),
          "0x" + walletFile.getAddress(), jsonStr);
      logger.info("ethHDWallet >>> {}", ethHDWallet);
    } catch (CipherException | JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  public static class EthHDWallet {
    String privateKey;
    String publicKey;
    List<String> mnemonic;
    String mnemonicPath;
    String Address;
    String keystore;

    public EthHDWallet(String privateKey, String publicKey, List<String> mnemonic, String mnemonicPath, String address, String keystore) {
      this.privateKey = privateKey;
      this.publicKey = publicKey;
      this.mnemonic = mnemonic;
      this.mnemonicPath = mnemonicPath;
      this.Address = address;
      this.keystore = keystore;
    }
  }

}
