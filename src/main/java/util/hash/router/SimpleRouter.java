package util.hash.router;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.hash.algorithm.HashAlgorithm;
import util.hash.node.PhysicalNode;

public class SimpleRouter implements Router {
  private final List<PhysicalNode> nodes;
  private final HashAlgorithm hashAlgorithm;

  public SimpleRouter(Collection<PhysicalNode> node, HashAlgorithm hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
    this.nodes = new ArrayList<>(node);
  }

  @Override
  public String get(String key) {
    if (nodes.isEmpty()) {
      return null;
    }
    Long hash = hashAlgorithm.hash(key);

    int index = (int) (hash % nodes.size());
    PhysicalNode node = nodes.get(index);

    return node.get(key);
  }

  @Override
  public void addNode(PhysicalNode node) {
    if (nodes.contains(node)) {
      return;
    }
    nodes.add(node);
  }

  @Override
  public void removeNode(PhysicalNode node) {
    nodes.remove(node);
  }

  public List<PhysicalNode> getNodes() {
    return nodes;
  }
}
