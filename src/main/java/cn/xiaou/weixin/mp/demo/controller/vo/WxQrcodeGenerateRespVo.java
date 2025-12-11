package cn.xiaou.weixin.mp.demo.controller.vo;

import lombok.Data;

/**
 *
 * @author xiaou
 * @date 2025/8/25
 */
@Data
public class WxQrcodeGenerateRespVo {
    private String ticket;
    /**
     * 如果为-1，说明是永久
     */
    private int expireSeconds = -1;
    private String url;

    private String qrcodeUrl;
}
