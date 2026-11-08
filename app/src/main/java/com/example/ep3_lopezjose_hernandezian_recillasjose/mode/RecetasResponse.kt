package com.example.ep3_lopezjose_hernandezian_recillasjose.mode

import com.google.gson.annotations.SerializedName

data class RecetasResponse(
    @SerializedName("meals") val results: List<Empanada>?
)