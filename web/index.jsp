<%@ page import="logic.*" %>
<%@ page import="websocket.GameServerEndPoint" %>
<%@ page import="java.awt.*" %>
<%@ page import="javax.websocket.Session" %>
<%@ page import="websocket.GameSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Game</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <script src="chat.js"></script>

    <script>
        var path = window.location.pathname;
        var webCtx = path.substring(0, path.indexOf('/', 1));
        var endPointURL = "ws://" + window.location.host + webCtx + "/game";
        var playerConnection = null;

        function connect() {
            playerConnection = new WebSocket(endPointURL);
            refresh();
            playerConnection.onmessage = function (event) {
                var msg = event.data;
                document.getElementById("contentfile").innerHTML = "      <b>"+event.data+"</b>";
                refresh();
            };
        }

        function disconnect() {
            playerConnection.close();
             refresh();
        }


        function refresh() {
            //  var myDate = new java.util.Date();
            <%--<%--%>
            <%--// if (GameServerEndPoint.getPlayersSessions().size()==1){--%>
            <%--for(int i = 0;i<10;i++){%>--%>
            <%--document.getElementById("contentfile").innerHTML += "      <b>myDate</b><br><hr><br>" + counter;--%>
            <%--counter++;--%>
            <%--<%}%>--%>


            <%--<%for (int i = 0;i<100;i+=20){%>--%>
            <%--document.getElementById("field").innerHTML +="<rect x=" + <%=i%> + " y=0 width=10 height=10 fill='rgb(65, 65, 65)' />";--%>
            <%--<%}%>--%>


            <%--<%--%>
            <%--GameSession gameSession = GameServerEndPoint.getCurrentGameSession();--%>
            <%--if (gameSession != null) {--%>
            <%--WaterSupplyMap waterSupplyMap = gameSession.getWaterSupplyMap();--%>
            <%--for (Pipeline pipeline : waterSupplyMap.getPipelines()) {--%>
            <%--for (House house : pipeline.getHouses()) {--%>
            <%--for (Node node : house.getHouseFragments()) {--%>
            <%--int i = node.getI() * 20;--%>
            <%--int j = node.getJ() * 20;%>--%>
            <%--document.getElementById("field").innerHTML +="<rect x=" + <%=j%> + " y=" +  <%=i%> + " width=20 height=20 fill='rgb(65, 65, 65)' />";--%>
            <%--<% }--%>
            <%--}--%>
            <%--}--%>
            <%--}--%>
            <%--%>--%>


        }


        var jsonObj;
        window.addEventListener("click", checkMouseClick);

        function checkMouseClick(mouse) {
            jsonObj = mouse.clientX + " , " + mouse.clientY;
            playerConnection.send(JSON.stringify(jsonObj));
        }

        window.addEventListener("keydown", checkKeyPress);

        function checkKeyPress(key) {
            if (key.keyCode === 87) {
                jsonObj = "UP";
            }
            if (key.keyCode === 65) {
                jsonObj = "LEFT";
            }
            if (key.keyCode === 68) {
                jsonObj = "RIGHT";
            }
            if (key.keyCode === 83) {
                jsonObj = "DOWN";
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
<div id="contentfile"><%--<b>NOW ON AIR</b><br>--%></div>
<svg id='field'></svg>
</body>
</html>


