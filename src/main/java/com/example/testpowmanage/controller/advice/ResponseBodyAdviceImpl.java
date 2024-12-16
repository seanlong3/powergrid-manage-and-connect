package com.example.testpowmanage.controller.advice;

import com.example.testpowmanage.common.ResponseResultVO;
import com.example.testpowmanage.common.ResponseStatusEnum;
import jakarta.annotation.Nonnull;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//统一api的返回数据格式
@RestControllerAdvice
@Log4j2
public class ResponseBodyAdviceImpl implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(@Nonnull MethodParameter returnType,
                            @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;//表示会拦截所有返回的响应体，不论类型和转换器
    }

    //这个是新特性语法，所以注掉了，下面替换成旧格式了，因为本项目java语法版本有问题
    /*    @Override
        public Object beforeBodyWrite(
                Object body,
                @Nonnull MethodParameter returnType,
                @Nonnull MediaType selectedContentType,
                @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                @Nonnull ServerHttpRequest request,
                @Nonnull ServerHttpResponse response) {
            switch (body) {
                case null -> {
                    return ResponseResultVO.failure();
                }
                case ResponseResultVO<?> ignored -> {
                    return body;
                }
                case InputStreamResource ignoredStream -> {
                    return body;
                }
                case Boolean b -> {
                    if (b) {
                        return ResponseResultVO.success("success");
                    }
                    return ResponseResultVO.failure(ResponseStatusEnum.BAD_REQUEST);
                }
                default -> {
                    return ResponseResultVO.success(body);
                }
            }
        }*/


    //响应体实际写出到客户端之前执行，决定如何处理和包装响应体，
    // 它对不同类型的响应体进行了分类处理，保证所有返回的数据都以统一的格式封装为 ResponseResultVO
    @Override
    public Object beforeBodyWrite(
            Object body,
            @Nonnull MethodParameter returnType,
            @Nonnull MediaType selectedContentType,
            @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @Nonnull ServerHttpRequest request,
            @Nonnull ServerHttpResponse response) {

        if (body == null) {
            return ResponseResultVO.failure();
        }

        if (body instanceof ResponseResultVO<?>) {
            return body;
        }
        //如果 body 是 InputStreamResource 类型（通常用于返回文件或流），则直接返回，
        // 因为这种情况下，返回的是文件内容或资源流，不适合包装成通用的响应结构。
        if (body instanceof InputStreamResource) {
            return body;
        }

        if (body instanceof Boolean) {
            Boolean b = (Boolean) body;
            if (b) {
                return ResponseResultVO.success("success");
            } else {
                return ResponseResultVO.failure(ResponseStatusEnum.BAD_REQUEST);
            }
        }
        //对于其他类型的响应体（比如 String、List 等），统一用 ResponseResultVO.success(body) 封装成成功响应，
        // 返回状态码 200 OK 并包含实际的返回数据
        return ResponseResultVO.success(body);
    }

}
