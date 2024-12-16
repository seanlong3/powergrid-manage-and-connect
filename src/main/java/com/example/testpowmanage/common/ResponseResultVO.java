package com.example.testpowmanage.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResultVO<T> implements Serializable {
    private Integer code;   //http状态码
    private String msg;     //传递消息
    private T data;         //泛型的数据

    public ResponseResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //OK
    public static <T> ResponseResultVO<T> success(T data){
        return new ResponseResultVO<>(ResponseStatusEnum.OK.getCode(), ResponseStatusEnum.OK.getDescription(), data);
    }
    //具体
    public static <T> ResponseResultVO<T> success(ResponseStatusEnum responseStatus, T data){
        return new ResponseResultVO<>(responseStatus.getCode(), responseStatus.getDescription(), data);
    }

    //服务端内部错误
    public static <T> ResponseResultVO<T> failure(){
        return new ResponseResultVO<>(
                ResponseStatusEnum.INTERNAL_SERVER_ERROR.getCode(),
                ResponseStatusEnum.INTERNAL_SERVER_ERROR.getDescription(),
                null);
    }
    //具体
    public static <T> ResponseResultVO<T> failure(ResponseStatusEnum statusEnum) {
        return new ResponseResultVO<>(statusEnum.getCode(), statusEnum.getDescription());
    }
    public static <T> ResponseResultVO<T> failure(ResponseStatusEnum statusEnum, String msg){
        return new ResponseResultVO<>(statusEnum.getCode(), msg);
    }
}
