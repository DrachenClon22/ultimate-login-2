package com.drachenclon.dreg.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.drachenclon.dreg.ByteManager.ByteMath;
import com.drachenclon.dreg.Exceptions.NoLocalPathException;
import com.drachenclon.dreg.FileManager.FileBuilder;
import com.drachenclon.dreg.FileManager.FileManager;

/**
 * Tests for testing managing file information.
 * <br><br>
 * Success: All read-write, replace and creating file function works.
 * 
 * @apiNote If writing new tests, don't forget to delete file after tests
 * to avoid creating garbage. Use {@link #DeleteFile()}.
 */
class FileManagingTests {

	/**
	 * Name of the file that should be generated and tested.
	 */
	private static final String FILENAME = "accounts_test";
	
	/**
	 * Delete file after test where file created. Use it to avoid creating garbage.
	 */
	private static void DeleteFile() {
		try {
			FileBuilder.init(Path.of("").toAbsolutePath().toFile(), FILENAME);
			FileBuilder.GetLocalFile().delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Testing init functions of FileBuilder to generate file.
	 * <br><br>
	 * Success: File generated and exists.
	 */
	@Test
	void testInit() {
		try {
			FileBuilder.init(Path.of("").toAbsolutePath().toFile(), FILENAME);
			Assert.assertNotNull("No file found", FileBuilder.GetLocalFile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		DeleteFile();
	}

	/**
	 * Testing read-write file information. Writing random bytes to file and then
	 * trying to read and find these bytes.
	 * <br><br>
	 * Success: Info written, bytes found.
	 */
	@Test
	void testReadWriteFile() {
		try {
			FileBuilder.init(Path.of("").toAbsolutePath().toFile(), FILENAME);
		} catch (NoLocalPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random rand = new Random();
		byte[] inputBytes = new byte[8];
		rand.nextBytes(inputBytes);
		String inputText = ByteMath.ByteToString(inputBytes);
		FileManager.TryWriteToFile(inputText);
		String test = FileManager.ReadFile();
		Assert.assertTrue("No info from file", test.contains(inputText));
		
		DeleteFile();
	}
	
	/**
	 * Testing replace function in file. Sadly, in main function by now (ver 2.0.0)
	 * replace function reads all info from file and completely rewrite the file, so this
	 * approach may be bad for performance speed with large texts.
	 * <br><br>
	 * Success: File read, information has been replaced and found in file.
	 * @apiNote There's a very small chance that the test will fail out of the blue, see comments.
	 */
	@Test
	void testReplaceInFile() {
		try {
			FileBuilder.init(Path.of("").toAbsolutePath().toFile(), FILENAME);
		} catch (NoLocalPathException e) {
			e.printStackTrace();
		}
		Random rand = new Random();
		/*
		 * Just random set of bytes, for testing 16 is okay.
		 * I don’t think this will ever happen, but if the blocks are generated the same, 
		 * then the test will fail, but for the test it will do.
		 */
		byte[] inputStart = new byte[16];
		byte[] inputReplace = new byte[16];
		rand.nextBytes(inputStart);
		rand.nextBytes(inputReplace);
		String inputStartString = ByteMath.ByteToString(inputStart);
		String inputReplaceString = ByteMath.ByteToString(inputReplace);
		
		FileManager.TryWriteToFile(inputStartString);
		String test = FileManager.ReadFile();
		
		if (inputStartString.equals(inputReplaceString)) {
			fail("The impossible happened. Blocks were generated the same. Restart the test");
		}
		FileManager.TryReplaceInFile(inputStartString, inputReplaceString);
		test = FileManager.ReadFile();
		
		if (test.contains(inputStartString)) {
			fail("File does still contain previous string");
		}
		
		Assert.assertTrue("File does not contain replaced string", test.contains(inputReplaceString));
		
		DeleteFile();
	}
}
