package cn.xiaou.weixin.mp.demo.controller;

import cn.xiaou.weixin.mp.demo.controller.vo.WxQrcodeGenerateReqVo;
import cn.xiaou.weixin.mp.demo.controller.vo.WxQrcodeGenerateRespVo;
import cn.xiaou.weixin.mp.demo.controller.vo.WxQrcodeResultReqVo;
import cn.xiaou.weixin.mp.demo.controller.vo.WxQrcodeResultRespVo;
import cn.xiaou.weixin.mp.demo.enums.WxQrcodeStatus;
import cn.xiaou.weixin.mp.demo.redis.WxRedis;
import cn.xiaou.weixin.mp.demo.utils.CommonResult;
import cn.xiaou.weixin.mp.demo.utils.GlobalErrorCodeConstants;
import cn.xiaou.weixin.mp.demo.utils.ServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author xiaou
 * @date 2025/8/25
 * @folder app/社交/微信
 */
@RestController
@AllArgsConstructor
@RequestMapping("/social/wx/user")
@Slf4j
public class WxUserController {

    private final static String WX_MP_SCENE_SPLIT = "#";

    private final WxMpService wxMpService;

    private final WxRedis wxRedis;

    /**
     * 生成二维码
     * @tags v1.1.0
     */
    @PostMapping("qrcode-create")
    @ResponseBody
    public CommonResult<WxQrcodeGenerateRespVo> userQrcodeCreate(@RequestBody @Validated WxQrcodeGenerateReqVo reqVo) {
        wxMpService.switchover(reqVo.getClientId());

        String sceneStr = reqVo.getSceneType() + WX_MP_SCENE_SPLIT + UUID.randomUUID();
        WxMpQrCodeTicket wxMpQrCodeTicket = null;
        String qrcodeUrl;

        try {
            wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(sceneStr, (int) TimeUnit.HOURS.toSeconds(1));
            qrcodeUrl = wxMpService.getQrcodeService().qrCodePictureUrl(wxMpQrCodeTicket.getTicket());
        } catch (WxErrorException e) {
            log.error("userQrcodeCreate--出现异常--", e);
            throw new ServiceException(GlobalErrorCodeConstants.UNKNOWN);
        }

        wxRedis.startQrcode(wxMpQrCodeTicket.getTicket(),
                new WxQrcodeLogin()
                        .setStatus(WxQrcodeStatus.WAIT_SCAN.getValue())
                        .setClientId(reqVo.getClientId()));

        WxQrcodeGenerateRespVo respVo = new WxQrcodeGenerateRespVo();
        respVo.setUrl(wxMpQrCodeTicket.getUrl());
        respVo.setTicket(wxMpQrCodeTicket.getTicket());
        respVo.setExpireSeconds(wxMpQrCodeTicket.getExpireSeconds());
        respVo.setQrcodeUrl(qrcodeUrl);
        return CommonResult.success(respVo);
    }

    /**
     * 二维码扫码结果
     * @tags v1.1.0
     */
    @GetMapping("qrcode-result")
    @ResponseBody
    public CommonResult<WxQrcodeResultRespVo> userQrcodeResult(@Validated WxQrcodeResultReqVo reqVo) {
        WxQrcodeLogin qrcodeResult = wxRedis.getQrcodeResult(reqVo.getTicket());
        WxQrcodeResultRespVo respVo = new WxQrcodeResultRespVo();
        if (Objects.isNull(qrcodeResult)) {
            respVo.setStatus(WxQrcodeStatus.EXPIRED.getValue());
        } else {
            if (WxQrcodeStatus.EXPIRED.getValue().equals(qrcodeResult.getStatus())) {
                // 已过期，删除缓存
                wxRedis.expireQrCode(reqVo.getTicket());
            }
            respVo.setStatus(qrcodeResult.getStatus());
            respVo.setToken(qrcodeResult.getToken());
        }
        return CommonResult.success(respVo);
    }

    /**
     * 微信用户授权个人信息
     * @param code 授权 code
     * @param clientId 服务号 appId
     * @tags v1.1.0
     */
    @GetMapping("/user-info")
    @ResponseBody
    public String userInfo(String code, @RequestParam("state") String clientId) throws Exception {
         // 参数验证
        if (code == null || code.trim().isEmpty()) {
            throw new ServiceException(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "微信授权码不能为空");
        }
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new ServiceException(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "状态参数不能为空");
        }

        wxMpService.switchover(clientId);
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
        // TODO 个人信息保存到数据库中
        /*SysSocialUserDO sysSocialUserDO = SysSocialUserConvert.INSTANCE.convert(wxMpUser);
        sysSocialUserService.updateInfoByOpenId(sysSocialUserDO);*/
        return "信息更新成功";
    }
}
