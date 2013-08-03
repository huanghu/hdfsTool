package com.jd.hdfsTool;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.junit.Before;
import org.junit.Test;


public class UploadFileTest {
	UploadFile uf;
	String pathHdfs;
	FileSystem fs;
	
	@Before
	public void before(){
		try {
			uf = new UploadFile();
			fs = FileSystem.get(uf.getConf());
			pathHdfs = "/user/huanghu/oozie/workflow/main/vendor/stc/apSuppliersStc";
			
//			Path path = new Path(pathHdfs);
//			if(fs.exists(path)){
//				fs.delete(path ,true);				
//			}
//			fs.mkdirs(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	public void getFileRecursiveTest(){
		try {
			String path = "D:\\java\\program\\ErpToOracle-testBarch\\ebsdi-apps\\src\\main\\resources\\main\\vendor\\stc\\apSuppliersStc";
			File f = new File(path);
			uf.getFileRecursive(f, fs ,pathHdfs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void uploadJarTest(){
		try {
			String mainPath = "D:\\java\\program\\ErpToOracle-testBarch\\";
			uf.uploadJar(fs ,mainPath ,pathHdfs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
