package util.hash.router;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.hash.algorithm.MD5Hash;
import util.hash.node.PhysicalNode;

class SimpleRouterTest {
  private SimpleRouter simpleRouter;

  @BeforeEach
  void setUp() {
    simpleRouter = new SimpleRouter(List.of(), new MD5Hash());
  }

  @Test
  void 노드추가() {
    simpleRouter.addNode(new PhysicalNode("test1"));

    List<PhysicalNode> nodes = simpleRouter.getNodes();

    assertTrue(nodes.contains(new PhysicalNode("test1")));
  }

  @Test
  void 노드중복() {
    simpleRouter.addNode(new PhysicalNode("test1"));
    simpleRouter.addNode(new PhysicalNode("test1"));

    List<PhysicalNode> nodes = simpleRouter.getNodes();

    assertEquals(1, nodes.size());
  }
  @Test
  void 노드4개추가() {
    for (int i = 0; i < 4; i++) {
      simpleRouter.addNode(new PhysicalNode("test" + i));
    }

    List<PhysicalNode> nodes = simpleRouter.getNodes();

    assertEquals(4, nodes.size());
  }
  @Test
  void 노드4개추가_데이터1000000개추가_재시도(){
    for (int i = 0; i < 4; i++) {
      simpleRouter.addNode(new PhysicalNode("test" + i));
    }
    for (int i = 0; i < 1000000; i++) {
      simpleRouter.get("testInput" + i);
    }

    List<PhysicalNode> nodes = simpleRouter.getNodes();

    for (PhysicalNode node : nodes) {
      System.out.println("첫번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }

    for (int i = 0; i < 1000000; i++) {
      simpleRouter.get("testInput" + i);
    }
    for (PhysicalNode node : nodes) {
      System.out.println("두번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clear();
    }

  }
  @Test
  void 노드4개추가_데이터1000000개_조회_노드1개삭제_재시도(){
    for (int i = 0; i < 4; i++) {
      simpleRouter.addNode(new PhysicalNode("test" + i));
    }
    for (int i = 0; i < 1000000; i++) {
      simpleRouter.get("testInput" + i);
    }

    List<PhysicalNode> nodes = simpleRouter.getNodes();

    for (PhysicalNode node : nodes) {
      System.out.println("첫번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clearHit();
      node.clearMiss();
    }
    simpleRouter.removeNode(new PhysicalNode("test0"));

    for (int i = 0; i < 1000000; i++) {
      simpleRouter.get("testInput" + i);
    }
    for (PhysicalNode node : nodes) {
      System.out.println("두번째 시도"+
          node.getNodeKey() + " : " + node.getCacheHit() + " : " + node.getCacheMiss());
      node.clear();
    }
  }
}