package vn.tat.hexagonal.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.tat.hexagonal.dto.MemoryStats;
import vn.tat.hexagonal.message.ResponseFile;
import vn.tat.hexagonal.message.ResponseMessage;
import vn.tat.hexagonal.model.FileDB;
import vn.tat.hexagonal.provision.config.DatabaseConnectionConfig;
import vn.tat.hexagonal.provision.pipe.DataPipeLineManager;
import vn.tat.hexagonal.provision.pipe.IngestDataPipeOperator;
import vn.tat.hexagonal.provision.pipe.PostgresDatabasePipeOperator;
import vn.tat.hexagonal.provision.processor.IngestDataProcessor;
import vn.tat.hexagonal.provision.processor.ReadDataProcessor;
import vn.tat.hexagonal.provision.processor.WriteDataProcessor;
import vn.tat.hexagonal.provision.util.SqlBuilder;
import vn.tat.hexagonal.service.FileStorageService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 10/20/21 13:52
 */
@RestController
//@RequestMapping(path = "simple", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    private final FileStorageService storageService;

    private final DatabaseConnectionConfig databaseConnectionConfig;

    public FileController(FileStorageService fileStorageService, DatabaseConnectionConfig databaseConnectionConfig) {
        this.storageService = fileStorageService;
        this.databaseConnectionConfig = databaseConnectionConfig;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("files") MultipartFile[] files) {
        String message = "";
        try {
            for (MultipartFile f : files)
                storageService.store(f);

            message = "Uploaded the file successfully: " + files[0].getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + files[0].getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/upload/{param}")
    public ResponseEntity<?> doProvision(@PathVariable final long param) throws IOException, SQLException {

        PostgresDatabasePipeOperator dbOper1 = new PostgresDatabasePipeOperator(databaseConnectionConfig.getSource(), new ReadDataProcessor());

        IngestDataPipeOperator dataPipeOperator = new IngestDataPipeOperator(dbOper1.getStreamInfra().getDataItor(), new IngestDataProcessor());

        PostgresDatabasePipeOperator dbOper2 = new PostgresDatabasePipeOperator(databaseConnectionConfig.getTarget(), dataPipeOperator.getStreamInfra().getDataItor(), new WriteDataProcessor());

        dbOper1.setSqlStatement(SqlBuilder.buildSource());
        dbOper2.setSqlStatement(SqlBuilder.buildTarget());

        DataPipeLineManager dataPipeLineManager = new DataPipeLineManager();
        dataPipeLineManager.addOperator(dbOper1);
        dataPipeLineManager.addOperator(dataPipeOperator);
        dataPipeLineManager.addOperator(dbOper2);
        dataPipeLineManager.runPipeLine();

        return new ResponseEntity<>(new ResponseMessage("DONE"), HttpStatus.OK);
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();
            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getDataField().length);

        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) throws SQLException {
        FileDB fileDB = storageService.getFile(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE); // (3) Content-Type: application/octet-stream
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\""); // (4) Content-Disposition: attachment; filename="demo-file.txt"

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(fileDB.getDataField());

        /*return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(fileDB.getDataField().getBytes(0, (int) fileDB.getDataField().length()));*/

       /* InputStreamResource resource = new InputStreamResource(fileDB.getDataField().getBinaryStream());
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resource);*/
    }

    @GetMapping("memory-status")
    public MemoryStats getMemoryStatistics() {
        MemoryStats stats = new MemoryStats();
        stats.setHeapSize(Runtime.getRuntime().totalMemory());
        stats.setHeapMaxSize(Runtime.getRuntime().maxMemory());
        stats.setHeapFreeSize(Runtime.getRuntime().freeMemory());
        return stats;
    }
}