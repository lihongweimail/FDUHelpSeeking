package cn.edu.fudan.se.helpseeking.test.clientlogin;


//[cseFduTest]
//cx=014132305786414512501:prmarh9sn8k
//projectid=rosy-resolver-863
//key=AIzaSyAlqmv7SHFrJptyWdEKjXnR5_PkTVAHpfs
//csename=Helpseekingfdu
//OAuth=AIzaSyAlqmv7SHFrJptyWdEKjXnR5_PkTVAHpfs
//clientid=433540504100-ogf00jmc1kj142ctognbn8cbjbjegegt.apps.googleusercontent.com
//CLIENTSECRET=dBJKZCKSyRwnFK73dsGxDcRX


public class ClientLoginDemoMain {
	public static void main(String[] args) {
		String auth = ClientLogin.getAuthToken("helpseekingfdu@gmail.com", "helpseekingfdu2015", "cprose", "");
		if (auth != null) {
			System.out.println("AuthToken is : " + auth);
		} else {
			System.out.println("An error has occurred.");
		}
	}

}
