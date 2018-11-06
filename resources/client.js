'use strict';

var socket = null;

function appendMessage(msg) {
    document.getElementById("messages").innerHTML += msg + "\n";
}

function clearChat() {
    document.getElementById("messages").innerHTML = "";
}

// message handler
function message(object) {
    switch (object.type) {
        case "text":
            appendMessage(object.from + ": " + object.text);
            break;
        case "users":
            var users = document.getElementById("users");
            users.innerHTML = object.users;
            break;
    }
}

function closed(e) {
    appendMessage("-------------------");
    appendMessage("Lost connection to server");
    appendMessage("please refresh page");
}

function opened(e) {
    appendMessage("Connected to server");
    appendMessage("-------------------");
}

function resolveWsUrl(url) {
    var location = window.location;
    var protocol = location.protocol == "https:" ? "wss:" : "ws:";

    return protocol + "//" + location.host + url;
}

function randomId() {
    return Math.floor(((1 + Math.random()) * 0x1000000)).toString(16);
}

function connect(url) {
    var socket = new WebSocket(resolveWsUrl(url));
    socket.onopen = opened;
    socket.onclose = closed;
    socket.onmessage = function (event) {
        var obj = JSON.parse(event.data);
        message(obj);
    }

    return socket;
}

function sendMessage(e) {
    var text = document.getElementById("message").value;
    var login = document.getElementById("login").value;
    socket.send(JSON.stringify(
    {type: "message",
     text: text}));
}

window.onload = function () {
    var login = document.getElementById("login").value;
    socket = connect("/ws?session=" + randomId() + "&login=" + login);
}
