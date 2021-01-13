package com.atgui.zkdemo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.nio.channels.Pipe;
import java.util.List;

/**
 * @Author 贾
 * @Date 2020/9/1521:12
 */
public class ZKDemo {

    private static String connect = "192.168.37.144:2181,192.168.37.145:2181,192.168.37.146:2181";
    private static int timeout = 2000;
    private static ZooKeeper zooKeeper= null;

    //获取zkClient
    @Before
    public void  getZkClient() throws Exception{
        zooKeeper = new ZooKeeper(connect, timeout, new Watcher() {
            //时间出发之后，做出的业务逻辑
            public void process(WatchedEvent watchedEvent) {
                System.out.println("watchedEvent.getType() = " + watchedEvent.getType());
                System.out.println("watchedEvent.getPath() = " + watchedEvent.getPath());
                System.out.println("watchedEvent.getState() = " + watchedEvent.getState());
                System.out.println("watchedEvent.getWrapper() = " + watchedEvent.getWrapper());

                try {
                    //继续注册监听事件
                    List<String> children = zooKeeper.getChildren("/atguigu", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    //创建节点
    @Test
    public void testCreate() throws Exception{
        String path = "/atguigu";
        byte[] data = "atguigu".getBytes();

        String s = zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("s = " + s);
    }

    //创建节点
    @Test
    public void testIsExsit() throws Exception{
        String path = "/atguigu";
        Stat exists = zooKeeper.exists(path, false);
        System.out.println(exists);
    }

    //获取所有子节点
    @Test
    public void getChildNode() throws Exception{
        String path = "/atguigu";
        List<String> children = zooKeeper.getChildren(path, true);
        for (String child : children) {
            System.out.println("child = " + child);
        }
    }

    // 修改节点内容
    @Test
    public void setNode()throws Exception{
        Stat stat = zooKeeper.setData("/atguigu", "i love ".getBytes(), -1);
        System.out.println("stat = " + stat);
    }

    // 获取节点内容
    @Test
    public void getNode()throws Exception{
        byte[] data = zooKeeper.getData("/atguigu", false, null);
        System.out.println("new String(data) = " + new String(data));
    }



}
