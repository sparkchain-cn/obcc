package cn.obcc.exception.observer;

import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.vo.RetData;

public interface SpcExceptionObserver {

    RetData exec(EExceptionCode codeEnum, ObccException ex);
}
