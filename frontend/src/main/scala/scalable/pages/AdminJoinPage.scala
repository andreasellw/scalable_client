package scalable.pages

import diode.react.ModelProxy
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, ReactEventFromInput, ScalaComponent}
import org.scalajs.dom.html.Div
import scala.concurrent.ExecutionContext.Implicits.global

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scalable.config.Config
import scalable.diode.{AppCircuit, AppState, SetPartyId}
import scalable.json.RestService
import scalable.router.AppRouter

object AdminJoinPage {

  @js.native
  @JSImport("lodash.throttle", JSImport.Default)
  private object _throttle extends js.Any
  def throttle: js.Dynamic = _throttle.asInstanceOf[js.Dynamic]


  case class Props (
                     proxy: ModelProxy[AppState],
                     ctl: RouterCtl[AppRouter.Page]
                   )

  case class AdminJoinState(
                             var code : String,
                             var pw : String
                           )


  class Backend(bs: BackendScope[Props, AdminJoinState]) {
    val host: String = Config.AppConfig.apiHost


    def joinAsAdmin(partyId: String, password: String) : Callback = {
      if(!partyId.isEmpty && !password.isEmpty) {
        val loginApprovedBooleanFuture = RestService.joinPartyAsAdmin(partyId, password)
        val futureCallback = loginApprovedBooleanFuture.map { bool =>
          if (bool) {
            AppCircuit.dispatch(SetPartyId(partyId))
            navigateToAdminPage(partyId)
          } else
            Callback.alert("Entered room code or password was not correct.")
        }
        Callback.future(futureCallback)
      } else
        Callback.alert("Room code or password may not be empty.")
    }

    def onTextCodeChange(adminJoinState: AdminJoinState)(e: ReactEventFromInput) = Callback {
      adminJoinState.code = e.target.value
    }

    def onTextPWChange(adminJoinState: AdminJoinState)(e: ReactEventFromInput) = Callback {
      adminJoinState.pw = e.target.value
    }


    def navigateToAdminPage(input: String): Callback = bs.props.flatMap { props =>
      props.ctl.set(AppRouter.AdminRoute(input))
    }

    def render(p: Props, s: AdminJoinState): VdomTagOf[Div] = {
      val proxy = p.proxy()

      <.div(^.cls := "d-flex flex-column justify-content-center form-group",
        ^.height := "100vh",
        <.label(^.`for` := "roomcode", "Roomcode:"),
        <.div(^.cls := "column", ^.id := "roomcode-row",
          <.div(^.cls := "col-xs-1",
            <.input(^.`type` := "text", ^.cls := "form-control",
              ^.onChange ==> onTextCodeChange(s)
            )
          ),
          <.div(
            ^.cls := "mt-2",
            <.label("Password:")
          ),
          <.div(
            <.input(^.`type` := "text", ^.cls := "form-control",
              ^.onChange ==> onTextPWChange(s)
            )
          ),
          <.div(
            <.button(^.`type` := "button", ^.cls := "btn btn-primary custom-button-width mt-2",
            ^.onClick --> joinAsAdmin(s.code, s.pw),
            "Login"
          )
        )
        )
      )
    }

  }

  val Component = ScalaComponent.builder[Props]("AdminJoinPage")
    .initialState(AdminJoinState(code = "", pw = ""))
    .renderBackend[Backend]
    .build

}
