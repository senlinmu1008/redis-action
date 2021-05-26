package net.zhaoxiaobin.cache.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author zhaoxb
 * @date 2020/08/06 5:29 下午
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 5485617646232613710L;
    private Long id;
    private String name;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}