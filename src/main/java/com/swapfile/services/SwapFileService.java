package com.swapfile.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.swapfile.dtos.SwapFileDTO;

public class SwapFileService {

	public SwapFileDTO swap() {
		FileLock lock = null;

		Path source = Paths.get("/src/test/resources/example1.properties");
		Path target = Paths.get("/src/test/resources/example2.properties");

		// locking to another system not move or delete the file while
		// processing
		try (RandomAccessFile raf = new RandomAccessFile(source.toString(), "rw");
				FileChannel channel = raf.getChannel()) {

			lock = tryLockFile(channel);

			Files.move(source, source.resolveSibling("example1OLD.properties"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			tryReleaseLockedFile(lock);
		}
		return null;
	}

	private FileLock tryLockFile(FileChannel channel) throws IOException {
		//FileLock lock = channel.lock();
		FileLock lock = channel.tryLock();
		return lock;
	}

	private void tryReleaseLockedFile(FileLock lock) {
		try {
			if (lock != null)
				lock.release();
		} catch (Exception e) {
		}
	}

}
