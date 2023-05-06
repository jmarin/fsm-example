package com.jmarin.fsm.model

import java.util.UUID

final case class AssetDetails(id: UUID, state: State)
