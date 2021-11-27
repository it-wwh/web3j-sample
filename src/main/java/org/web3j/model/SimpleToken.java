package org.web3j.model;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.6.4.
 */
@SuppressWarnings("rawtypes")
public class SimpleToken extends Contract {
    public static final String BINARY = "60806040526000805460a060020a60ff021916815560038190556004553480156200002957600080fd5b506040516200183238038062001832833981016040908152815160208084015192840151606085015160008054600160a060020a03191633179055600184905593850180519395909491019290916200008891600791860190620000d3565b5081516200009e906008906020850190620000d3565b50600955505060008054600160a060020a0316815260026020526040902055600a805460a060020a60ff021916905562000178565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200011657805160ff191683800117855562000146565b8280016001018555821562000146579182015b828111156200014657825182559160200191906001019062000129565b506200015492915062000158565b5090565b6200017591905b808211156200015457600081556001016200015f565b90565b6116aa80620001886000396000f3006080604052600436106101955763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde03811461019a5780630753c30c14610224578063095ea7b3146102475780630e136b191461026b5780630ecb93c01461029457806318160ddd146102b557806323b872dd146102dc57806326976e3f1461030657806327e235e314610337578063313ce56714610358578063353907141461036d5780633eaaf86b146103825780633f4ba83a1461039757806359bf1abe146103ac5780635c658165146103cd5780635c975abb146103f457806370a08231146104095780638456cb591461042a578063893d20e81461043f5780638da5cb5b1461045457806395d89b4114610469578063a9059cbb1461047e578063c0324c77146104a2578063cc872b66146104bd578063db006a75146104d5578063dd62ed3e146104ed578063dd644f7214610514578063e47d606014610529578063e4997dc51461054a578063e5b5019a1461056b578063f2fde38b14610580578063f3bdc228146105a1575b600080fd5b3480156101a657600080fd5b506101af6105c2565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101e95781810151838201526020016101d1565b50505050905090810190601f1680156102165780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561023057600080fd5b50610245600160a060020a0360043516610650565b005b34801561025357600080fd5b50610245600160a060020a03600435166024356106e8565b34801561027757600080fd5b506102806107aa565b604080519115158252519081900360200190f35b3480156102a057600080fd5b50610245600160a060020a03600435166107ba565b3480156102c157600080fd5b506102ca61082c565b60408051918252519081900360200190f35b3480156102e857600080fd5b50610245600160a060020a03600435811690602435166044356108e8565b34801561031257600080fd5b5061031b6109be565b60408051600160a060020a039092168252519081900360200190f35b34801561034357600080fd5b506102ca600160a060020a03600435166109cd565b34801561036457600080fd5b506102ca6109df565b34801561037957600080fd5b506102ca6109e5565b34801561038e57600080fd5b506102ca6109eb565b3480156103a357600080fd5b506102456109f1565b3480156103b857600080fd5b50610280600160a060020a0360043516610a67565b3480156103d957600080fd5b506102ca600160a060020a0360043581169060243516610a89565b34801561040057600080fd5b50610280610aa6565b34801561041557600080fd5b506102ca600160a060020a0360043516610ab6565b34801561043657600080fd5b50610245610b76565b34801561044b57600080fd5b5061031b610bf1565b34801561046057600080fd5b5061031b610c00565b34801561047557600080fd5b506101af610c0f565b34801561048a57600080fd5b50610245600160a060020a0360043516602435610c6a565b3480156104ae57600080fd5b50610245600435602435610d4f565b3480156104c957600080fd5b50610245600435610de4565b3480156104e157600080fd5b50610245600435610e8f565b3480156104f957600080fd5b506102ca600160a060020a0360043581169060243516610f3a565b34801561052057600080fd5b506102ca611005565b34801561053557600080fd5b50610280600160a060020a036004351661100b565b34801561055657600080fd5b50610245600160a060020a0360043516611020565b34801561057757600080fd5b506102ca61108f565b34801561058c57600080fd5b50610245600160a060020a0360043516611095565b3480156105ad57600080fd5b50610245600160a060020a03600435166110e7565b6007805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156106485780601f1061061d57610100808354040283529160200191610648565b820191906000526020600020905b81548152906001019060200180831161062b57829003601f168201915b505050505081565b600054600160a060020a0316331461066757600080fd5b600a805460a060020a74ff0000000000000000000000000000000000000000199091161773ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03831690811790915560408051918252517fcc358699805e9a8b7f77b522628c7cb9abd07d9efb86b6fb616af1609036a99e916020908290030190a150565b604060443610156106f857600080fd5b600a5460a060020a900460ff161561079b57600a54604080517faee92d33000000000000000000000000000000000000000000000000000000008152336004820152600160a060020a038681166024830152604482018690529151919092169163aee92d3391606480830192600092919082900301818387803b15801561077e57600080fd5b505af1158015610792573d6000803e3d6000fd5b505050506107a5565b6107a58383611193565b505050565b600a5460a060020a900460ff1681565b600054600160a060020a031633146107d157600080fd5b600160a060020a038116600081815260066020908152604091829020805460ff19166001179055815192835290517f42e160154868087d6bfdc0ca23d96a1c1cfa32f1b72ba9ba27b69b98a0d819dc9281900390910190a150565b600a5460009060a060020a900460ff16156108e057600a60009054906101000a9004600160a060020a0316600160a060020a03166318160ddd6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156108ad57600080fd5b505af11580156108c1573d6000803e3d6000fd5b505050506040513d60208110156108d757600080fd5b505190506108e5565b506001545b90565b60005460a060020a900460ff16156108ff57600080fd5b600160a060020a03831660009081526006602052604090205460ff161561092557600080fd5b600a5460a060020a900460ff16156109b357600a54604080517f8b477adb000000000000000000000000000000000000000000000000000000008152336004820152600160a060020a03868116602483015285811660448301526064820185905291519190921691638b477adb91608480830192600092919082900301818387803b15801561077e57600080fd5b6107a5838383611241565b600a54600160a060020a031681565b60026020526000908152604090205481565b60095481565b60045481565b60015481565b600054600160a060020a03163314610a0857600080fd5b60005460a060020a900460ff161515610a2057600080fd5b6000805474ff0000000000000000000000000000000000000000191681556040517f7805862f689e2f13df9f062ff482ad3ad112aca9e0847911ed832e158c525b339190a1565b600160a060020a03811660009081526006602052604090205460ff165b919050565b600560209081526000928352604080842090915290825290205481565b60005460a060020a900460ff1681565b600a5460009060a060020a900460ff1615610b6657600a54604080517f70a08231000000000000000000000000000000000000000000000000000000008152600160a060020a038581166004830152915191909216916370a082319160248083019260209291908290030181600087803b158015610b3357600080fd5b505af1158015610b47573d6000803e3d6000fd5b505050506040513d6020811015610b5d57600080fd5b50519050610a84565b610b6f8261143d565b9050610a84565b600054600160a060020a03163314610b8d57600080fd5b60005460a060020a900460ff1615610ba457600080fd5b6000805474ff0000000000000000000000000000000000000000191660a060020a1781556040517f6985a02210a168e66602d3235cb6db0e70f92b3ba4d376a33c0f3d9434bff6259190a1565b600054600160a060020a031690565b600054600160a060020a031681565b6008805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156106485780601f1061061d57610100808354040283529160200191610648565b60005460a060020a900460ff1615610c8157600080fd5b3360009081526006602052604090205460ff1615610c9e57600080fd5b600a5460a060020a900460ff1615610d4157600a54604080517f6e18980a000000000000000000000000000000000000000000000000000000008152336004820152600160a060020a0385811660248301526044820185905291519190921691636e18980a91606480830192600092919082900301818387803b158015610d2457600080fd5b505af1158015610d38573d6000803e3d6000fd5b50505050610d4b565b610d4b8282611458565b5050565b600054600160a060020a03163314610d6657600080fd5b60148210610d7357600080fd5b60328110610d8057600080fd5b6003829055600954610d9c908290600a0a63ffffffff6115c516565b600481905560035460408051918252602082019290925281517fb044a1e409eac5c48e5af22d4af52670dd1a99059537a78b31b48c6500a6354e929181900390910190a15050565b600054600160a060020a03163314610dfb57600080fd5b60015481810111610e0b57600080fd5b60008054600160a060020a031681526002602052604090205481810111610e3157600080fd5b60008054600160a060020a03168152600260209081526040918290208054840190556001805484019055815183815291517fcb8241adb0c3fdb35b70c24ce35c5eb0c17af7431c99f827d44a445ca624176a9281900390910190a150565b600054600160a060020a03163314610ea657600080fd5b600154811115610eb557600080fd5b60008054600160a060020a0316815260026020526040902054811115610eda57600080fd5b60018054829003905560008054600160a060020a031681526002602090815260409182902080548490039055815183815291517f702d5967f45f6513a38ffc42d6ba9bf230bd40e8f53b16363c7eb4fd2deb9a449281900390910190a150565b600a5460009060a060020a900460ff1615610ff257600a54604080517fdd62ed3e000000000000000000000000000000000000000000000000000000008152600160a060020a03868116600483015285811660248301529151919092169163dd62ed3e9160448083019260209291908290030181600087803b158015610fbf57600080fd5b505af1158015610fd3573d6000803e3d6000fd5b505050506040513d6020811015610fe957600080fd5b50519050610fff565b610ffc83836115fb565b90505b92915050565b60035481565b60066020526000908152604090205460ff1681565b600054600160a060020a0316331461103757600080fd5b600160a060020a038116600081815260066020908152604091829020805460ff19169055815192835290517fd7e9ec6e6ecd65492dce6bf513cd6867560d49544421d0783ddf06e76c24470c9281900390910190a150565b60001981565b600054600160a060020a031633146110ac57600080fd5b600160a060020a038116156110e4576000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0383161790555b50565b60008054600160a060020a031633146110ff57600080fd5b600160a060020a03821660009081526006602052604090205460ff16151561112657600080fd5b61112f82610ab6565b600160a060020a0383166000818152600260209081526040808320929092556001805485900390558151928352820183905280519293507f61e6e66b0d6339b2980aecc6ccc0039736791f0ccde9ed512e789a7fbdd698c692918290030190a15050565b604060443610156111a357600080fd5b81158015906111d45750336000908152600560209081526040808320600160a060020a038716845290915290205415155b156111de57600080fd5b336000818152600560209081526040808320600160a060020a03881680855290835292819020869055805186815290519293927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925929181900390910190a3505050565b600080806060606436101561125557600080fd5b600160a060020a03871660009081526005602090815260408083203384529091529020546003549094506112a4906127109061129890889063ffffffff6115c516565b9063ffffffff61162616565b92506004548311156112b65760045492505b6000198410156112f5576112d0848663ffffffff61163d16565b600160a060020a03881660009081526005602090815260408083203384529091529020555b611305858463ffffffff61163d16565b600160a060020a038816600090815260026020526040902054909250611331908663ffffffff61163d16565b600160a060020a038089166000908152600260205260408082209390935590881681522054611366908363ffffffff61164f16565b600160a060020a0387166000908152600260205260408120919091558311156113fb5760008054600160a060020a03168152600260205260409020546113b2908463ffffffff61164f16565b60008054600160a060020a0390811682526002602090815260408084209490945591548351878152935190821693918b169260008051602061165f833981519152928290030190a35b85600160a060020a031687600160a060020a031660008051602061165f833981519152846040518082815260200191505060405180910390a350505050505050565b600160a060020a031660009081526002602052604090205490565b6000806040604436101561146b57600080fd5b611486612710611298600354876115c590919063ffffffff16565b92506004548311156114985760045492505b6114a8848463ffffffff61163d16565b336000908152600260205260409020549092506114cb908563ffffffff61163d16565b3360009081526002602052604080822092909255600160a060020a038716815220546114fd908363ffffffff61164f16565b600160a060020a0386166000908152600260205260408120919091558311156115905760008054600160a060020a0316815260026020526040902054611549908463ffffffff61164f16565b60008054600160a060020a03908116825260026020908152604080842094909455915483518781529351911692339260008051602061165f83398151915292918290030190a35b604080518381529051600160a060020a03871691339160008051602061165f8339815191529181900360200190a35050505050565b6000808315156115d857600091506115f4565b508282028284828115156115e857fe5b04146115f057fe5b8091505b5092915050565b600160a060020a03918216600090815260056020908152604080832093909416825291909152205490565b600080828481151561163457fe5b04949350505050565b60008282111561164957fe5b50900390565b6000828201838110156115f057fe00ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3efa165627a7a7230582090b41a32414c19d69d6c528b5bba64d562d2ffb52ce2a923840f4fbc568313d60029";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_DEPRECATE = "deprecate";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_DEPRECATED = "deprecated";

