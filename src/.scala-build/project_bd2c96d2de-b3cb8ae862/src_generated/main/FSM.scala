



object FSM {
def args = FSM_sc.args$
/*<script>*///> using dep "org.typelevel::cats-core:2.9.0"
//> using dep "org.typelevel::cats-effect:3.4.9"
import FSM.State.Done
import FSM.State.Outstanding
import cats.effect.kernel.Deferred
import cats.effect.kernel.Ref

import cats.effect.IO

// Generic State Machine

// (S, A) => (S, B)
// It's basically a function that takes a State S and a value A and produces a new State with a new value B

trait Machine[F[_], S, A, B](s: Ref[F, S]):
  def act(a: A): F[B]

// Recipe for concurrent state machine

// 1. Define the coordination interface:
//    1.1. enumerate the roles of the coordinating componets, along with each role's methods; and
//    1.2  define the behavior of each method, which may be state dependent
// 2. Implement interface by building a state machine where:
//   a) we model the state as an algebraic data type S
//   b) we manage the state as a Ref value
//   c) each interface method is implemented as:
//     1. a state transition function affecting the Ref; and
//     2. any state-depending blocking behavior is controlled via Deferred values

//1 Define coordination interface

// Roles
// Waiters --> block until latch opens
// Notifiers --> notifies the latch that work is complete

trait CountdownLatch:
  def await(): IO[Unit]
  def decrement(): IO[Unit]

// Model the state as an algebraic data type

sealed trait State

object State:
  case class Outstanding(n: Long, whenDone: Deferred[IO, Unit])
      extends State // when n reaches 0, we complete the Deferred
  case class Done() extends State

// We manage the state as a Ref value

object CountdownLatch:
  def apply(n: Long): IO[CountdownLatch] =
    for
      whenDone <- Deferred[IO, Unit]
      state <- Ref[IO].of[State](Outstanding(n, whenDone))
    yield ???

// Implement interface by building a state machine where each interface method is implemented as

// 1. a state transition function affecting the Ref
// 2. any state-dependent blocking behavior is controlled via Deferred values

class CountdownLatchFSM(state: Ref[IO, State]) extends CountdownLatch:

  import State.*

  def await(): IO[Unit] =
    state.get.flatMap {
      case Outstanding(_, whenDone) => whenDone.get
      case Done()                   => IO.unit
    }

  def decrement(): IO[Unit] =
    state.modify {
      case Outstanding(1, whenDone) =>
        Done() -> whenDone.complete(())
      case Outstanding(n, whenDone) =>
        Outstanding(n - 1, whenDone) -> IO.unit
      case Done() =>
        Done() -> IO.unit
    }
/*</script>*/ /*<generated>*/
/*</generated>*/
}

object FSM_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }
  def main(args: Array[String]): Unit = {
    args$set(args)
    FSM.hashCode() // hasCode to clear scalac warning about pure expression in statement position
  }
}

