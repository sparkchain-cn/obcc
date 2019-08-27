package cn.obcc.driver.vo;

/**
 * <t>
 * 如果数据没有就不写，JSON
 */
public class BcMemo {

    public BcMemo(String bizId, String data) {
        this.bizId = bizId;
        this.data = data;
    }

    //对于db的一条记录，可能分成多条，并且有修改历史。
    //修改历史采用:id_1,id_2,id_3,在db statement中记数
    // private String clientId;
    private String bizId;

    //对于原bizId
    //当前的序号，从1开始,如果只有1条，就不需要生成数据
    private Long seq;
    //总条数sum，从1开始,如果只有1条，就不需要生成数据
    private Long sum;
    //上一条hash
    //private String lastHash;
    //修改的前一个hash等放到data对象中去
    private String data;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

//    public String getClientId() {
//        return clientId;
//    }

//    public void setClientId(String clientId) {
//        this.clientId = clientId;
//    }


    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
