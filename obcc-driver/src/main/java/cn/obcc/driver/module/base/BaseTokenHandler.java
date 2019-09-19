package cn.obcc.driver.module.base;

import cn.obcc.config.ExConfig;
import cn.obcc.db.utils.BeanUtils;
import cn.obcc.def.token.ITokenParser;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.ITokenHandler;
import cn.obcc.driver.utils.ConvertUtils;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.enums.ETransferState;
import cn.obcc.listener.IStateListener;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.contract.ContractRec;
import cn.obcc.vo.contract.TokenRec;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.TokenInfo;
import lombok.NonNull;
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
    public String createToken(@NonNull String bizId, @NonNull FromAccount account,
                              @NonNull String tokenName, @NonNull String tokenCode, @NonNull Long tokenSupply,
                              IStateListener fn, @NonNull ExConfig config) throws Exception {

        return createToken(bizId, account, null, null, tokenName, tokenCode, tokenSupply, fn, config);
    }

    @Override
    public String createToken(@NonNull String bizId, @NonNull FromAccount account, @NonNull String contract, String contractName,
                              String tokenName, String tokenCode, Long tokenSupply,
                              IStateListener fn, ExConfig config) throws Exception {

        List<Object> params = new ArrayList<Object>() {{
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
            return null;
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
            return null;
        }
        IStateListener fn1 = (BizState bizState, Object resp) -> {
            // String bizId, String hash, EUpchainType upchainType, ETransferState state,
            if (bizState.getTransferState() == ETransferState.STATE_CHAIN_CONSENSUS) {
                BlockTxInfo txInfo = (BlockTxInfo) resp;
                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setBizId(bizId);
                tokenInfo.setHash(bizState.getHashes());
                tokenInfo.setContract(info.getContent());
                tokenInfo.setCode(tokenCode);
                tokenInfo.setName(tokenName);
                tokenInfo.setSupply(tokenSupply);
                tokenInfo.setPrecisions(18);
                tokenInfo.setContractAbi(info.getAbi());
                tokenInfo.setContractAddress(txInfo.getContractAddress());
                tokenInfo.setState(1);

                getDriver().getLocalDb().getTokenInfoDao().add(tokenInfo);
            }
            if (fn != null) {
                fn.exec(bizState, resp);
            }

        };


        return getDriver().getContractHandler().deploy(UuidUtils.get() + "", account, info, params, fn1, config);

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
        ITokenParser tokenParse = null;
        if (StringUtils.isNotNullOrEmpty(info.getParseClsName())) {
            //todo:cache it
            tokenParse = (ITokenParser) BeanUtils.newInstance(info.getParseClsName());
        } else {
            tokenParse = (ITokenParser) BeanUtils.newInstance(config.getStardardTokenParserClzName());
            /// tokenParse = new BaseTokenParser();
        }

        return tokenParse.parse(info, rec);

    }

    @Override
    public String balanceOf(TokenInfo token, String address, ExConfig config) throws Exception {
        String hexValue = getDriver().getContractHandler().queryWithAddr(address,
                token.getContractAddress(), "balanceOf",
                new ArrayList<Object>() {{
                    add(address);
                }}, config);

        if (StringUtils.isNullOrEmpty(hexValue)) {
            logger.error("没有找到{}账户的余额", address);
            return null;
        }
        BigInteger v = new BigInteger(ConvertUtils.cleanHexPrefix(hexValue), 16);
        String vStr = ConvertUtils.toBigStr(v, token.getPrecisions());
        return vStr;
    }


    protected abstract boolean check(TokenInfo token, String methodName, List<Object> params) throws Exception;

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
    public String transfer(String bizId, FromAccount account, TokenInfo token, String destAccount,
                           String amount, IStateListener fn, ExConfig config) throws Exception {
        String methodName = "transfer";
        List<Object> parmas = new ArrayList<Object>() {
            {
                add(destAccount);
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, methodName, parmas, fn, config);
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
    public String burn(String bizId, FromAccount account, TokenInfo token,
                       String amount, IStateListener fn, ExConfig config) throws Exception {
        String methodName = "burn";
        List<Object> parmas = new ArrayList<Object>() {
            {
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, methodName, parmas, fn, config);

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
    public String supply(String bizId, FromAccount account, TokenInfo token,
                         String amount, IStateListener fn, ExConfig config) throws Exception {
        String methodName = "supply";
        List<Object> parmas = new ArrayList<Object>() {
            {
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, methodName, parmas, fn, config);
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
    public String approve(String bizId, FromAccount account, TokenInfo token, String spenderAddr, String
            amount, IStateListener fn, ExConfig config) throws Exception {
        String methodName = "approve";
        List<Object> parmas = new ArrayList<Object>() {
            {
                add(spenderAddr);
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, methodName, parmas, fn, config);

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
    public String transferFrom(String bizId, FromAccount account, TokenInfo token, String srcAddr, String toAddr,
                               String amount, IStateListener fn, ExConfig config) throws Exception {
        String methodName = "transferFrom";
        List<Object> parmas = new ArrayList<Object>() {
            {
                add(srcAddr);
                add(toAddr);
                add(amount);
            }
        };
        check(token, methodName, parmas);

        ContractInfo contractInfo = getDriver().getContractHandler().getContract(token.getContractAddress());
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, methodName, parmas, fn, config);
    }


}
