package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Hash;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.util.Arrays;

/**
 * 在发布合约前计算合约地址，根据签名后的交易信息计算TxHash
 */
public class Calculate {

  private static final Logger logger = LoggerFactory.getLogger(Calculate.class);

  public static void main(String[] args) {
    // https://ropsten.etherscan.io/tx/0xc4fbbdc273ec3f4daee9d1f9ed5646555ea275268f9795b763c9603348a089f0
    long nonce = 1;
    String calculatedAddressAsHex = calculateContractAddress("0x38AabFFc1239788232025B37610Dd0FE7EB3aD73".toLowerCase(), nonce);
    logger.info("calculatedAddressAsHex >>> {}", calculatedAddressAsHex);
    String signedData = "";
    String txHash = calculateTransactionHash(signedData);
    logger.info("transaction hash >>> {}", txHash);
  }

  /**
   * 发布前 计算合约地址
   */
  private static String calculateContractAddress(String address, long nonce) {
    byte[] addressAsBytes = Numeric.hexStringToByteArray(address);
    byte[] calculatedAddressAsBytes =
        Hash.sha3(RlpEncoder.encode(
            new RlpList(
                RlpString.create(addressAsBytes),
                RlpString.create((nonce)))));
    calculatedAddressAsBytes = Arrays.copyOfRange(calculatedAddressAsBytes,
        12, calculatedAddressAsBytes.length);
    return Numeric.toHexString(calculatedAddressAsBytes);
  }

  /**
   * 提交前 计算交易hash
   */
  private static String calculateTransactionHash(String signedData) {
    return Hash.sha3(signedData);
  }

}
