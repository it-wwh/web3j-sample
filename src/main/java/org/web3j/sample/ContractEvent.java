package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * Event log相关
 * 监听合约event
 */
public class ContractEvent {

  private static final Logger logger = LoggerFactory.getLogger(ContractEvent.class);
  private static final Web3j web3j = Web3j.build(new HttpService(Environment.RPC_URL));
  private static final String contractAddress = "0x2A8E92800C7B739473Ef77c94cA87D9b7219C3d0";

  public static void main(String[] args) {
    EthFilter filter = new EthFilter(
        DefaultBlockParameterName.EARLIEST,
        DefaultBlockParameterName.LATEST,
        contractAddress);
    // 监听ERC20 token Transfer 交易
    Event event = new Event("Transfer",
        Arrays.asList(
            new TypeReference<Address>(true) {
            },
            new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(false) {
            }
        )
    );
    String topicData = EventEncoder.encode(event);
    filter.addSingleTopic(topicData);
    web3j.ethLogFlowable(filter).subscribe(log -> {
      logger.info("block number >>> {}", log.getBlockNumber());
      logger.info("transaction hash >>> {}", log.getTransactionHash());
      List<String> topics = log.getTopics();
      for (String topic : topics) {
        logger.info("log topic >>> {}", topic);
      }
    }).isDisposed();
  }

}
