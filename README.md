# Micronaut JMS Example Application

This app is a toy example for implementing Micronaut JMS. It also serves as the primary integration
test platform for changes made to the Micronaut JMS project

## How to use

### Prerequisites

1. A copy of the source code (via git clone)
2. Docker and docker-compose

Then run 
```bash
./gradlew build && docker-compose up
```

(this command will build the JAR and start the PostgreSQL database and ActiveMQ broker
with the required configuration)

then

```bash
curl -X POST "http://localhost:8080/messages/string" -H  "accept: text/plain" -H  "Content-Type: application/json" -d "{\"message\":\"test\"}"
```

or navigate to the Swagger UI at `http://localhost:8080/swagger-ui`

This endpoint will return a UUID which you can use to track the message's progress through the 
message broker.

## Architecture

### Producing A Message

The `POST` endpoints at `/messages/*` allow you to send a message to a message queue 
with a variety of inputs - currently as a `Map`, as a `String`, as an `Object`, or as a `byte[]`

The destination of this message is configurable in the `application.yml` under the property
`example.jms.destination`

### Receiving messages

The `MessageListener` is defined to listen to the `example.jms.destination` and persist
the receipt of this message to a PostgreSQL database. If the `X-Max-Depth` and `X-Current-Depth`
headers are set on the message then message receipt will also trigger further messages to the queue.
The number of messages produced at each level of recursion is defined by the `example.jms.batch-size`
property in the `application.yml`.

### Retrieving a Specific Message

If you have the UUID for a particular message then you can retrieve it from the 
`GET /messages/{id}` endpoint. 

### Retrieving Details of a Full Recursion

When you initiate a recursive message test then you can retrieve the results in a JSON
blob using the `GET /test/{id}` where the `id` corresponds to the first message ID found in 
the response.