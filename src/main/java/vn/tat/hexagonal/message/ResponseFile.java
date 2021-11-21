package vn.tat.hexagonal.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 10/20/21 13:47
 */
@AllArgsConstructor
@Data
public class ResponseFile {
    private String name;
    private String url;
    private String type;
    private long size;

}
