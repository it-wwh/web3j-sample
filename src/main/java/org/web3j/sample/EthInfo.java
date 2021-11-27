package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Environment;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 连接节点的相关信息接口
 */
public class EthInfo {

  private static final Logger logger = LoggerFactory.getLogger(EthInfo.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));

  public static void main(String[] args) {
    getEthInfo();
  }

  /**
   * 请求区块链的信息
   */
  private static void getEthInfo() {
    Web3ClientVersion web3ClientVersion;
    try {
      // 客户端版本
      web3ClientVersion = web3j.web3ClientVersion().send();
      String clientVersion = web3ClientVersion.getWeb3ClientVersion();
      logger.info("clientVersion >>> {}", clientVersion);
      // 区块数量
      EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
      BigInteger blockNumber = ethBlockNumber.getBlockNumber();
      logger.info("blockNumber >>> {}", blockNumber);
      // 挖矿奖励账户
      EthCoinbase ethCoinbase = web3j.ethCoinbase().send();
      String coinbaseAddress = ethCoinbase.getAddress();
      logger.info("coinbaseAddress >>> {}", coinbaseAddress);
      // 是否在同步区块
      EthSyncing ethSyncing = web3j.ethSyncing().send();
      boolean isSyncing = ethSyncing.isSyncing();
      logger.info("isSyncing >>> {}", isSyncing);
      // 是否在挖矿
      EthMining ethMining = web3j.ethMining().send();
      boolean isMining = ethMining.isMining();
      logger.info("isMining >>> {}", isMining);
      // 当前gas price
      EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
      BigInteger gasPrice = ethGasPrice.getGasPrice();
      logger.info("gasPrice >>> {}", gasPrice);
      // 挖矿速度
      EthHashrate ethHashrate = web3j.ethHashrate().send();
      BigInteger hashRate = ethHashrate.getHashrate();
      logger.info("hashRate >>> {}", hashRate);
      // 协议版本
      EthProtocolVersion ethProtocolVersion = web3j.ethProtocolVersion().send();
      String protocolVersion = ethProtocolVersion.getProtocolVersion();
      logger.info("protocolVersion >>> {}", protocolVersion);
      // 连接的节点数
      NetPeerCount netPeerCount = web3j.netPeerCount().send();
      BigInteger peerCount = netPeerCount.getQuantity();
      logger.info("peerCount >>> {}", peerCount);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
