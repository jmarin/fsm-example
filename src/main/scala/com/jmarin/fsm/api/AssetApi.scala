package com.jmarin.fsm.api

import com.jmarin.fsm.model.AssetState
import cats.effect.kernel.Deferred

trait AssetApi[F[_]]:
  def getState: F[AssetState]
  def uploadOriginalFile(): F[Unit]
  def downloadOriginalFile(): F[Unit]
