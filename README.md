# CloudWorker
Turn any computer into a processing-agent. Manage all these cloudworkers through a centralised management system.

#Wiki
* https://github.com/CloudWorkers/cloudworker/wiki/Data-Model

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
python node.py <server> <secret>
```
For example
```
python node.py http://localhost:8080 1753bb75-f3da-4fd7-9227-475f6b95dbaa 
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
