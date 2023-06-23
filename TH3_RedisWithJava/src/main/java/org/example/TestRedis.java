package org.example;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestRedis {
	public static void main(String[] args) {
		JedisPool pool = new JedisPool("172.29.52.231", 6379);

		try (Jedis jedis = pool.getResource()) {
//			thêm
//			jedis.set("Name", "An");
//			jedis.set("Surname", "Nguyễn");
//			jedis.set("Company", "DHCN");
//			jedis.set("Age", "21");

//			get
//			String surname = jedis.get("Surname");
//			String name = jedis.get("Name");
//			String company = jedis.get("Company");
//			String age = jedis.get("Age");

//			System.out.println("Surname: " + surname + "\nName: " + name + "\nCompany: " + company + "\nAge: " + age);

//			xóa
//			jedis.del("Surname", "Name", "Company", "Age");
//			System.out.println("Surname: " + surname + "\nName: " + name + "\nCompany: "+ company + "\nAge: " + age);
		
			Map<String, String> hash = new HashMap<>();
			hash.put("Surname", "Nguyễn");
			hash.put("Name", "An");
			hash.put("company", "DHCN");
			hash.put("Age", "21");
			jedis.hmset("user:an", hash);
			
//			jedis.del("user:an"); //xóa
			System.out.println(jedis.hgetAll("user:an"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
