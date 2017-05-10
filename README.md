# WeTranslate

## Compile
    mvn clean package
    
## Run LoadBalancer
    java -cp target/wetranslate.jar loadbalancer.LoadBalancer <port>
    
## Run Node
    java -cp target/wetranslate.jar:dependencies/pgjdbc.jar server.Node <port>
