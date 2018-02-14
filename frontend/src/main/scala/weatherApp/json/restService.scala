package weatherApp.json

import scala.concurrent.ExecutionContext.Implicits.global
import weatherApp.config.Config
import weatherApp.models._
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.ext.Ajax
import scala.concurrent.Future
import scala.scalajs.js.JSON


object restService {

  val host: String = Config.AppConfig.apiHost

  def createParty(partyName: String): Future[PartyCreateResponse] = {
    val content = partyName.asInstanceOf[Ajax.InputData]
    Ajax.put(
      url = s"$host/party",
      data = content,
      headers = Map("Content-Type" -> "text/plain")
    ).map { res =>
      val partyCreateResponse = JSON.parse(res.responseText).asInstanceOf[PartyCreateResponse]
      partyCreateResponse
    }
  }

  def addSongToParty(partyID : String, sendSong: SendSong): Future[Song] = {
    val content = sendSong.asJson.asInstanceOf[Ajax.InputData]
    Ajax.put(
      url = s"$host/party/song/$partyID",
      data = content,
      headers = Map("Content-Type" -> "application/json")
    ).map { res =>
      val song = JSON.parse(res.responseText).asInstanceOf[Song]
      song
    }
  }

  def getSongs(partyID: String): Future[SongResponse] = {

    Ajax.get(
      url = s"$host/party/song/$partyID"
    ).map { res =>
      val songResponse= JSON.parse(res.responseText).asInstanceOf[SongResponse]
      songResponse
    }
  }

    def addPartyVote(partyID: String, song: Song, positive: Boolean, voteType: String): Future[Int] = {
    val content = PartyVote(partyID, song.id, positive, voteType).asJson.asInstanceOf[Ajax.InputData]
     Ajax.post(
        url = s"$host/party/vote",
        data = content,
        headers = Map("Content-Type" -> "application/json")
      ).map(res => res.asInstanceOf[Int])
    }

    def setSongPlayed(songID: Long, partyID: String): Future[Int] = {
      val content = SetSongPlayed(songID, partyID).asJson.asInstanceOf[Ajax.InputData]
      Ajax.post(
        url = s"$host/party/song",
        data = content,
        headers = Map("Content-Type" -> "application/json")
      ).map(res => res.asInstanceOf[Int])
    }
}
