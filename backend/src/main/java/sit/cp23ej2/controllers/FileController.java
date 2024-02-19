package sit.cp23ej2.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sit.cp23ej2.services.FileStorageService;


@RestController
@RequestMapping("/api/files")
public class FileController {

	@Autowired
	private FileStorageService fileStorageService;
	
    // private final FileStorageService fileStorageService;

	// public FileController(FileStorageService storageService) {
	// 	this.fileStorageService = storageService;
	// }

	// @GetMapping("/")
	// public String listUploadedFiles(Model model) throws IOException {

	// 	model.addAttribute("files", fileStorageService.loadAll().map(
	// 			path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
	// 					"serveFile", path.getFileName().toString()).build().toUri().toString())
	// 			.collect(Collectors.toList()));

	// 	return "uploadForm";
	// }

	// @GetMapping("/files/{filename:.+}")
	// @ResponseBody
	// public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

	// 	Resource file = fileStorageService.loadAsResource(filename);

	// 	if (file == null)
	// 		return ResponseEntity.notFound().build();

	// 	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
	// 			"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	// }

	// @PostMapping("/")
	// public String handleFileUpload(@RequestParam("file") MultipartFile file,
	// 		RedirectAttributes redirectAttributes) {

	// 	fileStorageService.store(file);
	// 	redirectAttributes.addFlashAttribute("message",
	// 			"You successfully uploaded " + file.getOriginalFilename() + "!");

	// 	return "redirect:/";
	// }

	@GetMapping("/filesBook/{bookId}")
	public ResponseEntity<?> getFileBook(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer bookId) {
		
		byte[] fileDate = new byte[0];
		try{
			fileDate = fileStorageService.downlioadImageFromFileSysteBook(bookId);
			return ResponseEntity.ok().contentType(MediaType.valueOf("image/jpeg")).body(fileDate);
		}catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
// src/main/java/sit/cp23ej2/services/FileStorageService.java src/main/java/sit/cp23ej2/controllers/HistoryController.java