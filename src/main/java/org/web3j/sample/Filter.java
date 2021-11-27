package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Environment;

import java.math.BigInteger;

/**
 * filter相关
 * 监听区块、交易
 * 所有监听都在Web3jRx中
 * https://javadoc.io/doc/org.web3j/core/latest/org/web3j/protocol/rx/Web3jRx.html
 */
public class Filter {

  private static final Logger logger = LoggerFactory.getLogger(Filter.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));

  public static void main(String[] args) {
    // 新区块监听
    newBlockFilter();
    // 新交易监听
    newTransactionFilter();
    // 遍历旧区块、交易
    replayFilter();
    // 从某一个区块开始直到最新区块、交易
    catchUpFilter();
  }

  private static void newBlockFilter() {
    web3j.blockFlowable(false).subscribe(ethBlock ->
            logger.info("new block come in block number >>>> {}", ethBlock.getBlock().getNumber()),
        throwable -> logger.error("block filter error message >>>> {}", throwable.getMessage())).isDisposed();
  }

  private static void newTransactionFilter() {
    web3j.transactionFlowable().subscribe(transaction ->
            logger.info("transaction come in transaction txHash >>> {}", transaction.getHash()),
        throwable -> logger.error("transaction filter error message >>>> {}", throwable.getMessage())).isDisposed();
  }

  private static void replayFilter() {
    BigInteger startBlock = BigInteger.valueOf(11491873);
    BigInteger endBlock = BigInteger.valueOf(11491876);
    // 遍历旧区块
    web3j.replayPastBlocksFlowable(
            DefaultBlockParameter.valueOf(startBlock),
            DefaultBlockParameter.valueOf(endBlock),
            false)
        .subscribe(ethBlock -> logger.info("replay block >>>> {}", ethBlock.getBlock().getNumber())).isDisposed();
    // 遍历旧交易
    web3j.replayPastTransactionsFlowable(
            DefaultBlockParameter.valueOf(startBlock),
            DefaultBlockParameter.valueOf(endBlock))
        .subscribe(transaction -> logger.info("replay transaction txHash >>>> {}", transaction.getHash())).isDisposed();
  }

  private static void catchUpFilter() {
    BigInteger startBlock = BigInteger.valueOf(11491873);
    // 遍历旧区块，监听新区块
    web3j.replayPastAndFutureBlocksFlowable(DefaultBlockParameter.valueOf(startBlock), false)
        .subscribe(ethBlock -> logger.info("block number >>> {}", ethBlock.getBlock().getNumber())).isDisposed();
    // 遍历旧交易，监听新交易
    web3j.replayPastAndFutureTransactionsFlowable(DefaultBlockParameter.valueOf(startBlock))
        .subscribe(transaction -> logger.info("transaction txHash >>> {}", transaction.getHash())).isDisposed();
  }

}
