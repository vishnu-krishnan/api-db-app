package com.study.projects;

import org.apache.camel.Header;
import redis.clients.jedis.Jedis;

public class CacheOperations {

    public void doCacheOperations(@Header("cacheKey") String key, @Header("cacheValue") String value,
                                  @Header("cacheOperations") String opertion){

        try {
            Jedis jedis = new Jedis("localhost",6379);
            //jedis.auth("redisPassword");
            System.out.println("connected to REDIS");
            jedis.set(key, value);
            String redisValue = jedis.get(key);
            System.out.println("Redis cache value -> "+redisValue);
        } catch ( Exception e){
            e.printStackTrace();
        }
    }
}
