package cn.xiaou.weixin.mp.demo.redis;

import cn.xiaou.weixin.mp.demo.controller.WxQrcodeLogin;
import cn.xiaou.weixin.mp.demo.enums.WxQrcodeStatus;
import cn.xiaou.weixin.mp.demo.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaou
 * @date 2025/8/25
 */
@Component
@RequiredArgsConstructor
public class WxRedis {

    private final static String QRCODE_RESULT_PREFIX= "social:wx:qrcode:result:%s";

    private final StringRedisTemplate redisTemplate;

    public void startQrcode(String ticket, WxQrcodeLogin login) {
        String redisKey = String.format(QRCODE_RESULT_PREFIX, ticket);
        redisTemplate.opsForValue().set(redisKey,
                JsonUtils.toJsonString(login),
                15,
                TimeUnit.MINUTES);
    }

    public void scannedQrcode(String ticket) {
        String redisKey = String.format(QRCODE_RESULT_PREFIX, ticket);
        WxQrcodeLogin login = JsonUtils.parseObject(redisTemplate.opsForValue().get(redisKey),
                WxQrcodeLogin.class);
        if (login == null) {
            return;
        }
        WxQrcodeLogin newLogin = login.setStatus(WxQrcodeStatus.SCANNED.getValue());
        redisTemplate.opsForValue().set(redisKey,
                JsonUtils.toJsonString(newLogin),
                5,
                TimeUnit.MINUTES);
    }

    public void loginSuccess(String ticket, String token) {
        String redisKey = String.format(QRCODE_RESULT_PREFIX, ticket);
        WxQrcodeLogin login = JsonUtils.parseObject(redisTemplate.opsForValue().get(redisKey),
                WxQrcodeLogin.class);
        if (login == null) {
            return;
        }
        WxQrcodeLogin newLogin = login.setToken(token)
                .setStatus(3);
        redisTemplate.opsForValue().set(redisKey,
                JsonUtils.toJsonString(newLogin),
                2,
                TimeUnit.MINUTES);
    }

    public @Nullable WxQrcodeLogin getQrcodeResult(String ticket) {
        String redisKey = String.format(QRCODE_RESULT_PREFIX, ticket);
        String value = redisTemplate.opsForValue().get(redisKey);
        return JsonUtils.parseObject(value, WxQrcodeLogin.class);
    }

    public void expireQrCode(String ticket) {
        String redisKey = String.format(QRCODE_RESULT_PREFIX, ticket);
        redisTemplate.delete(redisKey);
    }
}
