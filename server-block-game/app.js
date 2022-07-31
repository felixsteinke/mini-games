const Express = require("express")();
const HttpServer = require("http").Server(Express);
const SocketIO = require("socket.io")(HttpServer, {
    cors: {
        origin: "http://localhost:8080"
    }
});

class RoomState {
    connections = 1;
    position = {
        x: 200,
        y: 200
    }
}

const roomStates = new Map();

SocketIO.on("connection", (socket) => {
    socket.on("join", (room) => {
        socket.join(room);
        if (roomStates.has(room)) {
            const state = roomStates.get(room);
            state.connections++;
            roomStates.set(room, state);
        } else {
            roomStates.set(room, new RoomState());
        }
        SocketIO.sockets.in(room).emit("position", roomStates.get(room).position);
        console.log("Room Connection", roomStates.get(room), socket.rooms);

        socket.on("move", (data) => {
            const state = roomStates.get(room);
            switch (data) {
                case "up":
                    state.position.y -= 5;
                    break;
                case "down":
                    state.position.y += 5;
                    break;
                case "left":
                    state.position.x -= 5;
                    break;
                case "right":
                    state.position.x += 5;
                    break;
            }
            SocketIO.sockets.in(room).emit("position", roomStates.get(room).position);
            console.log("Movement", roomStates.get(room));
        });

        socket.on("leave", (room) => {
            socket.leave(room);
            if (roomStates.has(room)) {
                const state = roomStates.get(room);
                state.connections--;
                roomStates.set(room, state);
            }
            console.log("Room Disconnect", roomStates.get(room), socket.rooms);
        });
    });
});

HttpServer.listen(3000, () => {
    console.log("Server listening on port: 3000");
});
