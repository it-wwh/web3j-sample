package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.List;

/**
 * 加密后的交易数据解析
 */
public class DecodeMessage {

  private static final Logger logger = LoggerFactory.getLogger(DecodeMessage.class);

  /**
   * 通过签名后会得到一个加密后的字符串
   * 本类将分析这个字符串
   */
  public static void main(String[] args) {
    // https://ropsten.etherscan.io/tx/0xa92c4a8c7068a0f0efe873e208fca5224d30efc0d60f5c54aa36a001e9f307bb
    String signedData = "0xf86d098502540be400830493e0946db645a875e9d475d824142ca1532c245930be94880de0b6b3a76400008029a053667ae7c1caa682eac88d06a455fa4420766eee57fdbc9e8e1c22bea31c728ea032281836d021aec75eaee898e165f18861a4297f05f83b0eecfd48ed58259640";
    decodeMessage(signedData);
    decodeMessageV340(signedData);
  }

  private static void decodeMessage(String signedData) {
    try {
      logger.info("singedData >>> {}", signedData);
      logger.info("解密 start >>> {}", System.currentTimeMillis());
      RlpList rlpList = RlpDecoder.decode(Numeric.hexStringToByteArray(signedData));
      List<RlpType> values = ((RlpList) rlpList.getValues().get(0)).getValues();
      BigInteger nonce = Numeric.toBigInt(((RlpString) values.get(0)).getBytes());
      BigInteger gasPrice = Numeric.toBigInt(((RlpString) values.get(1)).getBytes());
      BigInteger gasLimit = Numeric.toBigInt(((RlpString) values.get(2)).getBytes());
      String to = Numeric.toHexString(((RlpString) values.get(3)).getBytes());
      BigInteger value = Numeric.toBigInt(((RlpString) values.get(4)).getBytes());
      String data = Numeric.toHexString(((RlpString) values.get(5)).getBytes());
      RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
      RlpString v = (RlpString) values.get(6);
      RlpString r = (RlpString) values.get(7);
      RlpString s = (RlpString) values.get(8);

      logger.info("v >>> {}", Numeric.toBigInt(v.getBytes()).toString(16));
      logger.info("r >>> {}", Numeric.toBigInt(r.getBytes()).toString(16));
      logger.info("s >>> {}", Numeric.toBigInt(s.getBytes()).toString(16));

      Sign.SignatureData signatureData = new Sign.SignatureData(
          v.getBytes()[0],
          Numeric.toBytesPadded(Numeric.toBigInt(r.getBytes()), 32),
          Numeric.toBytesPadded(Numeric.toBigInt(s.getBytes()), 32));

      BigInteger pubKey = Sign.signedMessageToKey(TransactionEncoder.encode(rawTransaction), signatureData);
      logger.info("publicKey >>> {}", pubKey.toString(16));
      String address = Numeric.prependHexPrefix(Keys.getAddress(pubKey));
      logger.info("address >>> {}", address);
      logger.info("解密 end >>> {}", System.currentTimeMillis());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 可以看到交易数据本身是没有加密的，是可以直接获取到。
   * v r s是用私钥加密的数据，利用v r s加上交易数据可以得到私钥对应的公钥及地址。
   * 所以RawTransaction里是没有fromAddress的参数的。
   * 解密出的地址就是发出交易的地址。这样一来完成了验证。
   */
  private static void decodeMessageV340(String signedData) {
    logger.info("解密 start >>> {}", System.currentTimeMillis());
    RawTransaction rawTransaction = TransactionDecoder.decode(signedData);
    if (rawTransaction instanceof SignedRawTransaction) {
      try {
        String from = ((SignedRawTransaction) rawTransaction).getFrom();
        logger.info("address >>> {}", from);
      } catch (SignatureException e) {
        e.printStackTrace();
      }
    }
    logger.info("解密 end >>> {}", System.currentTimeMillis());
  }
}
