package sit.cp23ej2.services;

import java.io.File;
import java.io.IOException;
// import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.http.fileupload.FileUtils;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

// import sit.cp23ej2.exception.HandleExceptionFile;
import sit.cp23ej2.exception.HandleExceptionNotFound;
import sit.cp23ej2.properties.FileStorageProperties;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

     public FileStorageService(FileStorageProperties fileStorageProperties) throws HandleExceptionNotFound, IOException, Exception {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new HandleExceptionNotFound("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    // public Resource loadFileAsResource(String fileName, Integer id) throws HandleExceptionNotFound {
        // Event event = eventRepository.findById(id).orElseThrow(() -> new MyFileNotFoundException("Event not found with id " + id));
        // String userDir = event.getBookingEmail() != null ? "User/" + "User_" + event.getBookingEmail() : "Guest";
        // String eventDir = "Event_" + event.getId().toString();

        // System.out.println("userDir: " + userDir);
        // System.out.println("eventDir: " + eventDir);

        // try {
            // Path filePath = this.fileStorageLocation.resolve(userDir).resolve(eventDir).resolve(fileName).normalize();
            // Resource resource = new UrlResource(filePath.toUri());
            // System.out.println(filePath.toUri());
            // System.out.println(resource);
            // if(resource.exists()) {
            //     return resource;
            // } else {
            //     throw new HandleExceptionNotFound("File not found " + "eventNo" + fileName);
            // }
        // } catch (MalformedURLException ex) {
        //     throw new HandleExceptionFile("File not found " + "eventNo" + fileName, ex);
        // }
    // }

    public void deleteFile(String fileName) throws IOException, HandleExceptionNotFound {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.delete(filePath);
        } catch (IOException ex) {
            throw new HandleExceptionNotFound("File not found " + fileName, ex);
        }
    }

    public void deleteDir(String dir) throws IOException, HandleExceptionNotFound {
        try {
            FileUtils.deleteDirectory(new File(dir));
        } catch (IOException ex) {
            throw new HandleExceptionNotFound("Dir not found " + dir, ex);
        }
    }

}
