package com.fz.gateway.test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import reactor.core.publisher.Mono;

/**
 * MonoTest Program
 *
 * @author Shameer.Koya
 *
 */

public class MonoTest {

	public static void testMono() {
		Mono<String> monoData = Mono.just("JSA");

		monoData.map(item -> "Mono Data : " + item.toLowerCase())
				.subscribe(System.out::println);
	}

	public static void testMonoList() {

		List<String> stringList = new ArrayList<String>();
		stringList.add("one");
		stringList.add("two");

		Mono<List<String>> monoList = Mono.just(stringList);

		monoList.map(item -> "Mono Data : " + item).subscribe(System.out::println);
	}

	public static void testMonoList3() {

		Mono<String> monoData = Mono.just("test");

		monoData.then();
	}

	/**
	 * uncomment to run the program...
	 *
	 *
	 * public static void main(String[] arg) { //testMono(); testMonoList();
	 * //testMonoList3(); }
	 *
	 */

}
