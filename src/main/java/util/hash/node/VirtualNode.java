package util.hash.node;

public class VirtualNode implements Node {
  private final PhysicalNode physicalNode;

  private final int replicaIndex;

  public VirtualNode(PhysicalNode physicalNode, int replicaIndex) {
    this.physicalNode = physicalNode;
    this.replicaIndex = replicaIndex;
  }

  @Override
  public String getKey() {
    return physicalNode.getKey() + "-" + replicaIndex;
  }

  @Override
  public String get(String key) {
    return physicalNode.get(key);
  }
  @Override
  public boolean equals(Object o) {
    if (o == this) return true; // 1
    if (!(o instanceof VirtualNode p)) return false; // 2

    return this.getKey().equals(p.getKey());
  }

  @Override
  public int hashCode() {
    return this.getKey().hashCode();
  }

  public boolean isVirtualOf(PhysicalNode physicalNode) {
    return physicalNode.getKey().equals(this.physicalNode.getKey());
  }

  public PhysicalNode getPhysicalNode() {
    return physicalNode;
  }

  public int getReplicaIndex() {
    return replicaIndex;
  }
}
