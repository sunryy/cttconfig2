package com.ctg.mes.config.console.util;

import com.ctg.mes.config.common.util.JsonUtil;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public class ZookeeperUtils {
    private final static Logger log = LoggerFactory.getLogger(ZookeeperUtils.class);

    public static byte[] getBytes(CuratorFramework zkClient, String path)
            throws Exception {
        if (zkClient.checkExists().forPath(path) == null) {
            log.debug("checkExists forPath is null . path = {}", path);
            return null;
        }
        return zkClient.getData().forPath(path);
    }

    /**
     * 从zk的指定path上读取数据并转换为实体类
     */
    public static <T> T getEntity(CuratorFramework zkClient, String path, Class<T> classOfT)
            throws Exception {
        return JsonUtil.fromJson(getDataAsString(zkClient, path), classOfT);
    }

    /**
     * 获取指定path的所有子节点，并将其数据转为为实体类
     */
    public static <T> Map<String, T> getChildrenEntiy(CuratorFramework zkClient, String path,
                                                      Class<T> tClass) throws Exception {
        Map<String, T> result = Maps.newTreeMap();
        if (zkClient.checkExists().forPath(path) == null) {
            return result;
        }

        List<String> childrenPathList = zkClient.getChildren().forPath(path);
        for (String childrenPath : childrenPathList) {
            result.put(childrenPath,
                    ZookeeperUtils.getEntity(zkClient, ZKPaths.makePath(path, childrenPath), tClass));
        }
        return result;
    }

    /**
     * 将传入的实体类数据持久化到zk的指定路径上，如果路径不存在，则新建一个永久节点再存储
     */
    public static <T> void saveEntity(CuratorFramework zkClient, String path, T objectOfT)
            throws Exception {
        byte[] data = toJsonBytes(objectOfT);
        saveData(zkClient, path, data);
    }

    public static void saveData(CuratorFramework zkClient, String path, byte[] data)
            throws Exception {
        if (null == zkClient.checkExists().forPath(path)) {
            zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, data);
        } else {
            zkClient.setData().forPath(path, data);
        }
    }

    /**
     * 将传入的实体类数据持久化到zk的指定路径上，如果路径不存在，则新建一个临时节点再存储
     */
    public static <T> void saveEntityEphemeral(CuratorFramework zkClient, String path, T objectOfT)
            throws Exception {
        byte[] data = toJsonBytes(objectOfT);
        if (null == zkClient.checkExists().forPath(path)) {
            zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, data);
        } else {
            zkClient.setData().forPath(path, data);
        }
    }

    /**
     * 将传入的实体类数据持久化到zk的指定路径上，如果路径不存在，则新建一个临时节点再存储
     */
    public static <T> void saveEntityEphemeralWithoutParents(CuratorFramework zkClient, String path, T objectOfT)
            throws Exception {
        byte[] data = toJsonBytes(objectOfT);
        if (null == zkClient.checkExists().forPath(path)) {
            zkClient.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, data);
        } else {
            zkClient.setData().forPath(path, data);
        }
    }

    /**
     * 将对象转换为byte数组
     */
    public static <T> byte[] toJsonBytes(T objectOfT) {
        return JsonUtil.toJsonExcludeField(objectOfT).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 尝试删除指定节点以及子节点
     */
    public static void tryDeleteNode(CuratorFramework zkClient, String path) throws Exception {
        if (null != zkClient.checkExists().forPath(path)) {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
        }
    }

    /**
     * 删除并重新创建节点
     */
    public static <T> void recreateNode(CuratorFramework zkClient, String path, T objectOfT)
            throws Exception {
        tryDeleteNode(zkClient, path);
        saveEntity(zkClient, path, objectOfT);
    }

    /**
     * 尝试删除指定路径的所有子节点（路径本身对应的节点不删除）
     */
    public static void tryDeleteChildren(CuratorFramework zkClient, String path) throws Exception {
        if (null != zkClient.checkExists().forPath(path)) {
            for (String childrenPath : zkClient.getChildren().forPath(path)) {
                zkClient.delete()
                        .deletingChildrenIfNeeded()
                        .forPath(ZKPaths.makePath(path, childrenPath));
            }
        }
    }

    /**
     * 从zk的指定节点读取string
     */
    public static String getDataAsString(CuratorFramework zkClient, String path) throws Exception {
        byte[] data = getData(zkClient, path);
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * 从zk的指定节点读取数据
     */
    public static byte[] getData(CuratorFramework zkClient, String path) throws Exception {
        return zkClient.getData().forPath(path);
    }

    /**
     * 解释gson,并转成对象
     */
    public static <T> T getEntity(byte bytes[], Class<T> c) {
        return JsonUtil.fromJson(new String(bytes, StandardCharsets.UTF_8), c);
    }

    /**
     * 判定是否存在路径为path的元素
     */
    public static boolean pathExist(CuratorFramework zkClient, String path) throws Exception {
        return zkClient.checkExists().forPath(path) != null;
    }
}
