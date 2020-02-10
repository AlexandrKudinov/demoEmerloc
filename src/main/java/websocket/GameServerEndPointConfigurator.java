
package websocket;


import javax.websocket.server.ServerEndpointConfig.Configurator;


public class GameServerEndPointConfigurator extends Configurator {

    private GameServerEndPoint gameServer = new GameServerEndPoint();
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass)
            throws InstantiationException {
        return (T)gameServer;
    }
}


