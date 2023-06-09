package cr.ac.una.spotify.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Busqueda(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val texto: String,
    val fecha : Date
)