package co.jinear.core.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisPrefixedKeySerializer implements RedisSerializer<String> {

    private final String prefix;
    private final StringRedisSerializer serializer = new StringRedisSerializer();

    public RedisPrefixedKeySerializer(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public byte[] serialize(String key) throws SerializationException {
        if (key == null) {
            return serializer.serialize(null);
        }
        String prefixedKey = prefix + key;
        return serializer.serialize(prefixedKey);
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        String key = serializer.deserialize(bytes);
        if (key != null && key.startsWith(prefix)) {
            return key.substring(prefix.length());
        }
        return key;
    }
}
