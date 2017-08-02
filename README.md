## Simple Ioc

Simple Ioc is a little pet project, for creating a lightweight
dependency injection container, it's primary users are people who are new to the concept
and don't want all the complexity of frameworks such as google guice, or spring .

Simple Ioc's current features are :

   * classpath scanning
   * autowiring beans 
   
for usage see the demo project, it essentially boils down
to adding the jar to the class path, and booting up a container 

```java 
    // boot the container, this will automaticaly start a classpath scanning
    Container.boot();
    
    // this will register the bean manually 
    Container.register(App.class); 
    
    /*  Usage  */
    
    @Wire 
    private Component c; 
    
   this will get automaticaly loaded for you
```