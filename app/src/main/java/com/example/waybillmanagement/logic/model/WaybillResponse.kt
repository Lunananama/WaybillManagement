package com.example.waybillmanagement.logic.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "waybillTable", strict = false)
data class WaybillResponse constructor(


    @field:ElementList(name = "waybillRecord", entry = "waybillRecord", inline = true, required = false)
    var waybillRecord: List<Waybill> ?= null)