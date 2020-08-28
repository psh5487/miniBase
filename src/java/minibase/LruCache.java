package minibase;

import java.util.*;


/**
 * @param <K> Type of the Cache Key
 * @param <V> Type of the Cache Elements
 */
public class LruCache <K, V> {
	private int capacity;
    	private LinkedHashMap<K, V> map;
	
	/**
	 * Constructor
	 * @param capacity
	 */
	public LruCache (int capacity) {
	  // TODO: some code goes here 
	  this.capacity = capacity;
          this.map = new LinkedHashMap<K, V>();
	}

	/**
     	* @param key
     	* @return True if Cache contains key k
     	*/
    	public boolean containsKey(K key){
          return map.containsKey(key);
    	}

	public boolean isCached(K key) {
	  // TODO: some code goes here 
	  return false;
	}

	public synchronized V get (K key) {
	  // TODO: some code goes here 
	  V value = this.map.get(key);
          if (value != null) {
            this.put(key, value);
          }
          return value;
	}

	public synchronized void put (K key, V value) {
	  // TODO: some code goes here 
	  if (this.map.containsKey(key)) {
            this.map.remove(key);
          } else if (this.map.size() == this.capacity) {
            this.evict();
          }
          map.put(key, value);
	}

	public V evict() {
	  // TODO: some code goes here 
	  Iterator<K> it = this.map.keySet().iterator();
          K k = it.next();
          V v = map.get(k);
          it.remove();
          return v;
	}

	public int size() {
	  // TODO: some code goes here 
	  return map.size();
	}

	/**
   	 * @return The iterator to key set of this cache
     	 */
    	public Iterator<K> keySet(){
          return this.map.keySet().iterator();
    	}
    
    	/**
     	 * @return The iterator to values of this cache
     	 */
    	public Iterator<V> values(){
          return this.map.values().iterator();
    	}
    
    	/**
     	 * Removes element with given key
     	 * @param key The key of element to be removed
     	 */
    	public synchronized void remove(K key){
          this.map.remove(key);
    	}
	
	public Iterator<V> iterator() {
	  // TODO: some code goes here,,, please implement freely! you can remove and make new method 
	  return null;
	}


}
