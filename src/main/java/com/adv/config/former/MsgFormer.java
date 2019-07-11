package com.adv.config.former;


public class MsgFormer<T> {

    /*提示信息 */
    private String status;

    /*具体内容*/
    private  T data;

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }

    public void setData(T data){
        this.data = data;
    }

    public T getData(){
        return this.data;
    }
}
