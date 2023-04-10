package com.swapfile.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.swapfile.dtos.SwapFileDTO;

@Service
public class SwapFileService {

	public SwapFileDTO swap() {

		SwapFileDTO response = new SwapFileDTO();

		FileLock lock = null;

		Path source = Paths.get("/src/test/resources/example1.properties");
		Path target = Paths.get("/src/test/resources/example2.properties");

		// locking to another system not move or delete the file while
		// processing
		try (RandomAccessFile raf = new RandomAccessFile(source.toString(), "rw");
				FileChannel channel = raf.getChannel()) {

			lock = tryLockFile(channel);

			Files.move(source, source.resolveSibling("example1OLD.properties"));

			response.setMessage("Success");

		} catch (FileNotFoundException e) {
			response.setMessage("File not found: " + e.getMessage());
		} catch (IOException | OverlappingFileLockException e) {
			response.setMessage("The files are in use by another process, try again: " + e.getMessage());
			response.setMessage(e.getMessage());
		} finally {

			tryReleaseLockedFile(lock);
		}

		return response;
	}

	private FileLock tryLockFile(FileChannel channel) throws IOException, OverlappingFileLockException {
		return channel.tryLock();
	}

	private void tryReleaseLockedFile(FileLock lock) {
		try {
			if (lock != null)
				lock.release();
		} catch (Exception e) {
		}
	}

}
