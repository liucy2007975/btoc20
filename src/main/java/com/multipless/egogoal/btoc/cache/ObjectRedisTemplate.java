package com.multipless.egogoal.btoc.cache;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author ChenKai
 * @date 2015年9月9日
 * 由于stringRedisTemplate只能对字符串类型进行操作，所以仿照stringRedisTemplate写一个能对对象进行操作的ObjectRedisTemplate，然后在配置文件中使用spring注入，单元测试时使用
 * 完全仿照stringRedisTemplate来进行操作，只是使用了泛型，对任意类型进行序列化
 */
public class ObjectRedisTemplate<T> extends RedisTemplate<String, T> {
	
	
	public ObjectRedisTemplate(){
		
	}
	public ObjectRedisTemplate(RedisConnectionFactory connectionFactory, Class<T> clazz) {
		RedisSerializer<T> objectSerializer = new Jackson2JsonRedisSerializer<T>(clazz);
		RedisSerializer<String> objectKeySerializer = new Jackson2JsonRedisSerializer<String>(String.class);
		setKeySerializer(objectKeySerializer);
		setValueSerializer(objectSerializer);
		setHashKeySerializer(objectSerializer);
		setHashValueSerializer(objectSerializer);
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();

	}

	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}
}
