package com.atgui.ZKRegistry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @Author 贾
 * @Date 2020/9/1522:22
 */
public class Provider {

    private static String connect = "192.168.37.144:2181,192.168.37.145:2181,192.168.37.146:2181";

    private static int timeOut = 2000;
    private static ZooKeeper zkclient = null;
    public static final String root = "/servers";

    public static void main(String[] args) throws Exception {
        zkclient = getZKClient();
        //项目启动注册
        registerServer(args[0]);
        //业务逻辑
        dobussiness(args[0]);
    }

    public static void dobussiness(String hostName)throws Exception {
        System.out.println("hostName = " + hostName+"-->处理任务了");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void registerServer(String hostName) throws Exception {
        Stat exists = zkclient.exists(root, false);
        if(exists==null){
            zkclient.create(root,"1.0.0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
        String path = zkclient.create(root+"/"+hostName, hostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("path = " + path+"   上线了");
    }

    public static ZooKeeper getZKClient() throws Exception {
        return new ZooKeeper(connect, timeOut, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType()+"---->"+watchedEvent.getPath());
            }
        });
    }
}
