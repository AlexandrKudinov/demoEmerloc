<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Game</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">


    <script>
        var van1 = new Image();
        van1.src = 'Van1.png';
        var van2 = new Image();
        van2.src = 'Van2.png';

        var path = window.location.pathname;
        var webCtx = path.substring(0, path.indexOf('/', 1));
        var endPointURL = "ws://" + window.location.host + webCtx + "/game";
        var playerConnection = null;


        function connect(event) {
            playerConnection = new WebSocket(endPointURL);

            playerConnection.onmessage = function (event) {
                let data = JSON.parse(event.data);
                var canvas = document.getElementById("field");
                var ctx = canvas.getContext('2d');
                ctx.fillStyle = 'rgb(24, 24, 24)';
                ctx.fillRect(0, 0, 1200, 600);

                if (data == 1) {
                    ctx.font = "bold 30px Calibri";
                    ctx.fillStyle = 'rgb(0, 168, 243)';
                    ctx.textAlign = "center";
                    ctx.fillText("waiting for another player...", canvas.width/2-35, canvas.height/2);
                    document.getElementById("button").innerHTML = "<p></p>";
                    document.getElementById("field").innerHTML = ctx;

                    return;
                }

                if (data == -1) {
                    ctx.font = "bold 30px Calibri";
                    ctx.fillStyle = 'rgb(0, 168, 243)';
                    ctx.textAlign = "center";
                    ctx.fillText("another player leaved game in fear, you win!", canvas.width/2-30, canvas.height/2);
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

                ctx.drawImage(van1, data.playerOnePosition[0], data.playerOnePosition[1], 20, 20);
                ctx.drawImage(van2, data.playerTwoPosition[0], data.playerTwoPosition[1], 20, 20);
                document.getElementById("field").innerHTML += ctx;
            };
        }


        function disconnect() {
            playerConnection.close();
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

        var jsonObj;
        window.addEventListener("click", checkMouseClick);

        function checkMouseClick(mouse) {
            jsonObj = mouse.clientX + " " + mouse.clientY;
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
    </script>

    <style>
        body {
            background-color: RGB(24, 24, 24);
            color: #22acff;
            margin: 0;
        }

        #field {
            margin-left: 40px;
            margin-top: 10px;
            width: 1200px;
            height: 600px;
        }
    </style>


</head>
<body onload="connect();" onunload="disconnect()">
<div id="button"><button style="position: absolute; top:50%; left:50%;" onclick="myFunction()">Play</button> </div>
<canvas id="field" width="1200" height="600">
    <script type="text/javascript">
        var canvas = document.getElementById("field");
        var ctx = canvas.getContext('2d');
        ctx.font = "bold 50px Calibri";
        ctx.fillStyle = "red";
        ctx.textAlign = "center";
        ctx.fillText("Emer", canvas.width/2-15, canvas.height/2-30);
        ctx.fillStyle = 'rgb(0, 168, 243)';
        ctx.textAlign = "center";
        ctx.fillText("loc", canvas.width/2+70, canvas.height/2-30);
    </script>
</canvas>

</body>
</html>


