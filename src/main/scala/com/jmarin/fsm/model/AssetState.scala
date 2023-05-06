package com.jmarin.fsm.model

import cats.effect.kernel.Deferred

enum State:
  case Creating extends State
  case Uploading extends State
  case Approving extends State
  case ReadyForProcessing extends State
  case Processing extends State
  case ReadyForDownload extends State
  case Unavailable extends State
