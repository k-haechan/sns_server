package site.haechan.sns_backend.global.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	public void set(RedisKeyType type, String param, Object value, Duration timeout) {
		// Redis에 value를 저장, 키는 type.getKey(param)으로 생성
		// TTL을 직접 지정하고 싶을 때 사용
		redisTemplate.opsForValue().set(type.getKey(param), value, timeout);
	}

	public Object get(RedisKeyType type, String param) {
		// Redis에서 type.getKey(param) 키로 저장된 값을 조회
		return redisTemplate.opsForValue().get(type.getKey(param));
	}

	public void expire(RedisKeyType type, String param, Duration timeout) {
		// Redis 키의 TTL(Time To Live)을 지정된 timeout으로 변경
		redisTemplate.expire(type.getKey(param), timeout);
	}

	public boolean hasKey(RedisKeyType type, String param) {
		// Redis에 해당 키가 존재하는지 여부 확인
		Boolean result = redisTemplate.hasKey(type.getKey(param));
		return Boolean.TRUE.equals(result);
	}

	public void delete(RedisKeyType type, String param) {
		// Redis에서 해당 키를 삭제
		redisTemplate.delete(type.getKey(param));
	}
}
