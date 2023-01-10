package org.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) throws Throwable {
        ServerSocket server = new ServerSocket(9889);
        while (true) {
            Socket socket = server.accept();
            System.out.println("Client accepted.");
            Thread service = new Thread(new ServiceThread(socket));
            service.run();
        }
    }

    private static class ServiceThread implements Runnable {
        private Socket socket;
        private InputStream input;
        private OutputStream output;

        public ServiceThread(Socket socket) throws IOException {
            this.socket = socket;
            input = socket.getInputStream();
            output = socket.getOutputStream();
        }

        @Override
        public void run() {
            try {
                readInputHeaders();
                writeResponse("<html><body><h1>Server accept you request!</h1></body></html>");
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            output.write(result.getBytes());
            output.flush();
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            while(true) {
                String s = br.readLine();
                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
        }
    }
}
