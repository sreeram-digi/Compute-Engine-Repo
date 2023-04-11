package com.portal.action;

public interface Action<T,T1,T2>{
	void action(T object, T1 object2,T2 object3) throws Exception;
}

