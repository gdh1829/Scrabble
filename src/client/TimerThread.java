package client;

public class TimerThread implements Runnable {

	private GameRoom gr;
	private boolean flag;

	public TimerThread(GameRoom gr) {
		this.gr = gr;
		flag = true;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		try {
			int i = 60;
			while (flag && i >= 0) {
				gr.getLbTimer().setText(setTimer(i));
				if (i == 0) {
					gr.timeOut();
				}
				Thread.sleep(1000);
				i--;

			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			flag = false;
		}
	}

	public String setTimer(int time) {
		if (time == 60) {
			return "01:00";
		} else if (time < 10) {
			return "00:0" + time;
		} else {
			return "00:" + time;
		}
	}

}
