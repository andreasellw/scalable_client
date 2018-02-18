package scalable.components

import diode.react.ModelProxy
import firebase.Firebase
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^.{<, ^, _}
import japgolly.scalajs.react.{BackendScope, Callback, ReactEventFromInput, ScalaComponent}
import org.scalajs.dom.html
import org.scalajs.dom.html.Div

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.timers.SetIntervalHandle
import scalable.config.Config
import scalable.diode.{AppCircuit, AppState, SetPhotosForParty}
import scalable.services.{FirebaseService, RestService}
import scalable.models.PhotoReturn
import scalable.router.AppRouter


object PhotoFeedTab {


  @js.native
  @JSImport("lodash.throttle", JSImport.Default)
  private object _throttle extends js.Any

  def throttle: js.Dynamic = _throttle.asInstanceOf[js.Dynamic]


  case class Props(
                    proxy: ModelProxy[AppState],
                    ctl: RouterCtl[AppRouter.Page],
                    admin: Boolean
                  )


  class Backend(bs: BackendScope[Props, Unit]) {
    val host: String = Config.AppConfig.apiHost

    var fileChooser: html.Input = _
    var photoFeed: html.Div = _
    var props: Props = _

    val app = Firebase.app("scalable")

    var timer: SetIntervalHandle = _

    def mounted: Callback = Callback{
      startUpdateInterval()
      getData()
    }

    def startUpdateInterval(): Unit ={
      timer = js.timers.setInterval(1000) { // note the absence of () =>
        getData()
      }
    }

    def getData(): Unit = {
        props.proxy.value.partyId match {
          case Some(id) => RestService.getPhotos(id).map { photos =>
            println("Getting Data")
            AppCircuit.dispatch(SetPhotosForParty(photos))
          }
          case None => println("NO PARTY ID")
        }

    }


    def unmounted: Callback = Callback {
      println("Unmounted")
      js.timers.clearInterval(timer)
    }


    def publishLink(url: String, roomCode: String): Unit = {
      RestService.addPhoto(url, roomCode).onComplete(_ => getData())
    }

    def onPhotoChanged()(e: ReactEventFromInput) = Callback {
      val choosenFile = e.target.files.item(0)
      props.proxy.value.partyId match {
          case Some(pId) => FirebaseService.uploadPhoto(file = choosenFile, id = pId, success => {
            publishLink(success.downloadURL.toString, pId)}, _ => {})
          case None => println("NO PARTY ID")
        }

      fileChooser.select()
      js.undefined
    }

    def getPhotofeed(partyId: String): Future[List[PhotoReturn]] = {
      RestService.getPhotos(partyId)
    }

    def render(p: Props): VdomTagOf[Div] = {
      val proxy = p.proxy()
      props = p

      <.div(^.cls := "form-group",
        <.label("Fotofeed"),
        <.div(
          <.input(^.`type` := "file", ^.cls := "form-control", ^.id := "files", ^.maxWidth := 800.px,
            ^.onChange ==> onPhotoChanged())
            .ref(fileChooser = _)
        ), <.div(
          PhotoFeedBox(PhotoFeedBox.Props(p.proxy, p.ctl, admin = p.admin))
        )
      )
    }

  }

  val Component = ScalaComponent.builder[Props]("PhotoFeedPage")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted)
    .componentWillUnmount(scope => scope.backend.unmounted)
    .build


  def apply(props: Props) = Component(props)

}
