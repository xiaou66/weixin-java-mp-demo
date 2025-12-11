package cn.xiaou.weixin.mp.demo.handler;

import cn.xiaou.weixin.mp.demo.builder.TextBuilder;
import cn.xiaou.weixin.mp.demo.controller.WxQrcodeLogin;
import cn.xiaou.weixin.mp.demo.controller.vo.UserLoginRespVo;
import cn.xiaou.weixin.mp.demo.redis.WxRedis;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 扫码事件处理
 * @author xiaou
 * @date 2025/8/25
 */
@Component
@RequiredArgsConstructor
public class LoginAndRegisterHandler extends AbstractHandler{
    private final WxRedis wxRedis;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context,
                                    WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        logger.info("登录或注册事件处理--LoginAndRegisterHandler--{}--{}", wxMessage, context);
        wxRedis.scannedQrcode(wxMessage.getTicket());
        WxMpUser userWxInfo = wxMpService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
        String ticket = wxMessage.getTicket();
        logger.info("userWxInfo--{}", userWxInfo);
        WxQrcodeLogin qrcodeResult = wxRedis.getQrcodeResult(ticket);
        String openId = userWxInfo.getOpenId();
        // 获取对应的用户 id
        // Long userId = sysSocialUserBindService.getUserIdByOpenId(SocialTypeEnum.WECHAT_MP, openId);
        Long userId = 1L;
        String returnMessageStr = "登录成功";
        if (userId == null) {
            // 新用户, 需要在系统中生成一下用户
            // SysSocialUserDO socialUser = SysSocialUserConvert.INSTANCE.convert(userWxInfo, qrcodeResult);
           // userId = sysSocialUserService.registerUser(socialUser);

            // TODO 这里是扫码之后可以给用户发送信息
            String url = wxMpService.getOAuth2Service().buildAuthorizationUrl("xxxx",
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                    wxMpService.getWxMpConfigStorage().getAppId());
            returnMessageStr = String.format("""
                    接下来你可以 <a href="%s">完善用户信息</a>
                    """, url);
        }

        UserLoginRespVo userLoginRespVo = new UserLoginRespVo();
        userLoginRespVo.setToken("我是一个 token");
        wxRedis.loginSuccess(ticket, userLoginRespVo.getToken());

        try {
            return new TextBuilder().build(returnMessageStr.strip(), wxMessage, wxMpService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
        return null;
    }
}
