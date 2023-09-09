package util.hash.router;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.hash.algorithm.MD5Hash;
import util.hash.node.PhysicalNode;
import util.hash.node.VirtualNode;

class ConsistentRouterTest {
  private ConsistentRouter consistentRouter;

  @BeforeEach
  void setUp() {
    consistentRouter = new ConsistentRouter(List.of(), 5, new MD5Hash());
  }

  @Test
  void 노드1개추가확인() {
    consistentRouter.addNode(new PhysicalNode("test1"));

    List<PhysicalNode> nodes = consistentRouter.getPhysicalNodes();

    assertTrue(nodes.contains(new PhysicalNode("test1")));
  }

  @Test
  void 가상노드10개추가확인() {
    consistentRouter.addNode(new PhysicalNode("test1"));

    List<VirtualNode> nodes = consistentRouter.getVirtualNodes();

    assertEquals(10, nodes.size());
  }

  @Test
  void 노드중복확인() {
    consistentRouter.addNode(new PhysicalNode("test1"));
    consistentRouter.addNode(new PhysicalNode("test1"));

    List<PhysicalNode> nodes = consistentRouter.getPhysicalNodes();
    List<VirtualNode> virtualNodes = consistentRouter.getVirtualNodes();

    assertEquals(1, nodes.size());
    assertEquals(10, virtualNodes.size());
  }

  @Test
  void 노드4개추가(){
    for (int i = 0; i < 4; i++) {
      consistentRouter.addNode(new PhysicalNode("test" + i));
    }

    List<PhysicalNode> nodes = consistentRouter.getPhysicalNodes();
    List<VirtualNode> virtualNodes = consistentRouter.getVirtualNodes();

    assertEquals(4, nodes.size());
    assertEquals(40, virtualNodes.size());
  }

  @Test
  void 노드1개_캐시히트_확인(){
    consistentRouter.addNode(new PhysicalNode("test1"));

    consistentRouter.get("testInput1");

    List<PhysicalNode> nodes = consistentRouter.getPhysicalNodes();

    for (PhysicalNode node : nodes) {
      System.out.println("첫번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }

    consistentRouter.get("testInput1");

    for (PhysicalNode node : nodes) {
      System.out.println("두번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }
  }
  @Test
  void 노드4개_캐시히트_확인(){
    for (int i = 0; i < 4; i++) {
      consistentRouter.addNode(new PhysicalNode("hash-table-" + i));
    }

    consistentRouter.get("testInput1");

    List<PhysicalNode> nodes = consistentRouter.getPhysicalNodes();

    for (PhysicalNode node : nodes) {
      System.out.println("첫번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }

    consistentRouter.get("testInput1");

    for (PhysicalNode node : nodes) {
      System.out.println("두번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }
  }

  @Test
  void 노드4개_데이터1000000개_조회_재시도(){
    for (int i = 0; i < 4; i++) {
      consistentRouter.addNode(new PhysicalNode("hash-table-" + i));
    }
    for (int i = 0; i < 1000000; i++) {
      consistentRouter.get("testInput" + i);
    }

    System.out.println("---------------------------------------------");
    System.out.println("ConsistentRouterTest");
    System.out.println("ConsistentRouterTest");
    System.out.println("ConsistentRouterTest");
    System.out.println("---------------------------------------------");
    System.out.println("virtual node size : " + consistentRouter.getVirtualNodeCount());

    List<PhysicalNode> nodes = consistentRouter.getPhysicalNodes();

    for (PhysicalNode node : nodes) {
      System.out.println("첫번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }
    System.out.println("---------------------------------------------");

    for (int i = 0; i < 1000000; i++) {
      consistentRouter.get("testInput" + i);
    }
    for (PhysicalNode node : nodes) {
      System.out.println("두번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clear();
    }
  }

  @Test
  void 노드4개_데이터1000000개_조회_노드1개삭제_재시도(){
    for (int i = 0; i < 4; i++) {
      consistentRouter.addNode(new PhysicalNode("hash-table-" + i));
    }
    var startTime = System.currentTimeMillis();
    for (int i = 0; i < 1000000; i++) {
      consistentRouter.get("testInput" + i);
    }

    List<PhysicalNode> nodes = consistentRouter.getPhysicalNodes();

    System.out.println("---------------------------------------------");
    System.out.println("ConsistentRouterTest");
    System.out.println("ConsistentRouterTest");
    System.out.println("ConsistentRouterTest");
    System.out.println("---------------------------------------------");
    System.out.println("virtual node size : " + consistentRouter.getVirtualNodeCount());
    for (PhysicalNode node : nodes) {
      System.out.println("첫번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }

    consistentRouter.removeNode(new PhysicalNode("hash-table-0"));

    nodes = consistentRouter.getPhysicalNodes();
    System.out.println("---------------------------------------------");
    for (int i = 0; i < 1000000; i++) {
      consistentRouter.get("testInput" + i);
    }
    for (PhysicalNode node : nodes) {
      System.out.println("두번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clear();
    }

    System.out.println("endTime: " + (System.currentTimeMillis()-startTime));
  }

}