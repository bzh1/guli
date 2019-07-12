package com.guli.common.exception;

import com.guli.common.constants.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("全局异常")
@Data
public class GuliException extends RuntimeException {

    @ApiModelProperty("状态码")
    private Integer code;

    /**
     * 接受状态码和消息
     * @param message
     * @param code
     */
    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接受枚举类型
     * @param resultCodeEnum
     */
    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
