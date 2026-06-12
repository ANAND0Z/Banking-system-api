package org.bank.bankingsystem.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String,Object> redisTemplate;

    public void put(String key,Object value){

        redisTemplate.opsForValue()
                .set(key,value);
    }

    public <T> T get(String key, Class<T> type) {

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> type) {

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        return (List<T>) value;
    }

    public void delete(String key){

        redisTemplate.delete(key);
    }
}