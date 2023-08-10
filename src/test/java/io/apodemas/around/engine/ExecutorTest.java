package io.apodemas.around.engine;

import io.apodemas.around.dag.DAG;
import io.apodemas.around.dag.Graph;
import io.apodemas.around.engine.com.ForkJoinFetcher;
import io.apodemas.around.engine.com.ListAssembler;
import io.apodemas.around.engine.node.ListAssembleNode;
import io.apodemas.around.engine.node.ListFetchNode;
import io.apodemas.around.engine.exec.SyncContext;
import io.apodemas.around.engine.exec.NodeVisitor;
import io.apodemas.around.engine.exec.Resource;
import io.apodemas.around.engine.exec.ExecNode;
import io.apodemas.around.mock.Org;
import io.apodemas.around.mock.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class ExecutorTest {

    @Test
    public void mock_org_user_join_should_work() {
        // resources
        final MockResource orgRes = new MockResource("org");
        final MockResource userRes1 = new MockResource("user1");
        final MockResource userRes2 = new MockResource("user2");

        // thread pool
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        // build executors
        List<Function<Org, Long>> extractors = new ArrayList<>();
        extractors.add(Org::getOperatorId);
        final ForkJoinFetcher<Org, User, Long> creatorFetcher = new ForkJoinFetcher<>(Org::getCreatorId, this::fetchUser, 1);
        final ListFetchNode<MockResource, Org, User, Long> v1 = new ListFetchNode<>(orgRes, userRes1, creatorFetcher, executor);

        final ForkJoinFetcher<Org, User, Long> operatorFetcher = new ForkJoinFetcher<>(Org::getOperatorId, this::fetchUser, 1);
        final ListFetchNode<MockResource, Org, User, Long> v2 = new ListFetchNode<>(orgRes, userRes2, operatorFetcher, executor);


        final ListAssembler<User, Org, Long> creatorAssembler = new ListAssembler<>(User::getId, Org::getCreatorId, (user, org) -> org.setCreatorName(user.getName()));
        final ListAssembleNode<MockResource, User, Org, Long> v3 = new ListAssembleNode<>(userRes1, orgRes, creatorAssembler);

        final ListAssembler<User, Org, Long> operatorAssembler = new ListAssembler<>(User::getId, Org::getOperatorId, (user, org) -> org.setOperatorName(user.getName()));
        final ListAssembleNode<MockResource, User, Org, Long> v4 = new ListAssembleNode<>(userRes2, orgRes, operatorAssembler);


        // build dag engine
        final SyncContext<Resource> ctx = new SyncContext<>();
        ctx.set(orgRes, getOrgList());

        final NodeVisitor<MockResource> visitor = new NodeVisitor(ctx);
        final Graph<ExecNode<MockResource>> graph = new Graph<>();
        graph.addEdge(v1, v3);
        graph.addEdge(v2, v4);
        final DAG<ExecNode<MockResource>> dag = graph.toDAG();

        // run
        dag.concurrentTraverse(visitor);
        final List<Org> orgList = ctx.get(orgRes);

        // assert
        Assert.assertEquals(5, orgList.size());
        final Map<Long, Org> orgMap = orgList.stream().collect(Collectors.toMap(Org::getId, org -> org));
        final Org enterprise = orgMap.get(1L);
        Assert.assertEquals("Tom", enterprise.getCreatorName());
        Assert.assertEquals("Jerry", enterprise.getOperatorName());
        final Org corp = orgMap.get(2L);
        Assert.assertEquals("Jerry", corp.getCreatorName());
        Assert.assertEquals("Lily", corp.getOperatorName());
        final Org shop = orgMap.get(3L);
        Assert.assertNull(shop.getCreatorName());
        Assert.assertEquals("Kitty", shop.getOperatorName());
        final Org workshop = orgMap.get(4L);
        Assert.assertEquals("Kitty", workshop.getCreatorName());
        Assert.assertNull(workshop.getOperatorName());
        final Org depart = orgMap.get(5L);
        Assert.assertEquals("Lily", depart.getCreatorName());
        Assert.assertNull(depart.getOperatorName());

    }

    public List<Org> getOrgList() {
        final List<Org> orgList = new ArrayList<>();
        orgList.add(new Org(1, "Enterprise", 1L, 2L));
        orgList.add(new Org(2, "Corp", 2L, 5L));
        orgList.add(new Org(3, "Shop", null, 3L));
        orgList.add(new Org(4, "Workshop", 3L, null));
        orgList.add(new Org(5, "Depart", 5L, 6L));

        return orgList;
    }

    public List<User> getUser() {
        final List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Tom"));
        userList.add(new User(2, "Jerry"));
        userList.add(new User(3, "Kitty"));
        userList.add(new User(4, "Nobody"));
        userList.add(new User(5, "Lily"));

        return userList;
    }

    public List<User> fetchUser(List<Long> userIds) {
        final Map<Long, User> map = getUser().stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        final List<User> userList = new ArrayList<>();
        for (Long id : userIds) {
            if (map.containsKey(id)) {
                userList.add(map.get(id));
            }
        }
        return userList;
    }

    public static class MockResource implements Resource {
        private String name;

        public MockResource(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MockResource that = (MockResource) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String name() {
            return name;
        }
    }

}
