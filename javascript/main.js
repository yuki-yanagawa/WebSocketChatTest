var webSocket;
$(function(){
    $("#websocketconnect").on('click',function(e){
        webSocket = new WebSocket("ws://127.0.0.1:9998");
        webSocket.onopen = function() {
            console.log("open ....");
            $("#websocketconnect").attr('disabled', true);
            $("#sendMess").attr('disabled', false);
        };
        webSocket.onerror = function() {
            console.log("error ....");
            $("#websocketconnect").attr('disabled', false);
            $("#sendMess").attr('disabled', true);
        };
        webSocket.onmessage = function(e) {
            console.log(e.data);
        };
        webSocket.onclose = function() {
            $("#websocketconnect").attr('disabled', false);
            $("#sendMess").attr('disabled', true);
        };
    });

    $("#sendMess").on('click',function(e){
        if($("#sendtext").val() === '') {
            return;
        }
        webSocket.send($("#sendtext").val());
    });
    $("#sendMess").attr('disabled', true);
});

