package cn.xiaou.weixin.mp.demo.utils;

import lombok.Data;
import lombok.Getter;

/**
 * @author xiaou
 * @date 2024/2/3
 */
@Data
@Getter
public class ErrorCode {
    private final Integer code;
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }
}
