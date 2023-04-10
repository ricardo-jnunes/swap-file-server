package com.swapfile.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.swapfile.dtos.SwapFileDTO;

@Service
public class SwapFileService {

	public SwapFileDTO swap() {

		SwapFileDTO response = new SwapFileDTO();

		Path source = Paths.get("C:/swap-file-server/src/test/resources/example.properties");
		Path target = Paths.get("C:/swap-file-server/src/test/resources/reduced_example.properties");

		if (Files.notExists(source)) {
			response.setMessage("File not found: " + source.getFileName());
			return response;
		}

		if (Files.notExists(target)) {
			response.setMessage("File not found: " + target.getFileName());
			return response;
		}

		try {

			// temporary file
			Files.move(source, source.resolveSibling("example_moved.properties"));
			Files.move(target, target.resolveSibling("reduced_example_moved.properties"));

			response.setMessage("Success");

		} catch (IOException e) {
			response.setMessage(e.getMessage());
		}

		try {

			Path source2 = Paths.get("C:/swap-file-server/src/test/resources/example_moved.properties");
			Path target2 = Paths.get("C:/swap-file-server/src/test/resources/reduced_example_moved.properties");

			// temporary file
			Files.move(source2, source2.resolveSibling("reduced_example.properties"));
			Files.move(target2, target2.resolveSibling("example.properties"));

			response.setMessage("Success");

		} catch (IOException e) {
			response.setMessage(e.getMessage());
		}

		return response;
	}

}
