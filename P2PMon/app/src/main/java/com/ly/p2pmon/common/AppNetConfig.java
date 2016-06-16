package com.ly.p2pmon.common;

/**
 * Created by 12758 on 2016/5/7.
 * 配置程序中所有程序的接口请求
 */
public class AppNetConfig {
    public static final String HOST = "192.168.1.127";
    public static final String BASEURL = "http://"+ HOST+":8080/P2PInvest/";
    public static final String LOGIN = BASEURL + "login";
    public static final String PRODUCT = BASEURL+"product";
    public static final String INDEX = BASEURL+"index";
}
