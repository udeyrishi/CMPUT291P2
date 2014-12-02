public class KVPair<K,V>{
    public KVPair(K key, V val) {
        this.key = key;
        this.value = val;
    }

    K key;
    V value;

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getKey()+"\n"+getValue();
    }
}
