package com.firstlinecode.basalt.protocol;

public final class HandyUtils {
	private HandyUtils(){};
	
	public static boolean equalsEvenNull(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null)
			return true;
		
		if (obj1 == null) {
			return obj2 == null;
		} else {
			return obj1.equals(obj2);
		}
	}
	
	public static boolean equalsExceptNull(Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null)
			return false;
		
		return obj1.equals(obj2);
	}
	
	public static boolean isBlank(String string) {
		return string == null || "".equals(string);
	}
}
