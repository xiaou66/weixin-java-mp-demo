package cn.xiaou.weixin.mp.demo.controller;

import cn.xiaou.weixin.mp.demo.enums.WxQrcodeStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaou
 * @date 2025/8/25
 */
@Data
@Accessors(chain = true)
public class WxQrcodeLogin {
    private String token;
    /**
     * 扫码状态
     * @see cn.xiaou.weixin.mp.demo.enums.WxQrcodeStatus
     */
    private Integer status;
    /**
     * 登录系统应用 id
     */
    private String appId;
    /**
     * 公众号 id
     */
    private String clientId;
}
