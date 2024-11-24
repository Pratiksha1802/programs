public class CacheTest {
    public static void main(String[] args) throws InterruptedException {
       
        CustomCache<String, String> cache = new CustomCache<>(5000);

        cache.put("key1", "value1");
        cache.put("key2", "value2", 2000);

        
        System.out.println("key1: " + cache.get("key1")); 
        System.out.println("key2: " + cache.get("key2")); 

        
        Thread.sleep(3000);

        System.out.println("key1: " + cache.get("key1"));
        System.out.println("key2: " + cache.get("key2")); 

       
        cache.put("key1", "newValue1");
        System.out.println("key1: " + cache.get("key1")); 

      
        cache.remove("key1");
        System.out.println("key1: " + cache.get("key1")); 

       
        cache.shutdown();
    }
}
