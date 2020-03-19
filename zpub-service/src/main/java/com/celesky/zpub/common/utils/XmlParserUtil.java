package com.celesky.zpub.common.utils;

import com.celesky.zpub.jenkins.JenkinsBuildStatus;
import com.celesky.zpub.wechatsdk.WechatInboundEcry;
import com.celesky.zpub.wechatsdk.WechatInboundMsg;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @desc: xml解析 借助dom4j
 * 解析百唔的xml响应
 * @author: panqiong
 * @date: 2018/11/7
 */
public class XmlParserUtil {



    /**
     * <xml>
     *    <ToUserName><![CDATA[toUser]]></ToUserName>
     *    <AgentID><![CDATA[toAgentID]]></AgentID>
     *    <Encrypt><![CDATA[msg_encrypt]]></Encrypt>
     * </xml>
     * @param xml
     * @return
     */
    public static WechatInboundEcry parseInboundEcry(String xml) throws Exception{
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        String touserName = root.elementTextTrim("ToUserName");
        String agentId = root.elementTextTrim("AgentID");
        String msgEncrypt = root.elementTextTrim("Encrypt");

        WechatInboundEcry sms = WechatInboundEcry.builder()
                    .toUserName(touserName)
                    .agentId(agentId)
                    .Encrypt(msgEncrypt)
                    .build();

        return sms;
    }


    /**
     * <xml>
     *    <ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName>
     *    <FromUserName><![CDATA[mycreate]]></FromUserName>
     *    <CreateTime>1409659813</CreateTime>
     *    <MsgType><![CDATA[text]]></MsgType>
     *    <Content><![CDATA[hello]]></Content>
     *    <MsgId>4561255354251345929</MsgId>
     *    <AgentID>218</AgentID>
     * </xml>
     *
     * https://work.weixin.qq.com/api/doc#90000/90135/90239/%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF
     *
     * 解析明文消息结构体
     * @param xml
     * @throws Exception
     */
    public static WechatInboundMsg parseInboundMessage(String xml) throws Exception{
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        String touserName = root.elementTextTrim("ToUserName");
        String fromUserName = root.elementTextTrim("FromUserName");
        String createTime = root.elementTextTrim("CreateTime");
        String msgType = root.elementTextTrim("MsgType");
        String msgId = root.elementTextTrim("MsgId");
        String agentId = root.elementTextTrim("AgentID");
        String content = root.elementTextTrim("Content");
        String picUrl = root.elementTextTrim("PicUrl");

        WechatInboundMsg sms = WechatInboundMsg.builder()
                .agentId(agentId)
                .content(content)
                .createTime(createTime)
                .fromUserName(fromUserName)
                .msgId(msgId)
                .msgType(msgType)
                .touserName(touserName)
                .picUrl(picUrl)
                .build();
        return sms;
    }

    public static JenkinsBuildStatus parseJenkinsBuildInfo(String xml) throws Exception{
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        String buiding = root.elementTextTrim("building");
        String id = root.elementTextTrim("id");
        String fullDisplayName = root.elementTextTrim("fullDisplayName");
        String result = root.elementTextTrim("result");

        JenkinsBuildStatus sms = JenkinsBuildStatus.builder()
                .buiding(Boolean.parseBoolean(buiding))
                .fullDisplayName(fullDisplayName)
                .id(Integer.parseInt(id))
                .result(result)
                .build();
        return sms;
    }


