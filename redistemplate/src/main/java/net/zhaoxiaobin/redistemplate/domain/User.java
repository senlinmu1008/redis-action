/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.redistemplate.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author zhaoxb
 * @create 2020-05-07 10:48
 */
@Data
public class User {
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码，序列化时忽略此属性
     */
    @JsonIgnore
    private String password;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别 1=男 2=女 其他=保密
     */
    private Integer sex;
}