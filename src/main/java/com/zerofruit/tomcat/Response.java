package com.zerofruit.tomcat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

@RequiredArgsConstructor
public class Response implements ServletResponse {
    private static final int BUFFER_SIZE = 1024;
    @Setter
    private Request request;
    @NonNull
    private OutputStream outputStream;

    /*
    * sendStaticResource serve static pages
    * */
    public void sendStaticResource() throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(Constants.WEB_ROOT, request.getUri());
            if (file.exists()) {
                fis = new FileInputStream(file);
                sendIndex(fis, file.length());
            }
            else {
                sendError();
            }
        } catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString() );
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    private void sendIndex(FileInputStream fis, long contentLength) throws IOException {
        String headerMessage = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + contentLength +"\r\n" +
                "\r\n";
        outputStream.write(headerMessage.getBytes());

        byte[] bytes = new byte[BUFFER_SIZE];

        int ch = fis.read(bytes, 0, BUFFER_SIZE);
        while (ch != -1) {
            outputStream.write(bytes, 0, ch);
            ch = fis.read(bytes, 0, BUFFER_SIZE);
        }
    }

    private void sendError() throws IOException {
        String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 23\r\n" +
                "\r\n" +
                "<h1>File Not Found</h1>";
        outputStream.write(errorMessage.getBytes());
    }

    /*
     * Implementation of the ServletResponse
     * */

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        // if autoFlush is true, println() will flush,
        // but print() will not.
        return new PrintWriter(outputStream, true);
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
