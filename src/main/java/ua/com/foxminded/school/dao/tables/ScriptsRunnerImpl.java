package ua.com.foxminded.school.dao.tables;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.exception.DataBaseSqlRuntimeException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ScriptsRunnerImpl implements ScriptsRunner {

    private static final Logger LOGGER = Logger.getLogger(ScriptsRunnerImpl.class);
    private static final String  LINE_BREAKER = "\n";

    private ConnectorDB connectorDB;

    @Inject
    public ScriptsRunnerImpl(ConnectorDB connectorDB) {
        this.connectorDB = connectorDB;
    }

    @Override
    public void runScript(String pathToSQLScript) throws IOException {

        try(Connection connection = connectorDB.getConnection();
            Statement statement = connection.createStatement()){
            statement.execute(readQuery(pathToSQLScript));
        }catch (SQLException e){
            LOGGER.info("DB connection error: ", e);
            throw new DataBaseSqlRuntimeException("DB connection error: ", e);
        }
    }

    private String readQuery(String pathToSQLScript) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(pathToSQLScript));

        return String.join(LINE_BREAKER, lines);
    }
}
