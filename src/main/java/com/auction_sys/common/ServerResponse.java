package com.auction_sys.common;



import com.auction_sys.common.constant.CommonConst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by liuda
 */
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable {

    private int status;
    private int msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, int msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, int msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess(){
        return this.status == CommonConst.ResponseCode.SUCCESS;
    }
    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public int getMsg(){
        return msg;
    }


    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(CommonConst.ResponseCode.SUCCESS);
    }

    public static <T> ServerResponse<T> createBySuccessMessage(int msg){
        return new ServerResponse<T>(CommonConst.ResponseCode.SUCCESS,msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(CommonConst.ResponseCode.SUCCESS,data);
    }
    public static <T> ServerResponse<T> createBySuccess(int msg){
        return new ServerResponse<T>(CommonConst.ResponseCode.SUCCESS,msg);
    }
    public static <T> ServerResponse<T> createBySuccess(int msg,T data){
        return new ServerResponse<T>(CommonConst.ResponseCode.SUCCESS,msg,data);
    }


    public static <T> ServerResponse<T> createByError(int errorMessage){
        return new ServerResponse<T>(CommonConst.ResponseCode.ERROR,errorMessage);
    }
    public static <T> ServerResponse createByErrorIllegalOperation(){
        return new ServerResponse<T>(CommonConst.ResponseCode.ERROR,CommonConst.CommonResponStateConst.ILLEGAL_OPERATION);
    }


    public static <T> ServerResponse createByErrorUnknow(){
        return new ServerResponse<T>(CommonConst.ResponseCode.ERROR,CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }
    public static <T> ServerResponse createByErrorIllegalParam(){
        return new ServerResponse<T>(CommonConst.ResponseCode.ERROR,CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
    }
    public static <T> ServerResponse createBySuccessOperationSuccess(){
        return new ServerResponse<T>(CommonConst.ResponseCode.SUCCESS,CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
    }
    public static <T> ServerResponse createBySuccessOperationSuccess(T data){
        return new ServerResponse<T>(CommonConst.ResponseCode.SUCCESS,CommonConst.CommonResponStateConst.OPRATION_SUCCESS,data);
    }
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(CommonConst.ResponseCode.ERROR);
    }





}
