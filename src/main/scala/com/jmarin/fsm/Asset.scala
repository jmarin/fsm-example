package com.jmarin.fsm

import cats.effect.kernel.Ref
import org.typelevel.log4cats.Logger
import cats.effect.kernel.Sync
import cats.effect.kernel.Concurrent
import com.jmarin.fsm.model.State
import com.jmarin.fsm.model.State.*
import com.jmarin.fsm.api.AssetApi
import cats.implicits.*
import java.util.UUID
import cats.effect.kernel.Async
import cats.effect.std.Random
import concurrent.duration.DurationInt
import cats.effect.kernel.Deferred
import cats.effect.implicits.*

final case class Asset[F[_]: Async: Logger: Random](
    id: UUID,
    state: Ref[F, State]
) extends AssetApi[F]:

  given F[Random[F]] = Random.scalaUtilRandom

  def getState: F[State] = state.get

  def uploadOriginalFile(): F[Unit] =
    state.modify {
      case Creating =>
        Logger[F].info(s"Uploading original file")
        (Uploading, uploadFile())
      case _ =>
        Logger[F].error(s"Cannot upload file when in state: $state")
        (Creating, Async[F].unit)
    }.flatten

  def downloadOriginalFile(): F[Unit] =
    state.modify {
      case Uploading =>
        Logger[F].error(s"Cannot download file when in Uploading state")
        (Uploading, Async[F].unit)

      case _ =>
        Logger[F].error(s"Cannot download file when in state: $state")
        (State.ReadyForDownload, Async[F].unit) // WRONG!!!

    }

  private def uploadFile(): F[Unit] =
    for
      deferred <- Deferred[F, Int]
      fib1 <- deferredOperation("File Upload", deferred).start
      fib2 <- deferredExecution(deferred).start
      _ <- fib1.join
      _ <- fib2.join
      _ <- state.modify {
        case Uploading =>
          (ReadyForProcessing, Async[F].unit)
        case _ => (Uploading, Async[F].unit)
      }
    yield ()

  private def deferredOperation(
      name: String,
      whenDone: Deferred[F, Int]
  ): F[Unit] =
    for
      _ <- Logger[F].info(s"...Starting $name...")
      time <- whenDone.get
      _ <- Logger[F].info(s"...$name Completed in $time seconds...")
    yield ()

  private def deferredExecution(deferred: Deferred[F, Int]) =
    for
      time <- randomDelay
      _ <- Async[F].sleep(time.seconds)
      r <- deferred.complete(time)
    yield r

  private def downloadFile(whenDone: Deferred[F, Unit]): F[Unit] =
    Async[F].unit

  private def randomDelay: F[Int] =
    Random[F].betweenInt(1, 5)
