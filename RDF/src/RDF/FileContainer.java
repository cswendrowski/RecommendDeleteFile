package RDF;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class FileContainer {

	File file;
	FileTime time;
	
	public FileContainer(File f) {
		file = f;
	}
	
	public long lastAccess() {
		if (time == null) {
			Path path = Paths.get(file.getPath());
			BasicFileAttributes attrs = null;
			try {
				attrs = Files.readAttributes(path, BasicFileAttributes.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			time = attrs.lastAccessTime();
		}
		return time.toMillis();
	}
	
	public String name() {
		return file.getName();
	}
	
	public long size() {
		return file.length();
	}

	public String location() {
		return file.getPath();
	}
}
