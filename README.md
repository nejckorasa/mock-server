# Mock Server

Easy way to mock a server API, returning back request data in JSON format.

Run with Docker:

```$xslt
docker run -it -p 8080:8080 --rm nejckorasa/mock-server
```

## Intro 

Very simple, it listens to all requests on a desired port, returning back all the request data in a response, for example:

```json
{
    "method": "POST",
    "uri": "/api/v1/random-path?paramOne=1&paramTwo=2",
    "path": "/api/v1/random-path",
    "queryParameters": [
        {
            "paramOne": [
                "1"
            ]
        },
        {
            "paramTwo": [
                "2"
            ]
        }
    ],
    "body": "{\n\t\"name\": \"Nejc\"\n\t\"where\": \"London\"\n}",
    "port": 8080,
    "scheme": "http",
    "version": "HTTP/1.1",
    "host": "localhost",
    "remoteHost": "172.17.0.1",
    "time" : "15:01:18.144",
    "headers": [
        {
            "Content-Type": [
                "application/json"
            ]
        },
        {
            "Authorization": [
                "Basic blableblu"
            ]
        },
        {
            "User-Agent": [
                "PostmanRuntime/7.11.0"
            ]
        },
        {
            "Accept": [
                "*/*"
            ]
        },
        {
            "Cache-Control": [
                "no-cache"
            ]
        },
        {
            "Host": [
                "localhost:8080"
            ]
        },
        {
            "cookie": [
                "Cookie_var1=value; Cookie_var2=value2"
            ]
        },
        {
            "accept-encoding": [
                "gzip, deflate"
            ]
        },
        {
            "content-length": [
                "38"
            ]
        },
        {
            "Connection": [
                "keep-alive"
            ]
        }
    ],
    "cookies": {
        "Cookie_var2": "value2",
        "Cookie_var1": "value"
    }
}
```

## Response config

By default response contains the following request data:

- method 
- uri
- path
- queryParameters
- body
- port
- scheme
- version
- host
- remoteHost
- time
- headers
- cookies

There is also an option to just return the requests body.

#### Just the body

Have the url contain ```just-the-body``` anywhere in the URL (as path or query parameter) and the response will only contain the request's body.

For example POST to `localhost:8321/api/iv1/random-path/just-the-body`

#### Pretty JSON print

Have the url contain ```just-the-json-body``` anywhere in the URL (as path or query parameter) and the response will only contain the request's JSON body, pretty printed:

For example POST to `localhost:8321/api/iv1/random-path/just-the-json-body` (with the same body as response all the way above) returns:

```json
{
 "name" : "Nejc",
 "location" : "London"
}
``` 

## Run with docker

Image is available on Docker Hub as [nejckorasa/mock-server](https://cloud.docker.com/u/nejckorasa/repository/docker/nejckorasa/mock-server)

```$xslt
docker run -it -p 8080:8080 --rm nejckorasa/mock-server
```

## Install and run locally

#### Build an application package
```$xslt
./gradlew build
```

#### Building and running the Docker image

Build and tag an image:

```$xslt
docker build -t mock-server .
```

Start an image:

```$xslt
docker run -it -p 8080:8080 --rm mock-server
```
