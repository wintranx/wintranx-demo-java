package com.wintranx.client.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MapUtil {
	public static Map<String, Object> convertObjToMap(Object obj) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		if (obj == null)
			return null;
		try {
			Class<?> objClass = obj.getClass();
			while (objClass != null) {
				Field[] fields = objClass.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					try {
						Field f = objClass.getDeclaredField(fields[i].getName());
						f.setAccessible(true);
						Object o = f.get(obj);
						reMap.put(fields[i].getName(), o);
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				objClass = objClass.getSuperclass();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return reMap;
	}
	public static Map<String,Object> object2Map(Object object){
		Map<String,Object> result=new HashMap<>();
		Field[]fields=object.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String name = new String(field.getName());
				result.put(name, field.get(object));
			}
		}catch (Exception e){
			log.error("map error ",e);
		}
		return result;
	}
	
	public static Map<String,String> object2StrMap(Object object){
		Map<String,String> result=new HashMap<>();
		Field[]fields=object.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String name = new String(field.getName());
				result.put(name, (String)field.get(object));
			}
		}catch (Exception e){
			log.error("map error ",e);
		}
		return result;
	}
	

	public static Map<String,String> object2StrMapNotNull(Object object){
		Map<String,String> result=new HashMap<>();
		Field[]fields=object.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String name = new String(field.getName());
				String value= (String)field.get(object);
				if(StringUtil.isNotEmpty(value)) {
					result.put(name,value);
				}
			}
		}catch (Exception e){
			log.error("map error ",e);
		}
		return result;
	}
	
	
	public static void rmNullValue(Map<String,String> reqMap){
		Map<String,String> map = new HashMap<String, String>();
		if(reqMap!=null) {
			reqMap.forEach((key, value) -> {
				if(StringUtil.isNotEmpty(value)) {
					map.put(key, value);
				}
			});
		}
		reqMap.clear();
		reqMap.putAll(map);
	}
	
}
