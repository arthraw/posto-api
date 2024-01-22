package com.example.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

@Serializable
data class Transaction(
    val id: Int,
    val transactionTypeId: Int,
    val cost: Double,
    val gas : Double,
    @Serializable(with = LocalDateSerializer::class)
    val timestamp: LocalDate,
)

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}

object Transactions : Table() {
    val id = integer("id").autoIncrement()
    val transactionTypesId = (integer("transaction_type_id").default(TransactionType().id))
    val cost = double("cost")
    val gas = double("gas")
    val timestamp = date("transaction_date")

    override val primaryKey = PrimaryKey(id)
}

data class TransactionType(
    val payment: Map<Int, String> = mapOf<Int, String>(1 to "money", 2 to "debit", 3 to "credit", 4 to "pix"),
    val id: Int = payment.keys.first(),
)
