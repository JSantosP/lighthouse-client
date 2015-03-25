# lighthouse-client
HTTP Client for lighthouse-server.

## Creating a client instance

```scala
	import lighthouse.Client
	import Client._
	val client = Client("http://127.0.0.1:8080/")
```

## Querying some key

```scala
	val value: Option[ResourceValue] = client ? "key"
```

## TODOs

- ...
