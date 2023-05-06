package com.jmarin.fsm.model

import cats.effect.kernel.Deferred

enum AssetState:
  case Creating extends AssetState
  case Uploading extends AssetState
  case Approving extends AssetState
  case ReadyForProcessing extends AssetState
  case Processing extends AssetState
  case ReadyForDownload extends AssetState
  case Unavailable extends AssetState
