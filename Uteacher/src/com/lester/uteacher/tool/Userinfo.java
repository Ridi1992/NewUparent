package com.lester.uteacher.tool;


import org.json.JSONObject;

import com.lester.uteacher.info.Teacherinfo.ImUser;
import com.lester.uteacher.info.Teacherinfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 用户信息处理类
 * @author Administrator
 *
 */
public class Userinfo {
	public static String str="uteacher";
//	public static String mychild="umychild";
	public static void setinfo(Activity a, Teacherinfo data){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		Editor editor = shared.edit();
		editor.putBoolean("login", true);//已登录
		editor.putString("password", data.getPassword());
		editor.putString("photo",data.getPhoto());
		editor.putString("id", data.getId());
		editor.putString("name",data.getName());
		editor.putString("mobile_phone",data.getMobile_phone());
		editor.putString("id_card",data.getId_card());
		editor.putString("school_id",data.getSchool_id());
		editor.putString("schoolName",data.getSchoolName());
		editor.commit();
	}
	public static void setpassword(Activity a, String password){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		Editor editor = shared.edit();
		editor.putString("password", password);
		editor.commit();
	}
	public static void logofff(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		Editor editor = shared.edit();
		editor.putBoolean("login", false);//已登录
		editor.putString("password", "");
		editor.putString("photo","");
		editor.putString("id", "");
		editor.putString("name","");
		editor.putString("mobile_phone", "");
		editor.putString("id_card", "");
		editor.putString("school_id", "");
		editor.putString("schoolName", "");
		editor.commit();
	}
	public static Boolean islogin(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getBoolean("login", false);
	}
	public static String getid(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("id", "");
	}
	public static String getpassword(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("password", "");
	}
	public static String getschoolName(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("schoolName", "");
	}
	public static String getname(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("name", "");
	}
	public static String getmobile_phone(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("mobile_phone", "");
	}
	public static String getschoolid(Activity a){
		SharedPreferences shared =a.getSharedPreferences(str, 0);
		return shared.getString("school_id", "");
	}

}
