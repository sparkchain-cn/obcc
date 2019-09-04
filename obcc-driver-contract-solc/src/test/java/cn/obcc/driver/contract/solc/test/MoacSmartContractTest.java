package cn.obcc.driver.contract.solc.test;

import cn.obcc.driver.contract.solc.core.AbiParser;
import cn.obcc.driver.contract.solc.core.SolcCompiler;
import cn.obcc.driver.contract.solc.vo.ContractInfo;
import com.alibaba.fastjson.JSONObject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoacSmartContractTest {

    //region Autowired

    //endregion

    //region sol file
    final static String contractCode_v_0_5_0 = "pragma solidity ^0.5.0;\n" +
            "contract SpcToken {  // s1: symbol\n" +
            "\tstring public name ;  // s2: name\n" +
            "\tstring public symbol;  // s3: symbol\n" +
            "\tuint8 public decimals = 18;\n" +
            "    // token总量，默认会为public变量生成一个getter函数接口，名称为totalSupply().\n" +
            "    uint256 public totalSupply;\n" +
            "\tuint256 public ONE_TOKEN = (10 ** uint256(decimals));\n" +
            "\tmapping (address => uint256) balances;\n" +
            "\n" +
            "    constructor(string memory tokenCode, string memory tokenName, uint256 amount) public  // s5: symbol\n" +
            "    {\n" +
            "        balances[msg.sender] = amount*ONE_TOKEN; // 初始token数量给予消息发送者\n" +
            "        totalSupply = amount*ONE_TOKEN;         // 设置初始总量\n" +
            "        symbol=tokenCode;\n" +
            "        name=tokenName;\n" +
            "    }\n" +
            "\n" +
            "    //发生转账时必须要触发的事件\n" +
            "    event Transfer(address indexed _from, address indexed _to, uint256 _value);\n" +
            "\n" +
            "    //从消息发送者账户中往_to账户转数量为_value的token\n" +
            "    function transfer(address _to, uint256 _value, string memory memos) public returns (bool success) {\n" +
            "        require(balances[msg.sender] >= _value && balances[_to] + _value > balances[_to]);\n" +
            "        balances[msg.sender] -= _value;//从消息发送者账户中减去token数量_value\n" +
            "        balances[_to] += _value;//往接收账户增加token数量_value\n" +
            "        emit Transfer(msg.sender, _to, _value);//触发转币交易事件\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    // 获取账户_owner拥有token的数量  constant/view/pure\n" +
            "    function balanceOf(address _owner) public view returns (uint256 balance) {\n" +
            "        return balances[_owner];\n" +
            "    }\n" +
            " }\n" +
            " \n" +
            "contract HelloWorld {\n" +
            "\tfunction helloWorld() external pure returns (string memory) {\n" +
            "\t\treturn \"Hello, World!\";\n" +
            "\t}\n" +
            "}";

    final static String contractCode_v_0_4_24 = "pragma solidity ^0.4.24; \n" +
            "contract SpcToken {  \n" +
            "\tstring public name ;  // s2: name\n" +
            "\tstring public symbol;  // s3: symbol\n" +
            "\tuint8 public decimals = 18; \n" +
            "\t// token总量，默认会为public变量生成一个getter函数接口，名称为totalSupply(). \n" +
            "\tuint256 public totalSupply; \t\n" +
            "\tuint256 public ONE_TOKEN = (10 ** uint256(decimals)); \n" +
            "\tmapping (address => uint256) balances; \n" +
            "\n" +
            "\tfunction SpcToken (string  tokenCode, string  tokenName, uint256 amount) public  // s5: symbol\n" +
            "\t{ \n" +
            "\t\tbalances[msg.sender] = amount*ONE_TOKEN; // 初始token数量给予消息发送者 \n" +
            "\t\ttotalSupply = amount*ONE_TOKEN;         // 设置初始总量            \n" +
            "\t\tsymbol=tokenCode;\n" +
            "\t\tname=tokenName;\n" +
            "\t} \n" +
            "\t\n" +
            "\t//发生转账时必须要触发的事件 \n" +
            "\tevent Transfer(address indexed _from, address indexed _to, uint256 _value); \n" +
            "\t\n" +
            "\t//从消息发送者账户中往_to账户转数量为_value的token \n" +
            "\tfunction transfer(address _to, uint256 _value,string memos) public returns (bool success) { \n" +
            "\t\trequire(balances[msg.sender] >= _value && balances[_to] + _value > balances[_to]); \n" +
            "\t\trequire(_to != 0x0); \n" +
            "\t\tbalances[msg.sender] -= _value;//从消息发送者账户中减去token数量_value \n" +
            "\t\tbalances[_to] += _value;//往接收账户增加token数量_value \n" +
            "\t\temit Transfer(msg.sender, _to, _value);//触发转币交易事件 \n" +
            "\t\treturn true; \n" +
            "\t} \n" +
            "\n" +
            "\t/// 获取账户_owner拥有token的数量  constant/view/pure\n" +
            "\tfunction balanceOf(address  _owner) public view returns (uint256 balance) { \n" +
            "\t\treturn balances[_owner]; \n" +
            "\t} \n" +
            "}\n" +
            "\n" +
            "contract HelloWorld {\n" +
            "\tfunction helloWorld() external pure returns (string memory) {\n" +
            "\t\treturn \"Hello, World!\";\n" +
            "\t}\n" +
            "}";
    //endregion

    //region consts
    final static String ABI = "[{\"constant\":true,\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"totalSupply\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"decimals\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_to\",\"type\":\"address\"},{\"name\":\"_value\",\"type\":\"uint256\"},{\"name\":\"memos\",\"type\":\"string\"}],\"name\":\"transfer\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_owner\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"balance\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"symbol\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"ONE_TOKEN\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"tokenCode\",\"type\":\"string\"},{\"name\":\"tokenName\",\"type\":\"string\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"_from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"_to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"Transfer\",\"type\":\"event\"}]";
    final static String binary = "60806040526002805460ff19166012179081905560ff16600a0a60045534801561002857600080fd5b506040516105d13803806105d1833981016040908152815160208084015183850151600480543360009081526005865296909620958202909555935484026003559184018051909492909201929161008691600191908601906100a3565b50815161009a9060009060208501906100a3565b5050505061013e565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100e457805160ff1916838001178555610111565b82800160010185558215610111579182015b828111156101115782518255916020019190600101906100f6565b5061011d929150610121565b5090565b61013b91905b8082111561011d5760008155600101610127565b90565b6104848061014d6000396000f3006080604052600436106100825763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde03811461008757806318160ddd14610111578063313ce5671461013857806356b8c7241461016357806370a08231146101ed57806395d89b411461021b578063e443348e14610230575b600080fd5b34801561009357600080fd5b5061009c610245565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100d65781810151838201526020016100be565b50505050905090810190601f1680156101035780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011d57600080fd5b506101266102d3565b60408051918252519081900360200190f35b34801561014457600080fd5b5061014d6102d9565b6040805160ff9092168252519081900360200190f35b34801561016f57600080fd5b50604080516020600460443581810135601f81018490048402850184019095528484526101d994823573ffffffffffffffffffffffffffffffffffffffff169460248035953695946064949201919081908401838280828437509497506102e29650505050505050565b604080519115158252519081900360200190f35b3480156101f957600080fd5b5061012673ffffffffffffffffffffffffffffffffffffffff600435166103d0565b34801561022757600080fd5b5061009c6103f8565b34801561023c57600080fd5b50610126610452565b6000805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102cb5780601f106102a0576101008083540402835291602001916102cb565b820191906000526020600020905b8154815290600101906020018083116102ae57829003601f168201915b505050505081565b60035481565b60025460ff1681565b336000908152600560205260408120548311801590610327575073ffffffffffffffffffffffffffffffffffffffff8416600090815260056020526040902054838101115b151561033257600080fd5b73ffffffffffffffffffffffffffffffffffffffff8416151561035457600080fd5b3360008181526005602090815260408083208054889003905573ffffffffffffffffffffffffffffffffffffffff881680845292819020805488019055805187815290519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060019392505050565b73ffffffffffffffffffffffffffffffffffffffff1660009081526005602052604090205490565b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102cb5780601f106102a0576101008083540402835291602001916102cb565b600454815600a165627a7a7230582029775a68401b578925c7ed7204fa190b7ba5c58c3a09539fd464f7e62e03bb8a0029";

    final static String erc20 = "0x436f23a9cf4415a46dd6081e81ba1e6173b64376";
    //    final static String erc20 = "0x32ccc2c27d26033c33916c079bdac0c85ae413c7";
    final static String addr = "0x53ca93457e5d2e551d98cbdaa5f6767fa52d4d61";
    final static String secret = "d4cf687b9695d958a56e082519f68c2a66381e9f4b841ac0abd1bfa40883bf86";
    final static String addr1 = "3b2412ed2cccca6e2a55c46034731241e41988f6";
    final static String secret1 = "97aaad3099357b760d57dd70676f34db88f4895feba013bf57c9c04f2e74472b";

    // SysConfig config = new SysConfig("moacTest");

    List<String> inputTypes = new ArrayList<String>() {
        {
        }
    };

    List<String> inputValues = new ArrayList<String>() {
        {
        }
    };

    List<String> outputTypes = new ArrayList<String>() {
        {
            add("uint256");
        }
    };

    Map<String, Object> otherParams = new HashMap<String, Object>() {
        {
            put("contractAddress", erc20);
            put("abi", ABI);
            put("functionName", "balanceOf");
            put("inputTypes", inputTypes);
            put("inputValues", inputValues);
            put("outputTypes", outputTypes);
        }
    };
    //endregion

    //region basic test
    @BeforeClass
    public void setupMockMvc() throws Exception {
        // mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testAbiGetFunctionInputs() throws IOException {
        Map<String, String> map;

        map = AbiParser.getFunctionInputs(ABI, "balanceOf");
        System.out.println("balanceOf:" + map);
        assert (map.size() == 1 && map.get("_owner").equalsIgnoreCase("address"));

        map = AbiParser.getFunctionInputs(ABI, "transfer");
        System.out.println("transfer:" + map);
        assert (map.size() == 3 && map.get("_value").equalsIgnoreCase("uint256"));
    }

    @Test
    public void testAbiGetFunctionOutputs() throws IOException {
        Map<String, String> map;
        map = AbiParser.getFunctionOutputs(ABI, "balanceOf");
        System.out.println("balanceOf:" + map);
        assert (map.size() == 1 && map.get("balance").equalsIgnoreCase("uint256"));
    }

//    @Test
//    public void testGetEncodeData() {
//        String result1;
//        String result2;
//
//        //new getbalance
//        result1 = MoacCommonContract.getContractBalanceData(addr);
//        System.out.println("balance1:" + result1);
//
//        //old getbalance
//        Function function2 = MoacCommonContract.getContractBalanceData_2(addr);
//        result2 = FunctionEncoder.encode(function2);
//        System.out.println("balance2:" + result2);
//
//        assert (result1.equalsIgnoreCase(result2));
//
//        result1 = MoacCommonContract.getContractTransferData(addr, "1", "hello");
//        System.out.println("transfer1:" + result1);
//
//        result2 = MoacCommonContract.getContractTransferData_2(addr, "1", "hello");
//        System.out.println("transfer2:" + result2);
//
//        assert (result1.equalsIgnoreCase(result2));
//    }

    //endregion

    //region function invoke and value
//    @Test
//    public void testGetValue1() {
//        inputTypes = new ArrayList<String>(){
//            {
////                add("address");
//            }
//        };
//
//        inputValues = new ArrayList<String>(){
//            {
////                add(addr);
//            }
//        };
//
//        outputTypes = new ArrayList<String>(){
//            {
////                add("uint256");
//                add("string");
//            }
//        };
//
//        otherParams = new HashMap<String, Object>() {
//            {
////                put("contractAddress", erc20);
//                put("contractAddress", "0xe988d47da02cef42155dc6270211ed02cb8523a9");
//                put("abi",ABI);
//                put("functionName", "name");
//                put("inputTypes", inputTypes);
//                put("inputValues", inputValues);
//                put("outputTypes", outputTypes);
//            }
//        };
//
//        String result1;
//        String result2;
//
//        //new getbalance
//        result1 = contractService.getValue(addr, otherParams, config).get("returnValue");
////        result1 = Convert.fromSha(new BigDecimal(Numeric.toBigInt(result1)), Convert.Unit.MC).toPlainString();
//        System.out.println("balance1:" + result1);
//
//        //old getbalance
//        result2 = ((JSONObject)JSONObject.parse(balanceService.getErc20Balance(addr, "moac", otherParams, config)
//                .get("data"))).get("balance").toString();
//        System.out.println("balance2:" + result2);
//
////        assert (result1.equalsIgnoreCase(result2));
//    }

//    @Test
//    public void testGetValue() {
//        String result1;
//        String result2;
//
//        //new getbalance
//        result1 = getBalance(addr, erc20);
//        result1 = Convert.fromSha(new BigDecimal(Numeric.toBigInt(result1)), Convert.Unit.MC).toPlainString();
//        System.out.println("balance1:" + result1);
//
//        //old getbalance
//        result2 = ((JSONObject) JSONObject.parse(balanceService.getErc20Balance(addr, "moac", otherParams, config)
//                .get("data"))).get("balance").toString();
//        System.out.println("balance2:" + result2);
//
//        assert (result1.equalsIgnoreCase(result2));
//    }

    private String getBalance(String from, String contractAddress) {
        inputTypes = new ArrayList<String>() {
            {
                add("address");
            }
        };

        inputValues = new ArrayList<String>() {
            {
                add(from);
            }
        };

        outputTypes = new ArrayList<String>() {
            {
                add("uint256");
            }
        };

        otherParams = new HashMap<String, Object>() {
            {
                put("contractAddress", contractAddress);
                put("abi", ABI);
                put("functionName", "balanceOf");
                put("inputTypes", inputTypes);
                put("inputValues", inputValues);
                put("outputTypes", outputTypes);
            }
        };

        return null;
        // return contractService.getValue(from, otherParams, config).get("returnValue");
    }

    @Test
    public void testInvoke() {
        Map<String, String> result = transfer(addr, secret, addr1, 0.0002, 18, "hello", erc20);
        System.out.println("invoke:" + result);
    }

    private Map<String, String> transfer(String from, String secret, String to, double value, int decimal, String memo, String contractAddress) {
        inputTypes = new ArrayList<String>() {
            {
                add("address");
                add("uint256");
                add("string");
            }
        };

        inputValues = new ArrayList<String>() {
            {
                add(to);
                //   add(MoacCommonContract.toBig(value + "", decimal).toString());
                add(memo);
            }
        };

        outputTypes = new ArrayList<String>() {
            {
                add("bool");
            }
        };

        otherParams = new HashMap<String, Object>() {
            {
                put("contractAddress", contractAddress);
                put("abi", ABI);
                put("functionName", "transfer");
                put("inputTypes", inputTypes);
                put("inputValues", inputValues);
                put("outputTypes", outputTypes);
                put("value", "0");
            }
        };

        //     Map<String, String> result = contractService.invoke(from, secret, otherParams, config);

        //   return result;
        return null;
    }

    @Test
    public void testDeploy() {
        Map<String, String> result = deploy(addr, secret, "TT1", "Test Token 1", 999980,
                binary, ABI);
        System.out.println("deploy:" + result);
    }

    private Map<String, String> deploy(String from, String secret, String tokenSymbol, String tokenName,
                                       long totalCount, String binary, String ABI) {
        inputTypes = new ArrayList<String>() {
            {
                add("string");
                add("string");
                add("uint256");
            }
        };

        inputValues = new ArrayList<String>() {
            {
                add(tokenSymbol);
                add(tokenName);
                add(totalCount + "");
            }
        };

        outputTypes = new ArrayList<String>() {
            {
            }
        };

        otherParams = new HashMap<String, Object>() {
            {
                put("binary", binary);
                put("abi", ABI);
                put("inputTypes", inputTypes);
                put("inputValues", inputValues);
                put("outputTypes", outputTypes);
                put("value", "0");
            }
        };

        //    Map<String, String> result = contractService.deploy(from, secret, otherParams, config);
        //   return result;
        return null;
    }

    //endregion

    //region solidity compiler
    @Test
    public void testCompile() throws IOException {

        //region expected
//        String spc_bin = "60806040526012600260006101000a81548160ff021916908360ff160217905550600260009054906101000a900460ff1660ff16600a0a60045534801561004557600080fd5b5060405161094d38038061094d8339818101604052606081101561006857600080fd5b81019080805164010000000081111561008057600080fd5b8281019050602081018481111561009657600080fd5b81518560018202830111640100000000821117156100b357600080fd5b505092919060200180516401000000008111156100cf57600080fd5b828101905060208101848111156100e557600080fd5b815185600182028301116401000000008211171561010257600080fd5b5050929190602001805190602001909291905050506004548102600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600454810260038190555082600190805190602001906101809291906101a0565b5081600090805190602001906101979291906101a0565b50505050610245565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101e157805160ff191683800117855561020f565b8280016001018555821561020f579182015b8281111561020e5782518255916020019190600101906101f3565b5b50905061021c9190610220565b5090565b61024291905b8082111561023e576000816000905550600101610226565b5090565b90565b6106f9806102546000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c806356b8c7241161005b57806356b8c7241461014757806370a082311461024457806395d89b411461029c578063e443348e1461031f5761007d565b806306fdde031461008257806318160ddd14610105578063313ce56714610123575b600080fd5b61008a61033d565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100ca5780820151818401526020810190506100af565b50505050905090810190601f1680156100f75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61010d6103db565b6040518082815260200191505060405180910390f35b61012b6103e1565b604051808260ff1660ff16815260200191505060405180910390f35b61022a6004803603606081101561015d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001906401000000008111156101a457600080fd5b8201836020820111156101b657600080fd5b803590602001918460018302840111640100000000831117156101d857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103f4565b604051808215151515815260200191505060405180910390f35b6102866004803603602081101561025a57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506105d7565b6040518082815260200191505060405180910390f35b6102a4610620565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102e45780820151818401526020810190506102c9565b50505050905090810190601f1680156103115780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103276106be565b6040518082815260200191505060405180910390f35b60008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103d35780601f106103a8576101008083540402835291602001916103d3565b820191906000526020600020905b8154815290600101906020018083116103b657829003601f168201915b505050505081565b60035481565b600260009054906101000a900460ff1681565b600082600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054101580156104c45750600560008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205483600560008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205401115b6104cd57600080fd5b82600560003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555082600560008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508373ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef856040518082815260200191505060405180910390a3600190509392505050565b6000600560008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106b65780601f1061068b576101008083540402835291602001916106b6565b820191906000526020600020905b81548152906001019060200180831161069957829003601f168201915b505050505081565b6004548156fea265627a7a723058204c065f7d919563aaeca7a72c3aa58d53ab151c288e48e9fa36b2a915b39ee68164736f6c634300050a0032";
        String spc_abi = "[{\"constant\":true,\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"totalSupply\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"decimals\",\"outputs\":[{\"name\":\"\",\"type\":\"uint8\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_to\",\"type\":\"address\"},{\"name\":\"_value\",\"type\":\"uint256\"},{\"name\":\"memos\",\"type\":\"string\"}],\"name\":\"transfer\",\"outputs\":[{\"name\":\"success\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_owner\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"balance\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"symbol\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"ONE_TOKEN\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"tokenCode\",\"type\":\"string\"},{\"name\":\"tokenName\",\"type\":\"string\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"_from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"_to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"Transfer\",\"type\":\"event\"}]";
//        String hello_bin = "608060405234801561001057600080fd5b5061011d806100206000396000f3fe6080604052348015600f57600080fd5b506004361060285760003560e01c8063c605f76c14602d575b600080fd5b603360ab565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101560715780820151818401526020810190506058565b50505050905090810190601f168015609d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60606040518060400160405280600d81526020017f48656c6c6f2c20576f726c64210000000000000000000000000000000000000081525090509056fea265627a7a723058204050613854307857ab99321272d2b3ee04fdcb8d8470fdb196ab09d69c07a4f364736f6c634300050a0032";
        String hello_abi = "[{\"constant\":true,\"inputs\":[],\"name\":\"helloWorld\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"pure\",\"type\":\"function\"}]";
        //endregion

        //region compile
        // Map<String, Object> map;
        ContractInfo vo;
        String source = contractCode_v_0_4_24;
        String solcPath = "c:\\solc\\run\\";
        String tempPath = "c:\\solc\\run\\result\\";
        boolean deleteTemp = true;
        vo = SolcCompiler.compile(source, solcPath, tempPath, deleteTemp);
//        System.out.println("testCompile:" + map);

        String compiled_spc_bin = (vo.getMap().get("SpcToken")).getBinary();
        String compiled_spc_abi = (vo.getMap().get("SpcToken")).getAbi();
        String compiled_hello_bin = (vo.getMap().get("HelloWorld")).getBinary();
        String compiled_hello_abi = (vo.getMap().get("HelloWorld")).getAbi();

        //assert (map.size() == 3);
//        assert (compiled_spc_bin.equalsIgnoreCase(spc_bin)); //looks the binary will be little different even the source is same.
        assert (compiled_spc_abi.equalsIgnoreCase(spc_abi));
//        assert (compiled_hello_bin.equalsIgnoreCase(hello_bin));
        assert (compiled_hello_abi.equalsIgnoreCase(hello_abi));
        //endregion

        //region deploy

        long totalSupply = 999998;
        Map<String, String> result = deploy(addr, secret, "TT1", "Test Token 1", totalSupply,
                compiled_spc_bin, compiled_spc_abi);
        System.out.println("deploy:" + result);
        JSONObject data = JSONObject.parseObject(result.get("data"));
        String deploy_hash = data.get("hash").toString();
        String contractAddress = data.get("contractAddress").toString();

        //endregion

        //region get original balance

        String balance = getBalance(addr, contractAddress);
        //  long balance1 = Convert.fromSha(new BigDecimal(Numeric.toBigInt(balance)), Convert.Unit.MC).longValue();
        //   System.out.println("balance1:" + balance1);
        //  assert (balance1 == totalSupply);

        //endregion

        //region transfer
        long transfer_count = 3;
        Map<String, String> transferResult = transfer(addr, secret, addr1, transfer_count, 18, "hello", contractAddress);
        System.out.println("transfer:" + transferResult);

        data = JSONObject.parseObject(transferResult.get("data"));
        String transfer_hash = data.get("hash").toString();

        assert (!transfer_hash.isEmpty());
        //endregion

        //region get updated balance
        otherParams = new HashMap<String, Object>() {
        };
        Map<String, String> payment;
        String blockNumber = "";
        int count = 1;

//        while (blockNumber.isEmpty()) {
//            payment = moacClientService.payment(null, transfer_hash, otherParams, config);
//            data = JSONObject.parseObject(payment.get("data"));
//            try {
//                blockNumber = data.get("blockNumber").toString();
//            } catch (Exception ex) {
//            }
////            System.out.println(count ++ + ". payment:" + payment);
//        }

        balance = getBalance(addr, contractAddress);
        //  long balance2 = Convert.fromSha(new BigDecimal(Numeric.toBigInt(balance)), Convert.Unit.MC).longValue();
        //   System.out.println("balance2:" + balance2);
        //   assert (balance2 == totalSupply - transfer_count);

        //endregion
    }

    //endregion
}
