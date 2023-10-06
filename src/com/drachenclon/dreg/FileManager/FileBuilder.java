package com.drachenclon.dreg.FileManager;

import java.io.File;

import com.drachenclon.dreg.Exceptions.NoLocalFileException;
import com.drachenclon.dreg.Exceptions.NoLocalPathException;

/**
 * FileBuilder class uses for storing current working file to work with
 * so not getting file every time it is needed to write or read something
 * from or to. 
 * <br>
 * Always use {@link #init(path, filename)} before calling any
 * method from this class.
 */
public final class FileBuilder {
	
	private static File _localPath;
	private static File _localFile;
	
	/**
	 * Uses for init local working class variable to work with.
	 * <br>
	 * Always use <strong>before</strong> try to get access to any method from this class.
	 * @param filename how file with login named
	 * @throws NoLocalPathException throws when localPath is null
	 */
	public static void init(File localPath, String filename) throws NoLocalPathException {
		if (localPath == null) {
			throw new NoLocalPathException("Local path is null.");
		}
		
		_localPath = localPath;
		_localFile = CreateFile(filename);
	}
	
	/**
	 * Used for getting local working file. Always use <strong>after</strong> init() function!
	 * @return Local file if there is one
	 * @throws NoLocalFileException throws when no local file exists. 
	 * Usually happens when {@link #init(path, filename)} has never been called.
	 */
	public static File GetLocalFile() throws NoLocalFileException {
		if (_localFile == null) {
			throw new NoLocalFileException("No local file found. Use FileBuilder.init(\"path\").");
		}
		
		return _localFile;
	}
	
	private static File CreateFile(String name) {
		File file = new File(_localPath, name);
		if (file.exists()) {
			return file;
		} else {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return file;
	}
}
