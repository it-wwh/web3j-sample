# web3j-sample

web3j for java 样例程序 (基于web3j 5.0.0)   
环境 idea maven  
运行前提 需要有一个开启RPC或者IPC服务的以太坊节点

## 一、通过solidity文件生成对应的abi、bin、java文件

```shell
mvn web3j:generate-sources
```

**PS:上面的mvn命令目前只支持到 solidity 0.7.1，超过版本可以下载[web3j](http://docs.web3j.io/4.8.7/command_line_tools/) ，通过abi、bin文件通过下面的命令手动生成 solidity 文件对应的 java 文件**
```shell
# windows需要手动配置环境变量
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
web3j -v

solc <contract>.sol --bin --abi --optimize -o <output-dir>/
web3j generate solidity -a abi文件  -b bin文件 -o 生成Java文件文件  -p=包名 
web3j generate solidity -a PancakeRouter.abi -b PancakeRouter.bin -o ./java -p org.web3j.model
```


## 二、主要功能

- QuickStart -> 快速开始
- AccountManager -> 账号相关接口
- TransactionClient -> eth转账相关接口
- TokenClient -> token代币相关查询及转账
- ColdWallet -> 冷钱包创建、交易
- TokenBalanceTask -> 批量token代币余额查询
- EthInfo -> 连接节点的相关信息接口
- Security -> 公钥私钥相关接口
- EthMnemonic -> 生成、导入助记词(需要比特币的jar包 可以查看pom.xml)
- Filter -> 新块、新交易相关监听
- ContractEvent -> 执行合约相关log监听
- DecodeMessage -> 加密后的交易数据解析
- IBAN -> 根据官方规则生成iban及付款二维码
- Calculate -> 在发布合约前计算合约地址，根据签名后的交易信息计算TxHash
- SimpleTokenSample -> 智能合约的部署和使用
- PancakePairScanSample -> pancake新建流动性池扫描
- PairCreatedEventTask -> PancakeFactory合约监听PairCreated事件
--- 

打赏 eth/bsc 地址:0xd6723d84aD34B4cCA001ce0DB9686Ea2037fCF98
