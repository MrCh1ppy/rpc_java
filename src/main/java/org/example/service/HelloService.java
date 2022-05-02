package org.example.service;

import org.example.utils.regerster.annotation.ServiceProvider;

@ServiceProvider
public interface HelloService {
	String hello(String name);
}
