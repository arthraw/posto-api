package com.example.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
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
    val id: Int = 0,
    @SerialName("transactionTypeId")
    val transactionTypeId: Int,
    val transaction: String,
    val cost: Double,
    val gas : Double,
    @Serializable(with = LocalDateSerializer::class)
    val timestamp: LocalDate,
)
@Serializable
data class TransactionInsert(
    @SerialName("transactionTypeId")
    val transactionTypeId: Int,
    val transaction: String,
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
    val transactionType = varchar("transaction_type", 10).default(TransactionType().paymentName)
    val cost = double("cost")
    val gas = double("gas")
    val timestamp = date("transaction_date")

    override val primaryKey = PrimaryKey(id)
}

data class TransactionType(
    val payment: Map<Int, String> = mapOf(1 to "money", 2 to "debit", 3 to "credit", 4 to "pix"),
    val idOptions: Set<Int> = payment.keys,
    val id: Int = idOptions.first(),
    val paymentName: String = payment[id] ?: "Unknown"
)
