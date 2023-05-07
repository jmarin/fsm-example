package com.jmarin.fsm

import cats.effect.IOApp
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.kernel.Ref
import com.jmarin.fsm.model.State
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jFactory
import org.typelevel.log4cats.*
import java.util.UUID
import cats.effect.std.Random
import cats.effect.kernel.Async
import cats.implicits.*
import cats.effect.kernel.Deferred
import com.jmarin.fsm.Asset
import cats.effect.std.Queue
import com.jmarin.fsm.model.Notification

object Main extends IOApp.Simple:

  given unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] =
    (for
      _ <- Logger[IO].info(s"Starting Asset Finite State Machine")
      ref <- Ref.of[IO, State](State.Creating)
      queue <- Queue.unbounded[IO, Notification]
      r <- Random.scalaUtilRandom[IO]
      fsm =
        given Random[IO] = r
        Asset(UUID.randomUUID(), ref, queue)
      initialState <- fsm.getState
      _ <- Logger[IO].info(s"Asset FSM initial state: ${initialState}")
      _ <- (fsm.uploadOriginalFile(), fsm.processOriginal()).parTupled
      _ <- fsm.downloadOriginalFile()
    yield ())

  private def logState[F[_]: Async: Logger](fsm: Asset[F]): F[Unit] =
    for
      currentState <- fsm.getState
      _ <- Logger[F].info(s"Asset FSM current state: $currentState")
    yield ()
