package whprotocol;

import com.google.gson.Gson;

public abstract class WHMessageContent {

	protected int status;
	protected static Gson gson = new Gson();

	public WHMessageContent(int status) {
		super();
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return gson.toJson(this);
	}
}