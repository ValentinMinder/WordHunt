package whproperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Abstraction over configuration files.
 */
public class WHProperties extends Properties {

	/**
	 * Default constructor.
	 */
	public WHProperties() {
		super();
	}

	/**
	 * Constructor of a Property set with a filename as argument.
	 * 
	 * @param filename
	 *            should be relative to current project.
	 */
	public WHProperties(String filename) {
		try {
			load(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Overrides getProperty to use the safer version of getString();
	 */
	@Override
	public String getProperty(String key) {
		return getString(key);
	}

	/**
	 * Get the string value corresponding to the key, or throws an exception if
	 * not found.
	 * 
	 * @param key
	 * @return
	 */
	public String getString(final String key) {
		String result = super.getProperty(key);
		if (null == result) {
			throw new RuntimeException("Key not found");
		}
		return result;
	}

	/**
	 * Get the integer value corresponding to the key, or throws an exception if
	 * not found or not parsable to int.
	 * 
	 * @param key
	 * @return
	 */
	public Integer getInteger(final String key) {
		return Integer.parseInt(super.getProperty(key));
	}

	/**
	 * Get the double value corresponding to the key, or throws an exception if
	 * not found or not parsable to int.
	 * 
	 * @param key
	 * @return
	 */
	public Double getDouble(final String key) {
		return Double.parseDouble(super.getProperty(key));
	}
}