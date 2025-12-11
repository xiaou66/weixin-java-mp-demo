package cn.xiaou.weixin.mp.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author xiaou
 * @date 2025/12/9
 */
@AllArgsConstructor
@Getter
public enum WxQrcodeStatus {
    /**
     * 待扫码
     */
    WAIT_SCAN(1, "待扫码"),

    /**
     * 已扫码
     */
    SCANNED(2, "已扫码"),

    /**
     * 处理成功
     * 用于业务处理完成了通知前端走下一步操作
     */
    SUCCESS(3, "处理成功"),

    /**
     * 二维码过期
     */
    EXPIRED(4, "二维码过期")
    ;

    /**
     * 状态码
     */
    private final Integer value;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static WxQrcodeStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (WxQrcodeStatus status : values()) {
            if (status.getValue().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
