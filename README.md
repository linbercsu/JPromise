# JPromise

## How to use

### Normal
```
        Promise.promise("Hello world").then(new Then<String>(){

            @Override
            protected void then(String value) throws Throwable {
                //do some. value is 'Hello world'
            }
        });
```

### handle error
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


