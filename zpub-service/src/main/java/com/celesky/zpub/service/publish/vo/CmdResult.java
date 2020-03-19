package com.celesky.zpub.service.publish.vo;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * S是标准输出对象,E是标准错误对象
 * @desc: 指令执行结果
 * @author: panqiong
 * @date: 2019-12-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public  class  CmdResult {
    /**
     * 结果码
     */
    private int ret;

    /**
     * 指令
     */
    private String cmdStr;

    /**
     * 标准输出错误信息列表
     */
    private List<String> stdOutList;

    /**
     * 标准输出错误信息列表
     */
    private List<String> stdErrList;


    /**
     * 系统错误错误信息列表
     */
    @Builder.Default
    private String sysError = "";



    public boolean success(){
        return ret==0;
    }
    public boolean fail(){
        return ret!=0;
    }

    public String getStdOutStr(){
        if(stdOutList!=null&&stdOutList.size()>0){
            return Joiner.on("\n").join(stdOutList);
        }else{
            return "";
        }
    }

    public String getStdErrStr(){
        if(stdErrList!=null&&stdErrList.size()>0){
            return Joiner.on("\n").join(stdErrList);
        }else{
            return "";
        }
    }

    @Override
    public String toString() {
        return  "|cmdStr:" + cmdStr + "\n"+
                "|result:" + ret +"\n" +
                "|stdOut:" + getStdOutStr() + "\n"+
                "|stdErr:" + getStdErrStr() + "\n"+
                "|sysError:" + sysError + "\n";
    }
}
