package util.hash.router;

import util.hash.node.Node;
import util.hash.node.PhysicalNode;

public interface Router {
  public String get(String key);
  public void addNode(PhysicalNode node);
  public void removeNode(PhysicalNode node);
}
