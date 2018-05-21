package com.rsaladocid.util.data;

/**
 * A wrapper of a simple value that provides information about the time in which
 * the value was set.
 *
 * @param <T>
 *            the value type of the data record
 */
public class DataRecord<T> {

	/**
	 * The value of the data record
	 */
	private T value;

	/**
	 * Time in milliseconds in which the current value was set
	 */
	private long timestamp;

	/**
	 * Creates a data record with the given value
	 * 
	 * @param value
	 *            the value of the data
	 */
	public DataRecord(T value) {
		setValue(value);
	}

	/**
	 * Returns the timestamp in which the value was set
	 * 
	 * @return the time in milliseconds
	 * @see System#currentTimeMillis()
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns the value of the data
	 * 
	 * @return the value of the data
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets the value of the data and sets the current time
	 * 
	 * @param value
	 *            the value of the data
	 */
	public synchronized void setValue(T value) {
		this.value = value;
		setTimestamp(currentTimestamp());
	}

	/**
	 * Returns the current timestamp in milliseconds
	 * 
	 * @return the current time
	 * @see System#currentTimeMillis()
	 */
	protected long currentTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * Sets the timestamp in milliseconds
	 * 
	 * @param timestamp
	 *            the timestamp to set
	 */
	protected void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
