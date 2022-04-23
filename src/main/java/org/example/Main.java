package org.example;

import org.example.dao.message.RpcRequestMsg;
import org.example.utils.service.ServiceFactory;

import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args)  {
		var request = new RpcRequestMsg(
				"org.example.service.HelloService",
				"hello",
				String.class,
				new Class[]{String.class},
				new Object[]{"css"}
		);
	}
}