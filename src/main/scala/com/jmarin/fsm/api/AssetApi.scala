package com.jmarin.fsm.api

import com.jmarin.fsm.model.State
import cats.effect.kernel.Deferred

trait AssetApi[F[_]]:
  def getState: F[State]
  def uploadOriginalFile(): F[Unit]
  def downloadOriginalFile(): F[Unit]
  def processOriginal(): F[Unit]
