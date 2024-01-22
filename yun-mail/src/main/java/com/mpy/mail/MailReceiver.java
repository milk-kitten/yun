package com.mpy.mail;

import com.mpy.server.pojo.Employee;
import com.mpy.server.pojo.MailConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 消息接收者
 *
 */
//component 被spring容器进行管理
@Component
public class MailReceiver {
    //logger 进行打印日志
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;
    // 邮件配置
    @Autowired
    private MailProperties mailProperties;
    // 模板引擎 ----> //templateEngine会从下面的context拿相应的数据。（从context拿到相应内容，放到mail）
    // 从而前端会拿到相应得数据
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 接收发送命令
     * 邮件发送
     */
    //有了这个RabbitListener注解后，会根据路由，会将接收到发送端得消息"给该用户发送邮件得信息"
    //employee就是需要发送邮件的信息
//    @RabbitListener(queues = "mail.welcome")
//    public void handler(Employee employee) {
//        //创建消息
//        MimeMessage msg = javaMailSender.createMimeMessage();
//        System.out.println("创建消息：====》" + msg);
//
//        MimeMessageHelper helper = new MimeMessageHelper(msg);
//        try {
//            //发件人
//            helper.setFrom(mailProperties.getUsername());
//            //收件人
//            helper.setTo(employee.getEmail());
//            //主题
//            helper.setSubject("入职欢迎邮件");
//            //发送日期
//            helper.setSentDate(new Date());
//            //邮件内容
//            Context context = new Context();
//            context.setVariable("name",employee.getName());
//            context.setVariable("posName",employee.getPosition().getName());
//            context.setVariable("joblevelName",employee.getJoblevel().getName());
//            context.setVariable("departmentName",employee.getDepartment().getName());
//            // templateEngine会从context拿相应的数据。（从context拿到相应内容，放到mail）
//            String mail = templateEngine.process("mail", context);
//            helper.setText(mail,true);
//
//            //发送邮件
//            javaMailSender.send(msg);
//        } catch (MessagingException e) {
//            LOGGER.error("邮件发送失败=====>{}",e.getMessage());
//        }
//    }

    /**
     * 发送邮件
     * @param message
     * @param channel
     */
    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException {
        Employee employee = (Employee) message.getPayload();

        MessageHeaders headers = message.getHeaders();
        //消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                //redis中包含key，说明消息已经被消费
                LOGGER.info("消息已经被消费=====>{}", msgId);
                /**
                 * 手动确认消息
                 * tag：消息序号
                 * multiple：是否多余
                 */
                channel.basicAck(tag,false);
                return;
            }
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(employee.getEmail());
            //主题
            helper.setSubject("入职欢迎邮件");
            //发送日期
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            //发送邮件
            javaMailSender.send(msg);
            LOGGER.info("邮件发送成功");
            //将消息id存入redis
            hashOperations.put("mail_log",msgId,"OK");
            //手动确认消息
            channel.basicAck(tag,false);
        } catch (Exception e)  {
            try {
                /**
                 * 手动确认消息
                 * tag：消息序号
                 * multiple：是否多余
                 * requeue：是否回退到队列
                 */
                channel.basicNack(tag,false,true);
            } catch (IOException ex) {
                LOGGER.error("消息确认失败=====>{}", ex.getMessage());
            }
            LOGGER.error("邮件发送失败=====>{}", e.getMessage());
        }
    }
}
