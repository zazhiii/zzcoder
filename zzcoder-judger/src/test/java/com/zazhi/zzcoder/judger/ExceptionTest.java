package com.zazhi.zzcoder.judger;

import com.zazhi.zzcoder.judger.common.exception.SystemException;
import org.junit.jupiter.api.Test;

/**
 * @author zazhi
 * @date 2025/5/4
 * @description: TODO
 */
public class ExceptionTest {

    @Test
    void test(){
        try {
            f();
        } catch (RuntimeException e) {
            System.out.println("捕获到异常");
        }
    }


    void f() throws RuntimeException {
        throw new SystemException("System error");
    }
}
