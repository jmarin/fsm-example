package com.jmarin.fsm.model

enum AssetEvent:
  case AssetCreated extends AssetEvent
  case FileUploaded extends AssetEvent
  case AssetAutoTagged extends AssetEvent
  case DuplicateChecked extends AssetEvent
  case DuplicateDeleted extends AssetEvent
  case DATIntermediateCreated extends AssetEvent
  case FileMetadataExtracted extends AssetEvent
  case FileMetadataSaved extends AssetEvent
  case TaxonomyCreated extends AssetEvent
  case AssetApproved extends AssetEvent
  case AssetPublished extends AssetEvent
  case AssetArchived extends AssetEvent
  case AssetDeleted extends AssetEvent
