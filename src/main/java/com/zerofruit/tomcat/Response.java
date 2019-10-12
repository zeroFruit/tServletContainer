package com.zerofruit.tomcat;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.*;

@RequiredArgsConstructor
public class Response {
    private static final int BUFFER_SIZE = 1024;
    @Setter
    private Request request;
    @NonNull
    private OutputStream outputStream;

    public void sendStaticResource() throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUri());
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
}
