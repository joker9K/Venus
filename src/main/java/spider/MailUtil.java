package spider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/**
 * Created by zhangwt n 2017/11/20.
 */
@Slf4j
public class MailUtil {
    private static final String host = "smtp.163.com"; // smtp服务器
    private static final String user = "M_notify@163.com"; // 用户名
    private static final String pwd = "mx123456"; // 163的授权码，并非密码


    public static boolean send(MailContent mail) {
        if(mail == null){
            log.info("邮件发送实体为空，发送失败！");
            return false;
        }
        Properties props = new Properties();
        // 设置发送邮件的邮件服务器的属性
        props.put("mail.smtp.host", host);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");
        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);
        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
//        session.setDebug(true);
        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(user));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
            // 加载标题
            message.setSubject(mail.getSubject());
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(mail.getContent());
            multipart.addBodyPart(contentPart);
            if(StringUtils.isNotBlank(mail.getAffix()) && new File(mail.getAffix()).exists()) {
                // 添加附件
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(mail.getAffix());
                // 添加附件的内容
                messageBodyPart.setDataHandler(new DataHandler(source));
                // 添加附件的标题
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                if(StringUtils.isBlank(mail.getAffixName())){
                    mail.setAffixName(new File(mail.getAffix()).getName());
                }
                messageBodyPart.setFileName("=?GBK?B?"
                        + enc.encode(mail.getAffixName().getBytes()) + "?=");
                multipart.addBodyPart(messageBodyPart);
            }
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            transport.connect(host, user, pwd);
            // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            log.error("邮件发送失败,mail:{}",mail);
            return false;
        }
        log.info("邮件发送成功");
        return true;
    }


    public static void main(String[] args) {
        String str = "马苋飞大傻逼";
        for(int i =0;i<str.length();i++){
            MailContent mail = MailContent.builder().to("1158401497@qq.com").subject(String.valueOf(str.charAt(i))).content("马苋飞大傻逼").build();
            MailUtil.send(mail);
        }
    }
}
