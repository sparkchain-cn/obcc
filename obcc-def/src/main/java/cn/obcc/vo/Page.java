package cn.obcc.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭仁夔
 * @email 546711211@qq.com
 * @time 2017/10/19 16:21
 */
@Data
public class Page<T> {
    private long total;
    private List<T> result;
    private int number = 1;
    private int size = 10;

    private int startRow = 0;

    private boolean needPage = true;

    private List<KeyValue> orders = new ArrayList<>();
    private Map<String, Object> meta = new HashMap<>();

    public Page(List<T> result, long total, int number, int size) {
        // PageImpl<T>
        this.total = total;
        this.result = result;
        this.number = number;
        this.size = size;
    }

    public Page(int number, int size) {
        // PageImpl<T>
        this.number = number;
        this.size = size;
    }

    public Page(List<T> result) {
        this.result = result;
    }

    public Page() {
    }


    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }


    public void setNumber(Integer number) {
        if (number != null && number == 0) {
            this.number = 1;
        } else if (number != null) {
            this.number = number;
        }

    }

    /**
     * 计算起止行号
     */
    public void calculateStartAndEndRow() {
        this.setStartRow(this.number > 0 ? (this.getNumber() - 1) * this.getSize() : 0);

    }

    public void setSize(Integer size) {
        if (size != null && size < 1) {
            this.size = 10;
        } else if (size != null) {
            this.size = size;
        }
    }

    public String getOrderStr() {
        if (orders == null || orders.isEmpty()) {
            return null;
        }

        StringBuffer sBuffer = new StringBuffer();

        for (int i = 0; i < orders.size(); i++) {
            if (i != orders.size() - 1) {
                KeyValue kv = orders.get(i);
                sBuffer.append(kv.getKey() + " " + kv.getVal() + "");
                sBuffer.append(",");
            }
        }

        return sBuffer.toString();
    }

    public int getStartRow() {
        calculateStartAndEndRow();
        return startRow;
    }

}
