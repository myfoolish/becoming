package com.xw.lock.redis;

import com.xw.util.client.exception.DefaultExceptionHandler;
import com.xw.util.client.exception.RedisException;
import com.xw.util.client.exception.RedisRuntimeException;
import com.xw.util.client.inter.IExceptionHandler;
import com.xw.util.client.inter.IRedisOperation;
import com.xw.util.client.template.RedisShardedPoolTemplate;
import com.xw.util.RedisClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.JedisCommands;

import java.util.Collections;
import java.util.Map;

/**  
* <p>Company: 北京宇信科技集团股份有限公司</p>  
* @ClassName: RedisDistributedLock  
* @Description: 用于redis的分布事物锁-排他锁 
* @author lvhd  
* @date 2018年12月13日  
* @mail lvhd@yusys.com.cn  
*/  
@Component
public class RedisDistributedLock {
	
	private static final ThreadLocal<String> keyTags = new ThreadLocal();
	private IExceptionHandler exceptionHandler = DefaultExceptionHandler.INSTANCE;

	private static final String UNLOCK_LUA;
	private final static Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);
	private Long expireTime = 10L; //redis生效时间10s
	private String keyLock = ".LOCK"; //将锁的key统一添加.LOCK后缀防止key与redis业务事物冲突

    //声明bean对象
//    @Resource(name = "redisMap")
    private Map<String, RedisClientUtil> redisMap;


	static {
		StringBuffer sb = new StringBuffer();
		sb.append("if redis.call(\"get\", KEYS[1]) == ARGV[1] ");
		sb.append("then ");
		sb.append("    return redis.call(\"del\", KEYS[1]) ");
		sb.append("else ");
		sb.append("    return 0 ");
		sb.append("end ");
		UNLOCK_LUA = sb.toString();
	}
	
	/**
	 * @Description: 设置redis分布式事物锁  
	 * @author lvhd  
	 * @date 2018年12月13日  
	 * @mail lvhd@yusys.com.cn
	 * @param key
	 * @param expire
	 * @return
	 */
	private boolean setLock(String key, long expire){
		
		try{
			//update by songy 2019/09/23 更改加锁逻辑
			String result= null;
			if (redisMap.get("redis-cache").getRedisShardedPoolTemplate() != null) {
				result= redisMap.get("redis-cache").getRedisShardedPoolTemplate().set(key, "Lock", "NX", "EX", expireTime);
			}else {
				result = redisMap.get("redis-cache").getRedisPoolTemplate().set(key, "Lock", "NX", "EX", expireTime);
			}
			return !StringUtils.isEmpty(result);
		}catch(Exception e){
			logger.error("set redis occured an exception", e);
		}
		return false;
	}
	
	/**
	 * @Description: 释放redis分布事物锁  
	 * @author lvhd  
	 * @date 2018年12月13日  
	 * @mail lvhd@yusys.com.cn
	 * @param key
	 * @param value
	 * @return
	 */
	private boolean releaseLock(String key, String value){
		
		try{
			Long result = redisMap.get("redis-cache").getRedisShardedPoolTemplate().del(key);
			return result != null && result > 0; 
		}catch(Exception e){
			logger.error("release lock occured an exception", e);
		}finally{
			
		}
		
		return false;
	}
	
	/**
	 * @Description: 根据key生成redis分布事物锁，模式生效时间为1000ms, 并返回value值,若返回null,说明没有获取到redis锁  
	 * @author lvhd  
	 * @date 2018年12月13日  
	 * @mail lvhd@yusys.com.cn
	 * @param key
	 * @return 
	 */
	public boolean setLock(String key){
		
		return setLock(key + keyLock, expireTime);
	}
	
	/**
	 * @Description: 根据key获取到redis锁的value,  
	 * @author lvhd  
	 * @date 2018年12月13日  
	 * @mail lvhd@yusys.com.cn
	 * @param key
	 * @return
	 */
	public String getLockValue(String key){
		return key + keyLock;
	}
	
	/**
	 * @Description: 根据key判断key是否存在，key的value是否相同，若相同删除该锁，返回true  
	 * @author lvhd  
	 * @date 2018年12月13日  
	 * @mail lvhd@yusys.com.cn
	 * @param key
	 * @return
	 */
	public boolean releaseLock(String key){
		String value = this.getLockValue(key);
		if(!StringUtils.isEmpty(value)){
			return this.releaseLock(key + keyLock, value);
		}else{
			return true;
		}
		
	}
	
	/* 重构分布式锁处理add by lxb 20200708*/
	
	/**
	 * 设置redis分布式事物锁
	 * @param key
	 * @param uuid
	 * @param expire 单位为秒
	 * @return
	 */
	public boolean setLock(String key,String uuid,long expire){
		String LOCK_SUCCESS = "OK";
		try{
			//此处exprie单位为秒
			String result = redisMap.get("redis-cache").getRedisShardedPoolTemplate().set(key, uuid, "NX", "EX", expire);
			if (LOCK_SUCCESS.equals(result)) {
				return true;
			}
		}catch(Exception e){
			logger.error("set redis occured an exception", e);
		}
		return false;
	}
	/**
	 * 设置redis分布式事物锁  
	 * @param key
	 * @param uuid
	 * @return
	 */
	public boolean setLock(String key, String uuid){
		return setLock(key, uuid, expireTime);
	}

	/**
	 * 判断是否存在分布式锁
	 * @param key 分布式锁key
	 * @return true 存在分布式锁
	 */
	public boolean existLock(String key){
		return !StringUtils.isEmpty(redisMap.get("redis-cache").getRedisShardedPoolTemplate().get(key));
	}

	
    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean releaseDistributedLock(String lockKey, String requestId) {
    	Long RELEASE_SUCCESS = 1L;
    	Object result;
    	checkKeyTagExists(lockKey);
    	result = doRedisWithExceptionHandler(redisMap.get("redis-cache").getRedisShardedPoolTemplate(), new IRedisOperation()
        {
        	public Object exec(Jedis jedis)
		    {
		      return jedis.eval(UNLOCK_LUA, Collections.singletonList(lockKey), Collections.singletonList(requestId));
		    }

			@Override
			public Object exec(JedisCommands paramR) {
				// TODO 自动生成的方法存根
				return ((Jedis) paramR).eval(UNLOCK_LUA, Collections.singletonList(lockKey), Collections.singletonList(requestId));
			}
        });
    	if (RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;
    }
    
    private void checkKeyTagExists(String key)
    {
      if (keyTags.get() == null) {
        keyTags.set(key);
      }
    }
    
    private <T> T doRedisWithExceptionHandler(RedisShardedPoolTemplate redisShardedPoolTemplate,IRedisOperation<T> operation)
    {
      T ret = null;
      String keyTag = (String)keyTags.get();
      try
      {
        ret = redisShardedPoolTemplate.doRedis(keyTag, operation);
      }
      catch (RedisRuntimeException re)
      {
        exceptionHandler.dealException(new RedisException(re.getMessage(), re));
      }
      catch (RedisException ex)
      {
        exceptionHandler.dealException(ex);
      }
      return ret;
    }
}
