package vn.tat.hexagonal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.engine.jdbc.BlobProxy;

import javax.persistence.*;
import java.sql.Blob;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 10/20/21 13:38
 */
@Entity
@Getter
@Setter
@Table(name = "files")
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

//    @Lob
//    private byte[] data;

    @Lob
//    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] dataField;

    public FileDB() {
    }

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
//        this.data = data;
//        this.dataField = BlobProxy.generateProxy(data);
        this.dataField = data;
    }
}
