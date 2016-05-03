# JPromise

## How to use

### Hello world
```
        Promise.promise("Hello world").then(new Then<String>(){

            @Override
            protected void then(String value) throws Throwable {
                //do some. value is 'Hello world'
            }
        });
```

### Handle error
```
        Promise.promise("Hello world").then(new Then<String>(){

            @Override
            protected void then(String value) throws Throwable {
                throw new RuntimeException("Hello world exception");
            }
        }).then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {

            }

            @Override
            public void error(Throwable throwable) {
                //handle the exception.
            }
        });
```

### Promise all
```
        Promise hello = Promise.promise("Hello");
        Promise world = Promise.promise("World");

        Promise.all(hello, world).then(new ArrayThen<String>(){

            @Override
            protected void doRun(List<String> result) {

            }
        });
```
### Promise race
```
        Promise hello = Promise.promise("Hello");
        Promise world = Promise.promise("World");

        Promise.race(hello, world).then(new Then() {
            @Override
            protected void then(Object value) throws Throwable {

            }
        });
```
