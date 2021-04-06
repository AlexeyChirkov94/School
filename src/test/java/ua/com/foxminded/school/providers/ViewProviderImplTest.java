package ua.com.foxminded.school.providers;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.assertj.core.api.Assertions.assertThat;

class ViewProviderImplTest {

    private static final ViewProvider VIEW_PROVIDER = new ViewProviderImpl();

    @Test
    void readShouldReturnStingArgumentsIsInputConsoleStream() {
        String data = "Hello, World!";
        System.setIn(new ByteArrayInputStream(data.getBytes()));

        String expected = "Hello, World!";
        String actual = VIEW_PROVIDER.read();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readIntReturnIntArgumentsIsInputConsoleStream() {
        String data = "5";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Integer expected = 5;
        Integer actual = VIEW_PROVIDER.readInt();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void printMessageNoReturnPrintStingIfArgumentsIsString() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String data = "hello again";
        VIEW_PROVIDER.printMessage(data);
        String actual = outContent.toString();
        String expected = "hello again\r\n";

        assertThat(actual).isEqualTo(expected);
    }

}
