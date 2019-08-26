package cn.obcc.exception.enums;

public enum EBlockPullState {

    N0Exist(1);

    //1:blocknumber 不存在，2：ioException,3:...,9:成功
    private int state = 0;

    private EBlockPullState(int state) {

    }
}
