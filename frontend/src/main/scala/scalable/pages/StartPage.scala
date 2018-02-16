package scalable.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, ReactEventFromInput, ScalaComponent, ScalaFnComponent}
import org.scalajs.dom.html.Div

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scalable.components.HeaderNav.NavLink
import scalable.config.Config
import scalable.diode._
import scalable.json.RestService
import scalable.router.AppRouter

object StartPage {

  @js.native
  @JSImport("lodash.throttle", JSImport.Default)
  private object _throttle extends js.Any

  def throttle: js.Dynamic = _throttle.asInstanceOf[js.Dynamic]


  case class Props(
                    proxy: ModelProxy[AppState],
                    ctl: RouterCtl[AppRouter.Page]
                  )


  class Backend(bs: BackendScope[Props, Unit]) {
    val host: String = Config.AppConfig.apiHost

    def navigateToJoinPage(): Callback = bs.props.flatMap { props =>
      props.ctl.set(AppRouter.JoinRoute)
    }

    def navigateToJoinAsAdmingPage(): Callback = bs.props.flatMap { props =>
      props.ctl.set(AppRouter.JoinAsAdminRoute)
    }

    def navigateToCreatePage(): Callback = bs.props.flatMap { props =>
      props.ctl.set(AppRouter.CreateRoute)
    }

    def render(props: Props): VdomTagOf[Div] = {
      val proxy = props.proxy()
      <.div(
        ^.cls := "d-flex flex-column align-items-center",
        ^.maxWidth := 800.px,
        <.div( // Child 1 AlbumCover
          ^.cls := "mb-5 mt-5",
          ^.flex := "0 0 auto",
          ^.width := 150.px,
          ^.height := 150.px,
          ^.backgroundClip := "padding-box",
          ^.backgroundImage := "url(/images/Scalable.png)",
          ^.backgroundSize := "cover",
          ^.backgroundPosition := "center center"
        ),
          <.button(^.`type` := "button", ^.cls := "btn btn-primary btn-block",
            ^.onClick --> navigateToCreatePage(),
            "Join Party"
          ),
        <.button(^.`type` := "button", ^.cls := "btn btn-outline-primary mt-2 btn-block",
          ^.onClick --> navigateToCreatePage(),
          "Join Party As Host"
        ),
        <.button(^.`type` := "button", ^.cls := "btn btn-outline-primary btn-block mt-0",
          ^.onClick --> navigateToCreatePage(),
          "Create New Party"
        )
      )
    }
  }

  val Component = ScalaComponent.builder[Props]("StartPage")
    .renderBackend[Backend]
    .build

}
