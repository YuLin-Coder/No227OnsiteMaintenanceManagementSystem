package com.javaweb.test;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.GET;

import org.apache.ibatis.annotations.One;
import org.junit.Test;

import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;

public class MyTest {
	
 
	public static void main(String[] args){
		File file = new File("xxx.avi");
		System.out.println(new MimetypesFileTypeMap().getContentType(file));
	}
	
}
