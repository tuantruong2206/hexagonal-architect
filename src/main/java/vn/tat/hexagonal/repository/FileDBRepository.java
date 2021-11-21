package vn.tat.hexagonal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tat.hexagonal.model.FileDB;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 10/20/21 13:40
 */
@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {
}
