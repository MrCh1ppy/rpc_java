package org.example.service.impl;

import org.example.service.HelloService;
import org.example.utils.regerster.annotation.ServiceProvider;

@ServiceProvider
public class HelloServiceImpl implements HelloService {
	@Override
	public String hello(String name) {
		return "hello"+name;
	}
}
