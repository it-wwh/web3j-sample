pragma solidity 0.7.1;

interface IPancakeFactory {
    function getPair(address _input1, address _input2) external view returns (address);
}

