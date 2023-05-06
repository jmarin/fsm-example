package com.jmarin.fsm

import cats.effect.IOApp
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.kernel.Ref
import com.jmarin.fsm.model.AssetState
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

object Main extends IOApp.Simple:

  given unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] =
    (for
      _ <- Logger[IO].info(s"Starting Asset Finite State Machine")
      ref <- Ref.of[IO, AssetState](AssetState.Creating)
      r <- Random.scalaUtilRandom[IO]
      fsm =
        given Random[IO] = r
        AssetFSM(UUID.randomUUID(), ref)
      currentState <- fsm.getState
      _ <- Logger[IO].info(s"Asset FSM initial state: ${currentState}")
      uploadDeferred <- Deferred[IO, Int]
      _ <- fsm.uploadOriginalFile()
      _ <- logState(fsm)
      _ <- fsm.downloadOriginalFile()
    yield ())

  private def logState[F[_]: Async: Logger](fsm: AssetFSM[F]): F[Unit] =
    for
      currentState <- fsm.getState
      _ <- Logger[F].info(s"Asset FSM current state: $currentState")
    yield ()
