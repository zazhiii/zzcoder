package com.zazhi.zzcoder.config;

import com.zazhi.zzcoder.common.enums.ContestStatus;
import com.zazhi.zzcoder.common.enums.GenericEnumTypeHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MyBatisEnumConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();

                // 注册你所有的枚举类型
                registry.register(ContestStatus.class, new GenericEnumTypeHandler<>(ContestStatus.class));

                // 如果有其他枚举，也一样添加
                // registry.register(Status.class, new GenericEnumTypeHandler<>(Status.class));
            }
        };
    }
}
