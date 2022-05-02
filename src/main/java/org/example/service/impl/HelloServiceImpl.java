package org.example.service.impl;

import org.example.service.HelloService;

public class HelloServiceImpl implements HelloService {
	@Override
	public String hello(String name) {
		return "hello"+name;
	}
}
