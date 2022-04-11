# CodeReview

#### About the project:
This is an implementation of Redis with operators GET, SET, DELETE, KEYS

#### Preparations and requirements:
Open this project with your IDE, start the Server by starting main method in MyHttpServer, open your browser and start making requests under localhost:8888.

#### Code details:
There are 3 .java files: Storage and MyHttpServer in src.main.java package and StorageTest in src.test.java package.
Storage class is a core of the project, there a based on HashMap storage for redis itself is implemented with 4 main operators. Each of them completes its functional part which follows from the name of the method and returns a String, explaining the result.

Get method returns nil if there is no such key and value, which corresponds to the key if there is one.

Set method returns OK in case of successful operation of putting the key - value pair into the storage.

Delete method deletes the key - value pair from the storage and returns value.

Keys method returns set of keys from the storage.

Server part of the application is written in MyHttpServer class and using simple HttpServer class in Java. It's function is to determine what kind of operation the client wants to carry out and make it happen. Here, it all depends on what you write into the url after writing localhost number and "/redis". Basically, you have to type the name of the operator ("get", "set", "delete", or "keys"), then quastion sign and then the rest of your request. After you've done so the html page is going to show you a string including the result of invoked method.
So, for example, if you write: "http://localhost:8888/redis/set?name=surname", a pair name = surname is going to be put into the storage and http page is going to show OK as an answer to your request.
