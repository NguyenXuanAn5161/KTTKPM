package org.example;

import com.google.gson.Gson;

import entity.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestUser {
	public static void main(String[] args) {
		JedisPool pool = new JedisPool("172.29.52.231", 6379);
		
		try(Jedis jedis = pool.getResource()) {
//			Tạo 1 user
			User useran= new User("Nguyễn", "An", "Đại học Công Nghiệp", "21");
//			Chuyển thành json
			Gson gson = new Gson();
			String json = gson.toJson(useran);
//			lưu vào jedis
			jedis.set("UserAn", json);
//			lấy
			String jsonRedis = jedis.get("UserAn");
			User useranRedis = gson.fromJson(jsonRedis, User.class);
			
			System.out.println(useranRedis.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
