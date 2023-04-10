package com.swapfile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swapfile.dtos.SwapFileDTO;
import com.swapfile.services.SwapFileService;

@RestController
@RequestMapping("/rest/swapfile")
public class SwapFileController {

	private static final String REQUEST_CREDENTIAL_HEADER = "Request-Credential";

	@Autowired
	private SwapFileService swapFileService;

	@PostMapping
	public ResponseEntity<SwapFileDTO> swapTheFiles(
			@RequestHeader(REQUEST_CREDENTIAL_HEADER) String requestCredentialHeader) {

		if (!StringUtils.hasLength(requestCredentialHeader)) {
			ResponseEntity.badRequest().body("Invalid credential.");
		}

		SwapFileDTO response = swapFileService.swap();

		if (!response.isSuccess()) {
			response.setMessage(response.getMessage() + "\n Restoring backups.");
			swapFileService.restoreBackups();
			return ResponseEntity.badRequest().body(response);
		}

		return ResponseEntity.ok().body(response);
	}

}
