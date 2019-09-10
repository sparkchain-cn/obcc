pragma solidity ^0.5.0;
contract SpcToken {  // s1: symbol
	string public name ;  // s2: name
	string public symbol;  // s3: symbol
	uint8 public decimals = 18;
    // token总量，默认会为public变量生成一个getter函数接口，名称为totalSupply().
    uint256 public totalSupply;
	uint256 public ONE_TOKEN = (10 ** uint256(decimals));
	mapping (address => uint256) balances;

    constructor(string memory tokenCode, string memory tokenName, uint256 amount) public  // s5: symbol
    {
        balances[msg.sender] = amount*ONE_TOKEN; // 初始token数量给予消息发送者
        totalSupply = amount*ONE_TOKEN;         // 设置初始总量
        symbol=tokenCode;
        name=tokenName;
    }

    //发生转账时必须要触发的事件
    event Transfer(address indexed _from, address indexed _to, uint256 _value);

    //从消息发送者账户中往_to账户转数量为_value的token
    function transfer(address _to, uint256 _value, string memory memos) public returns (bool success) {
        require(balances[msg.sender] >= _value && balances[_to] + _value > balances[_to]);
        balances[msg.sender] -= _value;//从消息发送者账户中减去token数量_value
        balances[_to] += _value;//往接收账户增加token数量_value
        emit Transfer(msg.sender, _to, _value);//触发转币交易事件
        return true;
    }

    // 获取账户_owner拥有token的数量  constant/view/pure
    function balanceOf(address _owner) public view returns (uint256 balance) {
        return balances[_owner];
    }
 }