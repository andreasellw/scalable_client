package scalable.diode

import diode._
import diode.react.ReactConnector
import scalable.models._

object AppCircuit extends Circuit[AppModel] with ReactConnector[AppModel] {
  def initialModel = AppModel(
    AppState(
      partyId = None,
      songList = List.empty[Song],
      videoSuggestions = List.empty[VideoResponse],
      selectedVideo = None: Option[VideoResponse],
      isLoading = false,
    )
  )

  override val actionHandler = composeHandlers(
    new PlaylistPageHandler(zoomTo(_.state)),
    new AppHandler(zoomTo(_.state))
  )
}



class PlaylistPageHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case SetPartyId(partyId) => updated(value.copy(partyId = Some(partyId)))
    case SetSongsForParty(songs) => updated(value.copy(songList = songs))
    case GetVideoSuggestions(videoSuggestions) => updated(value.copy(videoSuggestions = videoSuggestions))
    case SelectVideo(video) => updated(value.copy(selectedVideo = video))
    }
  }

class AppHandler[M](modelRW: ModelRW[M, AppState]) extends ActionHandler(modelRW) {
  override def handle = {
    case SetLoadingState() => updated(value.copy(isLoading = true))
    case ClearLoadingState() => updated(value.copy(isLoading = false))
  }
}
