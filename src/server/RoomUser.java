package server;

public class RoomUser {

	private String id;
	private boolean isReady;
	private int score;

	public RoomUser(String id) {
		super();
		this.id = id;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getId() {
		return id;
	}
}
