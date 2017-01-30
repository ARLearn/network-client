package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.network.ConnectionFactory;

public class AccountClient extends GenericClient{

	public static AccountClient instance;

	private AccountClient() {
		super("/account");
	}

	public static AccountClient getAccountClient() {
		if (instance == null) {
			instance = new AccountClient();
		}
		return instance;
	}
	
	public AuthResponse anonymousLogin(String accountToken) {
		return (AuthResponse)  executeGet(getUrlPrefix()+"/anonymousLogin/"+accountToken, null, AuthResponse.class);
	}

	public Account accountDetails(String token) {
		return (Account)  executeGet(getUrlPrefix()+"/accountDetails", token, Account.class);
	}

	public Account createAnonymousAccount(String name, String email){
		return (Account) executeGet(getUrlPrefix()+"/createAnonymousContact/"+email+"/"+name, null, Account.class);
	}

	public Account createAnonymousAccountPost(String name, String email) {
		Account account = new Account();
		account.setName(name);
		account.setEmail(email);
		HttpResponse httpResp = ConnectionFactory.getConnection().executePOST(getUrlPrefix()+"/createAnonymousContact", null, "application/json", toJson(account), "application/json");
		String entry;
		try {
			entry = EntityUtils.toString(httpResp.getEntity(), "utf-8");
			return (Account) jsonDeserialise(entry, Account.class);
		} catch (Exception e) {
			e.printStackTrace();
			Account respError = new Account();
			respError.setError("exception "+e.getMessage());
			return respError;
		}
	}
}
