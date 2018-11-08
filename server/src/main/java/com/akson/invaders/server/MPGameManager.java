package com.akson.invaders.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Server for multi-player game mode. Manages game state by sending/receiving UDP packets to/from players. TODO.
 */
public class MPGameManager extends Thread {

    private Logger logger = LoggerFactory.getLogger(MPGameManager.class);

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024];

    public MPGameManager() {
        try {
            socket = new DatagramSocket();
            setName("MPGameManager Thread " + socket.getLocalPort());
            logger.info("MPGameManager started on port: " + socket.getLocalPort());

        } catch (SocketException e) {
            logger.error("Error when creating the MPGameManager DatagramSocket", e);
        }
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try {
                logger.info("MPGameManager " + socket.getLocalPort() + " waiting...");

                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();

            int port = packet.getPort();

            packet = new DatagramPacket(buf, buf.length, address, port);

            String received = new String(packet.getData(), 0, packet.getLength());

            if (received.equals("end")) {
                running = false;
                continue;
            }

            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        socket.close();
    }

    /**
     * Returns the server DatagramSocket port which clients should be able to connect.
     *
     * @return MPGameManager DatagramSocket port
     */
    public int getPort() {
        return socket.getLocalPort();
    }
}
