package io.apodemas.around.engine;

import io.apodemas.around.engine.rule.AssembleRule;
import io.apodemas.around.engine.rule.JoinRule;
import io.apodemas.around.engine.rule.Rules;
import io.apodemas.around.mock.Org;
import io.apodemas.around.mock.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author: Cao Zheng
 * @date: 2023/7/27
 * @description:
 */
public class EngineTest {

    @Test
    public void engine_resolver_should_work() {
        final ExecutorService executor = Executors.newFixedThreadPool(3);
        final EngineSettings settings = new EngineSettings();
        settings.setExecutor(executor);
        settings.setPartitionSize(1);

        final RuleResolver resolver = new RuleResolver(settings);

        final TypedResource<Org> root = new TypedResource<>(Org.class);
        final TypedResource<User> u1 = new TypedResource<>(User.class, "u1");
        final TypedResource<User> u2 = new TypedResource<>(User.class, "u2");
        final Rules<Org> rules = new Rules<>();
        rules.root(root);

        final JoinRule<Org, User, Long> j1 = new JoinRule<>();
        j1.source(root);
        j1.dest(u1);
        j1.sourceKeyExtractor(Org::getCreatorId);
        j1.fetcher(this::fetchUser);
        rules.add(j1);

        final JoinRule<Org, User, Long> j2 = new JoinRule<>();
        j2.source(root);
        j2.dest(u2);
        j2.sourceKeyExtractor(Org::getOperatorId);
        j2.fetcher(this::fetchUser);
        rules.add(j2);

        final AssembleRule<Org, User, Long> a1 = new AssembleRule<>();
        a1.root(root);
        a1.source(u1);
        a1.rootKeyExtractor(Org::getCreatorId);
        a1.sourceKeyExtractor(User::getId);
        a1.assembler((user, org) -> org.setCreatorName(user.getName()));
        rules.add(a1);

        final AssembleRule<Org, User, Long> a2 = new AssembleRule<>();
        a2.root(root);
        a2.source(u2);
        a2.rootKeyExtractor(Org::getOperatorId);
        a2.sourceKeyExtractor(User::getId);
        a2.assembler((user, org) -> org.setOperatorName(user.getName()));
        rules.add(a2);

        final Engine<Org> engine = resolver.resolve(rules);
        final List<Org> orgList = getOrgList();
        engine.apply(orgList);

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
}
