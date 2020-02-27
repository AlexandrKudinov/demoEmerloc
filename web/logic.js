var van1 = new Image();
van1.src = 'Van1.png';
var van2 = new Image();
van2.src = 'Van2.png';

var path = window.location.pathname;
var webCtx = path.substring(0, path.indexOf('/', 1));
var endPointURL = "ws://" + window.location.host + webCtx + "/game";
var playerConnection = null;
var jsonObj;

function connect(event) {
    playerConnection = new WebSocket(endPointURL);

    playerConnection.onmessage = function (event) {
        document.getElementById("button").innerHTML = "<p></p>";
        let data = JSON.parse(event.data);
        var canvas = document.getElementById("field");
        var ctx = canvas.getContext('2d');
        ctx.fillStyle = 'rgb(24, 24, 24)';
        ctx.fillRect(0, 0, 1200, 600);

        if (data === 1) {
            ctx.font = "bold 30px Calibri";
            ctx.fillStyle = 'rgb(0, 168, 243)';
            ctx.textAlign = "center";
            ctx.fillText("waiting for another player...", canvas.width / 2 - 35, canvas.height / 2);
            document.getElementById("field").innerHTML = ctx;
            return;
        }

        if (data === -1) {
            ctx.font = "bold 30px Calibri";
            ctx.fillStyle = 'rgb(0, 168, 243)';
            ctx.textAlign = "center";
            ctx.fillText("another player leaved game in fear, you win!", canvas.width / 2 - 30, canvas.height / 2);
            document.getElementById("field").innerHTML = ctx;
            return;
        }

        if (data === -5) {
            ctx.font = "bold 30px Calibri";
            ctx.fillStyle = 'rgb(0, 168, 243)';
            ctx.textAlign = "center";
            ctx.fillText("You lose!", canvas.width / 2 - 30, canvas.height / 2);
            document.getElementById("field").innerHTML = ctx;
            return;
        }

        if (data === 5) {
            ctx.font = "bold 30px Calibri";
            ctx.fillStyle = 'rgb(0, 168, 243)';
            ctx.textAlign = "center";
            ctx.fillText("Congratulations! You win!", canvas.width / 2 - 30, canvas.height / 2);
            document.getElementById("field").innerHTML = ctx;
            return;
        }


        for (let i = 0; i < data.houses.length; i++) {
            let {coords, flag} = data.houses[i];
            drawHouse(coords, flag);
        }
        for (let i = 0; i < data.valves.length; i++) {
            let {coords, flag} = data.valves[i];
            drawValve(coords, flag);
        }
        for (let i = 0; i < data.dots.length; i++) {
            let {coords, flag} = data.dots[i];
            drawDot(coords, flag);
        }
        for (let i = 0; i < data.horizontalPlumbs.length; i++) {
            let {coords, flag} = data.horizontalPlumbs[i];
            drawHorizontalPlumb(coords, flag);
        }
        for (let i = 0; i < data.verticalPlumbs.length; i++) {
            let {coords, flag} = data.verticalPlumbs[i];
            drawVerticalPlumb(coords, flag);
        }
        for (let i = 0; i < data.emergencies.length; i++) {
            let {coords, flag} = data.emergencies[i];
            drawEmergency(coords, flag);
        }

        ctx.drawImage(van1, data.playerOnePosition[0], data.playerOnePosition[1], 20, 20);
        ctx.drawImage(van2, data.playerTwoPosition[0], data.playerTwoPosition[1], 20, 20);

        ctx.font = "bold 30px Calibri";
        ctx.fillStyle = "orange";
        ctx.fillText(data.playerOneScore, canvas.width / 2 - 10, canvas.height-10);
        ctx.fillStyle = 'green';
        ctx.fillText(data.playerTwoScore, canvas.width / 2 + 15, canvas.height-10);



        document.getElementById("field").innerHTML = ctx;

    };
}

function disconnect() {
    playerConnection.close();
}

function drawEmergency(coords, flag) {
    let color = flag === true ? 'red' : 'rgb(14, 209, 69)';
    var canvas = document.getElementById("field");
    var ctx = canvas.getContext('2d');
    ctx.fillStyle = color;
    ctx.fillRect(coords[0] + 6, coords[1] + 6, 8, 8);
}

function drawHorizontalPlumb(coords, flag) {
    let color = flag === true ? 'rgb(0, 168, 243)' : 'rgb(0, 67, 114)';
    var canvas = document.getElementById("field");
    var ctx = canvas.getContext('2d');
    ctx.fillStyle = color;
    ctx.fillRect(coords[0], coords[1], 12, 4);
}

function drawVerticalPlumb(coords, flag) {
    let color = flag === true ? 'rgb(0, 168, 243)' : 'rgb(0, 67, 114)';
    var canvas = document.getElementById("field");
    var ctx = canvas.getContext('2d');
    ctx.fillStyle = color;
    ctx.fillRect(coords[0], coords[1], 4, 12);
}

function drawHouse(coords, flag) {
    let color = flag === true ? 'rgb(65, 65, 65)' : 'rgb(45, 45, 45)';
    var canvas = document.getElementById("field");
    var ctx = canvas.getContext('2d');
    ctx.fillStyle = color;
    ctx.fillRect(coords[0], coords[1], 20, 20);
}


function drawDot(coords, flag) {
    let color = flag === true ? 'rgb(0, 168, 243)' : 'rgb(0, 67, 114)';
    var canvas = document.getElementById("field");
    var ctx = canvas.getContext('2d');
    ctx.fillStyle = color;
    ctx.fillRect(coords[0], coords[1], 4, 4);
}


function drawValve(coords, flag) {
    let color = flag === true ? 'white' : 'red';
    var canvas = document.getElementById("field");
    var ctx = canvas.getContext('2d');
    ctx.fillStyle = color;
    ctx.fillRect(coords[0], coords[1], 4, 4);
}


window.addEventListener("click", checkMouseClick);

function checkMouseClick(mouse) {
    jsonObj = mouse.clientX + " " + mouse.clientY;
    //var  jsonObj1 = [mouse.clientX, mouse.clientY];
    playerConnection.send(JSON.stringify(jsonObj));
}

window.addEventListener("keydown", checkKeyPress);

function checkKeyPress(key) {
    if (key.keyCode === 87) {
        jsonObj = 0;
    }
    if (key.keyCode === 65) {
        jsonObj = 1;
    }
    if (key.keyCode === 68) {
        jsonObj = 2;
    }
    if (key.keyCode === 83) {
        jsonObj = 3;
    }
    playerConnection.send(JSON.stringify(jsonObj));
}