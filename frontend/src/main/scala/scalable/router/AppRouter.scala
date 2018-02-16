package scalable.router

import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

import scalable.components.Layout
import scalable.diode.AppCircuit
import scalable.pages._

object AppRouter {
  sealed trait Page
  case object StartRoute extends Page
  case object PlaylistRoute extends Page
  case object JoinRoute extends Page
  case object CreateRoute extends Page
  case object JoinAsAdminRoute extends Page
  case class PhotoRoute(roomCode: String) extends Page
  case class AdminRoute(roomCode: String) extends Page
  case class PlaylistRoute(roomCode: String) extends Page

  val connection = AppCircuit.connect(_.state)

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._
    (trimSlashes
      | staticRoute(root, StartRoute) ~> renderR(renderJoinPage)
      | staticRoute("join", JoinRoute) ~> renderR(renderJoinPage)
      | staticRoute("adminjoin", JoinAsAdminRoute) ~> renderR(renderAdminJoinPage)
      | staticRoute("create", CreateRoute) ~> renderR(renderCreateRoomPage)
      | dynamicRouteCT(("admin"/ string(".*")).caseClass[AdminRoute]) ~> dynRenderR(renderAdminPage)
      | dynamicRouteCT(("gallery"/ string(".*")).caseClass[PhotoRoute]) ~> dynRenderR(renderPhotoPage)
      | dynamicRouteCT(("room"/ string(".*")).caseClass[PlaylistRoute]) ~> dynRenderR(renderPlaylistPage)
    )
      .notFound(redirectToPage(StartRoute)(Redirect.Replace))
      .renderWith(layout)
  }

  def renderStartPage(ctl: RouterCtl[Page]) = {
    connection(proxy => JoinPage.Component(JoinPage.Props(proxy, ctl)))
  }

  def renderJoinPage(ctl: RouterCtl[Page]) = {
    connection(proxy => JoinPage.Component(JoinPage.Props(proxy, ctl)))
  }

  def renderAdminJoinPage(ctl: RouterCtl[Page]) = {
    connection(proxy => AdminJoinPage.Component(AdminJoinPage.Props(proxy, ctl)))
  }

  def renderCreateRoomPage(ctl: RouterCtl[Page]) = {
    connection(proxy => CreatePage.Component(CreatePage.Props(proxy, ctl)))
  }

  def renderPhotoPage(p: PhotoRoute, ctl: RouterCtl[Page]) = {
    connection(proxy => PhotoFeedPage.Component(PhotoFeedPage.Props(proxy, p.roomCode, ctl)))
  }

  def renderAdminPage(p: AdminRoute, ctl: RouterCtl[Page]) = {
    connection(proxy => AdminPage.Component(AdminPage.Props(proxy, p.roomCode, ctl)))
  }

  def renderPlaylistPage(p: PlaylistRoute, ctl: RouterCtl[Page]) = {
    connection(proxy => PlaylistPage.Component(PlaylistPage.Props(proxy, ctl)))
  }


  def layout (c: RouterCtl[Page], r: Resolution[Page]) = connection(proxy => Layout(Layout.Props(proxy, c, r)))

  val baseUrl = BaseUrl.fromWindowOrigin_/

  val router = Router(baseUrl, routerConfig.logToConsole)
}
