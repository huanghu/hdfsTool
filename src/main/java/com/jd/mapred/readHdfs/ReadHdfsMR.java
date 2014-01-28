package com.jd.mapred.readHdfs;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class ReadHdfsMR {
	static Logger logger = Logger.getLogger(ReadHdfsMR.class);
	public static class ReadHdfsMapper extends Mapper<LongWritable, Text, NullWritable, NullWritable>{
		  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			  logger.info("value " + value);
			  context.write(NullWritable.get(), NullWritable.get());
		  }
	}
	
}
