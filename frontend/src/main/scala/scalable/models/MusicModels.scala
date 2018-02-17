package scalable.models

case class PartyCreateResponse
(
  id: String,
  name: String,
  password: String,
  createdAt: String
)

case class Song
(
  //id: Int,

  name: String,
  albumCoverUrl: String,
  streamingServiceID: String,
  downvotes: Int,
  upvotes: Int,
  id: Long,
  artist: String,
  album: String,
  playState: String
  //, createdAt: LocalDateTime

)

case class SongListElement
(
  name: String,
  artist: String
)

case class SendSong
(
  streamingServiceID: String,
  name: String,
  artist: String,
  album: String,
  albumCoverUrl: String
)

case class PartyVote
(
  partyID: String,
  songID: Long,
  positive: Boolean,
  voteType: String
)


case class VoteAble
(
  partyID: String,
  compId: Long,
  upvotes: Int,
  downvotes: Int,
  voteType: String

)

case class SongPlayStateUpdate(id:Long, partyID:String, playState:String)

case class DeleteSong
(
  id: Long,
  partyID: String,
  playState: String
)

case class PartyLoginRequest
(
  id: String,
  password: String
)



