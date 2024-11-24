import java.util.concurrent.*;
import java.util.*;

public class CustomCache<K, V> {
    private final Map<K, V> cache = new ConcurrentHashMap<>();
    private final Map<K, Long> expiryTimes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final long defaultExpiryDuration; 
    public CustomCache(long defaultExpiryDuration) {
        this.defaultExpiryDuration = defaultExpiryDuration;

        
        scheduler.scheduleAtFixedRate(this::removeExpiredEntries, 1, 1, TimeUnit.SECONDS);
    }




    public void put(K key, V value, long expiryDuration) {
        cache.put(key, value);
        expiryTimes.put(key, System.currentTimeMillis() + expiryDuration);
    }

    
    public void put(K key, V value) {
        put(key, value, defaultExpiryDuration);
    }
    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null; 
        }
        if (isExpired(key)) {
            remove(key);
            return null;
        }
        return cache.get(key);
    }
    public void remove(K key) {
        cache.remove(key);
        expiryTimes.remove(key);
    }
    private boolean isExpired(K key) {
        Long expiryTime = expiryTimes.get(key);
        return expiryTime != null && System.currentTimeMillis() > expiryTime;
    }
    private void removeExpiredEntries() {
        Iterator<Map.Entry<K, Long>> iterator = expiryTimes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, Long> entry = iterator.next();
            if (System.currentTimeMillis() > entry.getValue()) {
                cache.remove(entry.getKey());
                iterator.remove();
            }
        }
    }
    public void shutdown() {
        scheduler.shutdown();
    }
        }