package cn.com.cnpc.cpoa.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.concurrent.*;


public class SendMailUtil {

    static String HOST = "smtp.163.com"; // smtp服务器
    static String FROM = "ajyqgjyb@163.com"; // 发件人地址
    static String USER = "ajyqgjyb@163.com"; // 用户名
    static String PWD = "DZJLRTGOCZAJSOIA"; // 163的授权码

    private static Logger logger = LoggerFactory.getLogger(SendMailUtil.class);

    /**
     * 发送邮件
     */
    public static void send(String context, String subject, String[] TOS) {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.auth", "true");  //需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(props);//用props对象构建一个session
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);//用session为参数定义消息对象

        logger.info("发送内容是" + context + "发送邮箱是" + com.alibaba.fastjson.JSON.toJSONString(TOS));
        try {
            message.setFrom(new InternetAddress(FROM));// 加载发件人地址
            InternetAddress[] sendTo = new InternetAddress[TOS.length]; // 加载收件人地址
            for (int i = 0; i < TOS.length; i++) {
                sendTo[i] = new InternetAddress(TOS[i]);
            }
            message.addRecipients(Message.RecipientType.TO, sendTo);
            message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(FROM));//设置在发送给收信人之前给自己（发送方）抄送一份，不然会被当成垃圾邮件，报554错
            message.setSubject(subject);//加载标题
            Multipart multipart = new MimeMultipart();//向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            BodyPart contentPart = new MimeBodyPart();//设置邮件的文本内容
            contentPart.setText(context);
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);//将multipart对象放到message中
            message.saveChanges(); //保存邮件
            Transport transport = session.getTransport("smtp");//发送邮件
            transport.connect(HOST, USER, PWD);//连接服务器的邮箱
            transport.sendMessage(message, message.getAllRecipients());//把邮件发送出去
            transport.close();//关闭连接
        } catch (Exception e) {
            logger.error(String.format("邮件发送失败:%s,目标邮箱是：%s", e.getMessage(), com.alibaba.fastjson.JSON.toJSONString(TOS)), e);
            //e.printStackTrace();
        }
    }


    /**
     * 异步发送邮件
     * @param context
     * @param subject
     * @param TOS
     */
    public static void threadSendMail(String context, String subject, String[] TOS) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("mailMessage-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send(context,subject,TOS);
                    }
                })
        );

        singleThreadPool.shutdown();
    }


    private final static String getMailMessage() {
        StringBuffer sbf = new StringBuffer();
        sbf.append("niu13");
        sbf.append("公司 你好！\n");
        sbf.append("    你申请办理安检院院准入（或临时准入）已获同意。推荐使用谷歌公司的Chrome浏览器或360浏览器（需要设置为极速模式）打开以下准入地址，正确填写准入码，上传贵公司准\n");
        sbf.append("入资料办理准入。\n\n");
        sbf.append("准入地址：");
        sbf.append("http://cpoa.jkzj.com:55556/login/access/");
        sbf.append("1");
        sbf.append("\n\n");
        sbf.append("准入码");
        sbf.append("cc1");
        sbf.append("\n\n");
        sbf.append("业务咨询人：叶剑眉     0838-5150017");
        return sbf.toString();

    }


    public static void main(String[] args) {
//        String[] TOS = new String[]{"scchenyong@189.cn"};
//        send(getMailMessage(),TOS);
    }

}