    public static final String FUNC_ADDBLACKLIST = "addBlackList";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_UPGRADEDADDRESS = "upgradedAddress";

    public static final String FUNC_BALANCES = "balances";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_MAXIMUMFEE = "maximumFee";

    public static final String FUNC__TOTALSUPPLY = "_totalSupply";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_GETBLACKLISTSTATUS = "getBlackListStatus";

    public static final String FUNC_ALLOWED = "allowed";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_SETPARAMS = "setParams";

    public static final String FUNC_ISSUE = "issue";

    public static final String FUNC_REDEEM = "redeem";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_BASISPOINTSRATE = "basisPointsRate";

    public static final String FUNC_ISBLACKLISTED = "isBlackListed";

    public static final String FUNC_REMOVEBLACKLIST = "removeBlackList";

    public static final String FUNC_MAX_UINT = "MAX_UINT";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_DESTROYBLACKFUNDS = "destroyBlackFunds";

    public static final Event ISSUE_EVENT = new Event("Issue", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event REDEEM_EVENT = new Event("Redeem", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event DEPRECATE_EVENT = new Event("Deprecate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event PARAMS_EVENT = new Event("Params", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event DESTROYEDBLACKFUNDS_EVENT = new Event("DestroyedBlackFunds", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ADDEDBLACKLIST_EVENT = new Event("AddedBlackList", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event REMOVEDBLACKLIST_EVENT = new Event("RemovedBlackList", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAUSE_EVENT = new Event("Pause", 
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event UNPAUSE_EVENT = new Event("Unpause", 
            Arrays.<TypeReference<?>>asList());
    ;

    @Deprecated
    protected SimpleToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SimpleToken(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SimpleToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SimpleToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> deprecate(String _upgradedAddress) {
        final Function function = new Function(
                FUNC_DEPRECATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _upgradedAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _spender), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> deprecated() {
        final Function function = new Function(FUNC_DEPRECATED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addBlackList(String _evilUser) {
        final Function function = new Function(
                FUNC_ADDBLACKLIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _evilUser)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _from), 
                new org.web3j.abi.datatypes.Address(160, _to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> upgradedAddress() {
        final Function function = new Function(FUNC_UPGRADEDADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> balances(String param0) {
        final Function function = new Function(FUNC_BALANCES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> maximumFee() {
        final Function function = new Function(FUNC_MAXIMUMFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> _totalSupply() {
        final Function function = new Function(FUNC__TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> getBlackListStatus(String _maker) {
        final Function function = new Function(FUNC_GETBLACKLISTSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _maker)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> allowed(String param0, String param1) {
        final Function function = new Function(FUNC_ALLOWED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.Address(160, param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String who) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, who)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> pause() {
        final Function function = new Function(
                FUNC_PAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _to), 
                new org.web3j.abi.datatypes.generated.Uint256(_value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setParams(BigInteger newBasisPoints, BigInteger newMaxFee) {
        final Function function = new Function(
                FUNC_SETPARAMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(newBasisPoints), 
                new org.web3j.abi.datatypes.generated.Uint256(newMaxFee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> issue(BigInteger amount) {
        final Function function = new Function(
                FUNC_ISSUE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> redeem(BigInteger amount) {
        final Function function = new Function(
                FUNC_REDEEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _owner), 
                new org.web3j.abi.datatypes.Address(160, _spender)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> basisPointsRate() {
        final Function function = new Function(FUNC_BASISPOINTSRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> isBlackListed(String param0) {
        final Function function = new Function(FUNC_ISBLACKLISTED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeBlackList(String _clearedUser) {
        final Function function = new Function(
                FUNC_REMOVEBLACKLIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _clearedUser)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> MAX_UINT() {
        final Function function = new Function(FUNC_MAX_UINT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> destroyBlackFunds(String _blackListedUser) {
        final Function function = new Function(
                FUNC_DESTROYBLACKFUNDS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _blackListedUser)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<IssueEventResponse> getIssueEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ISSUE_EVENT, transactionReceipt);
        ArrayList<IssueEventResponse> responses = new ArrayList<IssueEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            IssueEventResponse typedResponse = new IssueEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<IssueEventResponse> issueEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, IssueEventResponse>() {
            @Override
            public IssueEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ISSUE_EVENT, log);
                IssueEventResponse typedResponse = new IssueEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<IssueEventResponse> issueEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ISSUE_EVENT));
        return issueEventFlowable(filter);
    }

    public List<RedeemEventResponse> getRedeemEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REDEEM_EVENT, transactionReceipt);
        ArrayList<RedeemEventResponse> responses = new ArrayList<RedeemEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RedeemEventResponse typedResponse = new RedeemEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RedeemEventResponse> redeemEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, RedeemEventResponse>() {
            @Override
            public RedeemEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REDEEM_EVENT, log);
                RedeemEventResponse typedResponse = new RedeemEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RedeemEventResponse> redeemEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REDEEM_EVENT));
        return redeemEventFlowable(filter);
    }

    public List<DeprecateEventResponse> getDeprecateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DEPRECATE_EVENT, transactionReceipt);
        ArrayList<DeprecateEventResponse> responses = new ArrayList<DeprecateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DeprecateEventResponse typedResponse = new DeprecateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DeprecateEventResponse> deprecateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, DeprecateEventResponse>() {
            @Override
            public DeprecateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DEPRECATE_EVENT, log);
                DeprecateEventResponse typedResponse = new DeprecateEventResponse();
                typedResponse.log = log;
                typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DeprecateEventResponse> deprecateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DEPRECATE_EVENT));
        return deprecateEventFlowable(filter);
    }

    public List<ParamsEventResponse> getParamsEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PARAMS_EVENT, transactionReceipt);
        ArrayList<ParamsEventResponse> responses = new ArrayList<ParamsEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ParamsEventResponse typedResponse = new ParamsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.feeBasisPoints = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.maxFee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ParamsEventResponse> paramsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ParamsEventResponse>() {
            @Override
            public ParamsEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PARAMS_EVENT, log);
                ParamsEventResponse typedResponse = new ParamsEventResponse();
                typedResponse.log = log;
                typedResponse.feeBasisPoints = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.maxFee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ParamsEventResponse> paramsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PARAMS_EVENT));
        return paramsEventFlowable(filter);
    }

    public List<DestroyedBlackFundsEventResponse> getDestroyedBlackFundsEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DESTROYEDBLACKFUNDS_EVENT, transactionReceipt);
        ArrayList<DestroyedBlackFundsEventResponse> responses = new ArrayList<DestroyedBlackFundsEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DestroyedBlackFundsEventResponse typedResponse = new DestroyedBlackFundsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._blackListedUser = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DestroyedBlackFundsEventResponse> destroyedBlackFundsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, DestroyedBlackFundsEventResponse>() {
            @Override
            public DestroyedBlackFundsEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DESTROYEDBLACKFUNDS_EVENT, log);
                DestroyedBlackFundsEventResponse typedResponse = new DestroyedBlackFundsEventResponse();
                typedResponse.log = log;
                typedResponse._blackListedUser = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DestroyedBlackFundsEventResponse> destroyedBlackFundsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTROYEDBLACKFUNDS_EVENT));
        return destroyedBlackFundsEventFlowable(filter);
    }

    public List<AddedBlackListEventResponse> getAddedBlackListEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDBLACKLIST_EVENT, transactionReceipt);
        ArrayList<AddedBlackListEventResponse> responses = new ArrayList<AddedBlackListEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddedBlackListEventResponse> addedBlackListEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddedBlackListEventResponse>() {
            @Override
            public AddedBlackListEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDBLACKLIST_EVENT, log);
                AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddedBlackListEventResponse> addedBlackListEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDBLACKLIST_EVENT));
        return addedBlackListEventFlowable(filter);
    }

    public List<RemovedBlackListEventResponse> getRemovedBlackListEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REMOVEDBLACKLIST_EVENT, transactionReceipt);
        ArrayList<RemovedBlackListEventResponse> responses = new ArrayList<RemovedBlackListEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RemovedBlackListEventResponse> removedBlackListEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, RemovedBlackListEventResponse>() {
            @Override
            public RemovedBlackListEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REMOVEDBLACKLIST_EVENT, log);
                RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RemovedBlackListEventResponse> removedBlackListEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REMOVEDBLACKLIST_EVENT));
        return removedBlackListEventFlowable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<PauseEventResponse> getPauseEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSE_EVENT, transactionReceipt);
        ArrayList<PauseEventResponse> responses = new ArrayList<PauseEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PauseEventResponse typedResponse = new PauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PauseEventResponse> pauseEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, PauseEventResponse>() {
            @Override
            public PauseEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PAUSE_EVENT, log);
                PauseEventResponse typedResponse = new PauseEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Flowable<PauseEventResponse> pauseEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSE_EVENT));
        return pauseEventFlowable(filter);
    }

    public List<UnpauseEventResponse> getUnpauseEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UNPAUSE_EVENT, transactionReceipt);
        ArrayList<UnpauseEventResponse> responses = new ArrayList<UnpauseEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UnpauseEventResponse typedResponse = new UnpauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<UnpauseEventResponse> unpauseEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, UnpauseEventResponse>() {
            @Override
            public UnpauseEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(UNPAUSE_EVENT, log);
                UnpauseEventResponse typedResponse = new UnpauseEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Flowable<UnpauseEventResponse> unpauseEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSE_EVENT));
        return unpauseEventFlowable(filter);
    }

