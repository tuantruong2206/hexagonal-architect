package vn.tat.hexagonal.dto;

import lombok.Data;
import lombok.Getter;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 10/20/21 17:06
 */
@Data
public class MemoryStats {
    private long heapSize;
    private long heapMaxSize;
    private long heapFreeSize;
}
