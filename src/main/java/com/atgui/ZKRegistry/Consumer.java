package com.atgui.ZKRegistry;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * @Author 贾
 * @Date 2020/9/1522:25
 */
public class Consumer {

    private static String connect = "192.168.37.144:2181,192.168.37.145:2181,192.168.37.146:2181";

    private static int timeOut = 2000;
    private static ZooKeeper zkclient = null;
    public static final String root = "/servers";

    public static void main(String[] args)throws Exception {
       zkclient =  getZKClient();
        //获取服务器列表
        getServers();
        //
        dobussiness();
    }

    private static void dobussiness() {
        System.out.println("consumer 处理业务了 " );
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getServers() throws Exception {
        List<String> children = zkclient.getChildren(root, true);
        for (String child : children) {
            byte[] data = zkclient.getData(root + "/" + child, false, null);
            System.out.println("登录获取服务端列表" + child+"/"+new String(data));
        }
    }

    public static ZooKeeper getZKClient() throws Exception {
        return new ZooKeeper(connect, timeOut, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType()+"---->"+watchedEvent.getPath());
                try {
                    List<String> children = zkclient.getChildren(root, true);
                    for (String child : children) {
                        byte[] data = zkclient.getData(root + "/" + child, false, null);
                        System.out.println("有服务器退出获取服务端列表" + child+"/"+new String(data));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
