package com.jd.hdfsTool;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 指定jars的名称
 * @author huanghu
 *
 */
public class UploadFileSpecifyJar {
	private String HOST_NAME = "hadoop-master.360buy.com";
	private String FS_DEFAULT_NAME = String.format("hdfs://%s:8020/", HOST_NAME);
	private String MAPRED_JOB_TRACKER = String.format("%s:8021", HOST_NAME);
	private String jars = null;
	
	Configuration conf;
	
	public UploadFileSpecifyJar(String jars){
		this.jars = jars;
		conf = new Configuration();
		conf.set("fs.default.name", FS_DEFAULT_NAME);
		conf.set("mapred.job.tracker", MAPRED_JOB_TRACKER);
		conf.set("hadoop.job.user", "huanghu");
	}
	
	public Configuration getConf() {
		return conf;
	}

	/**
	 * args[0] localMainPath
	 * args[1] localSubPath
	 * args[2] hdfsPath
	 * @param args
	 */
	public static void  main(String[] args){
		String mainPath = args[0];
		String subPath = args[1];
		String path = args[2];
		String jars = args[3];
		
		UploadFileSpecifyJar upload = new UploadFileSpecifyJar(jars);
		Configuration conf = upload.getConf();
		
		try {
			FileSystem fs = FileSystem.get(conf);

			
			Path p = new Path(path);
			
			//初始化hdfs目录
			upload.initHdfs(fs ,p);
			
			//上传xml
			upload.doUpload(fs ,mainPath ,subPath ,path);
			
			//上传jar
			upload.uploadJar(fs ,mainPath ,path);
			
			System.out.println("完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化HDFS目录。
	 * @param fs
	 * @param p
	 * @throws IOException
	 */
	private void initHdfs(FileSystem fs ,Path p) throws IOException{
		if(fs.exists(p)){
			fs.delete(p ,true);				
		}
		fs.mkdirs(p);
	}
	
	/**
	 * 上传xml文件
	 * @param fs
	 * @param path
	 * @throws IOException
	 */
	private void doUpload(FileSystem fs , String mainPath ,String subPath ,String path) throws IOException{
		//ebsdi-apps\\src\\main\\resources\\main\\vendor\\stc\\apSuppliersStc
		String srcPath = String.format("%s%s", mainPath ,subPath);
		System.out.println("src " + srcPath);
		File f = new File(srcPath);
		getFileRecursive(f, fs, path);		
	}
	
	/**
	 * 递归获得文件名称，如果是目录，就在HDFS创建目录
	 */
	public void getFileRecursive(File f ,FileSystem fs ,String path) throws IOException{
		if(f == null){
			System.out.println("File is 空");
		}
		
		if(!f.exists()){
			System.out.println(String.format("目录:%s不存在", f.getAbsoluteFile()));
			return;
		}
		
		doRecursive(f, fs, path);
	}
	
	/**
	 * 递归操作
	 * @param f
	 * @param fs
	 * @param path
	 * @throws IOException
	 */
	private void doRecursive(File f ,FileSystem fs ,String path) throws IOException{
		for(File subFile : f.listFiles()){
			if(subFile.isDirectory() == true){
				if(getIsSvnDir(subFile)){
					//是svn目录，不执行
					System.out.println("svn ");
				}else {
					//不是svn目录，在hdfs上创建目录，然后递归
					String pathTemp = String.format("%s/%s", path ,subFile.getName());
					fs.mkdirs(new Path(pathTemp));
					getFileRecursive(subFile, fs ,pathTemp);					
				}				
			}else {
				System.out.println("file name " + subFile.getAbsolutePath());
				Path srcPath = new Path(subFile.getAbsolutePath());
				Path dstPath = new Path(path);
				fs.copyFromLocalFile(srcPath, dstPath);
			}
		}
	}
	
	/**
	 * 判断是否.svn文件夹
	 */
	private boolean getIsSvnDir(File f){
		boolean isSvn = false;
		Pattern p = Pattern.compile("(.svn)");
		Matcher m = p.matcher(f.getName());
		
		if(m.find()){
			isSvn = true;
		}
		return isSvn;
	}
	
	/**
	 * 
	 * @param fs
	 * @param localMainPath 本地path
	 * @param hdfsMainPath hdfs path
	 */
	public void uploadJar(FileSystem fs ,String localMainPath ,String hdfsMainPath) throws IOException{
		System.out.println("上传lib jar");
		
		Path libPath = new Path(String.format("%s\\lib", hdfsMainPath));
		fs.mkdirs(libPath);
		
		String[] jarArray = jars.split(",");
		for (String jar : jarArray) {
			this.doUploadJar(fs, jar ,localMainPath, libPath);			
		}
	}
	
	/**
	 * 上传jar 操作
	 * @param fs
	 * @param name ebsdi-apps等
	 * @param localMainPath
	 * @param libPath
	 * @throws IOException
	 */
	private void doUploadJar(FileSystem fs ,String name , String localMainPath ,Path libPath) throws IOException{
		String target = "target";
		String appsPath = String.format("%s%s\\%s\\%s.jar", localMainPath ,name ,target ,name);
		fs.copyFromLocalFile(new Path(appsPath), libPath);
	}

}
