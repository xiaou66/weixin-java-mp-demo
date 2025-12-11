package cn.xiaou.weixin.mp.demo.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xiaou
 * @date 2025/8/25
 */
@Data
@Accessors(chain = true)
public class WxQrcodeResultRespVo {
    private String token;
    /**
     * 状态
     * 1-待扫码
     * 2-已扫码
     * 3-登录成功
     * 4-过期
     */
    private Integer status;
}
