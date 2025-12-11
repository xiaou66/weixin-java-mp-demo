package cn.xiaou.weixin.mp.demo.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * 通用返回结果
 *
 */
public record CommonResult<T>(Integer code, T data, String msg) implements Serializable {

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(GlobalErrorCodeConstants.SUCCESS.getCode(), data, "");
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        return new CommonResult<>(code, null, message);
    }

    public static CommonResult<Object> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    @JsonIgnore
    public boolean isSuccess() {
        return isSuccess(code);
    }

    @JsonIgnore
    public boolean isError() {
        return !isSuccess(code);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(code, msg);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常 <br/>
     * 如果没有，则返回 {@link #data} 数据
     */
    @JsonIgnore
    public T getCheckedData() {
        checkError();
        return data;
    }

    public static <T> CommonResult<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

}
