package model;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Represents a player in the Akropolis game.
 * It's connected directly to the model package.
 */
public class Client extends Player {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(String name, Socket socket) throws IOException {
        super(name);
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Client created");
    }

    // Method to send a tile to the server
    public void sendTile(Tile tile) throws IOException {
        out.writeObject(tile);
    }

    // Method to receive the tiles from the server
    public Tile receiveTile() throws IOException, ClassNotFoundException {
        return (Tile) in.readObject();
    }

    // Method to update the board with the new tiles
    public void updateBoard(ArrayList<Tile> newTiles) {
        for (Tile tile : newTiles) {
            // Incorrect method call fix the logic
            getGrid().addTile(tile);
        }
    }

    // Method to close the connection
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
