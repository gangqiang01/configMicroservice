package com.adv.config.former;

public class Response {
    /**
     * success
     * @param object
     * @return
     */
    public static MsgFormer success(Object object){
        MsgFormer msg=new MsgFormer();
        msg.setStatus("success");
        msg.setData(object);
        return msg;
    }

    public static MsgFormer success(){
        return success(null);
    }

    public static MsgFormer error(String resultmsg){
        MsgFormer msg=new MsgFormer();
        msg.setStatus("error");
        msg.setData(resultmsg);
        return msg;
    }


}
