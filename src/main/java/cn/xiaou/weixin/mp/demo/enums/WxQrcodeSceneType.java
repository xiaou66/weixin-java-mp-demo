package cn.xiaou.weixin.mp.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author xiaou
 * @date 2025/8/25
 */
@AllArgsConstructor
@Getter
public enum WxQrcodeSceneType {
    /**
     * 登录或者注册
     */
    LOGIN_OR_REGISTER,
    ;

    public String getEventKeyRegex() {
        return "^(qrscene_)?" + this.name() +"#.*";
    }
}
