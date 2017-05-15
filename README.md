# WeTranslate

## Compile
    mvn clean package
    
## Run LoadBalancer
    java -cp target/wetranslate.jar:dependencies/* loadbalancer.LoadBalancer -p <port>    
## Run Node
    java -cp target/wetranslate.jar:dependencs/* node.Node [-localhost] -p <port>

