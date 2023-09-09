package util.hash.router;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import util.hash.algorithm.HashAlgorithm;
import util.hash.node.PhysicalNode;
import util.hash.node.VirtualNode;

public class ConsistentRouter implements Router {

  private final TreeMap<Long, VirtualNode> nodes;

  private final HashAlgorithm hashAlgorithm;

  private final int virtualNodeCount;

  public ConsistentRouter(Collection<PhysicalNode> physicalNodes, int virtualNodeCount,
      HashAlgorithm hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
    this.nodes = new TreeMap<Long, VirtualNode>();
    this.virtualNodeCount = virtualNodeCount;

    for (PhysicalNode physicalNode : physicalNodes) {
      addNode(physicalNode);
    }
  }

  public List<PhysicalNode> getPhysicalNodes() {
    return nodes.values().stream().map(VirtualNode::getPhysicalNode).distinct().toList();
  }

  public List<VirtualNode> getVirtualNodes() {
    return nodes.values().stream().toList();
  }


  @Override
  public String get(String key) {
    if (nodes.isEmpty()) {
      return null;
    }

    Long hash = this.hashAlgorithm.hash(key);
    SortedMap<Long, VirtualNode> biggerTailMap = nodes.tailMap(hash);

    Long nodeHash = biggerTailMap.isEmpty() ? nodes.firstKey() : biggerTailMap.firstKey();

    VirtualNode virtualNode = nodes.get(nodeHash);

    return virtualNode.get(key);
  }

  @Override
  public void addNode(PhysicalNode node) {
    for (int i = 0; i < virtualNodeCount; i++) {
      addVirtualNode(node, i);
    }
  }

  private void addVirtualNode(PhysicalNode physicalNode, int replicaIndex) {
    VirtualNode virtualNode = new VirtualNode(physicalNode, replicaIndex);
    nodes.put(hashAlgorithm.hash(virtualNode.getKey()), virtualNode);
  }

  @Override
  public void removeNode(PhysicalNode node) {
    for (int i = 0; i < virtualNodeCount; i++) {
      removeVirtualNode(node, i);
    }
  }

  private void removeVirtualNode(PhysicalNode physicalNode, int replicaIndex) {
    VirtualNode virtualNode = new VirtualNode(physicalNode, replicaIndex);
    nodes.remove(hashAlgorithm.hash(virtualNode.getKey()));
  }

  public int getVirtualNodeCount() {
    return virtualNodeCount;
  }
}
