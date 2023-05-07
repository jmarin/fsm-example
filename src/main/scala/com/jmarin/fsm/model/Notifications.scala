package com.jmarin.fsm.model

enum Notification(msg: String):
  case IsReadyForProcessing
      extends Notification("Asset is ready for processing")
  case IsReadyForDownload
      extends Notification("Asset's files are ready for downloading")
