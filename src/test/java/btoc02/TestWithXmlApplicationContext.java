package btoc02;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.multipless.egogoal.btoc.cache.ObjectRedisTemplate;
import com.multipless.egogoal.btoc.entity.UserEntity;

/**
 * @author ChenKai
 * @date 2015年9月8日
 */
public class TestWithXmlApplicationContext {

	/**
	 * 测试redis的使用stringRedisTemplate
	 * 127.0.0.1:6379> get myStr    查看string
	 * 127.0.0.1:6379> llen myList  查看list长度
	 * 127.0.0.1:6379> smembers mySet  查看set中的值
	 * 127.0.0.1:6379> hkeys myHash  查看hash中key
	 * 127.0.0.1:6379> hvals myHash 查看hash中value
	 * 127.0.0.1:6379> hgetall myHash 查看hash中key 和value
	 */
//	@Test
	public void testRedis() {
		ConfigurableApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext("spring/spring.xml");
			StringRedisTemplate stringRedisTemplate = ctx.getBean("stringRedisTemplate", StringRedisTemplate.class);
			// String操作
			stringRedisTemplate.delete("myStr");
			stringRedisTemplate.opsForValue().set("myStr", "saohuobang1123");
			System.out.println(stringRedisTemplate.opsForValue().get("myStr"));
			System.out.println("---------------");

			// list操作
			stringRedisTemplate.delete("myList");
			stringRedisTemplate.opsForList().rightPush("myList", "A");
			stringRedisTemplate.opsForList().rightPush("myList", "B");
			stringRedisTemplate.opsForList().rightPush("myList", "C");
			stringRedisTemplate.opsForList().rightPush("myList", "D");
			stringRedisTemplate.opsForList().leftPush("myList", "E");
//			stringRedisTemplate.opsForList().rightPop("myList");
			stringRedisTemplate.opsForList().leftPop("myList");
			List<String> list = stringRedisTemplate.opsForList().range("myList", 0, -1);
			for (String s : list) {
				System.out.println(s);
			}
			System.out.println("---------------");

			//set 操作
			stringRedisTemplate.delete("mySet");
			stringRedisTemplate.opsForSet().add("mySet", "setValue1");
			stringRedisTemplate.opsForSet().add("mySet", "setValue2");
			stringRedisTemplate.opsForSet().add("mySet", "setValue3");
			Set<String> members = stringRedisTemplate.opsForSet().members("mySet");
			for(String s :members){
				System.out.println(s);
			}
			System.out.println("---------------");
			
			//hash 操作
			stringRedisTemplate.delete("myHash");
			stringRedisTemplate.opsForHash().put("myHash", "china", "中国");
			stringRedisTemplate.opsForHash().put("myHash", "American", "美国");
			stringRedisTemplate.opsForHash().put("myHash", "Germany", "德国");
			Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("myHash");
			Set<Entry<Object, Object>> entrySet = entries.entrySet();
			for(Entry<Object, Object> s : entrySet){
				System.out.println(s.getKey()+"==>"+ s.getValue());
			}
			System.out.println("---------------");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ctx != null && ctx.isActive()) {
				ctx.close();
			}
		}

	}
	
	
	/**
	 * 测试redis的使用objectRedisTemplate
	 * 1、实体bean必须实现序列化接口，否则无法序列化，会报错
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testUseObjectTemplate() {
		ConfigurableApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext("spring/spring.xml");
			ObjectRedisTemplate objectRedisTemplate = ctx.getBean("objectRedisTemplate", ObjectRedisTemplate.class);
			
			//bean 操作
			UserEntity userEntity = new UserEntity();
			userEntity.setUserName("扫货邦");
			objectRedisTemplate.delete("user");
			objectRedisTemplate.opsForValue().set("user", userEntity);
			Object object = objectRedisTemplate.opsForValue().get("user");
			System.out.println(object);
			System.out.println("---------------");
			
			//list操作
			objectRedisTemplate.delete("okList");
			objectRedisTemplate.opsForList().rightPush("okList", "AAA");
			objectRedisTemplate.opsForList().rightPush("okList", "BBB");
			objectRedisTemplate.opsForList().rightPush("okList", "CCC");
			List<String> range = objectRedisTemplate.opsForList().range("okList", 0, -1);
			for(String s : range){
				System.out.println(s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ctx != null && ctx.isActive()) {
				ctx.close();
			}
		}

	}
	
}
