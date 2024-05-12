package com.example.service;


import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

/**
 * @author zhexueqi
 * @ClassName AiService
 * @since 2024/4/12    16:24
 */
public interface AiService {
    String AI(String content) throws NoApiKeyException, InputRequiredException;
}
