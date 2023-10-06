package com.drachenclon.dreg.Tests;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.drachenclon.dreg.AuthManager.AuthHandler;
import com.drachenclon.dreg.Exceptions.NoLocalPathException;
import com.drachenclon.dreg.Exceptions.StringCantBeValidatedException;
import com.drachenclon.dreg.FileManager.FileBuilder;
import com.drachenclon.dreg.FileManager.FileParser;
import com.drachenclon.dreg.HashManager.HashBuilder;
import com.drachenclon.dreg.HashManager.HashBuilders.SHA256Builder;
import com.drachenclon.dreg.PlayerManager.Hash.PlayerHashInfo;

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
		System.out.println(HashBuilder.GetByteSize());
		Assert.assertTrue("Can't register", AuthHandler.TryRegister("username2", "password2", "127.0.0.1"));
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
			// In this test SHA256 used, main plugin may use different algorithm.
			HashBuilder.init(new SHA256Builder());
		} catch (NoLocalPathException e) {
			e.printStackTrace();
		}
		
		PlayerHashInfo hashInfo = null;
		try {
			String info = FileParser.GetPlayerInfo("username2");
			hashInfo = new PlayerHashInfo(info);
		} catch (StringCantBeValidatedException e) {
			e.printStackTrace();
		}
		
		if (hashInfo==null) {
			fail("No player hash info found");
		}
		
		String passwordHash = HashBuilder.GetStringHash("password2" + hashInfo.GetSaltAsString());
		
		Assert.assertTrue("Can't login", hashInfo.GetPasswordHashAsString().equals(passwordHash));
	}
}
