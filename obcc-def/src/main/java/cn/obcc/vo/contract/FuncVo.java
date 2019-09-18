package cn.obcc.vo.contract;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *  *   {
 *  *     "outputs": [
 *  *       {
 *  *         "name": "",
 *  *         "type": "string"
 *  *       }
 *  *     ],
 *  *     "constant": true,
 *  *     "payable": false,
 *  *     "inputs": [],
 *  *     "name": "symbol",
 *  *     "stateMutability": "view",
 *  *     "type": "function"
 *  *   },
 *
 * </pre>
 *
 * @author pengrk
 * @version 1.0
 * @ClassName FuncVo
 * @desc TODO
 * @date 2019/9/18 0018  9:17
 **/
@Data
public class FuncVo implements Serializable {

    private String name;
    private String type;
    private String stateMutability;
    private boolean payable;
    private boolean constant;
    private List<NameTypeVo> outputs;
    private List<NameTypeVo> inputs;

}
