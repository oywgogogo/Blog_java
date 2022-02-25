package com.littleo.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author 欧阳巍
 * @Date 2022/2/7 17:00
 */
@Data
public class LoginUserVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String account;

    private String nickname;

    private String avatar;

}
