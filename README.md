# CloudWorker
For a developer or small team moving to the cloud can actually be expensive and tedious. Take advantage of the processing power of your old PC, laptop or Raspberry Pi. Turn any computer into a processing-node! You only need to install a small python agent on your old machine.
Manage and track all your cloudworker nodes through a centralised management system. Too easy!

#Features
* Process multiple commands/scripts on each code
* Send one off actions to a machine
* Elegant central management system
* Very easy installation
* You can run anything on your existing machine locally

#To Run

##Server

```
java -jar cloudworker-0.0.1-SNAPSHOT.war --spring.profiles.active=prod
```

##Node

```
cd node
virtualenv env
source env/bin/activate
pip install -r requirements.txt
```

```
python cwnagent.py <server> <secret>
```
For example
```
python cwnagent.py http://localhost:8080 1753bb75-f3da-4fd7-9227-475f6b95dbaa 
```


#Development

##Server

Backend
```
mvn spring-boot:run
```

Frontend
```
grunt serve
```

##Node

```
python node.py <server> <secret>
```
