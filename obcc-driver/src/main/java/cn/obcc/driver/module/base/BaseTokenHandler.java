package cn.obcc.driver.module.base;

import cn.obcc.config.ExProps;
import cn.obcc.db.utils.BeanUtils;
import cn.obcc.driver.ITokenParse;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.ITokenHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.utils.ConvertUtils;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.ContractRec;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.driver.vo.TokenRec;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName TokenBaseHandler
 * @desc TODO
 * @date 2019/9/9 0009  16:55
 **/
public abstract class BaseTokenHandler<T> extends BaseHandler<T> implements ITokenHandler<T> {
    public static final Logger logger = LoggerFactory.getLogger(BaseTokenHandler.class);

    @Override
    public void createToken(String bizId, SrcAccount account, String tokenName, String tokenCode, Long tokenSupply,
                            IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception {

        createToken(bizId, account, null, null, tokenName, tokenCode, tokenSupply, fn, config);
    }

    @Override
    public void createToken(String bizId, SrcAccount account, String contract, String contractName,
                            String tokenName, String tokenCode, Long tokenSupply,
                            IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception {

        List<String> params = new ArrayList<String>() {{
            add(tokenName);
            add(tokenCode);
            add(tokenSupply + "");
        }};
        if (StringUtils.isNullOrEmpty(contract)) {
            contract = getDefaultTokenContents();
        }
        CompileResult compileResult = getDriver().getContractHandler().compile(bizId, contract, config);

        //成功
        if (compileResult.getState() != 1) {
            logger.error("编译失败，bizid:{}，{}", bizId, compileResult.getCompileException());
            return;
        }
        //采用默认的名称
        if (StringUtils.isNullOrEmpty(contractName)) {
            contractName = compileResult.getContractBinList().get(0).getName();
        }
        if (compileResult.getContractBinList().size() == 1) {
            contractName = compileResult.getContractBinList().get(0).getName();
        }

        ContractInfo info = getDriver().getContractHandler().getContract(bizId, contractName);
        if (info == null) {
            logger.error("根据bizId:{} 和contractName:{}不能找到合约", bizId, contractName);
            return;
        }
        IUpchainFn fn1 = new IUpchainFn<BlockTxInfo>() {
            @Override
            public void exec(String bizId, String hash, EUpchainType upchainType, ETransferStatus state, BlockTxInfo resp) throws Exception {
                if (state == ETransferStatus.STATE_CHAIN_CONSENSUS) {
                    TokenInfo tokenInfo = new TokenInfo();
                    tokenInfo.setBizId(bizId);
                    tokenInfo.setHash(hash);
                    tokenInfo.setContract(info.getContent());
                    tokenInfo.setCode(tokenCode);
                    tokenInfo.setName(tokenName);
                    tokenInfo.setSupply(tokenSupply);
                    tokenInfo.setPrecisions(18);
                    tokenInfo.setContractAbi(info.getAbi());
                    tokenInfo.setContractAddress(resp.getContractAddress());
                    tokenInfo.setState(1);

                    getDriver().getLocalDb().getTokenInfoDao().add(tokenInfo);
                }
                if (fn != null) {
                    fn.exec(bizId, hash, upchainType, state, (BlockTxInfo) resp);
                }
            }
        };


        getDriver().getContractHandler().deploy(UuidUtils.get() + "", account, info, fn1, config, params);

    }

    public abstract String getDefaultTokenContents() throws Exception;

    @Override
    public void addToken(TokenInfo info) throws Exception {
        getDriver().getLocalDb().getTokenInfoDao().add(info);
    }

    @Override
    public boolean isExistToken(String contractAddr) throws Exception {
        //todo:修改
        String count = getDriver().getLocalDb().getTokenInfoDao()
                .getValue("select count(1) from token_info where contract_address= ? ", new Object[]{contractAddr});
        if (Long.parseLong(count) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public TokenInfo getToken(String contractAddr) throws Exception {
        List<TokenInfo> list = getDriver().getLocalDb().getTokenInfoDao().
                query(" contractAddress = ? or code= ?",
                        new Object[]{contractAddr, contractAddr});
        if (list == null || list.size() < 1) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public TokenRec parseTxInfo(String contractAddr, ContractRec rec) throws Exception {
        if (rec == null) {
            return null;
        }
        TokenInfo info = getToken(contractAddr);
        if (info == null) {
            return null;
        }
        ITokenParse tokenParse = null;
        if (StringUtils.isNotNullOrEmpty(info.getParseClsName())) {
            //todo:cache it
            tokenParse = (ITokenParse) BeanUtils.newInstance(info.getParseClsName());
        } else {
            tokenParse = new DefaultTokenParse();
        }

        return tokenParse.parse(info, rec);

    }

    @Override
    public String balanceOf(TokenInfo token, String address, ExProps config) throws Exception {
        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        if (contractInfo == null) {
            logger.error("在数据库中没有找到合约.");
            return null;
        }
        String hexValue = getDriver().getContractHandler().query(address, contractInfo, config, "balanceOf",
                new ArrayList<String>() {{
                    add(address);
                }});

        if (StringUtils.isNullOrEmpty(hexValue)) {
            logger.error("没有找到{}账户的余额", address);
            return null;
        }
        BigInteger v = new BigInteger(ConvertUtils.cleanHexPrefix(hexValue), 16);
        String vStr = ConvertUtils.toBigStr(v, token.getPrecisions());
        return vStr;
    }


    protected abstract boolean check(TokenInfo token, String methodName, List<String> params) throws Exception;

    /**
     * transfer(address _to, uint256 _value)
     *
     * @param bizId
     * @param account
     * @param token
     * @param destAccount
     * @param amount
     * @param config
     * @param fn
     * @return
     * @throws Exception
     */
    @Override
    public String transfer(String bizId, SrcAccount account, TokenInfo token, String destAccount,
                           String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        String methodName = "transfer";
        List<String> parmas = new ArrayList<String>() {
            {
                add(destAccount);
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, config, fn, methodName, parmas);
    }

    /**
     * burn(uint256 _value)
     *
     * @param bizId
     * @param account
     * @param token
     * @param amount
     * @param config
     * @param fn
     * @return
     * @throws Exception
     */
    @Override
    public String burn(String bizId, SrcAccount account, TokenInfo token,
                       String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        String methodName = "burn";
        List<String> parmas = new ArrayList<String>() {
            {
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, config, fn, methodName, parmas);

    }

    /**
     * @param bizId
     * @param account
     * @param token
     * @param amount
     * @param config
     * @return
     * @throws Exception
     */
    @Override
    public String supply(String bizId, SrcAccount account, TokenInfo token,
                         String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        String methodName = "supply";
        List<String> parmas = new ArrayList<String>() {
            {
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, config, fn, methodName, parmas);
    }

    /**
     * approve(address _spender, uint256 _value)
     *
     * @param bizId
     * @param account
     * @param token
     * @param spenderAddr
     * @param amount
     * @param config
     * @return
     * @throws Exception
     */
    @Override
    public String approve(String bizId, SrcAccount account, TokenInfo token, String spenderAddr, String
            amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        String methodName = "approve";
        List<String> parmas = new ArrayList<String>() {
            {
                add(spenderAddr);
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, config, fn, methodName, parmas);

    }

    /**
     * transferFrom(address _from, address _to, uint256 _value)
     *
     * @param bizId
     * @param account
     * @param token
     * @param amount
     * @param config
     * @return
     * @throws Exception
     */
    @Override
    public String transferFrom(String bizId, SrcAccount account, TokenInfo token, String srcAddr, String toAddr,
                               String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        String methodName = "transferFrom";
        List<String> parmas = new ArrayList<String>() {
            {
                add(srcAddr);
                add(toAddr);
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, config, fn, methodName, parmas);
    }


}
