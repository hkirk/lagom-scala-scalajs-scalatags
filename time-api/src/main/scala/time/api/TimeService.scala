package time.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait TimeService extends Service {

  def timeAt(tz: String): ServiceCall[NotUsed, String]

  override def descriptor: Descriptor = {
    import Service._

    named("timeService").withCalls(
      pathCall("/api/timeAt/:tz", timeAt _)
    ).withAutoAcl(true)
  }
}

