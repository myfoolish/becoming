package com.xw.prototype;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/10/20 10:25
 */
public class RabbitMQProducer {

    public static final String QUEUE_NAME = "xwor";
    public static final String FANOUT_EXCHANGE_NAME = "fanout";
    public static final String DIRECT_EXCHANGE_NAME = "direct";
    public static final String TOPIC_EXCHANGE_NAME = "topic";

    public void producer() throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 连接到rabbitMQ服务 若host为本地，则可不设置username和password
        connectionFactory.setHost("192.168.229.11");
        connectionFactory.setUsername("xwor");
        connectionFactory.setPassword("xwor");
        // 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建一个名为二呆的队列，该队列非持久(RabbitMQ重启后会消失)、非独占(非仅用于此链接)、非自动删除(服务器将不再使用的队列删除)
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msg = "hello, im xw。" + LocalDateTime.now();
        // 发布消息
        // 四个参数为：指定路由器，指定key，指定参数，和二进制数据内容
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("生产者发送消息结束，发送内容为：" + msg);
        // 关闭连接
        channel.close();
        connection.close();
    }

    /**
     * 广播，这种模式只需要将队列绑定到交换机上即可，不需要设置RoutingKey
     * @throws IOException
     * @throws TimeoutException
     */
    public void fanoutProducer() throws IOException, TimeoutException {
        // 通过连接工厂创建连接
        Connection connection = getConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建一个名为二呆的交换机
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 创建一个名为二呆的队列，该队列非持久(RabbitMQ重启后会消失)、非独占(非仅用于此链接)、非自动删除(服务器将不再使用的队列删除)
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msg = "hello, this is FANOUT " + LocalDateTime.now();
        // 发布消息
        // 四个参数为：指定路由器，指定key，指定参数，和二进制数据内容
//        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(FANOUT_EXCHANGE_NAME, "", null, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("生产者发送消息结束，发送内容为：" + msg);
        // 关闭连接
        channel.close();
        connection.close();
    }

    /**
     * 根据RoutingKey匹配消息路由到指定的队列
     * @throws IOException
     * @throws TimeoutException
     */
    public void directProducer() throws IOException, TimeoutException {
        // 通过连接工厂创建连接
        Connection connection = getConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建一个名为二呆的交换机
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建一个名为二呆的队列，该队列非持久(RabbitMQ重启后会消失)、非独占(非仅用于此链接)、非自动删除(服务器将不再使用的队列删除)
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msg = "hello, this is DIRECT and the RoutingKey is direct " + LocalDateTime.now();
        // 发布消息
        // 四个参数为：指定路由器，指定key，指定参数，和二进制数据内容
//        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(DIRECT_EXCHANGE_NAME, "direct1", null, msg.getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(DIRECT_EXCHANGE_NAME, "direct2", null, msg.getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(DIRECT_EXCHANGE_NAME, "direct3", null, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("生产者发送消息结束，发送内容为：" + msg);
        // 关闭连接
        channel.close();
        connection.close();
    }

    /**
     * 生产者指定RoutingKey 消息根据消费端指定的队列通过模糊匹配的方式进行相应转发
     * * 可以代替一个单词
     * # 可以匹配零个或多个单词
     * @throws IOException
     * @throws TimeoutException
     */
    public void topicProducer() throws IOException, TimeoutException {
        // 通过连接工厂创建连接
        Connection connection = getConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建一个名为二呆的交换机
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 创建一个名为二呆的队列，该队列非持久(RabbitMQ重启后会消失)、非独占(非仅用于此链接)、非自动删除(服务器将不再使用的队列删除)
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msg = "hello, this is TOPIC and the RoutingKey is topic " + LocalDateTime.now();
        // 发布消息
        // 四个参数为：指定路由器，指定key，指定参数，和二进制数据内容
//        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));

        channel.basicPublish(TOPIC_EXCHANGE_NAME, "topic.x.w", null, msg.getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(TOPIC_EXCHANGE_NAME, "topic.o", null, msg.getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(TOPIC_EXCHANGE_NAME, "topic.r", null, msg.getBytes(StandardCharsets.UTF_8));
        System.out.println("生产者发送消息结束，发送内容为：" + msg);
        // 关闭连接
        channel.close();
        connection.close();
    }

    public static Connection getConnection() {
        Connection connection;
        try {
            // 创建连接工厂
            ConnectionFactory connectionFactory = new ConnectionFactory();
            // 连接到rabbitMQ服务 若host为本地，则可不设置username和password
            connectionFactory.setHost("192.168.229.11");
            connectionFactory.setUsername("xwor");
            connectionFactory.setPassword("xwor");
            // 通过连接工厂创建连接
            connection = connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        RabbitMQProducer producer = new RabbitMQProducer();
//        producer.producer();
//        producer.fanoutProducer();
//        producer.directProducer();
        producer.topicProducer();
    }
}
