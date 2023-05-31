package io.apodemas.around.engine;

import io.apodemas.around.dag.DAGEngine;
import io.apodemas.around.dag.Graph;
import io.apodemas.around.engine.com.ListAssembler;
import io.apodemas.around.engine.com.ListKeyExtractor;
import io.apodemas.around.engine.executor.Assembler;
import io.apodemas.around.engine.executor.Extractor;
import io.apodemas.around.engine.executor.Getter;
import io.apodemas.around.engine.executor.Provider;
import io.apodemas.around.engine.task.SyncContext;
import io.apodemas.around.engine.task.TaskVisitor;
import io.apodemas.around.engine.task.ResourceType;
import io.apodemas.around.engine.task.TaskExecutor;
import io.apodemas.around.mock.Org;
import io.apodemas.around.mock.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
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
        final MockResource userIdRes = new MockResource("userId");
        final MockResource userRes = new MockResource("user");

        // build executors
        final Provider<MockResource, List<Org>> v1 = new Provider<>(orgRes, this::getOrgList);
        final ListKeyExtractor<Org, Long> userIdExtractor = new ListKeyExtractor<>();
        userIdExtractor.add(Org::getCreatorId);
        userIdExtractor.add(Org::getOperatorId);
        final Extractor<MockResource, List<Org>, List<Long>> v2 = new Extractor<>(orgRes, userIdRes, userIdExtractor);
        final Extractor<MockResource, List<Long>, List<User>> v3 = new Extractor<>(userIdRes, userRes, this::fetchUser);
        final ListAssembler<User, Org, Long> userAssembler = new ListAssembler<>(User::getId);
        userAssembler.add(Org::getCreatorId, (user, org) -> {
            org.setCreatorName(user.getName());
        });
        userAssembler.add(Org::getOperatorId, (user, org) -> {
            org.setOperatorName(user.getName());
        });
        final Assembler<MockResource, List<User>, List<Org>> v4 = new Assembler<>(userRes, orgRes, userAssembler);
        final Getter<MockResource, List<Org>> v5 = new Getter<>(orgRes);

        // build and execute dag engine
        final TaskVisitor<MockResource> visitor = new TaskVisitor<>(new SyncContext<>());
        final Graph<TaskExecutor<MockResource>> graph = new Graph<>();
        graph.addEdge(v1, v2);
        graph.addEdge(v2, v3);
        graph.addEdge(v3, v4);
        graph.addEdge(v4, v5);
        final DAGEngine<TaskExecutor<MockResource>> dag = graph.toDAG();
        dag.concurrentTraverse(visitor, Executors.newFixedThreadPool(3));

        final List<Org> orgList = v5.getData();
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
