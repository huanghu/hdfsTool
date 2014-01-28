package com.jd.mapred.readHdfs;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class ReadHdfsInputFormat extends FileInputFormat<LongWritable, Text> {

  public RecordReader<LongWritable, Text> 
    createRecordReader(InputSplit split,
                       TaskAttemptContext context) {
	  String delimiter = context.getConfiguration().get(
	  	"textinputformat.record.delimiter");
	  byte[] recordDelimiterBytes = null;
	  byte[] b = null;
	  if (null != delimiter)
//		  recordDelimiterBytes = delimiter.getBytes();
		  b = new byte[1];
	  	  b[0] = 2; //byte 1=\001 2=\002
		  recordDelimiterBytes = b;
	  
	  return new LineRecordReader(recordDelimiterBytes);
  }
	  
  @Override
  protected boolean isSplitable(JobContext context, Path file) {
    CompressionCodec codec = 
      new CompressionCodecFactory(context.getConfiguration()).getCodec(file);
    return codec == null;
  }

}
