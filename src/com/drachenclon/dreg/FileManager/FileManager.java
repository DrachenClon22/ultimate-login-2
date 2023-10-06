package com.drachenclon.dreg.FileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import com.drachenclon.dreg.Exceptions.NoLocalFileException;

public final class FileManager {
	
	public static String ReadFile() {
		String text = null;
		try {
			text = Files.readString(FileBuilder.GetLocalFile().toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoLocalFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	public static boolean TryWriteToFile(String input) {
		try {
			Files.write(FileBuilder.GetLocalFile().toPath(), input.getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean TryDeleteFromFile(String content) {
		return TryReplaceInFile(content, "");
	}
	
	public static boolean TryReplaceInFile(String from, String to) {
		String content = ReadFile();
		if (content == null) {
			return false;
		}
		content = content.replace(from, to);
		TryRewriteFile(content);
		return true;
	}
	
	private static boolean TryRewriteFile(String input) {
		try {
			Files.write(FileBuilder.GetLocalFile().toPath(), input.getBytes());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
