package com.swapfile.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swapfile.dtos.SwapFileDTO;

@Service
public class SwapFileService {

	private static final String SYSTEM_FILE_SEPARATOR = "file.separator";

	private static final String PREFIX_BKP = "bkp_";

	public static final String PREFIX_TEMP = "temp_";

	@Value("${filesToSwap.source}")
	private String source;

	@Value("${filesToSwap.target}")
	private String target;

	public SwapFileDTO swap() {

		SwapFileDTO response = new SwapFileDTO();

		Path lSource = Paths.get(this.source);
		Path lTarget = Paths.get(this.target);

		if (Files.notExists(lSource)) {
			response.setSuccess(false);
			response.setMessage("File not found: " + lSource.getFileName());
			return response;
		}

		if (Files.notExists(lTarget)) {
			response.setSuccess(false);
			response.setMessage("File not found: " + lTarget.getFileName());
			return response;
		}

		try {

			// creating backup files
			Files.copy(lSource, lSource.resolveSibling(PREFIX_BKP + lSource.getFileName()),
					StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(lTarget, lTarget.resolveSibling(PREFIX_BKP + lTarget.getFileName()),
					StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("Error creating backup files: " + e.getMessage());
			return response;
		}

		String movedSource = PREFIX_TEMP + lSource.getFileName();
		String movedTarget = PREFIX_TEMP + lTarget.getFileName();
		try {

			// temporary file
			Files.move(lSource, lSource.resolveSibling(movedSource));
			Files.move(lTarget, lTarget.resolveSibling(movedTarget));

		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("Error creating temporary files to swap: " + e.getMessage());
			return response;
		}

		try {

			Path lSource2 = Paths
					.get(lSource.getParent().toString() + System.getProperty(SYSTEM_FILE_SEPARATOR) + movedSource);
			Path lTarget2 = Paths
					.get(lTarget.getParent().toString() + System.getProperty(SYSTEM_FILE_SEPARATOR) + movedTarget);

			Files.move(lSource2, lSource2.resolveSibling(lTarget.getFileName()));
			Files.move(lTarget2, lTarget2.resolveSibling(lSource.getFileName()));

			response.setMessage("Success");

		} catch (Exception e) {
			response.setSuccess(false);
			response.setMessage("Error while swapping the files: " + e.getMessage());
		}

		return response;
	}

	public void restoreBackups() {

		Path lSource = Paths.get(this.source);
		Path lTarget = Paths.get(this.target);

		if (Files.exists(lSource)) {
			// nothing to do
			return;
		}

		if (Files.exists(lTarget)) {
			// nothing to do
			return;
		}

		try {

			Path bkpSource = Paths.get(lSource.getParent().toString() + System.getProperty(SYSTEM_FILE_SEPARATOR)
					+ PREFIX_BKP + lSource.getFileName());
			Path bkpTarget = Paths.get(lTarget.getParent().toString() + System.getProperty(SYSTEM_FILE_SEPARATOR)
					+ PREFIX_BKP + lTarget.getFileName());

			System.out.println(bkpSource.toAbsolutePath().toString() + bkpSource.getFileName());

			// restoring backup files
			Files.copy(bkpSource, lSource.resolveSibling(lSource.getFileName()), StandardCopyOption.COPY_ATTRIBUTES);
			Files.copy(bkpTarget, lTarget.resolveSibling(lTarget.getFileName()), StandardCopyOption.COPY_ATTRIBUTES);

		} catch (Exception e) {

		}
	}

}
