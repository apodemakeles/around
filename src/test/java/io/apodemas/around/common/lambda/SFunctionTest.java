package io.apodemas.around.common.lambda;

import io.apodemas.around.mock.Org;
import io.apodemas.around.mock.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author: Cao Zheng
 * @date: 2023/5/29
 * @description:
 */
public class SFunctionTest {

    @Test
    public void get_method_name_should_work() {
        SFunction<Org, Long> getOrgId = Org::getId;
        Assert.assertEquals("getId", getOrgId.getMethodName());
        final SFunction<Org, String> getOrgName = Org::getName;
        Assert.assertEquals("getName", getOrgName.getMethodName());
        final SFunction<User, Long> getUserId = User::getId;
        Assert.assertEquals("getId", getUserId.getMethodName());
    }
}
