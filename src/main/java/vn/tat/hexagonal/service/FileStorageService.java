package vn.tat.hexagonal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.tat.hexagonal.model.FileDB;
import vn.tat.hexagonal.repository.FileDBRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 10/20/21 13:41
 */
@Service

public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

        return fileDBRepository.save(FileDB);
    }

//    @Transactional(readOnly = false)
    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }
}
