package com.swapfile.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swapfile.dtos.SwapFileDTO;
import com.swapfile.services.SwapFileService;

@RestController
@RequestMapping("/rest/swapfile")
public class SwapFileController {

	@Autowired
	private SwapFileService swapFileService;

	@PostMapping(value = "/")
	public ResponseEntity<SwapFileDTO> swapTheFiles() {

		return ResponseEntity.ok().body(swapFileService.swap());
	}

}
