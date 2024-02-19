package sit.cp23ej2.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import sit.cp23ej2.dtos.Book.BookDTO;
// import sit.cp23ej2.dtos.Book.CreateBookDTO;
import sit.cp23ej2.entities.Book;
import sit.cp23ej2.exception.HandleExceptionFile;
import sit.cp23ej2.properties.FileStorageProperties;

import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

@Service
public class FileStorageService {

	private final Path rootLocation;

	SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public FileStorageService(FileStorageProperties properties) {
		// if(properties.getUploadDir().trim().length() == 0){
		// throw new HandleExceptionFile("File upload location can not be Empty.");
		// }
		this.rootLocation = Paths.get(properties.getUploadDir());

		try {
			Files.createDirectories(this.rootLocation);
		} catch (Exception ex) {
			throw new HandleExceptionFile("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public void store(MultipartFile file, Integer bookId) {
		// boo_name = boo_name.replace(" ", "_");
		// boo_name = boo_name + "_" + author;
		// String path = bookName + "_" + author;
		String path = "Book_Cover" + "_" + bookId.toString();
		try {
			if (file.isEmpty()) {
				throw new HandleExceptionFile("Failed to store empty file.");
			}
			String fileName = StringUtils.cleanPath((file.getOriginalFilename()));

			// if (fileName.contains("..")) {
			// // This is a security check
			// throw new HandleExceptionFile(
			// "Cannot store file with relative path outside current directory "
			// + fileName);
			// }

			// fileName = fileName.replace(" ", "_");
			// fileName = book.getBookName().toString();
			// fileName = book.getBookName().toString() + "_" + book.getAuthor().toString()
			// + "_" + fileName;
			// Path destinationFile = this.rootLocation.resolve(
			// Paths.get(file.getOriginalFilename()))
			// .normalize().toAbsolutePath();
			
			Path destinationFile = this.rootLocation.resolve("Book_Cover").resolve(path);
			if (destinationFile.toFile().exists()) {
				System.out.println("File Exists");
				FileSystemUtils.deleteRecursively(destinationFile.toFile());
			}
			Path filePath = Files.createDirectories(destinationFile).resolve(fileName);
			System.out.println("File PAth" + filePath);

			// if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath()))
			// {
			// // This is a security check
			// throw new HandleExceptionFile(
			// "Cannot store file outside current directory.");
			// }
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new HandleExceptionFile("Failed to store file.", e);
		}
	}

	public void storeUserProfile(MultipartFile file, Integer userId) {
		
		String path = "User_Profile" + "_" + userId.toString();
		try {
			if (file.isEmpty()) {
				throw new HandleExceptionFile("Failed to store empty file.");
			}
			String fileName = StringUtils.cleanPath((file.getOriginalFilename()));

			Path destinationFile = this.rootLocation.resolve("User_Profile").resolve(path);
			if (destinationFile.toFile().exists()) {
				System.out.println("File Exists");
				FileSystemUtils.deleteRecursively(destinationFile.toFile());
			}
			Path filePath = Files.createDirectories(destinationFile).resolve(fileName);
			System.out.println("File PAth" + filePath);
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new HandleExceptionFile("Failed to store file.", e);
		}
	}

	// public Stream<Path> loadAll() {
	// 	try {
	// 		return Files.walk(this.rootLocation, 1)
	// 				.filter(path -> !path.equals(this.rootLocation))
	// 				.map(this.rootLocation::relativize);
	// 	} catch (IOException e) {
	// 		throw new HandleExceptionFile("Failed to read stored files", e);
	// 	}

	// }

	public Path load(BookDTO book) {
		// String pathSave = book.getBookName().toString() + "_" + book.getAuthor().toString();
		String pathSave = "Book_Cover" + "_" + book.getBookId().toString();
		try{
			Path path =  rootLocation.resolve("Book_Cover").resolve(pathSave);
			if(path.toFile().exists()) {
				Path pathFile = Files.list(path).collect(Collectors.toList()).get(0);
				if(pathFile.toFile().exists()){
					return pathFile;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Path loadUserFile(Integer userId) {
		String pathSave = "User_Profile" + "_" + userId.toString();
		try{
			Path path =  rootLocation.resolve("User_Profile").resolve(pathSave);
			if(path.toFile().exists()) {
				Path pathFile = Files.list(path).collect(Collectors.toList()).get(0);
				if(pathFile.toFile().exists()){
					return pathFile;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	// public Resource loadAsResource(BookDTO book) {
	// 	try {
	// 		Path file = load(book);
	// 		Resource resource = new UrlResource(file.toUri());
	// 		if (resource.exists() || resource.isReadable()) {
	// 			return resource;
	// 		} else {
	// 			// throw new HandleExceptionNotFound(
	// 			// 		"Could not read file: " + file.getFileName());
	// 			return null;

	// 		}
	// 	} catch (MalformedURLException e) {
	// 		throw new HandleExceptionNotFound("Could not read file: " + book.getFile(), e);
	// 	}
	// }

	// public void deleteAll() {
	// 	FileSystemUtils.deleteRecursively(rootLocation.toFile());
	// }

	public void deleteFile(Book book) {
		// String path = book.getBookName().toString() + "_" + book.getAuthor().toString();
		String path = "Book_Cover" + "_" + book.getBookId().toString();
		Path destinationFile = this.rootLocation.resolve("Book_Cover").resolve(path);
			if (destinationFile.toFile().exists()) {
				System.out.println("File Exists Delete");
				FileSystemUtils.deleteRecursively(destinationFile.toFile());
		}
	}

	public void deleteUserFile(Integer userId) {
		// String path = book.getBookName().toString() + "_" + book.getAuthor().toString();
		String path = "User_Profile" + "_" + userId.toString();
		Path destinationFile = this.rootLocation.resolve("User_Profile").resolve(path);
			if (destinationFile.toFile().exists()) {
				System.out.println("File Exists Delete");
				FileSystemUtils.deleteRecursively(destinationFile.toFile());
		}
	}

	// public void init() {
	// 	try {
	// 		Files.createDirectories(rootLocation);
	// 	} catch (IOException e) {
	// 		throw new HandleExceptionFile("Could not initialize storage", e);
	// 	}
	// }

	public byte[] downlioadImageFromFileSysteBook(int bookId){
		try {
			Path path = rootLocation.resolve("Book_Cover").resolve("Book_Cover" + "_" + bookId);
			if(path.toFile().exists()) {
				Path pathFile = Files.list(path).collect(Collectors.toList()).get(0);
				if(pathFile.toFile().exists()){
					return Files.readAllBytes(pathFile);
				}else{
					return null;
				}
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] downlioadImageFromFileSysteUser(int userId){
		try {
			Path path = rootLocation.resolve("User_Profile").resolve("User_Profile" + "_" + userId);
			if(path.toFile().exists()) {
				Path pathFile = Files.list(path).collect(Collectors.toList()).get(0);
				if(pathFile.toFile().exists()){
					return Files.readAllBytes(pathFile);
				}else{
					return null;
				}
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
