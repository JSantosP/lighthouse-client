# lighthouse-client
HTTP Client for lighthouse-server.

## Example

```scala

scala> import lighthouse._;import model._;
import lighthouse._
import model._

scala> val client = Client("http://127.0.0.1:8080/")
client: lighthouse.Client = Client(http://127.0.0.1:8080/,3)

scala> client ? "my-key"
res1: Option[lighthouse.model.ResourceValue] = Some(ResourceValue(value1))

scala> client set "my-key" withValue ResourceValue("value1")
warning: there was one feature warning; re-run with -feature for details
res2: scala.util.Try[Unit] = Success(())

scala> client ? "my-key"
res3: Option[lighthouse.model.ResourceValue] = Some(ResourceValue(value1))

scala> client set "my-key" withValue ResourceValue("value2")
warning: there was one feature warning; re-run with -feature for details
res4: scala.util.Try[Unit] = Success(())

scala> client ? "my-key"
res5: Option[lighthouse.model.ResourceValue] = Some(ResourceValue(value2))

```

## TODOs

- Retry policies