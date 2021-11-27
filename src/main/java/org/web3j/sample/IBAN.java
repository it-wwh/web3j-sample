package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

import static java.lang.Integer.parseInt;

/**
 * 根据官方规则生成iban及付款二维码
 */
public class IBAN {

  private static final Logger logger = LoggerFactory.getLogger(IBAN.class);

  /**
   * 根据官方支持的IBAN规则生成二维码 目前支持的有imToken
   * 参考url
   * https://github.com/ethereum/web3.js/blob/develop/lib/web3/iban.js
   * 可以防止地址错误（有两位校验和）
   */
  public static void main(String[] args) {
    getIBAN();
  }

  public static void getIBAN() {
    String address = "0x38AabFFc1239788232025B37610Dd0FE7EB3aD73".toLowerCase();
    address = address.substring(2);
    BigInteger value = new BigInteger(address, 16);
    StringBuilder bban = new StringBuilder(value.toString(36).toUpperCase());
    while (bban.length() < 15 * 2) {
      bban.insert(0, '0');
    }
    logger.info("bban >>> {}", bban);
    String iban = "XE00" + bban;

    iban = iban.substring(4) + iban.substring(0, 4);
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < iban.length(); i++) {
      char chr = iban.charAt(i);
      if (chr >= 'A' && chr <= 'Z') {
        int temp = chr - 'A' + 10;
        code.append(String.valueOf(temp));
      } else {
        code.append(String.valueOf((chr - '0')));
      }
    }
    String remainder = code.toString();
    String block;
    while (remainder.length() > 2) {
      int endPoint = Math.min(remainder.length(), 9);
      block = remainder.substring(0, endPoint);
      remainder = parseInt(block, 10) % 97 + remainder.substring(block.length());
    }

    int checkNum = parseInt(remainder, 10) % 97;
    String checkDigit = ("0" + (98 - checkNum));
    checkDigit = checkDigit.substring(checkDigit.length() - 2);
    String IBAN = "XE" + checkDigit + bban;
    String qrCodeString = "iban:" + IBAN + "?token=SOTE&amount=3";
    logger.info("IBAN >>> {}", IBAN);
    logger.info("验证 >>> {}", validateIBAN(IBAN));
    logger.info("qrcode >>> {}", qrCodeString);
    decodeQRString(qrCodeString);
  }

  private static boolean validateIBAN(String iban) {
    int len = iban.length();
    if (len < 4 || !iban.matches("[0-9A-Z]+"))
      return false;

    iban = iban.substring(4) + iban.substring(0, 4);

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++)
      sb.append(Character.digit(iban.charAt(i), 36));

    BigInteger bigInt = new BigInteger(sb.toString());

    return bigInt.mod(BigInteger.valueOf(97)).intValue() == 1;
  }

  private static void decodeQRString(String result) {
    int ibanEndpoint = result.indexOf("?");
    String iban = result.substring(5, ibanEndpoint < 0 ? result.length() : ibanEndpoint);
    String address = IBAN2Address(iban);
    String query = result.substring(ibanEndpoint + 1, result.length());
    String[] params = query.split("&");
    String token = null;
    String amount = null;
    for (String param : params) {
      if (param.startsWith("token=")) {
        token = param.substring(6);
        continue;
      }
      if (param.startsWith("amount=")) {
        amount = param.substring(7);
      }
    }
    logger.info("decodeQRString");
    logger.info("address >>> {}", address);
    logger.info("token >>> {}", token);
    logger.info("amount >>> {}", address);
  }

  private static String IBAN2Address(String iban) {
    String base36 = iban.substring(4);
    StringBuilder base16 = new StringBuilder(new BigInteger(base36, 36).toString(16));
    while (base16.length() < 40) {
      base16.insert(0, "0");
    }
    return "0x" + base16.toString().toLowerCase();
  }

}
