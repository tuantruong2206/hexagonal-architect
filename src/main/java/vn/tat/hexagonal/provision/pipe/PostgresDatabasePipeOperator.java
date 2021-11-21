package vn.tat.hexagonal.provision.pipe;

import lombok.Data;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import vn.tat.hexagonal.provision.config.CsvConfig;
import vn.tat.hexagonal.provision.config.DatabaseConnectionConfig;
import vn.tat.hexagonal.provision.processor.DataProcessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Tuan.Truong Brian
 * @version 1.0
 * Class INFO
 * @date 11/21/21 09:51
 */
@Data
public class PostgresDatabasePipeOperator  extends PipeOperator{

    private Connection connection;

    private DatabaseConnectionConfig.ConnectionConfig connectionConfig;

    private CopyManager copyManager;

    private String sqlStatement;

    //copy from NAB, hence will do hard coding to build SQL as well as data process
//    private DatasetConfig
    public PostgresDatabasePipeOperator(DatabaseConnectionConfig.ConnectionConfig connectionConfig, DataProcessor dp) throws IOException, SQLException {
        super();
        this.connectionConfig = connectionConfig;
        Properties props = new Properties();
        props.setProperty("user", connectionConfig.getUsername());
        props.setProperty("password", connectionConfig.getPassword());
        this.connection = DriverManager.getConnection(connectionConfig.getUrl(),props);
        this.connection.setAutoCommit(false);
        this.copyManager = new CopyManager((BaseConnection) connection);
        this.dp = dp;
    }

    public PostgresDatabasePipeOperator(DatabaseConnectionConfig.ConnectionConfig connectionConfig, Iterator<String[]> itor, DataProcessor dp) throws IOException, SQLException {
        this(connectionConfig, dp);
        this.inputItor = itor;
        //point data out stream into the writer, then we traverse data from inputItor then write data out from there to the writer
        this.writer = CsvConfig.createWriter(this.streamInfra.getDataOutStream());
    }

    @Override
    protected void execute() {
        dp.process(this);
    }

    public Void copyIn() {
        try {
            this.copyManager.copyIn(sqlStatement, this.streamInfra.getDataInStream());
            connection.commit();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Void writeDataToStream() {
        while (inputItor.hasNext()) {
            String[] data = inputItor.next();
            writer.writeRow(data);
            logger.info("written row for table={}", data);
        }
        writer.close();
        logger.info("finish writing data to target=");
        return null;
    }

    public Void copyOut() {
        try {
            this.copyManager.copyOut(sqlStatement, this.streamInfra.getDataOutStream());
            this.streamInfra.getDataOutStream().close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        super.close();
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
