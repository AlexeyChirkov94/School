package ua.com.foxminded.school.dao.tables;

import java.io.IOException;

public interface ScriptsRunner {

    void runScript(String pathToSQLScript) throws IOException;
}
