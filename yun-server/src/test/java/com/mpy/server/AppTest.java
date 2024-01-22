package com.mpy.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//        assertTrue( true );
        //创建并配置JedisPoolConfig（连接池配置对象）
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        poolConfig.setMaxTotal(50);//设置连接池最大活动连接数
        poolConfig.setMaxIdle(10);//设置连接池最大空闲连接数
        poolConfig.setMinIdle(3);//连接池中的最小空闲连接数
        poolConfig.setMaxWaitMillis(1000);//获取资源的等待时间
        poolConfig.setTestOnBorrow(true);//获取资源时是否验证资源的有效性
        //创建连接池
        JedisPool jedisPool=new JedisPool(
                poolConfig,//连接池配置对象
                "192.168.220.128",//Redis服务器地址
                6379,//Redis服务端口号
                100000,//连接超时时间，单位为毫秒 默认2000ms
                "test",//Redis密码
                0//数据库索引

        );
        Jedis jedis = jedisPool.getResource();
        System.out.println(jedis.ping());
    }
}
