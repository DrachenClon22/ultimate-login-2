package com.drachenclon.dreg.Tests;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.drachenclon.dreg.AuthManager.AuthHandler;
import com.drachenclon.dreg.Exceptions.NoLocalPathException;
import com.drachenclon.dreg.FileManager.FileBuilder;
import com.drachenclon.dreg.HashManager.HashBuilder;
import com.drachenclon.dreg.HashManager.HashBuilders.SHA256Builder;

/**
 * Tests for testing user auth system.
 * <br>
 * For tests used login and pass: "username2":"password2" with ip "127.0.0.1".
 * <br><br>
 * Success: All registration and auth systems work.
 * 
 * @apiNote Before testing auth, always test registration first, because
 * in registration process file with user info being created.
 */
class UserAuthTests {

	/**
	 * Name of the file that should be generated and tested.
	 */
	private static final String FILENAME = "accounts_test";
	
	/**
	 * User registration testing.
	 * <br><br>
	 * Success: Registration func worked successfully, means registration went good.
	 * And file with user info created.
	 * 
	 * @apiNote No file deletion here, because file needed for auth testing.
	 */
	@Test
	void testUserRegistration() {
		try {
			FileBuilder.init(Path.of("").toAbsolutePath().toFile(), FILENAME);
			HashBuilder.init(new SHA256Builder());
		} catch (NoLocalPathException e) {
			e.printStackTrace();
		}
		Assert.assertTrue("Can't register", AuthHandler.TryRegister("username3", "password2", "127.0.0.1"));
	}
	
	/**
	 * User auth testing.
	 * <br><br>
	 * Success: If user file exists, user found and passwords matches;
	 * if user file doesn't exist, test will fail. Always use registration test
	 * before invoking this test.
	 * 
	 * @apiNote No file deletion here, because file needed for auth testing.
	 * And always invoke <strong>after</strong> registration test.
	 */
	@Test
	void testUserAuth() {
		try {
			FileBuilder.init(Path.of("").toAbsolutePath().toFile(), FILENAME);
			HashBuilder.init(new SHA256Builder());
		} catch (NoLocalPathException e) {
			e.printStackTrace();
		}
		
		// Password and login should match ones that in testUserRegistration()
		String login = "username3";
		String password = "password3";
		String ip = "127.0.0.1";
		
		Assert.assertTrue("Login with password failed", AuthHandler.TryAuthWithPassword(login, password));
		Assert.assertTrue("Login with IP failed", AuthHandler.TryAuthWithIp(login, ip));
	}
}
