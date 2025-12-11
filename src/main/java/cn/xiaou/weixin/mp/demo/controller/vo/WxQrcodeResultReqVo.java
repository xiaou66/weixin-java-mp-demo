package cn.xiaou.weixin.mp.demo.controller.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author xiaou
 * @date 2025/8/25
 */
@Data
public class WxQrcodeResultReqVo {
    @NotBlank(message = "ticket 不能为空")
    private String ticket;
}
