// This file is licensed under the Elastic License 2.0. Copyright 2021-present, StarRocks Limited.

package com.starrocks.catalog;

import com.starrocks.analysis.AccessTestUtil;
import com.starrocks.analysis.Analyzer;
import com.starrocks.common.DdlException;
import com.starrocks.persist.gson.GsonUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JDBCResourceTest {
    private Analyzer analyzer;

    @Before
    public void setUp() {
        analyzer = AccessTestUtil.fetchAdminAnalyzer(true);
    }

    private Map<String, String> getMockConfigs() {
        Map<String, String> configs = new HashMap<>();
        configs.put("user", "user");
        configs.put("password", "password");
        configs.put("driver", "driver");
        configs.put("jdbc_uri", "jdbc:postgresql://host1:port1,host2:port2/");
        return configs;
    }

    @Test
    public void testSerialization() throws Exception {
        JDBCResource resource0 = new JDBCResource("jdbc_resource_test", getMockConfigs());

        String json = GsonUtils.GSON.toJson(resource0);
        Resource resource1 = GsonUtils.GSON.fromJson(json, Resource.class);

        Assert.assertTrue(resource1 instanceof JDBCResource);
        Assert.assertEquals(resource0.getName(), resource1.getName());
        Assert.assertEquals(resource0.getProperty(JDBCResource.DRIVER), ((JDBCResource) resource1).getProperty(JDBCResource.DRIVER));
        Assert.assertEquals(resource0.getProperty(JDBCResource.URI), ((JDBCResource) resource1).getProperty(JDBCResource.URI));
        Assert.assertEquals(resource0.getProperty(JDBCResource.USER), ((JDBCResource) resource1).getProperty(JDBCResource.USER));
        Assert.assertEquals(resource0.getProperty(JDBCResource.PASSWORD), ((JDBCResource) resource1).getProperty(JDBCResource.PASSWORD));
    }

    @Test(expected = DdlException.class)
    public void testWithoutDriver() throws Exception {
        Map<String, String> configs = getMockConfigs();
        configs.remove(JDBCResource.DRIVER);
        JDBCResource resource = new JDBCResource("jdbc_resource_test");
        resource.setProperties(configs);
    }

    @Test(expected = DdlException.class)
    public void testWithoutURI() throws Exception {
        Map<String, String> configs = getMockConfigs();
        configs.remove(JDBCResource.URI);
        JDBCResource resource = new JDBCResource("jdbc_resource_test");
        resource.setProperties(configs);
    }

    @Test(expected = DdlException.class)
    public void testWithoutUser() throws Exception {
        Map<String, String> configs = getMockConfigs();
        configs.remove(JDBCResource.USER);
        JDBCResource resource = new JDBCResource("jdbc_resource_test");
        resource.setProperties(configs);
    }

    @Test(expected = DdlException.class)
    public void testWithoutPassword() throws Exception {
        Map<String, String> configs = getMockConfigs();
        configs.remove(JDBCResource.PASSWORD);
        JDBCResource resource = new JDBCResource("jdbc_resource_test");
        resource.setProperties(configs);
    }

    @Test(expected = DdlException.class)
    public void testWithUnknownProperty() throws Exception {
        Map<String, String> configs = getMockConfigs();
        configs.put("xxx", "xxx");
        JDBCResource resource = new JDBCResource("jdbc_resource_test");
        resource.setProperties(configs);
    }
}
