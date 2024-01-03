package sit.cp23ej2.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import sit.cp23ej2.services.FileStorageService;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileStorageService fileStorageService;

	public FileController(FileStorageService storageService) {
		this.fileStorageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", fileStorageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

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
}
