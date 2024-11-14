package com.zazhi.config;
  
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
  
/**  
 * @author zazhi  
 * @date 2024/11/13  
 * @description: 消息转换器配置
 */  
@Configuration  
public class MessageConverterConfig {


    @Bean
    public DirectExchange judgeExchange() {
        return new DirectExchange("judge_exchange");
    }

    @Bean
    public Queue judgeTaskQueue() {
        return new Queue("judge_task_queue");
    }

    @Bean
    public Queue judgeResultQueue() {
        return new Queue("judge_result_queue");
    }

    @Bean
    public Binding bindJudgeTaskQueue(DirectExchange judgeExchange, Queue judgeTaskQueue) {
        return BindingBuilder.bind(judgeTaskQueue).to(judgeExchange).with("judge_task_routing_key");
    }

    @Bean
    public Binding bindJudgeResultQueue(DirectExchange judgeExchange, Queue judgeResultQueue) {
        return BindingBuilder.bind(judgeResultQueue).to(judgeExchange).with("judge_result_routing_key");
    }


    @Bean  
    public MessageConverter messageConverter(){  
        // 1.定义消息转换器  
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();  
        // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息  
        jackson2JsonMessageConverter.setCreateMessageIds(true);  
        return jackson2JsonMessageConverter;  
    }  
}