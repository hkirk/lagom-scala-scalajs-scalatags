package time.impl

import java.time.{LocalTime, ZoneId}

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceCall}
import time.api.TimeService

import scala.concurrent.{ExecutionContext, Future}

class TimeServiceImpl(implicit ec: ExecutionContext) extends TimeService {

  override def timeAt(tz: String): ServiceCall[NotUsed, String] = new ServiceCall[NotUsed, String] {
      override def invoke(obj: NotUsed): Future[String] = {
        Future {
          LocalTime.now(ZoneId.of(tz, ZoneId.SHORT_IDS)).toString
        }
      }
    }

  override def descriptor: Descriptor = super.descriptor
}
