package com.example.service.impl;


import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.service.AiService;
import io.reactivex.Flowable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author zhexueqi
 * @ClassName AiServiceImpl
 * @since 2024/4/12    16:24
 */
@Service
public class AiServiceImpl implements AiService {

    @Override
    public String AI(String content) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        // 构建用户消息

        Message userMsg =
                Message.builder().role(Role.USER.getValue()).content(content).build();
        // 设置调用参数，包括模型选择、消息内容格式化、流式输出控制等
        GenerationParam param = GenerationParam.builder()
                .model("qwen-turbo")
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE) // 设置结果格式为消息
                .topP(0.8).enableSearch(true) // 设置流式输出相关参数
                .incrementalOutput(true) // 逐步获取流式输出内容
                .build();
        // 发起流式调用
        Flowable<GenerationResult> result = gen.streamCall(param);
        // 组装完整的内容
        StringBuilder fullContent = new StringBuilder();
        // 阻塞式遍历流式结果，并处理每个消息
        result.blockingForEach(message -> {
            fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
            System.out.println(message.getOutput().getChoices().get(0).getMessage().getContent());
        });
        // 打印完整的内容
        System.out.println("Full content: \n" + fullContent.toString());
        return fullContent.toString();
    }
}
