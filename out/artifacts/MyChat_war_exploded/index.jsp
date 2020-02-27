<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Game</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <script src="logic.js"></script>

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


<div id="button">
    <button style="position: absolute; top:50%; left:620px;" onclick="myFunction()">Play</button>
</div>


<canvas id="field" width="1200" height="600">
    <script type="text/javascript">
        var canvas = document.getElementById("field");
        var ctx = canvas.getContext('2d');
        ctx.font = "bold 50px Calibri";
        ctx.fillStyle = "red";
        ctx.textAlign = "center";
        ctx.fillText("Emer", canvas.width / 2 - 30, canvas.height / 2 - 30);
        ctx.fillStyle = 'rgb(0, 168, 243)';
        ctx.textAlign = "center";
        ctx.fillText("loc", canvas.width / 2 + 55, canvas.height / 2 - 30);
    </script>
</canvas>

</body>
</html>


