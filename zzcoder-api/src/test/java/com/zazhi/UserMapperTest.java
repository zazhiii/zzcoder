package com.zazhi;

import com.zazhi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author lixh
 * @since 2025/9/5 15:26
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void TestGetRoleAndPermissionByUserId(){
        System.out.println(userMapper.getRoleAndPermissionByUserId(8));
    }
}
