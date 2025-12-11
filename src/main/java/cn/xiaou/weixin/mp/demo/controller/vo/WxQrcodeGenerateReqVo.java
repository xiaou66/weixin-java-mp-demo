package cn.xiaou.weixin.mp.demo.controller.vo;

import cn.xiaou.weixin.mp.demo.enums.WxQrcodeSceneType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author xiaou
 * @date 2025/8/25
 */
@Data
public class WxQrcodeGenerateReqVo {
    @NotNull(message = "客户端 id 不能为空")
    private String clientId;

    @NotNull(message = "应用 id 不能为空")
    private String appId;

    /**
     * 二维码场景
     * @see WxQrcodeSceneType
     */
    @NotNull(message = "场景不能为空")
    private WxQrcodeSceneType sceneType;
}
