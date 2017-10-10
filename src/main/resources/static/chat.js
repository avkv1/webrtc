navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
var loggedIn = false;
var configuration = {
    'iceServers': [{
        'url': 'stun:stun.l.google.com:19302'
    }]
};
var pc;
var peer;

function logError(error) {
    console.log(error.name + ': ' + error.message);
}

function connect(username) {
    console.log('connect: '+username);
    var loc = window.location;
    var uri = "ws://" + loc.hostname + ":8080/signal";
    sock = new WebSocket(uri);

    sock.onopen = function(e) {
        console.log('open', e);
        sock.send(
            JSON.stringify(
                {
                    type: "login",
                    data: username
                }
            )
        );
        loggedIn = true;
    }

    sock.onclose = function(e) {
        console.log('close', e);
    }

    sock.onerror = function(e) {
        console.log('error', e);
    }

    sock.onmessage = function(e) {
        console.log('get message', e.data);
        if (!pc) {
            startRTC();
        }

        var message = JSON.parse(e.data);
        if (message.type === 'rtc') {
            if (message.data.sdp) {
                pc.setRemoteDescription(
                    new RTCSessionDescription(message.data.sdp),
                    function () {
                        // if we received an offer, we need to answer
                        if (pc.remoteDescription.type == 'offer') {
                            peer = message.dest;
                            pc.createAnswer(localDescCreated, logError);
                        }
                    },
                    logError);
            }  else {
                pc.addIceCandidate(new RTCIceCandidate(message.data.candidate));
            }
        }
    }

}

function startRTC() {
    console.log("Start RTC");

    pc = new RTCPeerConnection(configuration);

    pc.onicecandidate = function (evt) {
        if (evt.candidate) {
            sendMessage(
                {
                    type: "rtc",
                    dest: peer,
                    data: {
                        'candidate': evt.candidate
                    }
                }
            );
        }
    };

    // once remote stream arrives, sho480w it in the remote video element
    pc.onaddstream = function (evt) {
        console.log("pc.onaddstream");
        // $('#remoteView').src = URL.createObjectURL(evt.stream);
        remoteView.src = URL.createObjectURL(evt.stream);
    };

    // get a local stream, show it in a self-view and add it to be sent
    navigator.getUserMedia({
        'audio': true,
        'video': true
    }, function (stream) {
        selfView.src = URL.createObjectURL(stream);
        pc.addStream(stream);
    }, logError);

}

function offer(dest) {
    peer = dest;
    pc.createOffer(localDescCreated, logError);
}

function localDescCreated(desc) {
    pc.setLocalDescription(desc, function () {
        sendMessage(
            {
                type: "rtc",
                dest: peer,
                data: {
                    'sdp': pc.localDescription
                }
            }
        );
    }, logError);
};

function sendMessage(payload) {
    console.log("send message: "+JSON.stringify(payload));
    sock.send(JSON.stringify(payload));
}

function disconnect() {
    console.log('disconnect');
    if(sock != null) {
        sock.close();
    }
    loggedIn = false;
}
