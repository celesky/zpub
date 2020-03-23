package com.celesky.zpub.common.utils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.celesky.zpub.config.ServerNodeConfig;
import com.celesky.zpub.service.publish.vo.CmdResult;
import com.celesky.zpub.service.publish.vo.DockerPsRet;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-04
 */
@Slf4j
public class SShUtil {

    public static Connection login(ServerNodeConfig.Node node){
        //创建远程连接，默认连接端口为22，如果不使用默认，可以使用方法
        //new Connection(ip, port)创建对象
        Connection conn = new Connection(node.getIp(),node.getSshPort());
        try {
            //连接远程服务器
            conn.connect();
            //使用用户名和密码登录
            if("pwd".equals(node.getAuthType())){
                boolean result = conn.authenticateWithPassword(node.getAccount(), node.getPassword());
                if(result){
                    return conn;
                }
            }else{
                File file = new File(node.getKeyPath());
                boolean result = conn.authenticateWithPublicKey(node.getAccount(),file,null);
                if(result){
                    return conn;
                }
            }


        } catch (IOException e) {
            log.error("登录服务器失败！",e.getMessage(),e);
        }
        return null;
    }

    /**
     * 打开session
     * @param conn
     * @return
     */
    public static Session openSession(Connection conn){
        Session session = null;
        try {
            session = conn.openSession();
        } catch (IOException e) {
            log.error("打开session出错::",e.getMessage(),e);
        }
        return session;
    }

    /**
     * 使用已有session
     * @param session
     * @param cmds
     * @return
     */
    public static CmdResult exec(Session session, String cmds){
        if(session==null){
            return CmdResult.builder()
                    .ret(-1)
                    .cmdStr(cmds)
                    .stdOutList(null)
                    .stdErrList(null)
                    .sysError("指令执行出现了异常:未能成功打开连接session,session为空")
                    .build();
        }


        InputStream stdOutIs = null;
        InputStream stdErrIs = null;

        int ret = -1;

        try {
            //og.info("执行指令:"+cmds);
            //在远程服务器上执行linux指令
            session.execCommand(cmds);
            //指令执行结束后的输出
            stdOutIs = new StreamGobbler(session.getStdout());
            //指令执行结束后的错误
            stdErrIs = new StreamGobbler(session.getStderr());
            //等待指令执行结束
            session.waitForCondition(ChannelCondition.EXIT_STATUS, 120000);
            //取得指令执行结束后的状态
            ret = session.getExitStatus();

            CmdResult cmdResult = printOutput(cmds,ret, stdOutIs, stdErrIs);

            return cmdResult;

        }catch(Exception e){
            log.error("执行命令出错:",e.getMessage(),e);
            return CmdResult.builder()
                    .ret(-1)
                    .cmdStr(cmds)
                    .stdOutList(null)
                    .stdErrList(Arrays.asList("指令执行期间出现了异常:"+e.getMessage()))
                    .build();

        }finally {
            session.close();
        }

    }

    /**
     * 执行脚本,使用新session
     * @param conn Connection对象
     * @param cmds 要在linux上执行的指令
     */
    public static CmdResult exec(Connection conn, String cmds){
        Session session = SShUtil.openSession(conn);
        return exec(session,cmds);
    }

    /**
     * 打印输出 并返回error信息列表
     * @param cmds
     * @param stdOutIs
     * @param stdErrIs
     * @throws IOException
     */
    private static CmdResult printOutput(String cmds,int ret, InputStream stdOutIs, InputStream stdErrIs) throws IOException {
        List<String> stdOutList = new ArrayList<>();
        log.info("# "+cmds+" |执行结果: "+ret);
        print(cmds, stdOutIs, stdOutList);

        List<String> stdErrList = new ArrayList<>();
        print(cmds, stdErrIs, stdErrList);

        CmdResult cmdResult = CmdResult.builder()
                .ret(ret)
                .cmdStr(cmds)
                .stdErrList(stdErrList)
                .stdOutList(stdOutList)
                .build();
        return cmdResult;
    }

    /**
     * 打印输出
     * @param cmds
     * @param stdOutIs
     * @param stdOutList
     * @throws IOException
     */
    private static void print(String cmds, InputStream stdOutIs, List<String> stdOutList) throws IOException {
        BufferedReader stdOutBr;
        stdOutBr = new BufferedReader(new InputStreamReader
                (stdOutIs));
        //接收远程服务器执行命令的结果
        String line;
        StringBuilder outSb = new StringBuilder();
        outSb.append("\n>>>> ").append(cmds).append("\n");
        while ((line = stdOutBr.readLine()) != null) {
            log.info(">>| "+line);

            stdOutList.add(line);
            outSb.append(">>>> ").append(line).append("\n");
        }
    }



    public static void close(Connection conn){
        conn.close();
    }


    public static void main(String[] args) {
        ServerNodeConfig.Node node = ServerNodeConfig.Node.builder()
                .ip("192.168.0.1")
                .account("root")
                .authType("pwd")
                .password("xxxx")
                .sshPort(22)
                .build();
        Connection conn = SShUtil.login(node);
        if(conn!=null){
            Session session = SShUtil.openSession(conn);
            CmdResult ret = SShUtil.exec(session,"docker rm xxx");
            log.info("指令执行结果:"+ret.toString());
            SShUtil.close(conn);

            DockerPsRet.from(ret.getStdOutList());
        }
    }


}
