package com.lester.uteacher.info;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Best
 *	用户信息操作类
 */
public class mUserInfo {
	/**
	 * 保存用户信息
	 */
	public static Boolean SaveTeacherinfo(Activity a,Teacherinfo Teacherinfo){
		SharedPreferences sp = a.getSharedPreferences("Teacherinfo", 0);
		Editor edit = sp.edit();
		try {
			edit.putString("Teacherinfo", serialize(Teacherinfo));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		edit.commit();
		return true;
	}
	/**
	 * 反序列化对象,获取用户信息
	 */
	@SuppressWarnings("unused")
	public static Teacherinfo GetTeacherinfo(Activity a)  {
		try {
			long startTime = System.currentTimeMillis();
			String redStr = java.net.URLDecoder.decode(getObject(a), "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					redStr.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);
			Teacherinfo Teacherinfo = (Teacherinfo) objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
			long endTime = System.currentTimeMillis();
//			Log.i("serial", "反序列化耗时为:" + (endTime - startTime));
			return Teacherinfo;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	public static Teacherinfo GetTeacherinfo(SharedPreferences share)  {
		try {
//			long startTime = System.currentTimeMillis();
			String redStr = java.net.URLDecoder.decode(getObject(share), "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					redStr.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(
					byteArrayInputStream);
			Teacherinfo Teacherinfo = (Teacherinfo) objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
//			long endTime = System.currentTimeMillis();
//			Log.i("serial", "反序列化耗时为:" + (endTime - startTime));
			return Teacherinfo;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 序列化对象
	 */
	private static String serialize(Teacherinfo Teacherinfo) throws IOException {
//		long startTime = System.currentTimeMillis();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(Teacherinfo);
		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		objectOutputStream.close();
		byteArrayOutputStream.close();
//		Log.i("serial", "serialize str =" + serStr);
//		long endTime = System.currentTimeMillis();
//		Log.i("serial", "序列化耗时为:" + (endTime - startTime));
		return serStr;
	}

	private static String getObject(Activity a) {
		SharedPreferences sp = a.getSharedPreferences("Teacherinfo", 0);
		return sp.getString("Teacherinfo", null);
	}
	private static String getObject(SharedPreferences share) {
		
		return share.getString("Teacherinfo", null);
	}
	
}
