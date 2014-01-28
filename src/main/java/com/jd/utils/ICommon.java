package com.jd.utils;

public interface ICommon {
	public String HOST_NAME = "hadoop-master.360buy.com"; //������Ⱥ
	public String FS_DEFAULT_NAME = String.format("hdfs://%s:8020/", HOST_NAME);
	public String MAPRED_JOB_TRACKER = String.format("%s:8021", HOST_NAME);
}
