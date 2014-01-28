package com.jd.exec;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ExpeBasic extends Configured implements Tool {
	
//	private String HOST_NAME = "master.slave"; //���ؼ�Ⱥ
	private String HOST_NAME = "hadoop-master.360buy.com"; //������Ⱥ
	private String FS_DEFAULT_NAME = String.format("hdfs://%s:8020/", HOST_NAME);
	private String MAPRED_JOB_TRACKER = String.format("%s:8021", HOST_NAME);
	
	public static class MapClass extends Mapper<LongWritable, Text, TextPair, Text>{
		@Override
		public void map(LongWritable key ,Text value ,Context context){
			
			String[] values = value.toString().split("\t");
			
			TextPair outKey = new TextPair();
			outKey.set(new Text(values[0]), new Text(values[1]));
			try {
				System.out.println(String.format("%s %s", outKey ,value));
				context.write(outKey, value);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			args = new String[]{"/user/huanghu/exec" ,"/user/huanghu/exec/output"}; 
			int res = ToolRunner.run(new Configuration(), new ExpeBasic() ,args);
			System.exit(res);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		conf.set("fs.default.name", FS_DEFAULT_NAME);
		conf.set("mapred.job.tracker", MAPRED_JOB_TRACKER);
		conf.set("mapred.job.queue.name", "erpmerge");
		conf.set("hadoop.job.user","huanghu");
		
		Job job = new Job(conf ,"RPCtest");
		job.setJarByClass(ExpeBasic.class);
		((JobConf)job.getConfiguration()).setJar("D:\\java\\program\\hdfsTool\\target\\hdfsTool-0.0.1-SNAPSHOT.jar");
		
		Path in = new Path(args[0]);
		Path out = new Path(args[1]);
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		
		job.setMapperClass(MapClass.class);
		job.setOutputKeyClass(TextPair.class);
		job.setNumReduceTasks(1);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
		return 0;
	}

}
