package util.hash.node;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PhysicalNode implements Node {

  private final String nodeKey;

  private final Map<String, String> cache = new HashMap<>();
  private long cacheHit = 0;
  private long cacheMiss = 0;

  public PhysicalNode(String nodeKey) {
    this.nodeKey = nodeKey;
  }

  @Override
  public String getKey() {
    return nodeKey;
  }

  @Override
  public String get(String key) {
    if (cache.containsKey(key)){
      cacheHit++;
      return cache.get(key);
    }
    cacheMiss++;
    cache.put(key, key);
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true; // 1
    if (!(o instanceof PhysicalNode p)) return false; // 2

    return this.nodeKey.equals(p.nodeKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeKey);
  }

  public void clearHit() {
    cacheHit = 0;
  }
  public void clearMiss() {
    cacheMiss = 0;
  }

  public void clear() {
    cache.clear();
    cacheHit = 0;
    cacheMiss = 0;
  }

  public String getNodeKey() {
    return nodeKey;
  }

  public long getCacheHit() {
    return cacheHit;
  }

  public long getCacheMiss() {
    return cacheMiss;
  }
  private void hitCache() {
    cacheHit++;
  }
  private void missCache() {
    cacheMiss++;
  }
}
