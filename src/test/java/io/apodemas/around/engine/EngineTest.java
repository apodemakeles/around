package io.apodemas.around.engine;

import io.apodemas.around.dag.DAG;
import io.apodemas.around.dag.Graph;
import io.apodemas.around.engine.com.ForkFetcher;
import io.apodemas.around.engine.com.ListAssembler;
import io.apodemas.around.engine.com.ListKeyExtractor;
import io.apodemas.around.engine.executor.AssembleExecutor;
import io.apodemas.around.engine.executor.FetchExecutor;
import io.apodemas.around.engine.executor.Provider;
import io.apodemas.around.engine.task.SyncContext;
import io.apodemas.around.engine.task.TaskVisitor;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskAsyncExecutor;
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
public class EngineTest {

    @Test
    public void mock_org_user_join_should_work() {
        // resources
        final MockResource orgRes = new MockResource("org");
        final MockResource userRes = new MockResource("user");

        // thread pool
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        // build executors
        List<Function<Org, Long>> extractors = new ArrayList<>();
        extractors.add(Org::getCreatorId);
        extractors.add(Org::getOperatorId);
        final ForkFetcher<Org, User, Long> forkFetcher = new ForkFetcher<>(extractors, this::fetchUser, 1);
        final FetchExecutor<MockResource, Org, User, Long> v1 = new FetchExecutor<>(orgRes, userRes, forkFetcher, executor);

        final ListAssembler<Org, User, Long> creatorAssembler = new ListAssembler<>(Org::getCreatorId);
        creatorAssembler.add(User::getId, (org, user) -> org.setCreatorName(user.getName()));
        final AssembleExecutor<MockResource, Org, User, Long> v2 = new AssembleExecutor<>(orgRes, userRes, creatorAssembler);

        final ListAssembler<Org, User, Long> operatorAssembler = new ListAssembler<>(Org::getOperatorId);
        operatorAssembler.add(User::getId, (org, user) -> org.setOperatorName(user.getName()));
        final AssembleExecutor<MockResource, Org, User, Long> v3 = new AssembleExecutor<>(orgRes, userRes, operatorAssembler);


        // build dag engine
        final SyncContext<ResourceType> ctx = new SyncContext<>();
        ctx.set(orgRes, getOrgList());

        final TaskVisitor<MockResource> visitor = new TaskVisitor(ctx);
        final Graph<TaskAsyncExecutor<MockResource>> graph = new Graph<>();
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v3);
        final DAG<TaskAsyncExecutor<MockResource>> dag = graph.toDAG();

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

    public static class MockResource implements ResourceType {
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
    }

}
