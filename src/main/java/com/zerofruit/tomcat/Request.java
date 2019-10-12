package com.zerofruit.tomcat;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class Request {
    @NonNull
    private InputStream inputStream;
//    @Getter
    private String uri;

    public void parse() {
        // Read a set of characters from the socket
        StringBuffer request = new StringBuffer(2048);
        int readSize;
        byte[] buffer = new byte[2048];
        try {
            readSize = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            readSize = -1;
        }

        for (int i=0; i < readSize; i++) {
            request.append((char) buffer[i]);
        }
        System.out.print(request.toString());
        this.uri = parseUri(request.toString());
    }

    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}
