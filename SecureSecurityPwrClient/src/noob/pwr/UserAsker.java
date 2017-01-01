package noob.pwr;

public class UserAsker extends Thread {

	public boolean askForUsers = true;
	public void run() {
		while(askForUsers)
		{
			ChatClient.instance.WriteMessage(ComConst.USERS,ComConst.EMPTY,ComConst.EMPTY);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
