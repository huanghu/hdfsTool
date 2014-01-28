package com.jd.hdfsTool;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.jd.mapred.readHdfs.ReadHdfsInputFormat;
import com.jd.mapred.readHdfs.ReadHdfsMR.ReadHdfsMapper;
import com.jd.utils.ICommon;

public class ReadHdfs extends Configured implements Tool  {
	public static void main(String[] args){
		// TODO Auto-generated method stub
		try {
			args = new String[]{"/user/huanghu/aa.txt" ,"/user/huanghu/temp_data/readHdfs"}; 
			int res = ToolRunner.run(new Configuration(), new ReadHdfs() ,args);
			System.exit(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		conf.set("fs.default.name", ICommon.FS_DEFAULT_NAME);
		conf.set("mapred.job.tracker", ICommon.MAPRED_JOB_TRACKER);
		conf.set("mapred.job.queue.name", "erpmerge");
		conf.set("hadoop.job.user","huanghu");
		//分隔符
		String delimiter = "2";
		conf.set("textinputformat.record.delimiter", delimiter);
		byte[] delimiters = delimiter.getBytes();
		for (byte delimit : delimiters) {
			System.out.println("de " + delimit);
		}
		
		Job job = new Job(conf ,"ReadHdfs");
		job.setJarByClass(ReadHdfs.class);
		((JobConf)job.getConfiguration()).setJar("D:\\Java\\program\\hdfsTool\\target\\hdfsTool-0.0.1-SNAPSHOT.jar");
		
		Path in = new Path(args[0]);
		Path out = new Path(args[1]);
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		
		this.clear(conf, out);
		
		job.setMapperClass(ReadHdfsMapper.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setInputFormatClass(ReadHdfsInputFormat.class);
		
		job.setNumReduceTasks(0);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		return 0;
	}
	
	private void clear(Configuration conf ,Path out) throws IOException{
		FileSystem fs = FileSystem.get(conf);
		fs.delete(out ,true);
	}
		
}
