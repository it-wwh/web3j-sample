package org.web3j.model;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
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
 * <p>Generated with web3j version 4.8.7.
 */
@SuppressWarnings("rawtypes")
public class Verification extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610205806100206000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c806319045a2514610030575b600080fd5b6100dd6004803603604081101561004657600080fd5b8135919081019060408101602082013564010000000081111561006857600080fd5b82018360208201111561007a57600080fd5b8035906020019184600183028401116401000000008311171561009c57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506100f9945050505050565b604080516001600160a01b039092168252519081900360200190f35b600080600080845160411461011457600093505050506101c9565b50505060208201516040830151606084015160001a601b81101561013657601b015b8060ff16601b1415801561014e57508060ff16601c14155b1561015f57600093505050506101c9565b60018682858560405160008152602001604052604051808581526020018460ff1681526020018381526020018281526020019450505050506020604051602081039080840390855afa1580156101b9573d6000803e3d6000fd5b5050506020604051035193505050505b9291505056fea264697066735822122089c3702a73bbd9d1603513ced13e0b904d1839efede03c59617f7280a5b1f33364736f6c63430007010033";

    public static final String FUNC_RECOVER = "recover";

    @Deprecated
    protected Verification(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Verification(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Verification(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Verification(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> recover(byte[] hash, byte[] signature) {
        final Function function = new Function(FUNC_RECOVER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(hash), 
                new org.web3j.abi.datatypes.DynamicBytes(signature)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static Verification load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Verification(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Verification load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Verification(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Verification load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Verification(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Verification load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Verification(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Verification> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Verification.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Verification> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Verification.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Verification> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Verification.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Verification> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Verification.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
