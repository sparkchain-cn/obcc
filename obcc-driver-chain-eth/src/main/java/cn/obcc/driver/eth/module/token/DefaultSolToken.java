package cn.obcc.driver.eth.module.token;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName DefaultSolToken
 * @desc TODO
 * @date 2019/9/9 0009  16:45
 **/
public class DefaultSolToken {

    public static final String DEFAULT_TOKEN_STR = "pragma solidity ^0.4.24;\n" +
            "\n" +
            "/*\n" +
            " * @history\n" +
            " * v0.1.0 Create ERC20 based on openzeppelin erc20. Combine all contracts in one file.\n" +
            " * v0.1.1 Add mintRoler in ERC20Burnable.\n" +
            " * v0.2.2 Combine most contracts into ERC20 contract to make it compile bytecode out.  Use ownerable to replace pauseable and mintable.\n" +
            " */\n" +
            "\n" +
            "\n" +
            "\n" +
            "/*\n" +
            " * @dev Provides information about the current execution context, including the\n" +
            " * sender of the transaction and its data. While these are generally available\n" +
            " * via msg.sender and msg.data, they should not be accessed in such a direct\n" +
            " * manner, since when dealing with GSN meta-transactions the account sending and\n" +
            " * paying for execution may not be the actual sender (as far as an application\n" +
            " * is concerned).\n" +
            " *\n" +
            " * This contract is only required for intermediate, library-like contracts.\n" +
            " */\n" +
            "contract Context {\n" +
            "    // Empty internal constructor, to prevent people from mistakenly deploying\n" +
            "    // an instance of this contract, which should be used via inheritance.\n" +
            "    constructor () internal { }\n" +
            "    // solhint-disable-previous-line no-empty-blocks\n" +
            "\n" +
            "    function _msgSender() internal view returns (address) {\n" +
            "        return msg.sender;\n" +
            "    }\n" +
            "\n" +
            "    function _msgData() internal view returns (bytes memory) {\n" +
            "        this; // silence state mutability warning without generating bytecode - see https://github.com/ethereum/solidity/issues/2691\n" +
            "        return msg.data;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "\n" +
            "\n" +
            "/**\n" +
            " * @dev Wrappers over Solidity's arithmetic operations with added overflow\n" +
            " * checks.\n" +
            " *\n" +
            " * Arithmetic operations in Solidity wrap on overflow. This can easily result\n" +
            " * in bugs, because programmers usually assume that an overflow raises an\n" +
            " * error, which is the standard behavior in high level programming languages.\n" +
            " * `SafeMath` restores this intuition by reverting the transaction when an\n" +
            " * operation overflows.\n" +
            " *\n" +
            " * Using this library instead of the unchecked operations eliminates an entire\n" +
            " * class of bugs, so it's recommended to use it always.\n" +
            " */\n" +
            "library SafeMath {\n" +
            "    /**\n" +
            "     * @dev Returns the addition of two unsigned integers, reverting on\n" +
            "     * overflow.\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `+` operator.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - Addition cannot overflow.\n" +
            "     */\n" +
            "    function add(uint256 a, uint256 b) internal pure returns (uint256) {\n" +
            "        uint256 c = a + b;\n" +
            "        require(c >= a, \"SafeMath: addition overflow\");\n" +
            "\n" +
            "        return c;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the subtraction of two unsigned integers, reverting on\n" +
            "     * overflow (when the result is negative).\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `-` operator.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - Subtraction cannot overflow.\n" +
            "     */\n" +
            "    function sub(uint256 a, uint256 b) internal pure returns (uint256) {\n" +
            "        return sub(a, b, \"SafeMath: subtraction overflow\");\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the subtraction of two unsigned integers, reverting with custom message on\n" +
            "     * overflow (when the result is negative).\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `-` operator.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - Subtraction cannot overflow.\n" +
            "     *\n" +
            "     * NOTE: This is a feature of the next version of OpenZeppelin Contracts.\n" +
            "     * @dev Get it via `npm install @openzeppelin/contracts@next`.\n" +
            "     */\n" +
            "    function sub(uint256 a, uint256 b, string memory errorMessage) internal pure returns (uint256) {\n" +
            "        require(b <= a, errorMessage);\n" +
            "        uint256 c = a - b;\n" +
            "\n" +
            "        return c;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the multiplication of two unsigned integers, reverting on\n" +
            "     * overflow.\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `*` operator.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - Multiplication cannot overflow.\n" +
            "     */\n" +
            "    function mul(uint256 a, uint256 b) internal pure returns (uint256) {\n" +
            "        // Gas optimization: this is cheaper than requiring 'a' not being zero, but the\n" +
            "        // benefit is lost if 'b' is also tested.\n" +
            "        // See: https://github.com/OpenZeppelin/openzeppelin-contracts/pull/522\n" +
            "        if (a == 0) {\n" +
            "            return 0;\n" +
            "        }\n" +
            "\n" +
            "        uint256 c = a * b;\n" +
            "        require(c / a == b, \"SafeMath: multiplication overflow\");\n" +
            "\n" +
            "        return c;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the integer division of two unsigned integers. Reverts on\n" +
            "     * division by zero. The result is rounded towards zero.\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `/` operator. Note: this function uses a\n" +
            "     * `revert` opcode (which leaves remaining gas untouched) while Solidity\n" +
            "     * uses an invalid opcode to revert (consuming all remaining gas).\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - The divisor cannot be zero.\n" +
            "     */\n" +
            "    function div(uint256 a, uint256 b) internal pure returns (uint256) {\n" +
            "        return div(a, b, \"SafeMath: division by zero\");\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the integer division of two unsigned integers. Reverts with custom message on\n" +
            "     * division by zero. The result is rounded towards zero.\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `/` operator. Note: this function uses a\n" +
            "     * `revert` opcode (which leaves remaining gas untouched) while Solidity\n" +
            "     * uses an invalid opcode to revert (consuming all remaining gas).\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - The divisor cannot be zero.\n" +
            "\n" +
            "     * NOTE: This is a feature of the next version of OpenZeppelin Contracts.\n" +
            "     * @dev Get it via `npm install @openzeppelin/contracts@next`.\n" +
            "     */\n" +
            "    function div(uint256 a, uint256 b, string memory errorMessage) internal pure returns (uint256) {\n" +
            "        // Solidity only automatically asserts when dividing by 0\n" +
            "        require(b > 0, errorMessage);\n" +
            "        uint256 c = a / b;\n" +
            "        // assert(a == b * c + a % b); // There is no case in which this doesn't hold\n" +
            "\n" +
            "        return c;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the remainder of dividing two unsigned integers. (unsigned integer modulo),\n" +
            "     * Reverts when dividing by zero.\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `%` operator. This function uses a `revert`\n" +
            "     * opcode (which leaves remaining gas untouched) while Solidity uses an\n" +
            "     * invalid opcode to revert (consuming all remaining gas).\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - The divisor cannot be zero.\n" +
            "     */\n" +
            "    function mod(uint256 a, uint256 b) internal pure returns (uint256) {\n" +
            "        return mod(a, b, \"SafeMath: modulo by zero\");\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the remainder of dividing two unsigned integers. (unsigned integer modulo),\n" +
            "     * Reverts with custom message when dividing by zero.\n" +
            "     *\n" +
            "     * Counterpart to Solidity's `%` operator. This function uses a `revert`\n" +
            "     * opcode (which leaves remaining gas untouched) while Solidity uses an\n" +
            "     * invalid opcode to revert (consuming all remaining gas).\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - The divisor cannot be zero.\n" +
            "     *\n" +
            "     * NOTE: This is a feature of the next version of OpenZeppelin Contracts.\n" +
            "     * @dev Get it via `npm install @openzeppelin/contracts@next`.\n" +
            "     */\n" +
            "    function mod(uint256 a, uint256 b, string memory errorMessage) internal pure returns (uint256) {\n" +
            "        require(b != 0, errorMessage);\n" +
            "        return a % b;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "\n" +
            "\n" +
            "/**\n" +
            " * @title Ownable\n" +
            " * @dev The Ownable contract has an owner address, and provides basic authorization control\n" +
            " * functions, this simplifies the implementation of \"user permissions\".\n" +
            " */\n" +
            "contract Ownable {\n" +
            "  address public owner;\n" +
            "\n" +
            "\n" +
            "  /**\n" +
            "   * @dev The Ownable constructor sets the original `owner` of the contract to the sender\n" +
            "   * account.\n" +
            "   */\n" +
            "  constructor() public {\n" +
            "    owner = msg.sender;\n" +
            "  }\n" +
            "\n" +
            "\n" +
            "  /**\n" +
            "   * @dev Throws if called by any account other than the owner.\n" +
            "   */\n" +
            "  modifier onlyOwner() {\n" +
            "    require(msg.sender == owner);\n" +
            "    _;\n" +
            "  }\n" +
            "\n" +
            "\n" +
            "  /**\n" +
            "   * @dev Allows the current owner to transfer control of the contract to a newOwner.\n" +
            "   * @param newOwner The address to transfer ownership to.\n" +
            "   */\n" +
            "  function transferOwnership(address newOwner) public onlyOwner {\n" +
            "    if (newOwner != address(0)) {\n" +
            "      owner = newOwner;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "}\n" +
            "\n" +
            "\n" +
            "\n" +
            "/**\n" +
            " * @title Pausable\n" +
            " * @dev Base contract which allows children to implement an emergency stop mechanism.\n" +
            " */\n" +
            "contract Pausable is Ownable {\n" +
            "  event Pause();\n" +
            "  event Unpause();\n" +
            "\n" +
            "  bool public paused = false;\n" +
            "\n" +
            "\n" +
            "  /**\n" +
            "   * @dev modifier to allow actions only when the contract IS paused\n" +
            "   */\n" +
            "  modifier whenNotPaused() {\n" +
            "    require(!paused);\n" +
            "    _;\n" +
            "  }\n" +
            "\n" +
            "  /**\n" +
            "   * @dev modifier to allow actions only when the contract IS NOT paused\n" +
            "   */\n" +
            "  modifier whenPaused {\n" +
            "    require(paused);\n" +
            "    _;\n" +
            "  }\n" +
            "\n" +
            "  /**\n" +
            "   * @dev called by the owner to pause, triggers stopped state\n" +
            "   */\n" +
            "  function pause() public onlyOwner whenNotPaused returns (bool) {\n" +
            "    paused = true;\n" +
            "    emit Pause();\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  /**\n" +
            "   * @dev called by the owner to unpause, returns to normal state\n" +
            "   */\n" +
            "  function unpause() public onlyOwner whenPaused returns (bool) {\n" +
            "    paused = false;\n" +
            "    emit Unpause();\n" +
            "    return true;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "\n" +
            "\n" +
            "contract ERC20 is Context, Ownable, Pausable {\n" +
            "    using SafeMath for uint256;\n" +
            "    \n" +
            "    string private _name;\n" +
            "    string private _symbol;\n" +
            "    uint8 private _decimals;\n" +
            "    uint256 private _totalSupply;\n" +
            "\n" +
            "    mapping (address => uint256) private _balances;\n" +
            "    \n" +
            "    mapping (address => mapping (address => uint256)) private _allowances;\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Emitted when `value` tokens are moved from one account (`from`) to\n" +
            "     * another (`to`).\n" +
            "     *\n" +
            "     * Note that `value` may be zero.\n" +
            "     */\n" +
            "    event Transfer(address indexed from, address indexed to, uint256 value);\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Emitted when the allowance of a `spender` for an `owner` is set by\n" +
            "     * a call to {approve}. `value` is the new allowance.\n" +
            "     */\n" +
            "    event Approval(address indexed owner, address indexed spender, uint256 value);\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Sets the values for `name`, `symbol`, and `decimals`. All three of\n" +
            "     * these values are immutable: they can only be set once during\n" +
            "     * construction.\n" +
            "     */\n" +
            "    constructor () public {\n" +
            "        _name = \"TestERC20\";\n" +
            "        _symbol = \"TE20\";\n" +
            "        _decimals = 6;\n" +
            "        _totalSupply = 99999000000;\n" +
            "        _balances[_msgSender()] = _totalSupply;  \n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the name of the token.\n" +
            "     */\n" +
            "    function name() public view returns (string memory) {\n" +
            "        return _name;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the symbol of the token, usually a shorter version of the\n" +
            "     * name.\n" +
            "     */\n" +
            "    function symbol() public view returns (string memory) {\n" +
            "        return _symbol;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Returns the number of decimals used to get its user representation.\n" +
            "     * For example, if `decimals` equals `2`, a balance of `505` tokens should\n" +
            "     * be displayed to a user as `5,05` (`505 / 10 ** 2`).\n" +
            "     *\n" +
            "     * Tokens usually opt for a value of 18, imitating the relationship between\n" +
            "     * Ether and Wei.\n" +
            "     *\n" +
            "     * NOTE: This information is only used for _display_ purposes: it in\n" +
            "     * no way affects any of the arithmetic of the contract, including\n" +
            "     * {IERC20-balanceOf} and {IERC20-transfer}.\n" +
            "     */\n" +
            "    function decimals() public view returns (uint8) {\n" +
            "        return _decimals;\n" +
            "    }\n" +
            "    \n" +
            "    /**\n" +
            "     * @dev See {IERC20-totalSupply}.\n" +
            "     */\n" +
            "    function totalSupply() public view returns (uint256) {\n" +
            "        return _totalSupply;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev See {IERC20-balanceOf}.\n" +
            "     */\n" +
            "    function balanceOf(address account) public view returns (uint256) {\n" +
            "        return _balances[account];\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev See {IERC20-transfer}.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     *\n" +
            "     * - `recipient` cannot be the zero address.\n" +
            "     * - the caller must have a balance of at least `amount`.\n" +
            "     */\n" +
            "    function transfer(address recipient, uint256 amount) public returns (bool) {\n" +
            "        _transfer(_msgSender(), recipient, amount);\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev See {IERC20-allowance}.\n" +
            "     */\n" +
            "    function allowance(address owner, address spender) public view returns (uint256) {\n" +
            "        return _allowances[owner][spender];\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev See {IERC20-approve}.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     *\n" +
            "     * - `spender` cannot be the zero address.\n" +
            "     */\n" +
            "    function approve(address spender, uint256 amount) public returns (bool) {\n" +
            "        _approve(_msgSender(), spender, amount);\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev See {IERC20-transferFrom}.\n" +
            "     *\n" +
            "     * Emits an {Approval} event indicating the updated allowance. This is not\n" +
            "     * required by the EIP. See the note at the beginning of {ERC20};\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     * - `sender` and `recipient` cannot be the zero address.\n" +
            "     * - `sender` must have a balance of at least `amount`.\n" +
            "     * - the caller must have allowance for `sender`'s tokens of at least\n" +
            "     * `amount`.\n" +
            "     */\n" +
            "    function transferFrom(address sender, address recipient, uint256 amount) public returns (bool) {\n" +
            "        _transfer(sender, recipient, amount);\n" +
            "        _approve(sender, _msgSender(), _allowances[sender][_msgSender()].sub(amount, \"ERC20: transfer amount exceeds allowance\"));\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Atomically increases the allowance granted to `spender` by the caller.\n" +
            "     *\n" +
            "     * This is an alternative to {approve} that can be used as a mitigation for\n" +
            "     * problems described in {IERC20-approve}.\n" +
            "     *\n" +
            "     * Emits an {Approval} event indicating the updated allowance.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     *\n" +
            "     * - `spender` cannot be the zero address.\n" +
            "     */\n" +
            "    function increaseAllowance(address spender, uint256 addedValue) public returns (bool) {\n" +
            "        _approve(_msgSender(), spender, _allowances[_msgSender()][spender].add(addedValue));\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Atomically decreases the allowance granted to `spender` by the caller.\n" +
            "     *\n" +
            "     * This is an alternative to {approve} that can be used as a mitigation for\n" +
            "     * problems described in {IERC20-approve}.\n" +
            "     *\n" +
            "     * Emits an {Approval} event indicating the updated allowance.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     *\n" +
            "     * - `spender` cannot be the zero address.\n" +
            "     * - `spender` must have allowance for the caller of at least\n" +
            "     * `subtractedValue`.\n" +
            "     */\n" +
            "    function decreaseAllowance(address spender, uint256 subtractedValue) public returns (bool) {\n" +
            "        _approve(_msgSender(), spender, _allowances[_msgSender()][spender].sub(subtractedValue, \"ERC20: decreased allowance below zero\"));\n" +
            "        return true;\n" +
            "    }\n" +
            "    \n" +
            "    function mint(address account, uint256 amount) public returns (bool) {\n" +
            "        _mint(account, amount);\n" +
            "        //return true;\n" +
            "    }\n" +
            "    \n" +
            "    function burn(address account, uint256 amount) public returns (bool) {\n" +
            "        _burn(account, amount);\n" +
            "        return true;\n" +
            "    }\n" +
            "    \n" +
            "    function burnFrom(address account, uint256 amount) public returns (bool) {\n" +
            "        _burnFrom(account, amount);\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Moves tokens `amount` from `sender` to `recipient`.\n" +
            "     *\n" +
            "     * This is internal function is equivalent to {transfer}, and can be used to\n" +
            "     * e.g. implement automatic token fees, slashing mechanisms, etc.\n" +
            "     *\n" +
            "     * Emits a {Transfer} event.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     *\n" +
            "     * - `sender` cannot be the zero address.\n" +
            "     * - `recipient` cannot be the zero address.\n" +
            "     * - `sender` must have a balance of at least `amount`.\n" +
            "     */\n" +
            "    function _transfer(address sender, address recipient, uint256 amount) internal whenNotPaused {\n" +
            "        require(sender != address(0), \"ERC20: transfer from the zero address\");\n" +
            "        require(recipient != address(0), \"ERC20: transfer to the zero address\");\n" +
            "\n" +
            "        _balances[sender] = _balances[sender].sub(amount, \"ERC20: transfer amount exceeds balance\");\n" +
            "        _balances[recipient] = _balances[recipient].add(amount);\n" +
            "        emit Transfer(sender, recipient, amount);\n" +
            "    }\n" +
            "    \n" +
            "    /**\n" +
            "     * @dev Sets `amount` as the allowance of `spender` over the `owner`s tokens.\n" +
            "     *\n" +
            "     * This is internal function is equivalent to `approve`, and can be used to\n" +
            "     * e.g. set automatic allowances for certain subsystems, etc.\n" +
            "     *\n" +
            "     * Emits an {Approval} event.\n" +
            "     *\n" +
            "     * Requirements:\n" +
            "     *\n" +
            "     * - `owner` cannot be the zero address.\n" +
            "     * - `spender` cannot be the zero address.\n" +
            "     */\n" +
            "    function _approve(address owner, address spender, uint256 amount) internal whenNotPaused {\n" +
            "        require(owner != address(0), \"ERC20: approve from the zero address\");\n" +
            "        require(spender != address(0), \"ERC20: approve to the zero address\");\n" +
            "\n" +
            "        _allowances[owner][spender] = amount;\n" +
            "        emit Approval(owner, spender, amount);\n" +
            "    }\n" +
            "\n" +
            "    /** @dev Creates `amount` tokens and assigns them to `account`, increasing\n" +
            "     * the total supply.\n" +
            "     *\n" +
            "     * Emits a {Transfer} event with `from` set to the zero address.\n" +
            "     *\n" +
            "     * Requirements\n" +
            "     *\n" +
            "     * - `to` cannot be the zero address.\n" +
            "     */\n" +
            "    function _mint(address account, uint256 amount) internal onlyOwner whenNotPaused {\n" +
            "        require(account != address(0), \"ERC20: mint to the zero address\");\n" +
            "\n" +
            "        _totalSupply = _totalSupply.add(amount);\n" +
            "        _balances[account] = _balances[account].add(amount);\n" +
            "        emit Transfer(address(0), account, amount);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Destroys `amount` tokens from `account`, reducing the\n" +
            "     * total supply.\n" +
            "     *\n" +
            "     * Emits a {Transfer} event with `to` set to the zero address.\n" +
            "     *\n" +
            "     * Requirements\n" +
            "     *\n" +
            "     * - `account` cannot be the zero address.\n" +
            "     * - `account` must have at least `amount` tokens.\n" +
            "     */\n" +
            "    function _burn(address account, uint256 amount) internal onlyOwner whenNotPaused {\n" +
            "        require(account != address(0), \"ERC20: burn from the zero address\");\n" +
            "\n" +
            "        _balances[account] = _balances[account].sub(amount, \"ERC20: burn amount exceeds balance\");\n" +
            "        _totalSupply = _totalSupply.sub(amount);\n" +
            "        emit Transfer(account, address(0), amount);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * @dev Destroys `amount` tokens from `account`.`amount` is then deducted\n" +
            "     * from the caller's allowance.\n" +
            "     *\n" +
            "     * See {_burn} and {_approve}.\n" +
            "     */\n" +
            "    function _burnFrom(address account, uint256 amount) internal onlyOwner whenNotPaused {\n" +
            "        _burn(account, amount);\n" +
            "        _approve(account, _msgSender(), _allowances[account][_msgSender()].sub(amount, \"ERC20: burn amount exceeds allowance\"));\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n";

}
