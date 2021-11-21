package vn.tat.hexagonal.provision.util;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:50
 */
public class SqlBuilder {

    public static String buildSource() {
        return "COPY (SELECT col1, col2, col3 FROM schema_source.table_source) TO STDIN WITH CSV HEADER";
    }

    public static String buildTarget() {
        return "COPY schema_target.table_target from STDOUT WITH CSV";
    }
}