    public static void main(String[] args) throws Exception {
        String text = "<xml> \n" +
                "   <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "   <AgentID><![CDATA[toAgentID]]></AgentID>\n" +
                "   <Encrypt><![CDATA[msg_encrypt]]></Encrypt>\n" +
                "</xml>";
        WechatInboundEcry dto = XmlParserUtil.parseInboundEcry(text);
        System.out.println(dto.toString());



        String txt = "<xml>\n" +
                "   <ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName>\n" +
                "   <FromUserName><![CDATA[mycreate]]></FromUserName>\n" +
                "   <CreateTime>1409659813</CreateTime>\n" +
                "   <MsgType><![CDATA[text]]></MsgType>\n" +
                "   <Content><![CDATA[hello]]></Content>\n" +
                "   <MsgId>4561255354251345929</MsgId>\n" +
                "   <AgentID>218</AgentID>\n" +
                "</xml>";


        WechatInboundMsg d = XmlParserUtil.parseInboundMessage(txt);

        System.out.println(d.toString());



        String txt2 = "<mavenModuleSetBuild _class='hudson.maven.MavenModuleSetBuild'>\n" +
                "    <action _class='hudson.model.ParametersAction'>\n" +
                "        <parameter _class='net.uaznia.lukanus.hudson.plugins.gitparameter.GitParameterValue'>\n" +
                "            <name>branch</name>\n" +
                "            <value>master</value>\n" +
                "        </parameter>\n" +
                "    </action>\n" +
                "    <action _class='hudson.model.CauseAction'>\n" +
                "        <cause _class='hudson.model.Cause$RemoteCause'>\n" +
                "            <shortDescription>Started by remote host 121.46.29.116 with note: 由panqiong通过企业微信发布助手远程触发</shortDescription>\n" +
                "            <addr>121.46.29.116</addr>\n" +
                "            <note>由panqiong通过企业微信发布助手远程触发</note>\n" +
                "        </cause>\n" +
                "    </action>\n" +
                "    <action></action>\n" +
                "    <action _class='hudson.plugins.git.util.BuildData'>\n" +
                "        <buildsByBranchName>\n" +
                "            <originmaster _class='hudson.plugins.git.util.Build'>\n" +
                "                <buildNumber>31</buildNumber>\n" +
                "                <marked>\n" +
                "                    <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                    <branch>\n" +
                "                        <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                        <name>origin/master</name>\n" +
                "                    </branch>\n" +
                "                </marked>\n" +
                "                <revision>\n" +
                "                    <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                    <branch>\n" +
                "                        <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                        <name>origin/master</name>\n" +
                "                    </branch>\n" +
                "                </revision>\n" +
                "            </originmaster>\n" +
                "            <refsremotesoriginmaster _class='hudson.plugins.git.util.Build'>\n" +
                "                <buildNumber>22</buildNumber>\n" +
                "                <marked>\n" +
                "                    <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                    <branch>\n" +
                "                        <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                        <name>refs/remotes/origin/master</name>\n" +
                "                    </branch>\n" +
                "                </marked>\n" +
                "                <revision>\n" +
                "                    <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                    <branch>\n" +
                "                        <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                        <name>refs/remotes/origin/master</name>\n" +
                "                    </branch>\n" +
                "                </revision>\n" +
                "            </refsremotesoriginmaster>\n" +
                "        </buildsByBranchName>\n" +
                "        <lastBuiltRevision>\n" +
                "            <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "            <branch>\n" +
                "                <SHA1>489d0964764aeabed19de2a43e5360f075e024c8</SHA1>\n" +
                "                <name>origin/master</name>\n" +
                "            </branch>\n" +
                "        </lastBuiltRevision>\n" +
                "        <remoteUrl>ssh://git@121.46.23.206:10023/CustomerService/${JOB_NAME}s.git</remoteUrl>\n" +
                "        <scmName></scmName>\n" +
                "    </action>\n" +
                "    <action _class='hudson.plugins.git.GitTagAction'></action>\n" +
                "    <action></action>\n" +
                "    <action _class='hudson.maven.reporters.MavenAggregatedArtifactRecord'></action>\n" +
                "    <action></action>\n" +
                "    <action></action>\n" +
                "    <action></action>\n" +
                "    <artifact>\n" +
                "        <displayPath>stream-service.jar</displayPath>\n" +
                "        <fileName>stream-service.jar</fileName>\n" +
                "        <relativePath>stream-service/target/stream-service.jar</relativePath>\n" +
                "    </artifact>\n" +
                "    <building>false</building>\n" +
                "    <displayName>#31</displayName>\n" +
                "    <duration>34207</duration>\n" +
                "    <estimatedDuration>32354</estimatedDuration>\n" +
                "    <fullDisplayName>stream-service #31</fullDisplayName>\n" +
                "    <id>31</id>\n" +
                "    <keepLog>false</keepLog>\n" +
                "    <number>31</number>\n" +
                "    <queueId>220</queueId>\n" +
                "    <result>SUCCESS</result>\n" +
                "    <timestamp>1557043691830</timestamp>\n" +
                "    <url>http://jks-repositories.zuzuche.com/job/stream-service/31/</url>\n" +
                "    <builtOn></builtOn>\n" +
                "    <changeSet _class='hudson.plugins.git.GitChangeSetList'>\n" +
                "        <kind>git</kind>\n" +
                "    </changeSet>\n" +
                "    <mavenArtifacts></mavenArtifacts>\n" +
                "    <mavenVersionUsed>3.5.4</mavenVersionUsed>\n" +
                "</mavenModuleSetBuild>";

        JenkinsBuildStatus dx= XmlParserUtil.parseJenkinsBuildInfo(txt2);
        System.out.println("dx = " + dx.toString());
    }
}
