package com.xw.prototype;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/10/20 10:45
 */
public class RabbitMQConsumer {

    public static final String QUEUE_NAME = "xwor";
    public static final String FANOUT_EXCHANGE_NAME = "fanout";
    public static final String DIRECT_EXCHANGE_NAME = "direct";
    public static final String TOPIC_EXCHANGE_NAME = "topic";

    public void consumer() throws IOException, TimeoutException {
        // 创建连工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 连接rabbitMQ server 若host为本地，则可不设置username和password
        connectionFactory.setHost("192.168.229.11");
        connectionFactory.setUsername("xwor");
        connectionFactory.setPassword("xwor");
        // 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建消费者，阻塞接收消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("-------------------------------------------");
                System.out.println("consumerTag : " + consumerTag);
                System.out.println("exchangeName : " + envelope.getExchange());
                System.out.println("routingKey : " + envelope.getRoutingKey());
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("消息内容 : " + msg);

                // 消息确认
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println("消息已确认");
            }
        };
        // 启动消费者消费指定队列
        channel.basicConsume(QUEUE_NAME, consumer);
//        channel.close();
//        connection.close();
    }

    public void fanoutConsumer() throws IOException {
        // 通过连接工厂创建连接
        Connection connection = getConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建一个名为二呆的交换机
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 创建一个非持久(RabbitMQ重启后会消失)、非独占(非仅用于此链接)、非自动删除(服务器将不再使用的队列删除)
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, FANOUT_EXCHANGE_NAME, "");
        // 创建消费者，阻塞接收消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("-------------------------------------------");
                System.out.println("consumerTag : " + consumerTag);
                System.out.println("exchangeName : " + envelope.getExchange());
                System.out.println("routingKey : " + envelope.getRoutingKey());
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("消息内容 : " + msg);

                // 消息确认
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println("消息已确认");
            }
        };
        // 启动消费者消费指定队列
        channel.basicConsume(queueName, consumer);
    }

    public void directConsumer() throws IOException {
        // 通过连接工厂创建连接
        Connection connection = getConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建一个名为二呆的交换机
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建一个非持久(RabbitMQ重启后会消失)、非独占(非仅用于此链接)、非自动删除(服务器将不再使用的队列删除)
        String queueName = channel.queueDeclare().getQueue();
        // 1个交换机绑定3个queue
        channel.queueBind(queueName, DIRECT_EXCHANGE_NAME, "direct1");
        channel.queueBind(queueName, DIRECT_EXCHANGE_NAME, "direct2");
        channel.queueBind(queueName, DIRECT_EXCHANGE_NAME, "direct3");
        // 创建消费者，阻塞接收消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("-------------------------------------------");
                System.out.println("consumerTag : " + consumerTag);
                System.out.println("exchangeName : " + envelope.getExchange());
                System.out.println("routingKey : " + envelope.getRoutingKey());
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("消息内容 : " + msg);

                // 消息确认
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println("消息已确认");
            }
        };
        // 启动消费者消费指定队列
        channel.basicConsume(queueName, consumer);
    }

    public void topicConsumer() throws IOException {
        // 通过连接工厂创建连接
        Connection connection = getConnection();
        // 通过连接创建通道
        Channel channel = connection.createChannel();
        // 创建一个名为二呆的交换机
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 创建一个非持久(RabbitMQ重启后会消失)、非独占(非仅用于此链接)、非自动删除(服务器将不再使用的队列删除)
        String queueName = channel.queueDeclare().getQueue();
        // 1个交换机绑定3个queue
        channel.queueBind(queueName, TOPIC_EXCHANGE_NAME, "topic.*");
//        channel.queueBind(queueName, TOPIC_EXCHANGE_NAME, "topic.#");
//        channel.queueBind(queueName, TOPIC_EXCHANGE_NAME, "*.x.*");
//        channel.queueBind(queueName, TOPIC_EXCHANGE_NAME, "#.w");
        // 创建消费者，阻塞接收消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("-------------------------------------------");
                System.out.println("consumerTag : " + consumerTag);
                System.out.println("exchangeName : " + envelope.getExchange());
                System.out.println("routingKey : " + envelope.getRoutingKey());
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("消息内容 : " + msg);

                // 消息确认
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println("消息已确认");
            }
        };
        // 启动消费者消费指定队列
        channel.basicConsume(queueName, consumer);
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
        RabbitMQConsumer consumer = new RabbitMQConsumer();
//        consumer.consumer();
//        consumer.fanoutConsumer();
//        consumer.directConsumer();
        consumer.topicConsumer();
    }
}
