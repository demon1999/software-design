package ru.demon1999.sd.refactoring.servlet;

import org.junit.Test;
import ru.demon1999.sd.refactoring.writer.WriterHTML;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class WriterHTMLTest {

    @Test
    public void testPrintInt() {
        StringWriter stringWriter = new StringWriter();
        WriterHTML writer = new WriterHTML(new PrintWriter(stringWriter));
        writer.printInt(10);
        assertEquals(stringWriter.toString(), "10\n");
    }

    @Test
    public void testPrintString() {
        StringWriter stringWriter = new StringWriter();
        WriterHTML writer = new WriterHTML(new PrintWriter(stringWriter));
        writer.printString("Hey!");
        assertEquals(stringWriter.toString(), "Hey!\n");
        writer.printString("");
        assertEquals(stringWriter.toString(), "Hey!\n\n");
    }

    @Test
    public void testPrintStartTags() {
        StringWriter stringWriter = new StringWriter();
        WriterHTML writer = new WriterHTML(new PrintWriter(stringWriter));
        writer.printStartTags();
        assertEquals(stringWriter.toString(), "<html><body>\n");
    }

    @Test
    public void testPrintEndTags() {
        StringWriter stringWriter = new StringWriter();
        WriterHTML writer = new WriterHTML(new PrintWriter(stringWriter));
        writer.printEndTags();
        assertEquals(stringWriter.toString(), "</body></html>\n");
    }

    @Test
    public void testPrintNamePrice() {
        StringWriter stringWriter = new StringWriter();
        WriterHTML writer = new WriterHTML(new PrintWriter(stringWriter));
        writer.printNamePrice("s", 365);
        assertEquals(stringWriter.toString(), "s\t365</br>\n");
    }

    @Test
    public void testPrintWithH1() {
        StringWriter stringWriter = new StringWriter();
        WriterHTML writer = new WriterHTML(new PrintWriter(stringWriter));
        writer.printWithH1("SSS");
        assertEquals(stringWriter.toString(), "<h1>SSS</h1>\n");
    }
}
