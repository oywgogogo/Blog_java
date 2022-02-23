package com.littleo.blog.vo.params;

import lombok.Data;

/**
 * @author 欧阳巍
 * @Date 2021/12/31 16:29
 */

@Data
public class LoginParam {
      private String account;

      private String password;

      private String nickname;
}
