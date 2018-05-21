package com.rsaladocid.util.data;

import static org.junit.Assert.*;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestDataHistory {

	@Test
	public void testPutSingleValueMultipleTimesInTheSameKey() {
		DataHistory<String, String> history = new DataHistory<String, String>();

		history.putSingle("test", "foo");
		history.putSingle("test", "bar");

		assertTrue(history.get("test").size() == 2);
		assertTrue(history.get("test").peekFirst().getValue().equals("foo"));
		assertTrue(history.get("test").peekLast().getValue().equals("bar"));
	}

	@Test
	public void testConcurrentPutSingleValueMultipleTimes() throws InterruptedException {
		final DataHistory<String, String> history = new DataHistory<String, String>();

		ExecutorService executor = Executors.newFixedThreadPool(100);

		for (int i = 0; i < 100; i++) {
			executor.submit(new Runnable() {

				public void run() {
					history.putSingle("test", "foo");
				}

			});
		}

		executor.awaitTermination(3000, TimeUnit.MILLISECONDS);

		Deque<DataRecord<String>> testStack = history.get("test");

		assertTrue(testStack.size() == 100);

		long previous = 0;
		for (DataRecord<String> record : testStack) {
			assertTrue(previous <= record.getTimestamp());
			previous = record.getTimestamp();
		}
	}

	@Test
	public void testGetAllMostRecentData() {
		DataHistory<String, String> history = new DataHistory<String, String>();

		history.putSingle("test1", "foo");
		history.putSingle("test1", "bar");
		history.putSingle("test2", "foo");
		history.putSingle("test2", "bar");

		Map<String, DataRecord<String>> mostRecent = history.getAllMostRecent();

		assertTrue(mostRecent.keySet().size() == 2);
		assertTrue(mostRecent.get("test1").getValue().equals("bar"));
		assertTrue(mostRecent.get("test2").getValue().equals("bar"));
	}

	@Test
	public void testGetAllMostRecentFromEmptyHistory() {
		DataHistory<String, String> history = new DataHistory<String, String>();
		Map<String, DataRecord<String>> mostRecent = history.getAllMostRecent();

		assertTrue(mostRecent.keySet().size() == 0);
	}

	@Test
	public void testGetMostRecentData() {
		DataHistory<String, String> history = new DataHistory<String, String>();

		history.putSingle("test1", "foo");
		history.putSingle("test1", "bar");

		assertTrue(history.getMostRecent("test1").getValue().equals("bar"));
	}

	@Test
	public void testGetMostRecentDataFromUnexistingKey() {
		DataHistory<String, String> history = new DataHistory<String, String>();

		history.putSingle("test1", "foo");
		history.putSingle("test1", "bar");

		assertTrue(history.getMostRecent("test2") == null);
	}

}
