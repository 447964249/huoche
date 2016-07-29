package cn.ucai.fulicenter.bean;

/**
 * Created by Administrator on 2016/7/29.
 */
public class MessageBean {


    /**
     * success : true
     * msg : 注册成功
     */

    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