    @Deprecated
    public static SimpleToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SimpleToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SimpleToken load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SimpleToken(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SimpleToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SimpleToken(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SimpleToken> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _initialSupply, String _name, String _symbol, BigInteger _decimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialSupply), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_symbol), 
                new org.web3j.abi.datatypes.generated.Uint256(_decimals)));
        return deployRemoteCall(SimpleToken.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<SimpleToken> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _initialSupply, String _name, String _symbol, BigInteger _decimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialSupply), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_symbol), 
                new org.web3j.abi.datatypes.generated.Uint256(_decimals)));
        return deployRemoteCall(SimpleToken.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SimpleToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, String _name, String _symbol, BigInteger _decimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialSupply), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_symbol), 
                new org.web3j.abi.datatypes.generated.Uint256(_decimals)));
        return deployRemoteCall(SimpleToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SimpleToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, String _name, String _symbol, BigInteger _decimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_initialSupply), 
                new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.Utf8String(_symbol), 
                new org.web3j.abi.datatypes.generated.Uint256(_decimals)));
        return deployRemoteCall(SimpleToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class IssueEventResponse extends BaseEventResponse {
        public BigInteger amount;
    }

    public static class RedeemEventResponse extends BaseEventResponse {
        public BigInteger amount;
    }

    public static class DeprecateEventResponse extends BaseEventResponse {
        public String newAddress;
    }

    public static class ParamsEventResponse extends BaseEventResponse {
        public BigInteger feeBasisPoints;

        public BigInteger maxFee;
    }

    public static class DestroyedBlackFundsEventResponse extends BaseEventResponse {
        public String _blackListedUser;

        public BigInteger _balance;
    }

    public static class AddedBlackListEventResponse extends BaseEventResponse {
        public String _user;
    }

    public static class RemovedBlackListEventResponse extends BaseEventResponse {
        public String _user;
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }

    public static class PauseEventResponse extends BaseEventResponse {
    }

    public static class UnpauseEventResponse extends BaseEventResponse {
    }
}
