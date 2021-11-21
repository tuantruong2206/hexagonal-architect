package vn.tat.hexagonal.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 10/20/21 13:51
 */
@Data
@AllArgsConstructor
public class ResponseMessage {
    private String message;
}
