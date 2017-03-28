package time.impl

import time.api.TimeService
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._

class TimeServiceApplication(context: LagomApplicationContext) extends LagomApplication(context) {

  override lazy val lagomServer = LagomServer.forServices(
    bindService[TimeService].to(wire[TimeServiceImpl])
  )

  override def serviceLocator: com.lightbend.lagom.scaladsl.api.ServiceLocator = ???
  override def wsClient: play.api.libs.ws.WSClient = ???

}
