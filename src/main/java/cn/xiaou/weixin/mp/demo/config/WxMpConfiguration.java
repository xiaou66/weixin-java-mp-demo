package cn.xiaou.weixin.mp.demo.config;

import cn.xiaou.weixin.mp.demo.enums.WxQrcodeSceneType;
import cn.xiaou.weixin.mp.demo.handler.LoginAndRegisterHandler;
import cn.xiaou.weixin.mp.demo.handler.SubscribeHandler;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.Optional;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;


@Slf4j
@AllArgsConstructor
@Configuration
public class WxMpConfiguration {

    private final LoginAndRegisterHandler loginAndRegisterHandler;

    private final SubscribeHandler subscribeHandler;

    @Bean
    public WxMpService wxMpService() {
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setConfigStorageFunction((clientId) -> {
            /*SysSocialClientService socialClientService = SpringUtils.getBean(SysSocialClientService.class);
            SysSocialClientDO socialClient = socialClientService.getOne(Wrappers.lambdaQuery(SysSocialClientDO.class)
                    .eq(SysSocialClientDO::getClientId, clientId)
                    .last("limit 1"));
            if (Objects.isNull(socialClient)) {
                return null;
            }

            WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
            config.setAppId(socialClient.getClientId());
            config.setSecret(socialClient.getClientSecret());
            JsonNode jsonNode = JsonUtils.parseTree(socialClient.getExtraConfig());
            Optional.ofNullable(jsonNode.get("token"))
                    .ifPresent(token -> {
                        config.setToken(token.asText());
                    });
            Optional.ofNullable(jsonNode.get("encodingAESKey"))
                    .ifPresent(encodingAESKey -> {
                        config.setAesKey(encodingAESKey.asText());
                    });*/
            return null;
        });

        return wxMpService;
    }
    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);

        // 登录注册事件路由
        router.rule().async(false)
                .msgType(EVENT)
                // 订阅扫码事件和关注事件
                .matcher((wxMessage) ->
                        WxConsts.EventType.SCAN.equals(wxMessage.getEvent())
                                || WxConsts.EventType.SUBSCRIBE.equals(wxMessage.getEvent()))
                // 过滤出登录注册二维码扫描
                .eventKeyRegex(WxQrcodeSceneType.LOGIN_OR_REGISTER.getEventKeyRegex())
                .handler(this.loginAndRegisterHandler)
                .end();

        // 关注路由
        router.rule().async(false)
                .msgType(EVENT)
                .event(WxConsts.EventType.SUBSCRIBE)
                .handler(this.subscribeHandler)
                .end();
        return router;
    }

}
