# ApplesAndOranges
Scala programming excercise

I have implemented a simple REST API for the checkout service, with just one endpoint

## API 


|  Method | Url  | Description  |Returns |
|---|---|---|---|
| POST | / | body with string containing items as scanned, eg "orange,apple,orange" | test/plain with body: Total cost of items |

## Usage


Example post curl commands:


```
curl -H "Content-Type: text/plain" -X POST -d 'orange,apple,orange' http://localhost:9000
1.10

curl -H "Content-Type: text/plain" -X POST -d 'orange,apple,orange,orange,apple' http://localhost:9000
1.10
```