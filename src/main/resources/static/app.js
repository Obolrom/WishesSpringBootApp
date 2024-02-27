const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket',
    connectHeaders: {
        // Replace 'Your-Authorization-Token' with your actual token
        Authorization: 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNSIsInJvbGUiOiJVU0VSIiwiaXNzIjoicnJyb21peCIsImV4cCI6MTc5NTM4NTEyOSwiaWF0IjoxNzA4OTg1MTI5LCJqdGkiOiJhZWYyMDVjYS03ZGVhLTQ1MjMtOGVkYy1mZmM4MzRiMjM2MWMiLCJ1c2VybmFtZSI6InJvbWl4In0.zhp17zjQ1fRKRM23qgWZRaiHJQwVd_h3OAQhgLPrNNM'
    },
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    // stompClient.subscribe('/topic/greetings', (greeting) => {
    //     showGreeting(JSON.parse(greeting.body).message);
    console.log('/user/' + $("#author").val() + '/queue/chat')
    stompClient.subscribe('/user/' + $("#author").val() + '/queue/chat', (greeting) => {
        showGreeting(JSON.parse(greeting.body).message);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({
        // destination: "/app/hello",
        // body: JSON.stringify({'message': $("#name").val()})
        destination: "/chat/direct",
        body: JSON.stringify({'message': $("#name").val(), 'authorId': $("#author").val(), 'receiverId': $("#receiver").val()})
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});